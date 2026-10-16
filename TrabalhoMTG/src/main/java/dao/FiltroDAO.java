package dao;

import model.Usuario;
import model.carta.Cor;
import model.carta.Subtipo;
import model.carta.Supertipo;
import model.carta.Tipo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FiltroDAO implements IFiltro {

    @Override
    public List<Cor> listarCores() {
        String sql = "SELECT * FROM Cor";
        List<Cor> lista = new ArrayList<>();

        try (Connection conexao = Conexao.getConexao(); PreparedStatement stmt = conexao.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Cor cor = new Cor(rs.getInt("corId"), rs.getString("nomeCor"));
                lista.add(cor);
            }

        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return lista;
    }

    @Override
    public List<Tipo> listarTipos() {
        String sql = "SELECT * FROM Tipo";
        List<Tipo> lista = new ArrayList<>();

        try (Connection conexao = Conexao.getConexao(); PreparedStatement stmt = conexao.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Tipo tipo = new Tipo(rs.getInt("tipoId"), rs.getString("nomeTipo"));
                lista.add(tipo);
            }

        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return lista;
    }

    @Override
    public List<Supertipo> listarSupertipos() {
        String sql = "SELECT * FROM Supertipo";
        List<Supertipo> lista = new ArrayList<>();

        try (Connection conexao = Conexao.getConexao(); PreparedStatement stmt = conexao.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Supertipo supertipo = new Supertipo(rs.getInt("supertipoId"), rs.getString("nomeSupertipo"));
                lista.add(supertipo);
            }

        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return lista;
    }

    @Override
    public List<Subtipo> listarSubtipos() {
        String sql = "SELECT * FROM Subtipo";
        List<Subtipo> lista = new ArrayList<>();

        try (Connection conexao = Conexao.getConexao(); PreparedStatement stmt = conexao.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Subtipo subtipo = new Subtipo(rs.getInt("subtipoId"), rs.getString("nomeSubtipo"));
                lista.add(subtipo);
            }

        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return lista;
    }

}
