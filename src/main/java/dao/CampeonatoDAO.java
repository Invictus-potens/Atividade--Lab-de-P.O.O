package dao;

import java.sql.Connection; 
import java.sql.PreparedStatement;
import java.sql.SQLException;

import model.Campeonato;
import model.Clube;
public class CampeonatoDAO {

    //DDL

    public void createTableSeNaoExistir() {
        String sql = "CREATE TABLE IF NOT EXISTS Campeonato (" +
                     "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                     "nome TEXT NOT NULL" +
                     ");";

        String sqlcamp_club = "CREATE TABLE IF NOT EXISTS campeonato_clube (" +
                                "campeonato_id INTEGER NOT NULL, " +
                                "clube_id INTEGER NOT NULL, " +
                                "PRIMARY KEY (campeonato_id, clube_id), " +
                                "FOREIGN KEY (campeonato_id) REFERENCES campeonato(id), " +
                                "FOREIGN KEY (clube_id) REFERENCES clube(id) " +
                                ");";

        try (Connection conn = ConnectionFactory.getConnection();
             java.sql.Statement stmt = conn.createStatement()) {
            
            stmt.execute(sql);
            stmt.execute(sqlcamp_club);
            
        } catch (SQLException e) {
            throw new RuntimeException("Erro estrutural ao inicializar a tabela Campeonato: " + e.getMessage(), e);
        }
    }

    /**
     * DML
     */
    public void cadastrar(Campeonato campeonato) {
        String sql = "INSERT INTO campeonato (nome) VALUES (?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, campeonato.getNome());
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Falha de persistência ao cadastrar campeonato: " + e.getMessage(), e);
        }
    }

    public void clubeCamp(Campeonato campeonato, Clube clube) {
        String sql = "INSERT INTO campeonato_clube (campeonato_id, clube_id) VALUES (?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, campeonato.getId());
            stmt.setInt(2, clube.getId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Falha no banco com clube ao campeonato: " + e.getMessage(), e);
        }
    }

    //DQL Inner join

    public java.util.List<model.Clube> listarClubesCampeonatos(int idCampeonato) {
        java.util.List<model.Clube> clubesVinculados = new java.util.ArrayList<>();
        
        String sql = "SELECT clube.id, clube.nome, clube.cidade FROM clube INNER JOIN campeonato_clube ON clube.id = campeonato_clube.clube_id WHERE campeonato_clube.campeonato_id = ?";

        try (java.sql.Connection conn = dao.ConnectionFactory.getConnection();
             java.sql.PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idCampeonato);

            try (java.sql.ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    model.Clube clube = new model.Clube(
                        rs.getString("nome"),
                        rs.getString("cidade")
                    );
                    clube.setId(rs.getInt("id"));
                    clubesVinculados.add(clube);
                }
            }

        } catch (java.sql.SQLException e) {
            throw new RuntimeException("Falha ao buscar os clubes do campeonato: " + e.getMessage(), e);
        }

        return clubesVinculados;
    }

    public java.util.List<model.Campeonato> listarAll() {
        java.util.List<model.Campeonato> campeonatos = new java.util.ArrayList<>();
        String sql = "SELECT * FROM campeonato";

        try (java.sql.Connection conn = ConnectionFactory.getConnection();
             java.sql.PreparedStatement stmt = conn.prepareStatement(sql);
             java.sql.ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                model.Campeonato campeonato = new model.Campeonato(
                    rs.getString("nome")
                );
                campeonato.setId(rs.getInt("id"));
                campeonatos.add(campeonato);
            }

        } catch (java.sql.SQLException e) {
            throw new RuntimeException("Falha ao consultar Campeonatos no banco: " + e.getMessage(), e);
        }

        return campeonatos;
    }
}