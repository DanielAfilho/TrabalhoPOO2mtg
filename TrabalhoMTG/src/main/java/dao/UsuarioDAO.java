package dao;

import model.Usuario;

import java.sql.*;

public class UsuarioDAO implements IUsuario{

    @Override
    public int cadastrarUsuario(Usuario usuario) {
        String sql = "INSERT INTO estudante (nomeUsuario, senha) VALUES (?, ?) RETURNING estudanteId";

        try (Connection conexao = Conexao.getConexao(); PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, usuario.getNomeUsuario());
            stmt.setString(2, usuario.getSenha());

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    return rs.getInt("usuarioId");
                }

            }

        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return -1;
    }

    @Override
    public Usuario acessarUsuario(String nome) {
        String sql = "SELECT * FROM Usuario WHERE Usuario.nome = ?";

        try (Connection conexao = Conexao.getConexao(); PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, nome);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Usuario(rs.getInt("usuarioId"), rs.getString("nomeUsuario"), rs.getString("senha"));
                }
            }

        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return new Usuario();
    }

    @Override
    public void atualizarUsuario(Usuario usuario) {
        String sql = "UPDATE Usuario SET nomeUsuario = ?, senha = ? WHERE usuarioId = ?";

        try (Connection conexao = Conexao.getConexao(); PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, usuario.getNomeUsuario());
            stmt.setString(2, usuario.getSenha());
            stmt.setInt(3, usuario.getId());
            stmt.executeUpdate();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void deletarUsuario(Usuario usuario) throws SQLException {
        String sql = "DELETE FROM Usuario WHERE usuarioId = ?";

        try (Connection conexao = Conexao.getConexao(); PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, usuario.getId());
            stmt.executeUpdate();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
