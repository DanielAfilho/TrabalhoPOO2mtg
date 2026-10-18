package dao;

import model.Carta;
import model.atributos.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CartaDAO implements ICarta {

    @Override
    public int cadastrarCarta(Carta carta) throws SQLException {
        String sql = "INSERT INTO Carta(nomeCarta, dano, resistencia, imagem) VALUES (?, ?, ?, ?) RETURNING cartaId";

        try (Connection conexao = Conexao.getConexao()) {
            conexao.setAutoCommit(false);
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setString(1, carta.getNomeCarta());
            stmt.setInt(2, carta.getDano());
            stmt.setInt(3, carta.getResistencia());
            stmt.setBytes(4, carta.getImagem());

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    carta.setId(rs.getInt("cartaId"));
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
                for (Cor c : carta.getCores()) {
                    ligar(conexao, carta.getId(), c.getId(), "TemCor", "corId");
                }

                conexao.commit();
            } catch (SQLException e) {
                conexao.rollback();
                throw e;
            }
        }

        return carta.getId();
    }

    @Override
    public Carta acessarCarta(String nome) throws SQLException {
        String consultaCarta = "SELECT * FROM Carta WHERE nomeCarta = ?";

        String consultaTipo =
                "SELECT * " +
                        "FROM TemTipo tt " +
                        "JOIN Tipo t ON t.tipoId = tt.tipoId " +
                        "WHERE tt.cartaId = ?";

        String consultaSupertipo =
                "SELECT * " +
                        "FROM TemSupertipo ts " +
                        "JOIN Supertipo s ON s.supertipoId = ts.supertipoId " +
                        "WHERE ts.cartaId = ?";

        String consultaSubtipo =
                "SELECT * " +
                        "FROM TemSubtipo ts " +
                        "JOIN Subtipo s ON s.subtipoId = ts.subtipoId " +
                        "WHERE ts.cartaId = ?";

        String consultaCor =
                "SELECT * " +
                        "FROM TemCor tc " +
                        "JOIN Cor c ON c.corId = tc.corId " +
                        "WHERE tc.cartaId = ?";

        try (Connection conexao = Conexao.getConexao()) {
            PreparedStatement stmt = conexao.prepareStatement(consultaCarta);
            stmt.setString(1, nome);
            ResultSet rs = stmt.executeQuery();

            if (!rs.next()) {
                return null;
            }

            Carta carta;
            carta = new Carta(rs.getInt("cartaId"), rs.getBytes("Imagem"), nome, rs.getInt("resistencia"), rs.getInt("dano"));

            stmt = conexao.prepareStatement(consultaTipo);
            stmt.setInt(1, carta.getId());
            List<Tipo> tipos = new ArrayList<>();
            rs = stmt.executeQuery();

            while (rs.next()) {
                Tipo t = new Tipo(rs.getInt("tipoId"), rs.getString("nomeTipo"));
                tipos.add(t);
            }

            carta.setTipos(tipos);

            stmt = conexao.prepareStatement(consultaSupertipo);
            stmt.setInt(1, carta.getId());
            List<Supertipo> supertipos = new ArrayList<>();
            rs = stmt.executeQuery();

            while (rs.next()) {
                Supertipo t = new Supertipo(rs.getInt("supertipoId"), rs.getString("nomeSupertipo"));
                supertipos.add(t);
            }

            carta.setSupertipos(supertipos);

            stmt = conexao.prepareStatement(consultaSubtipo);
            stmt.setInt(1, carta.getId());
            List<Subtipo> subtipos = new ArrayList<>();
            rs = stmt.executeQuery();

            while (rs.next()) {
                Subtipo t = new Subtipo(rs.getInt("subtipoId"), rs.getString("nomeSubtipo"));
                subtipos.add(t);
            }

            carta.setSubtipos(subtipos);

            stmt = conexao.prepareStatement(consultaCor);
            stmt.setInt(1, carta.getId());
            List<Cor> cores = new ArrayList<>();
            rs = stmt.executeQuery();

            while (rs.next()) {
                Cor t = new Cor(rs.getInt("corId"), rs.getString("nomeCor"));
                cores.add(t);
            }

            carta.setCores(cores);

            return carta;
        }
    }

    @Override
    public void atualizarCarta(Carta carta) throws SQLException {
        String sql = "UPDATE Carta Set nomeCarta = ?, dano = ?, resistencia = ?, Imagem = ? WHERE cartaId = ?; " +
                "DELETE FROM TemTipo WHERE cartaId = ?;" +
                "DELETE FROM TemSupertipo WHERE cartaId = ?;" +
                "DELETE FROM TemSubtipo WHERE cartaId = ?;" +
                "DELETE FROM TemCor WHERE cartaId = ?;";

        try (Connection conexao = Conexao.getConexao()) {
            conexao.setAutoCommit(false);
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setString(1, carta.getNomeCarta());
            stmt.setInt(2, carta.getDano());
            stmt.setInt(3, carta.getResistencia());
            stmt.setBytes(4, carta.getImagem());
            stmt.setInt(5, carta.getId());
            stmt.setInt(6, carta.getId());
            stmt.setInt(7, carta.getId());
            stmt.setInt(8, carta.getId());
            stmt.setInt(9, carta.getId());

            try {
                stmt.executeUpdate();

                for (Tipo t : carta.getTipos()) {
                    ligar(conexao, carta.getId(), t.getId(), "temTipo", "tipoId");
                }
                for (Subtipo t : carta.getSubtipos()) {
                    ligar(conexao, carta.getId(), t.getId(), "temSubtipo", "subtipoId");
                }
                for (Supertipo t : carta.getSupertipos()) {
                    ligar(conexao, carta.getId(), t.getId(), "temSupertipo", "supertipoId");
                }
                for (Cor c : carta.getCores()) {
                    ligar(conexao, carta.getId(), c.getId(), "TemCor", "corId");
                }

                conexao.commit();
            }
            catch (SQLException e) {
                conexao.rollback();
                throw e;
            }
        }

    }

    @Override
    public void deletarCarta(Carta carta) throws SQLException {
        String sql = "DELETE FROM Carta WHERE cartaId = ?";

        try (Connection conexao = Conexao.getConexao()) {
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setInt(1, carta.getId());
            stmt.executeUpdate();
        }
    }

    public void ligar(Connection conexao, int cartaId, int id, String nomeTabela, String nomeColuna) throws SQLException {
        String sql = "INSERT INTO " + nomeTabela + "(" + nomeColuna + ", cartaId ) VALUES(?,?)";
        PreparedStatement stmt = conexao.prepareStatement(sql);
        stmt.setInt(1, id);
        stmt.setInt(2, cartaId);
        stmt.executeUpdate();
    }

}