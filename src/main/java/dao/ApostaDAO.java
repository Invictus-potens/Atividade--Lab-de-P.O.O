package dao;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.Aposta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ApostaDAO {
    
    public void createTableSeNaoExixtir() {
        String sql = "CREATE TABLE IF NOT EXISTS aposta (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " + 
                    "user_id INTEGER NOT NULL , " +
                    "grupo_id INTEGER NOT NULL , " +
                    "partida_id INTEGER NOT NULL , " +
                    "gols_casa INTEGER NOT NULL , " +
                    "gols_visitante INTEGER NOT NULL , " +
                    "pontos INTEGER DEFAULT 0, " +
                    "FOREIGN KEY (user_id) REFERENCES pessoa(id), " +
                    "FOREIGN KEY (grupo_id) REFERENCES grupo_aposta(id), " +
                    "FOREIGN KEY (partida_id) REFERENCES partida(id), " +
                    "UNIQUE(user_id, grupo_id, partida_id));";

        try (Connection conn = ConnectionFactory.getConnection();
            Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Erro na table Aposta: " + e.getMessage(), e);
        }
    }

    public void registrar(int userId, int grupoId, int partidaId, int golsCasa, int golsVisi) {
        String sql = "INSERT INTO aposta (user_id, grupo_id, partida_id, gols_casa, gols_visitante) VALUES (?, ?, ?, ?, ?);";

        try (Connection conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setInt(1, userId);
                stmt.setInt(2, grupoId);
                stmt.setInt(3, partidaId);
                stmt.setInt(4, golsCasa);
                stmt.setInt(5, golsVisi);

                stmt.executeUpdate();

            } catch (SQLException e) {
                if (e.getMessage().contains("UNIQUE")) {
                    throw new RuntimeException("Já tem uma aposta nessa partida com esse grupo");
                }
                throw new RuntimeException("Falha ao registrar aposta no banco");
            }
    }

    public List<Aposta> listarApostaPartida(int partidaId) {
        List<Aposta> lista = new ArrayList<>();
        String sql = "SELECT id, gols_casa, gols_visitante FROM aposta WHERE partida_id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setInt(1, partidaId);

                try (ResultSet rs= stmt.executeQuery()) {
                    while(rs.next()) {
                        Aposta aposta = new Aposta();
                        aposta.setId(rs.getInt("id"));
                        aposta.setGolsCasaApostado(rs.getInt("gols_casa"));
                        aposta.setGolsVisitante(rs.getInt("gols_visitante"));

                        lista.add(aposta);
                    }
                }
    } catch (SQLException e) {
        throw new RuntimeException("Erro busca aposta partida: " + e.getMessage(), e);
    }
    return lista;
    }

    public void salvarPontos(int apostaId, int points) {
        String sql= "UPDATE aposta SET pontos = ? WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, points);
                stmt.setInt(2, apostaId);
                stmt.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException("Erro salvar os ponto: " + e.getMessage(), e);
            }
    }
}
