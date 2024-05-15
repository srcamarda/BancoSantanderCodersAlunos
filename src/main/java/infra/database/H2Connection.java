package infra.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class H2Connection {

    public static Connection getConnection() {
        Connection connection = null;

        try {
            Class.forName("org.h2.Driver");
            connection = DriverManager.getConnection("jdbc:h2:mem:test", "sa", "");

            try (Statement statement = connection.createStatement()) {
                String createTableConta = "CREATE TABLE CONTA (" +
                        "ID INT PRIMARY KEY," +
                        "CPF_CLIENTE VARCHAR(14)," +
                        "SALDO DOUBLE PRECISION," +
                        "SALDO_EMPRESTIMO DOUBLE PRECISION)";

                String createTableCliente = "CREATE TABLE CLIENTE (" +
                        "CPF VARCHAR(14) PRIMARY KEY," +
                        "NOME VARCHAR(50))";

                statement.execute(createTableConta);
                statement.execute(createTableCliente);
            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
}