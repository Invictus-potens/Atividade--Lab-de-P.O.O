package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import model.Pessoa;

public class PessoaDAO {

    /**
     * DDL - gerar a tabela física
     */
    public void createTableSeNaoExistir() {
        String sql = "CREATE TABLE IF NOT EXISTS pessoa (" +
                     "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                     "nome TEXT NOT NULL, " +
                     "login TEXT NOT NULL UNIQUE, " +
                     "senha TEXT NOT NULL" +
                     ");";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.execute();
            
        } catch (SQLException e) {
            throw new RuntimeException("Falha estrutural ao criar tabela Pessoa: " + e.getMessage(), e);
        }
    }

    /**
     * DML: inserção, leitura, atualização e exclusão
     */
    public void cadastrarP(Pessoa pessoa) {
        String sql = "INSERT INTO pessoa (nome, login, senha) VALUES (?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, pessoa.getNome());
            stmt.setString(2, pessoa.getLogin());
            stmt.setString(3, pessoa.getSenha());
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Falha ao cadastrar a pessoa no sistema: " + e.getMessage(), e);
        }
    }
}