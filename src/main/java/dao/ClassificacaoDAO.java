package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Classificacao;

public class ClassificacaoDAO {

    public List<Classificacao> listarPorGrupo(int grupoId) {
        String sql = "SELECT p.nome, SUM(a.pontos) as total_pontos " +
                     "FROM aposta a " +
                     "JOIN pessoa p ON a.user_id = p.id " +
                     "WHERE a.grupo_id = ? " +
                     "GROUP BY a.user_id " +
                     "ORDER BY total_pontos DESC";

        List<Classificacao> lista = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, grupoId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(new Classificacao(
                        rs.getString("nome"), 
                        rs.getInt("total_pontos")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar classificação: " + e.getMessage(), e);
        }
        return lista;
    }
}