package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ColecaoDAO implements IColecao {

    @Override
    public void adicionarCarta(int usuarioId, int cartaId) throws SQLException {
        String sql = "INSERT INTO TemCarta(usuarioId, cartaId) VALUES (?, ?)";

        try (Connection conexao = Conexao.getConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, usuarioId);
            stmt.setInt(2, cartaId);
            stmt.executeUpdate();
        }
    }

    @Override
    public void removerCarta(int usuarioId, int cartaId) throws SQLException {
        String sql = "DELETE FROM TemCarta WHERE usuarioId = ? AND cartaId = ?";

        try (Connection conexao = Conexao.getConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, usuarioId);
            stmt.setInt(2, cartaId);
            stmt.executeUpdate();
        }
    }

}