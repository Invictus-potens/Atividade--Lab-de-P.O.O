package dao;

import java.sql.Connection; // Ajuste caso o nome da sua classe modelo seja diferente
import java.sql.PreparedStatement;
import java.sql.SQLException;

import model.Clube;

public class ClubeDAO {

    public void createTabelSeNaoExistir() {
        String sql = "CREATE TABLE IF NOT EXISTS clube (" +
                     "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                     "nome TEXT NOT NULL, " +
                     "cidade TEXT" +
                     ");";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.execute();
            
        } catch (SQLException e) {
            throw new RuntimeException("Erro estrutural ao inicializar a tabela Clube: " + e.getMessage(), e);
        }
    }

    /**
     * Rotina DML responsável por injetar o estado do objeto no SGBD.
     */
    public void cadastrar(Clube clube) {
        String sql = "INSERT INTO clube (nome, cidade) VALUES (?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, clube.getNome());
            stmt.setString(2, clube.getCidade());
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Falha de persistência ao cadastrar clube: " + e.getMessage(), e);
        }
    }
}