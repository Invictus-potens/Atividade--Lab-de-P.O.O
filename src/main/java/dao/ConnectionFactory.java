package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
    
    // conexão para um arquivo local na raiz do projeto
    private static final String URL = "jdbc:sqlite:sistema_apostas.db";

    /**
     * Tenta estabelecer e retornar uma conexão ativa com o arquivo sqLite.
     * @return 
     * @throws RuntimeException 
     */
    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL);
        } catch (SQLException e) {
            throw new RuntimeException("Falha crítica ao estabelecer conexão com o arquivo SQLite: " + e.getMessage(), e);
        }
    }
}