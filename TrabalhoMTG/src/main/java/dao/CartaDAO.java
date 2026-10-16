package dao;

import model.carta.Carta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CartaDAO implements ICarta {

    @Override
    public int cadastrarCarta(Carta carta) {
        String sql = "INSERT INTO Usuario(nomeCarta, dano, resistencia, imagem) VALUES (?, ?, ?, ?)";

        try (Connection conexao = Conexao.getConexao(); PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, usuario.getNome());
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
    }

}
