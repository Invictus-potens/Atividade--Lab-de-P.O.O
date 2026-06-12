package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.management.RuntimeErrorException;

import model.Administrador;
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
                     "senha TEXT NOT NULL," +
                     "role TEXT NOT NULL" +
                     ");";

        try (Connection conn = ConnectionFactory.getConnection();
             Statement stmt = conn.createStatement()) {
            
            stmt.execute(sql);
            criarAdminPadrao();
            
        } catch (SQLException e) {
            throw new RuntimeException("Falha estrutural ao criar tabela Pessoa: " + e.getMessage(), e);
        }
    }

    /**
     * DML: inserção, leitura, atualização e exclusão
     */

    public void criarAdminPadrao() {
        String selSql = "SELECT COUNT(*) AS total FROM pessoa";
        String insSql = "INSERT INTO pessoa (nome, login, senha, role) VALUES ('Admin Mestre', 'admin', 'admin123', 'Administrator')";

        try (Connection conn = ConnectionFactory.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(selSql)) {

                if (rs.next() && rs.getInt("Total") == 0) {
                    stmt.executeUpdate(insSql);
                    System.out.println("Admin padrão feito: login 'admin', senha 'admin123'");
                }
            } catch (SQLException e) {
                System.out.println("Erro ao verficar admin padrão: " + e.getMessage());
            }
    } 

    public Pessoa autenticar(String login, String senha) {
        String sql = "SELECT * FROM pessoa WHERE login = ? AND Senha = ?";

        try (Connection conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, login);
                stmt.setString(2, senha);

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        String role = rs.getString("role");
                        Pessoa userAutenticado;

                        if (role.equals("administrador")) {
                            userAutenticado = new model.Administrador();
                        } else {
                            userAutenticado = new model.Usuario();
                        }

                        userAutenticado.setId(rs.getInt("id"));
                        userAutenticado.setNome(rs.getString("nome"));
                        userAutenticado.setLogin(rs.getString("login"));
                        userAutenticado.setSenha(rs.getString("senha"));
                        userAutenticado.setRole(("role"));

                        return userAutenticado;
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException("Erro no login: " + e.getMessage(), e);
            }
            return null;
    }

    public void cadastrarP(Pessoa pessoa) {
        String sql = "INSERT INTO pessoa (nome, login, senha) VALUES (?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, pessoa.getNome());
            stmt.setString(2, pessoa.getLogin());
            stmt.setString(3, pessoa.getSenha());
            stmt.setString(4, pessoa.getRole());
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Falha ao cadastrar a pessoa no sistema: " + e.getMessage(), e);
        }
    }
}