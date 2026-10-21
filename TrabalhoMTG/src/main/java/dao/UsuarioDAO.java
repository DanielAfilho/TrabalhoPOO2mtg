package dao;

import model.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO implements IUsuario{

    @Override
    public int cadastrarUsuario(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO Usuario(nomeUsuario, senha) VALUES (?, ?) RETURNING usuarioId";

        try (Connection conexao = Conexao.getConexao()) {
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setString(1, usuario.getNomeUsuario());
            stmt.setString(2, usuario.getSenha());

            try (ResultSet rs = stmt.executeQuery()) {
                rs.next();
                return rs.getInt("usuarioId");
            }

        }

    }

    @Override
    public Usuario acessarUsuario(String nome) throws SQLException {
        // CORRIGIDO: a coluna é nomeUsuario (não existe Usuario.nome)
        String sql = "SELECT * FROM Usuario WHERE nomeUsuario = ?";

        try (Connection conexao = Conexao.getConexao()) {
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setString(1, nome);
            ResultSet rs = stmt.executeQuery() ;

            if (!rs.next()) {
                return null;
            }

            return new Usuario(rs.getInt("usuarioId"), rs.getString("nomeUsuario"), rs.getString("senha"));
        }

    }

    @Override
    public void atualizarUsuario(Usuario usuario) throws SQLException {
        String sql = "UPDATE Usuario SET nomeUsuario = ?, senha = ? WHERE usuarioId = ?";

        try (Connection conexao = Conexao.getConexao(); PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, usuario.getNomeUsuario());
            stmt.setString(2, usuario.getSenha());
            stmt.setInt(3, usuario.getId());
            stmt.executeUpdate();
        }

    }

    @Override
    public void deletarUsuario(Usuario usuario) throws SQLException {
        String sql = "DELETE FROM Usuario WHERE usuarioId = ?";

        try (Connection conexao = Conexao.getConexao()) {
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setInt(1, usuario.getId());
            stmt.executeUpdate();
        }

    }

    @Override
    public List<Usuario> listarUsuarios() throws SQLException {
        String sql = "SELECT usuarioId, nomeUsuario, senha FROM Usuario ORDER BY nomeUsuario";
        List<Usuario> lista = new ArrayList<>();

        try (Connection conexao = Conexao.getConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(new Usuario(rs.getInt("usuarioId"), rs.getString("nomeUsuario"), rs.getString("senha")));
            }
        }

        return lista;
    }

}