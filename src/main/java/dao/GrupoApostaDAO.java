package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import model.GrupoAposta;

public class GrupoApostaDAO {
    
    public void createTabelSeNaoExistir() {
        String sql = "CREATE TABLE IF NOT EXISTS grupo_aposta (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "nome TEXT NOT NULL, " +
                        "criador_id INTEGER NOT NULL, " +
                        "FOREIGN KEY(criador_id) REFERENCES pessoa(id)" +
                        ");";

            try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

                    stmt.execute();
                } catch (SQLException e){
                    throw new RuntimeException("Tabela GrupoAposta não criada: " + e.getMessage(), e);
                }
            }

    public void cadastrarG(GrupoAposta grupo) {
        String sql = "INSERT INTO grupo_aposta (nome, criador_id) VALUES (?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, grupo.getNome());
                stmt.setInt(2, grupo.getCriador().getId());
                stmt.executeUpdate();

             } catch (SQLException e) {
                throw new RuntimeException("Falha ao cadastrar grupo: " + e.getMessage(), e);
             }

    }
}
