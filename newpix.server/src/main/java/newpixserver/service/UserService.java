package newpixserver.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import newpixserver.database.dao.TransactionDao;
import newpixserver.database.dao.UserDao;
import newpixserver.database.ConnectionFactory;
import newpixserver.database.entities.Transaction;
import newpixserver.database.entities.User;
import newpixserver.messages.json.TransactionJson;
import newpixserver.messages.json.UserJson;
import newpixserver.security.PasswordHasher;
import newpixserver.security.TokenGenerator;
import newpixserver.service.listener.UserServiceEvent;
import newpixserver.service.listener.UserServiceListener;
import newpixserver.type.Cpf;
import newpixserver.type.IsoDate;
import newpixserver.type.Name;
import newpixserver.type.Password;
import newpixserver.utils.AuthenticationException;

public class UserService {
	private final UserDao userDao;
    private final TransactionDao transactionDao;

    private final List<UserServiceListener> listeners = new ArrayList<>();

	public UserService(UserDao userDao, TransactionDao transactionDao) {
        this.userDao = userDao;
        this.transactionDao = transactionDao;
	}

    public void addListener(UserServiceListener listener) {
        this.listeners.add(listener);
    }

    private void userServiceEvent(UserServiceEvent event) {
        for (UserServiceListener listener : listeners) {
            listener.onUserServiceEvent(event);
        }
    }

    public User userLogin(Cpf cpf, Password requestedPassword) throws SQLException, AuthenticationException {
        User user = userDao.getActiveUserByCpf(cpf.getString());

        if (user == null || !PasswordHasher.checkPassword(requestedPassword.getString(), user.getPassword())) {
            throw new AuthenticationException("CPF ou senha inválidos.");
        }

        String token = TokenGenerator.generateSecureRandomToken();
        userDao.updateUserToken(user, token, IsoDate.getTokenDuration(60));
        user.setToken(token);

        return user;
    }

    public void userLogout(User authenticatedUser) throws SQLException {
        userDao.updateUserToken(authenticatedUser, null, null);
    }

    public void userCreate(Name name, Cpf cpf, Password password) throws SQLException, AuthenticationException {
        String hashedPassword = PasswordHasher.hashPassword(password.getString());
        User existingUser = userDao.getAnyUserByCpf(cpf.getString());

        if (existingUser != null) {
            if (existingUser.isActive() == 1) {
                throw new AuthenticationException("CPF já cadastrado.");
            } else {
                existingUser.setName(name.getString());
                existingUser.setPassword(hashedPassword);

                userDao.reactivateUser(existingUser);

                userServiceEvent(UserServiceEvent.USER_LIST_CHANGED);
                userServiceEvent(UserServiceEvent.TRANSACTION_LIST_CHANGED);

                return;
            }
        }

        User newUser = new User(name.getString(), cpf.getString(), hashedPassword);
        userDao.registerUser(newUser);

        userServiceEvent(UserServiceEvent.USER_LIST_CHANGED);
    }

    public String updateUser(User user, String newName, String newPassword) throws SQLException {
        boolean hasName = newName != null && !newName.isEmpty();
        boolean hasPassword = newPassword != null && !newPassword.isEmpty();

        Name name = hasName ? new Name(newName) : null;
        String hashedPassword = hasPassword ? PasswordHasher.hashPassword(new Password(newPassword).getString()) : null;

        if (hasName && hasPassword) {
            userDao.updateUserNameAndPassword(user, name.getString(), hashedPassword);
            userServiceEvent(UserServiceEvent.USER_LIST_CHANGED);
            userServiceEvent(UserServiceEvent.TRANSACTION_LIST_CHANGED);
            return "Usuário atualizado com sucesso.";

        } else if (hasName) {
            userDao.updateUserName(user, name.getString());
            userServiceEvent(UserServiceEvent.USER_LIST_CHANGED);
            userServiceEvent(UserServiceEvent.TRANSACTION_LIST_CHANGED);
            return "Nome atualizado com sucesso.";

        } else if (hasPassword) {
            userDao.updateUserPassword(user, hashedPassword);
            userServiceEvent(UserServiceEvent.USER_LIST_CHANGED);
            return "Senha atualizada com sucesso.";
        }

        return null;
    }

	public void deactivateUser(String token) throws SQLException {
        if(userDao.getUserByToken(token).getBalance() > 0) {
            throw new SQLException("Deleção negada, a conta possui saldo maior que zero.");
        }

		userDao.deactivateAndAnonymizeUser(token);

        userServiceEvent(UserServiceEvent.USER_LIST_CHANGED);
        userServiceEvent(UserServiceEvent.TRANSACTION_LIST_CHANGED);
	}

    public void createTransaction(String token, Double amount, String cpfDestination) throws SQLException {
        User origin = userDao.getUserByToken(token);
        User destination = userDao.getActiveUserByCpf(cpfDestination);

        if (origin == null) throw new SQLException("Token inválido");
        if (destination == null) throw new SQLException("Destinatário não encontrado.");
        if (amount <= 0.00) throw new SQLException("Valor inválido");
        if (origin.getBalance() < amount) throw new SQLException("Saldo insuficiente.");

        try (Connection conn = ConnectionFactory.getConnection()) {
            try {
                conn.setAutoCommit(false);

                transactionDao.createTransaction(origin.getCpf(), amount, cpfDestination, IsoDate.getCurrentDate());
                userDao.subtractMoneyFromUser(origin.getCpf(), amount);
                userDao.addMoneyToUser(cpfDestination, amount);

                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw new SQLException("Erro ao executar a transação.");
            }
        }

        userServiceEvent(UserServiceEvent.USER_LIST_CHANGED);
        userServiceEvent(UserServiceEvent.TRANSACTION_LIST_CHANGED);
    }

    public List<TransactionJson> transactionRead(String token, String startDate, String endDate) throws SQLException {
        String userCpf = userDao.getUserByToken(token).getCpf();


        List<Transaction> transactionList = transactionDao.getUserTransactions(userCpf, startDate, endDate);

        if(transactionList != null && (!transactionList.isEmpty())) {
            List<TransactionJson> transactionJsonList = new ArrayList<>();
            for(Transaction transaction : transactionList) {
                TransactionJson transactionJson = new TransactionJson();
                transactionJson.setId(transaction.getId());
                transactionJson.setValor_enviado(transaction.getAmount());
                transactionJson.setUsuario_enviador(new UserJson(transaction.getSenderName(), transaction.getSenderCpf()));
                transactionJson.setUsuario_recebedor(new UserJson(transaction.getReceiverName(), transaction.getReceiverCpf()));
                transactionJson.setCriado_em(transaction.getDate());
                transactionJson.setAtualizado_em(IsoDate.getCurrentDate());
                transactionJsonList.add(transactionJson);
            }

            userServiceEvent(UserServiceEvent.USER_LIST_CHANGED);
            return transactionJsonList;
        } else {
            throw new SQLException("Nenhuma transação encontrada.");
        }
    }

    public void deposit(String token, Double sentValue) throws Exception {
        if(sentValue < 1) {
            throw new Exception("Valor inválido. Digite um valor maior do que zero.");
        }

        userDao.userDeposit(token, sentValue);

        User user = userDao.getUserByToken(token);

        transactionDao.createTransaction(user.getCpf(), sentValue, user.getCpf(), IsoDate.getCurrentDate());

        userServiceEvent(UserServiceEvent.USER_LIST_CHANGED);
    }

    public User getUserByToken(String tokenFromRequest) throws SQLException {
        return userDao.getUserByToken(tokenFromRequest);
    }
}
