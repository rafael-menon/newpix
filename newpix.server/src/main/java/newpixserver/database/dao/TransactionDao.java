package newpixserver.database.dao;

import newpixserver.database.ConnectionFactory;
import newpixserver.database.entities.Transaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TransactionDao {
    public void createTransaction(String cpfOrigin, Double amount, String cpfDestination, String date) throws SQLException {
        String sql = "INSERT INTO user_transactions (cpf_origin, amount, cpf_destination, date) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cpfOrigin);
            stmt.setDouble(2, amount);
            stmt.setString(3, cpfDestination);
            stmt.setString(4, date);

            stmt.executeUpdate();
        }
    }

    public List<Transaction> getUserTransactions(String cpf, String startDate, String endDate) throws SQLException {
        String sql = """
            SELECT 
                t.transaction_id, t.amount, t.cpf_origin, t.cpf_destination, t.date,
                u_origin.name AS origin_name,
                u_dest.name AS destination_name
            FROM 
                user_transactions t
            JOIN 
                users u_origin ON t.cpf_origin = u_origin.cpf
            JOIN 
                users u_dest ON t.cpf_destination = u_dest.cpf
            WHERE 
                (t.cpf_origin = ? OR t.cpf_destination = ?) AND t.date BETWEEN ? AND ?
        """;
        List<Transaction> transactionList = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, cpf);
            stmt.setString(2, cpf);
            stmt.setString(3, startDate);
            stmt.setString(4, endDate);

            try (ResultSet rtst = stmt.executeQuery()) {
                while(rtst.next()) {
                    transactionList.add(createTransactionFromResultSet(rtst));
                }
            }
        }

        return transactionList;
    }

    public List<Transaction> getAllTransactions() throws SQLException {
        String sql = """
            SELECT 
                t.transaction_id, t.amount, t.cpf_origin, t.cpf_destination, t.date,
                u_origin.name AS origin_name,
                u_dest.name AS destination_name
            FROM 
                user_transactions t
            JOIN 
                users u_origin ON t.cpf_origin = u_origin.cpf
            JOIN 
                users u_dest ON t.cpf_destination = u_dest.cpf        
        """;
        List<Transaction> transactionList = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)){
            try (ResultSet rtst = stmt.executeQuery()) {
                while(rtst.next()) {
                    transactionList.add(createTransactionFromResultSet(rtst));
                }
            }
        }

        return transactionList;
    }

    private Transaction createTransactionFromResultSet(ResultSet rtst) throws SQLException {
        Transaction transaction = new Transaction();

        transaction.setId(rtst.getInt("transaction_id"));
        transaction.setSenderName(rtst.getString("origin_name"));
        transaction.setSenderCpf(rtst.getString("cpf_origin"));
        transaction.setReceiverName(rtst.getString("destination_name"));
        transaction.setReceiverCpf(rtst.getString("cpf_destination"));
        transaction.setAmount(rtst.getDouble("amount"));
        transaction.setDate(rtst.getString("date"));

        return transaction;
    }
}
