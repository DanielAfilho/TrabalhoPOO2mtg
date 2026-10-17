package dao;

import model.Usuario;
import model.carta.Carta;
import model.carta.Subtipo;
import model.carta.Supertipo;
import model.carta.Tipo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CartaDAO implements ICarta {

    @Override
    public Carta cadastrarCarta(Carta carta) {
        String sql = "INSERT INTO Carta(nomeCarta, dano, resistencia, imagem) VALUES (?, ?, ?, ?) RETURNING cartaId";

        try (Connection conexao = Conexao.getConexao(); PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, carta.getNomeCarta());
            stmt.setInt(2, carta.getDano());
            stmt.setInt(3, carta.getResistencia());
            stmt.setBytes(4, carta.getImagem());

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    carta.setId(rs.getInt("cartaId"));
                }

            }

            for (Tipo t : carta.getTipos()) {
                ligar(conexao, carta.getId(), t.getId(), "temTipo", "tipoId");
            }
            for (Subtipo t : carta.getSubtipos()) {
                ligar(conexao, carta.getId(), t.getId(), "temSubtipo", "subtipoId");
            }
            for (Supertipo t : carta.getSupertipos()) {
                ligar(conexao, carta.getId(), t.getId(), "temSupertipo", "supertipoId");
            }


        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public Carta acessarCarta(int id) throws SQLException {
        String sql = "RETURN * FROM Carta WHERE cartaId = ?";

        try (Connection conexao = Conexao.getConexao(); PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    return new Carta(id, );
                }

            }

        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return new Carta();
    }

    @Override
    public Carta atualizarCarta(Carta carta) throws SQLException {
        deletarCarta(carta);
        carta.setId(cadastrarCarta(carta));
        return carta;
    }

    @Override
    public void deletarCarta(Carta carta) throws SQLException {

    }

    public void ligar(Connection conexao, int cartaId, int id, String nomeTabela, String nomeColuna) throws SQLException {
        String sql = "INSERT INTO " + nomeTabela + "(" + nomeColuna + ", cartaId ) VALUES(?,?)";
        PreparedStatement stmt = conexao.prepareStatement(sql);
        stmt.setInt(1, id);
        stmt.setInt(2, cartaId);
        stmt.executeUpdate();
    }

}
