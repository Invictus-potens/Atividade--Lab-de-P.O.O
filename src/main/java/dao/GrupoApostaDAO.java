package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import model.GrupoAposta;

public class GrupoApostaDAO {
    
    public void createTableSeNaoExistir() {
        String sql = "CREATE TABLE IF NOT EXISTS grupo_aposta (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "nome TEXT NOT NULL, " +
                        "criador_id INTEGER NOT NULL, " +
                        "FOREIGN KEY(criador_id) REFERENCES pessoa(id)" +
                        ");";
                    
        String sqlPartic = "CREATE TABLE IF NOT EXISTS grupo_participantes (" +
                                    "grupo_id INTEGER, " +
                                    "user_id INTEGER, " +
                                    "PRIMARY KEY (grupo_id, user_id), " +
                                    "FOREIGN KEY (grupo_id) REFERENCES grupo_aposta(id), " +
                                    "FOREIGN KEY (user_id) REFERENCES pessoa(id) " +
                                    ");";

            try (Connection conn = ConnectionFactory.getConnection();
                Statement stmt = conn.createStatement()) {

                    stmt.execute(sql);
                    stmt.execute(sqlPartic);
                } catch (SQLException e){
                    throw new RuntimeException("Tabela GrupoAposta não criada: " + e.getMessage(), e);
                }
            }
    
    public void salvar(String nome, int criadorId) {
        String sqlGrupo = "INSERT INTO grupo_aposta (nome, criador_id) VALUES (?, ?)";
        String sqlPartics = "INSERT INTO grupo_participantes (grupo_id, usuario_id) VALUES (?, ?)";

        try (Connection conn = ConnectionFactory.getConnection()) {
            conn.setAutoCommit(false); //as duas inserções vão juntas
        
            int grupoId;
            try (PreparedStatement stmtG = conn.prepareStatement(sqlGrupo, Statement.RETURN_GENERATED_KEYS)) {
                stmtG.setString(1, nome);
                stmtG.setInt(2, criador_id);
                stmtG.executeUpdate();

                try (ResultSet rs = stmtG.getGeneratedKeys()) {
                    if (rs.next()) {
                        grupoId = rs.getInt(1);
                    } else {
                        throw new SQLException("Falha ao conseguir ID de grupo");
                    }
                }
            }

            try (PreparedStatement stmtP = conn.prepareStatement(sqlPartics)) {
                stmtP.setInt(1, grupoId);
                stmtP.setInt(2, criadorId);
                stmtP.executeUpdate();
            }

            conn.commit(); //Envia
    } catch (SQLException e) {
        throw new RuntimeException("Erro ao salvar grupo banco: " + e.getMessage(), e);
    }

    public void entrarGrupo(int grupoId, int usuariosId) {
        String sqlC = "SELECT COUNT(*) FROM grupo_participante WHERE user_id = ?";
        String sqlIns = "INSERT INTO grupo_participantes (grupo_id, user_id) VALUES (?, ?)";

        //Continua apartir daqui porque tem que fazer 2 try 
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
}
