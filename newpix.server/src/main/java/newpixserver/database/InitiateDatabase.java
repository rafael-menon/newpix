package newpixserver.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import newpixserver.database.entities.User;
import newpixserver.security.PasswordHasher;
import newpixserver.security.TokenGenerator;

public class InitiateDatabase {
	private static Connection connection;	

	private static void getConnection(Connection connectionUrl) {
		connection = connectionUrl;
	}

    public static void createDatabase(Connection connectionUrl, DatabaseEnum chosenDb) {
        getConnection(connectionUrl);

        createTableUsers(chosenDb);
        createTableUserTransaction(chosenDb);
    }

	private static void createTableUsers(DatabaseEnum chosenDb) {
		try {
			final String createTable = QueryFactory.getCreateTableUsersQuery(chosenDb);
			
			PreparedStatement stmt = connection.prepareStatement(createTable);
			
			stmt.executeUpdate();

			if(!checkIfTableExists()) {
				List<User> users = createUsers(10);

				for(User user : users) {
                    insertUsers(user);
                }
			}
		} catch(SQLException e) {
			System.out.println("Erro ao criar table users: " + e.getMessage());
		}
	}

    private static void createTableUserTransaction(DatabaseEnum chosenDb) {
        try {
            final String createTable = QueryFactory.getCreateTableUserTransactionsQuery(chosenDb);

            PreparedStatement stmt = connection.prepareStatement(createTable);

            stmt.executeUpdate();
        } catch(SQLException e) {
            System.out.println("Erro ao criar table users: " + e.getMessage());
        }
    }

	private static List<User> createUsers(int desiredUserCount) {
		final Random random = new Random();
		final String[] names = {"João", "Maria", "Carlos", "Ana", "Pedro", "Juliana", "Marcos", "Fernanda", "Ricardo", "Beatriz"};
		final String[] surnames = {"Silva", "Oliveira", "Pereira", "Costa", "Souza", "Santos", "Lima", "Almeida", "Gomes", "Rocha"};
		List<User> users = new ArrayList<>();

		for(int count = 0; count < desiredUserCount; count++) {
			User user = new User();
			String name = names[random.nextInt(names.length)] + " " + surnames[random.nextInt(surnames.length)];
			user.setName(name);

			StringBuilder cpfBuilder = new StringBuilder();
			for(int i = 0; i < 14; i++) {
				switch(i) {
				case 3, 7: cpfBuilder.append('.');
				break;
				case 11: cpfBuilder.append('-');
				break;
				default: cpfBuilder.append(random.nextInt(9));
				break;
				}
			}

			user.setCpf(cpfBuilder.toString());

			String textPassword = "password" + random.nextInt(10);
			user.setPassword(PasswordHasher.hashPassword(textPassword));

			double balance = random.nextDouble() * 5000;
			user.setBalance(Math.round(balance * 100.0) / 100.0);
			user.setToken(TokenGenerator.generateSecureRandomToken());

			users.add(user);
		}

		return users;
	}

	private static void insertUsers(User user) throws SQLException {
		final String insertUsers = "INSERT INTO users (name, cpf, password, balance) VALUES (?, ?, ?, ?)";

		PreparedStatement stmt = connection.prepareStatement(insertUsers);

		if (checkCpf(user.getCpf()) == null) {
			stmt.setString(1, user.getName());
			stmt.setString(2, user.getCpf());
			stmt.setString(3, user.getPassword());
			stmt.setDouble(4, user.getBalance());

			stmt.executeUpdate();
		}	
	}

	private static User checkCpf(String cpf) throws SQLException {
		final String searchCpf = "SELECT * FROM users WHERE cpf = ?";

		PreparedStatement stmt = connection.prepareStatement(searchCpf);

		stmt.setString(1, cpf);

		ResultSet rtst = stmt.executeQuery();

		if(rtst.next()) {
			User user = new User();
			user.setCpf(rtst.getString("cpf"));
			return user;
		}

		return null;
	}

	private static boolean checkIfTableExists() throws SQLException {
		final String selectAll = "SELECT * FROM users"; 

		PreparedStatement stmt = connection.prepareStatement(selectAll);

		ResultSet rtst = stmt.executeQuery();

		while(rtst.next()) {
			return true;
		}

		return false;
	}
}
