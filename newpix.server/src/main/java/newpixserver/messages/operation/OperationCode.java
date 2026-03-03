package newpixserver.messages.operation;

public final class OperationCode {
    public static final String CONNECT = "conectar";
    public static final String USER_LOGIN = "usuario_login";
    public static final String USER_LOGOUT = "usuario_logout";
    public static final String USER_CREATE = "usuario_criar";
    public static final String USER_READ = "usuario_ler";
    public static final String USER_UPDATE = "usuario_atualizar";
    public static final String USER_DELETE = "usuario_deletar";
    public static final String TRANSACTION_CREATE = "transacao_criar";
    public static final String TRANSACTION_READ = "transacao_ler";
    public static final String USER_DEPOSIT = "depositar";
    public static final String SERVER_ERROR = "erro_servidor";

    public static final String CONNECT_SUCCESS = "Conexão bem-sucedida.";
    public static final String USER_LOGIN_SUCCESS = "Login realizado com sucesso.";
    public static final String USER_LOGOUT_SUCCESS = "Logout realizado com sucesso";
    public static final String USER_CREATE_SUCCESS = "Usuário criado com sucesso.";
    public static final String USER_READ_SUCCESS = "Dados do usuário recuperados com sucesso.";
    public static final String USER_UPDATE_SUCCESS = "Usuário atualizado com sucesso.";
    public static final String USER_DELETE_SUCCESS = "Usuário deletado com sucesso.";
    public static final String TRANSACTION_CREATE_SUCCESS = "Transação realizada com sucesso.";
    public static final String TRANSACTION_READ_SUCCESS = "Transações recuperadas com sucesso.";
    public static final String USER_DEPOSIT_SUCCESS = "Deposito realizado com sucesso.";
}
