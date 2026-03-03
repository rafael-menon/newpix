package newpixserver.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionFactory {
    private static final HikariDataSource dataSource;
    private static final DatabaseEnum chosenDb = DatabaseEnum.SQLITE;

    static {
        try {
            String propertiesFile = switch (chosenDb) {
                case SQLITE -> "sqlite.properties";
                case MYSQL -> "mysql.properties";
            };


            Properties props = new Properties();
            try (InputStream input = ConnectionFactory.class.getClassLoader().getResourceAsStream(propertiesFile)) {
                if (input == null) {
                    throw new RuntimeException("Não foi possível encontrar o arquivo: " + propertiesFile);
                }

                props.load(input);
            }

            HikariConfig config = new HikariConfig(props);
            dataSource = new HikariDataSource(config);

            System.out.println("Verificando e criando o schema do banco de dados...");
            try (Connection conn = dataSource.getConnection()) {
                InitiateDatabase.createDatabase(conn, chosenDb);
                System.out.println("Schema do banco de dados inicializado com sucesso.");
            } catch (SQLException e) {
                throw new RuntimeException("Falha CRÍTICA ao inicializar o schema do banco de dados.", e);
            }
        } catch(IOException e) {
            throw new RuntimeException("Falha ao carregar o arquivo de configuração do banco de dados.", e);
        }
    }

	public static Connection getConnection() throws SQLException {
		return dataSource.getConnection();
	}

    public static void closeDataSource() {
        if (dataSource != null) {
            dataSource.close();
        }
    }
}
