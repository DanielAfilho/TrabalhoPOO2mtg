package dao;

import model.atributos.Cor;
import model.atributos.Subtipo;
import model.atributos.Supertipo;
import model.atributos.Tipo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AtributoDAO implements IAtributo {

    @Override
    public List<Cor> listarCores() throws SQLException {
        String sql = "SELECT * FROM Cor";
        List<Cor> lista = new ArrayList<>();

        try (Connection conexao = Conexao.getConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(new Cor(rs.getInt("corId"), rs.getString("nomeCor")));
            }
        }

        return lista;
    }

    @Override
    public List<Tipo> listarTipos() throws SQLException {
        String sql = "SELECT * FROM Tipo";
        List<Tipo> lista = new ArrayList<>();

        try (Connection conexao = Conexao.getConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(new Tipo(rs.getInt("tipoId"), rs.getString("nomeTipo")));
            }
        }

        return lista;
    }

    @Override
    public List<Supertipo> listarSupertipos() throws SQLException {
        String sql = "SELECT * FROM Supertipo";
        List<Supertipo> lista = new ArrayList<>();

        try (Connection conexao = Conexao.getConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(new Supertipo(rs.getInt("supertipoId"), rs.getString("nomeSupertipo")));
            }
        }

        return lista;
    }

    @Override
    public List<Subtipo> listarSubtipos() throws SQLException {
        String sql = "SELECT * FROM Subtipo";
        List<Subtipo> lista = new ArrayList<>();

        try (Connection conexao = Conexao.getConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(new Subtipo(rs.getInt("subtipoId"), rs.getString("nomeSubtipo")));
            }
        }

        return lista;
    }

}