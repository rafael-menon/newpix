package newpixserver.database.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import newpixserver.database.ConnectionFactory;
import newpixserver.database.entities.User;

public class UserDao {
    public void registerUser(User user) throws SQLException {
        String sql = "INSERT INTO users (name, cpf, password, balance, token) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getCpf());
            stmt.setString(3, user.getPassword());
            stmt.setDouble(4, user.getBalance());
            stmt.setString(5, user.getToken());

            stmt.executeUpdate();
        }
    }

    public void reactivateUser(User user) throws SQLException {
        String sql = "UPDATE users SET name = ?, password = ?, is_active = ? WHERE cpf = ?";

        try (Connection conn = ConnectionFactory.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getPassword());
            stmt.setInt(3, 1);
            stmt.setString(4, user.getCpf());

            stmt.executeUpdate();
        }
    }

    public User getActiveUserByCpf(String cpf) throws SQLException {
        String sql = "SELECT * FROM users WHERE cpf = ? AND is_active = 1"; // Adiciona a condição
        try (Connection conn = ConnectionFactory.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cpf);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return createUserFromResultSet(rs);
                }
            }
        }
        return null;
    }

    public User getAnyUserByCpf(String cpf) throws SQLException {
        String sql = "SELECT * FROM users WHERE cpf = ?";

        try (Connection conn = ConnectionFactory.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, cpf);

            try (ResultSet rtst = stmt.executeQuery()) {
                if (rtst.next()) {
                    return createUserFromResultSet(rtst);
                }
            }

            return null;
        }
    }

    public User getUserByToken(String token) throws SQLException {
        String sql = "SELECT * FROM users WHERE token = ? AND is_active = 1 AND token_expires_at > datetime('now')";

        try (Connection conn = ConnectionFactory.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, token);

            try (ResultSet rtst = stmt.executeQuery()) {
                if (rtst.next()) {
                    return createUserFromResultSet(rtst);
                }
            }

            return null;
        }
    }

    public User getUserById(int id) throws SQLException {
        String sql = "SELECT * FROM users WHERE user_id = ?";

        try (Connection conn = ConnectionFactory.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setInt(1, id);

            try (ResultSet rtst = stmt.executeQuery()) {
                if (rtst.next()) {
                    return createUserFromResultSet(rtst);
                }
            }

            return null;
        }
    }

    public void updateUserToken(User user, String token, String expiresAt) throws SQLException {
        String sql = "UPDATE users SET token = ?, token_expires_at = ? WHERE user_id = ?";

        try (Connection conn = ConnectionFactory.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            if (token != null) {
                stmt.setString(1, token);
                stmt.setString(2, expiresAt);
            } else {
                stmt.setNull(1, java.sql.Types.VARCHAR);
                stmt.setNull(2, java.sql.Types.VARCHAR);
            }

            stmt.setInt(3, user.getId());

            stmt.executeUpdate();
        }
    }

    public void updateUserNameAndPassword(User user, String name, String password) throws SQLException {
        String sql = "UPDATE users SET name = ?, password = ? WHERE user_id = ?";

        try (Connection conn = ConnectionFactory.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setString(2, password);
            stmt.setInt(3, user.getId());

            stmt.executeUpdate();
        }
    }

    public void updateUserName(User user, String name) throws SQLException {

        String sql = "UPDATE users SET name = ? WHERE user_id = ?";

        try (Connection conn = ConnectionFactory.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);
            stmt.setInt(2, user.getId());

            stmt.executeUpdate();
        }
    }

    public void updateUserPassword(User user, String password) throws SQLException {
        String sql = "UPDATE users SET password = ? WHERE user_id = ?";

        try (Connection conn = ConnectionFactory.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, password);
            stmt.setInt(2, user.getId());

            stmt.executeUpdate();
        }
    }


    public void userDeposit(String token, Double sentValue) throws SQLException {
        String sql = "UPDATE users SET balance = balance + ? WHERE token = ?";

        try (Connection conn = ConnectionFactory.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, sentValue);
            stmt.setString(2, token);

            stmt.executeUpdate();
        }
    }


    public void addMoneyToUser(String cpf, Double value) throws SQLException {
        String sql = "UPDATE users SET balance = balance + ? WHERE cpf = ?";

        try (Connection conn = ConnectionFactory.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, value);
            stmt.setString(2, cpf);

            stmt.executeUpdate();
        }
    }


    public void subtractMoneyFromUser(String cpf, Double value) throws SQLException {
        String sql = "UPDATE users SET balance = balance - ? WHERE cpf = ?";

        try (Connection conn = ConnectionFactory.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, value);
            stmt.setString(2, cpf);

            stmt.executeUpdate();
        }
    }

    public void deactivateAndAnonymizeUser(String token) throws SQLException {
        String sql = """
            UPDATE users 
            SET 
                name = 'Usuário Removido', 
                password = NULL, 
                token = NULL, 
                is_active = 0
            WHERE 
                token = ?
        """;

        try (Connection conn = ConnectionFactory.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, token);
            stmt.executeUpdate();
        }
    }


    public List<User> getAllUsers() throws SQLException {
        List<User> list = new ArrayList<>();

        String sql = "SELECT * FROM users WHERE is_active = 1";

        try (Connection conn = ConnectionFactory.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)){
            try (ResultSet rtst = stmt.executeQuery()) {
                while(rtst.next()) {
                    list.add(createUserFromResultSet(rtst));
                }
            }
        }

        return list;
    }

    private User createUserFromResultSet(ResultSet rtst) throws SQLException {
        User user = new User();

        user.setId(rtst.getInt("user_id"));
        user.setName(rtst.getString("name"));
        user.setCpf(rtst.getString("cpf"));
        user.setPassword(rtst.getString("password"));
        user.setBalance(rtst.getDouble("balance"));
        user.setToken(rtst.getString("token"));
        user.setActive(rtst.getInt("is_active"));

        return user;
    }
}