package newpixserver.service;

import newpixserver.database.dao.TransactionDao;
import newpixserver.database.dao.UserDao;
import newpixserver.database.entities.Transaction;
import newpixserver.database.entities.User;

import java.sql.SQLException;
import java.util.List;

public class AdminService {
    private final UserDao userDao;
    private final TransactionDao transactionDao;

    public AdminService(UserDao userDao, TransactionDao transactionDao) {
        this.userDao = userDao;
        this.transactionDao = transactionDao;
    }

    public List<User> getAllUsers() throws SQLException {
        return userDao.getAllUsers();
    }

    public List<Transaction> getAllTransactions() throws SQLException {
        return transactionDao.getAllTransactions();
    }
}
