package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.GrupoAposta;
import model.Usuario;

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
        String sqlPartics = "INSERT INTO grupo_participantes (grupo_id, user_id) VALUES (?, ?)";

        try (Connection conn = ConnectionFactory.getConnection()) {
            conn.setAutoCommit(false); //as duas inserções vão juntas
        
            int grupoId;
            try (PreparedStatement stmtG = conn.prepareStatement(sqlGrupo, Statement.RETURN_GENERATED_KEYS)) {
                stmtG.setString(1, nome);
                stmtG.setInt(2, criadorId);
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
    }

    public void entrarGrupo(int grupoId, int usuariosId) {
        String sqlC = "SELECT COUNT(*) FROM grupo_participantes WHERE user_id = ?";
        String sqlIns = "INSERT INTO grupo_participantes (grupo_id, user_id) VALUES (?, ?)";

        //Continua apartir daqui porque tem que fazer 3 try 
        // Maximo de 5 grupos
        try (Connection conn = ConnectionFactory.getConnection()) {
            try (PreparedStatement stmtC = conn.prepareStatement(sqlC)) {
                stmtC.setInt(1, usuariosId);
                try (ResultSet rs = stmtC.executeQuery()) {
                    if (rs.next() && rs.getInt(1) >= 5) {
                        throw new RuntimeException("Limite de 5 grupos :>");
                    }
                }
            }

            try (PreparedStatement stmtIns = conn.prepareStatement(sqlIns)) {
                stmtIns.setInt(1, grupoId);
                stmtIns.setInt(2, usuariosId);
                stmtIns.executeUpdate();
            }
        } catch (SQLException e) {
            if (e.getMessage().contains("PRIMARY KEY")) {
                throw new RuntimeException("Já está nesse grupo");
            }
             throw new RuntimeException("Erro ao participar do grupo: " + e.getMessage(), e);
        }
    }
       
        public List<GrupoAposta> listarTodos() {
            List<GrupoAposta> lista = new ArrayList<>();
            String sql = "SELECT g.id, g.nome, p.id as c_id, p.nome as c_nome, p.login as c_login, p.role as c_role " +
            "FROM grupo_aposta g " +
            "JOIN pessoa p ON g.criador_id = p.id";

            try (Connection conn = ConnectionFactory.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
                    while (rs.next()) {
                        Usuario criador = new Usuario();
                        criador.setId(rs.getInt("c_id"));
                        criador.setNome(rs.getString("c_nome"));
                        criador.setLogin(rs.getString("c_login"));
                        criador.setRole(rs.getString("c_role"));

                        GrupoAposta grupo = new GrupoAposta();
                        grupo.setId(rs.getInt("id"));
                        grupo.setNome(rs.getString("nome"));
                        grupo.setCriador(criador);

                        carregarPartic(conn, grupo);

                        lista.add(grupo);
                    }
                } catch(SQLException e) {
                    throw new RuntimeException("Erro lista grupo: " + e.getMessage(), e);
                }
            return lista;
        }

        private void carregarPartic(Connection conn, GrupoAposta grupo) throws SQLException {
            String sql = "SELECT p.id, p.nome, p.login, p.role FROM grupo_participantes gp " +
            "JOIN pessoa p ON gp.user_id = p.id WHERE gp.grupo_id = ?";

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, grupo.getId());

                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                    Usuario p = new Usuario();
                    p.setId(rs.getInt("id"));
                    p.setNome(rs.getString("nome"));
                    p.setLogin(rs.getString("login"));
                    p.setRole(rs.getString("role"));
                    grupo.listar().add(p);
                }
                
            }
        }

    }
}
