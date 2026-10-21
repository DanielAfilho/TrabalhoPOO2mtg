package dao;

import model.Carta;
import model.Usuario;
import model.atributos.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class consultaDAO implements IConsulta {
    private String sql = " " ;

    @Override
    public List<Carta> consultaFalta(Usuario usuario) throws SQLException {
        sql = " " +
                 " SELECT c.cartaId, c.Imagem FROM Carta c "  +
                 sql +
                 " EXCEPT " +
                 " SELECT c.cartaId, c.Imagem  "  +
                 " FROM temCarta t JOIN Carta c ON c.cartaId = t.cartaId "  +
                 " WHERE t.usuarioId =  "  + usuario.getId();
        return executar();
    }
    
    @Override
    public List<Carta> consultaTem(Usuario usuario) throws SQLException {
        sql =  "  "  +
                 " SELECT c.cartaId, c.Imagem  "  +
                 " FROM temCarta t JOIN Carta c ON c.cartaId = t.cartaId "  +
                 " WHERE t.usuarioId =  "  + usuario.getId() +
                sql;
        return executar();
    }

    @Override
    public void consultaTipo(List<Tipo> lista) {

        for (Atributo a : lista) {
            sql = sql +
                     " INTERSECT "  +
                     " SELECT c.cartaId, c.Imagem "  +
                     " FROM temTipo t JOIN Carta c ON c.cartaId = t.cartaId "  +
                     " WHERE t.tipoId =  "  + a.getId();
        }

    }

    @Override
    public void consultaSubtipo(List<Subtipo> lista) {

        for (Atributo a : lista) {
            sql = sql +
                     " INTERSECT "  +
                     " SELECT c.cartaId, c.Imagem "  +
                     " FROM temSubtipo t JOIN Carta c ON c.cartaId = t.cartaId "  +
                     " WHERE t.subtipoId =  "  + a.getId();
        }

    }

    @Override
    public void consultaSupertipo(List<Supertipo> lista) {

        for (Atributo a : lista) {
            sql = sql +
                     " INTERSECT "  +
                     " SELECT c.cartaId, c.Imagem "  +
                     " FROM temSupertipo t JOIN Carta c ON c.cartaId = t.cartaId "  +
                     " WHERE t.supertipoId =  "  + a.getId();
        }

    }

    @Override
    public void consultaCor(List<Cor> lista) {

        for (Atributo a : lista) {
            sql = sql +
                     " INTERSECT "  +
                     " SELECT c.cartaId, c.Imagem "  +
                     " FROM temCor t JOIN Carta c ON c.cartaId = t.cartaId "  +
                     " WHERE t.corId =  "  + a.getId();
        }

    }

    @Override
    public void consultaDano(int valor) {
        sql = sql +
                 "  INTERSECT "  +
                 " SELECT c.cartaId, c.Imagem "  +
                 " FROM Carta c WHERE c.dano =  "  + valor;
    }

    @Override
    public void consultaResistencia(int valor) {
        sql = sql +
                 "  INTERSECT  "  +
                 "  SELECT c.cartaId, c.Imagem  "  +
                 "  FROM Carta c WHERE c.resistencia =  "  + valor;
    }

    @Override
    public void consultaNome(String nome) {
        sql = sql +
                 "  INTERSECT "  +
                 " SELECT c.cartaId, c.Imagem "  +
                 " FROM Carta c WHERE c.nomeCarta ~* " + "'" + nome +  "'" ;
    }

    public List<Carta> executar() throws SQLException {
        List<Carta> lista = new ArrayList<>();

        try (Connection conexao = Conexao.getConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(new Carta(rs.getInt( "cartaId" ), rs.getBytes( "imagem" )));
            }

        }
        finally {
            sql =  "";
        }

        return lista;
    }

}
