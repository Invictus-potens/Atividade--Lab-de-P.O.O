package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import model.Campeonato;
import model.Clube;
import model.Partida;

public class PartidaDAO {
    
    // DDL - com gols

    public void createTableSeNaoExistir() {
        String sql = "CREATE TABLE IF NOT EXISTS partida (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "campeonato_id INTEGER NOT NULL, " +
                        "casa_id INTEGER NOT NULL, " +
                        "visitante_id INTEGER NOT NULL, " +
                        "resultado_def INTEGER DEFAULT 0, " +
                        "data_hora TEXT NOT NULL, " +
                        "gols_casa_real INTEGER, " +
                        "gols_visitante_real INTEGER, " +
                        "FOREIGN KEY(campeonato_id) REFERENCES Campeonato(id), " +
                        "FOREIGN KEY(casa_id) REFERENCES clube(id), " +
                        "FOREIGN KEY(visitante_id) REFERENCES clube(id) " +
                        ");";

        try (Connection conn = ConnectionFactory.getConnection();
             Statement stmt = conn.createStatement()) {
            
            stmt.execute(sql);

            } catch(SQLException e) {
                throw new RuntimeException("Falha criar table partida: " + e.getMessage(), e);
            }
        }

        public void registrarResultadoOfc(int partidaId, int golsCasa, int golsVisitante) {
            String sql = "UPDATE partida SET gols_casa_real = ?, gols_visitante_real = ?, resultado_def = 1 WHERE id = ?";

            try (Connection conn = ConnectionFactory.getConnection(); 
                PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setInt(1, golsCasa);
                    stmt.setInt(2, golsVisitante);
                    stmt.setInt(3, partidaId);
                    stmt.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException("Erro ao registrar o placar: " + e.getMessage(), e);
            }
        }
        
        public void cadastrar(Partida partida) {
            String sql = "INSERT INTO partida (campeonato_id, casa_id, visitante_id, data_hora, resultado_def) VALUES (?, ?, ?, ?, 0)";

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

        public List<Partida> listarTodas() {
            List<Partida> lista = new ArrayList<>();

            String sql = "SELECT p.id, p.data_hora, p.resultado_def, " +
                        "c.id AS camp_id, c.nome AS camp_nome, " +
                        "casa.id AS casa_id, casa.nome AS casa_nome, " +
                        "visi.id AS visi_id, visi.nome AS visi_nome " +
                        "FROM partida p " +
                        "JOIN campeonato c ON p.campeonato_id = c.id " +
                        "JOIN clube casa ON p.casa_id = casa.id " +
                        "JOIN clube visi ON p.visitante_id = visi.id " +
                        "WHERE p.resultado_def = 0 OR p.resultado_def IS NULL";
                        
                        try (Connection conn = ConnectionFactory.getConnection();
                            Statement stmt = conn.createStatement();
                            ResultSet rs = stmt.executeQuery(sql)) {

                                while (rs.next()) {
                                    Campeonato camp = new Campeonato();
                                    camp.setId(rs.getInt("camp_id"));
                                    camp.setNome(rs.getString("camp_nome"));

                                    Clube casa = new Clube();
                                    casa.setId(rs.getInt("casa_id"));
                                    casa.setNome(rs.getString("casa_nome"));

                                    Clube visitante = new Clube();
                                    visitante.setId(rs.getInt("visi_id"));
                                    visitante.setNome(rs.getString("visi_nome"));

                                    LocalDateTime dataHora = LocalDateTime.parse(rs.getString("data_hora"));

                                    Partida partida = new Partida(casa, visitante, dataHora, camp);
                                    partida.setId(rs.getInt("id"));

                                    if (rs.getInt("resultado_def") == 1) {
                                    partida.setResultado(new model.ResultadoPartida(0, 0)); 
                                    }

                                    lista.add(partida);
                                }
                            } catch (Exception e) {
                                throw new RuntimeException("Erro ao listar as partidas: " + e.getMessage(), e);
                            }
                            return lista;
        }
}
