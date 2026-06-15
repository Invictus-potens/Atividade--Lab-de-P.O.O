package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import model.Partida;

public class PartidaDAO {
    
    // DDL - mesmo de todos

    public void createTableSeNaoExistir() {
        String sql = "CREATE TABLE IF NOT EXISTS partida (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT " +
                        "campeonato_id INTEGER NOT NULL" +
                        "casa_id INTEGER NOT NULL" +
                        "visitante_id INTEGER NOT NULL" +
                        "data_hora TEXT NOT NULL" +
                        "gols_casa INTEGER" +
                        "gols_visitante INTEGER" +
                        "FOREIGN KEY(campeonato_id) REFERENCES Campeonato(id), " +
                        "FOREIGN KEY(casa_id) REFERENCES clube(id), " +
                        "FOREIGN KEY(visitante_id) REFERENCES clube(id), " +
                        ");";

        try (Connection conn = ConnectionFactory.getConnection();
             Statement stmt = conn.createStatement()) {
            
            stmt.execute(sql);

            } catch(SQLException e) {
                throw new RuntimeException("Falha criar table partida: " + e.getMessage(), e);
            }
        }
        
        public void cadastrar(Partida partida) {
            String sql = "INSERT INTO partida (campeonato_id, casa_id, visitante_id, data_hora) VALUES (?, ?, ?, ?)";

            try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

                    stmt.setInt(1, partida.getCampeonato().getId());
                    stmt.setInt(2, partida.getCasa().getId());
                    stmt.setInt(3, partida.getVisitante().getId());
                    stmt.setString(4, partida.getDataHora().toString());

                    stmt.executeUpdate();
                } catch (SQLException ex) {
                    throw new RuntimeException("Falha no cadastro partida: " + ex.getMessage(), ex);
                }
        }
}
