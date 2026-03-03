package newpixserver.database;

public class QueryFactory {
	public static String getCreateTableUsersQuery(DatabaseEnum options) {
        switch (options) {    
            case MYSQL:
                return "CREATE TABLE IF NOT EXISTS users("
                		+ "user_id INT PRIMARY KEY AUTO_INCREMENT,"	
                		+ "name VARCHAR(120) NOT NULL,"
                		+ "cpf VARCHAR(14) NOT NULL UNIQUE,"
                		+ "password VARCHAR(120), "
                		+ "balance DECIMAL(10, 2) NOT NULL DEFAULT 0.00,"
                		+ "token VARCHAR(200)"
                        + "is_active INT DEFAULT 1 NOT NULL"
                		+ ");";
            case SQLITE:
            	 return "CREATE TABLE IF NOT EXISTS users("
          				 + "user_id INTEGER PRIMARY KEY AUTOINCREMENT,"
          				 + "name VARCHAR(120) NOT NULL,"
          				 + "cpf VARCHAR(14) NOT NULL UNIQUE,"
          				 + "password VARCHAR(120), "
          				 + "balance DECIMAL(10, 2) NOT NULL DEFAULT 0.00,"
          				 + "token VARCHAR(200),"
                         + "is_active INTEGER DEFAULT 1 NOT NULL,"
                         + "token_expires_at VARCHAR(255)"
          				 + ");";
            default:
                throw new IllegalArgumentException("Opção de BD inválida");
        }
    }

    public static String getCreateTableUserTransactionsQuery(DatabaseEnum options) {
        switch (options) {
            case MYSQL:
                return "CREATE TABLE IF NOT EXISTS user_transactions("
                        + "transaction_id INT PRIMARY KEY AUTO_INCREMENT,"
                        + "cpf_origin VARCHAR(14) NOT NULL,"
                        + "amount DECIMAL(10, 2) NOT NULL NOT NULL,"
                        + "cpf_destination VARCHAR(14) NOT NULL,"
                        + "date VARCHAR(25) NOT NULL,"
                        + "FOREIGN KEY (cpf_origin) REFERENCES users (cpf)"
                        + ");";
            case SQLITE:
                return "CREATE TABLE IF NOT EXISTS user_transactions("
                        + "transaction_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + "cpf_origin VARCHAR(14) NOT NULL,"
                        + "amount DECIMAL(10, 2) NOT NULL NOT NULL,"
                        + "cpf_destination VARCHAR(14) NOT NULL,"
                        + "date VARCHAR(25) NOT NULL,"
                        + "FOREIGN KEY (cpf_origin) REFERENCES users (cpf)"
                        + ");";
            default:
                throw new IllegalArgumentException("Opção de BD inválida");
        }
    }
}
