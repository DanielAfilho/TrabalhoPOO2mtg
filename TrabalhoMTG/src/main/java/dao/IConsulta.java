package dao;

import dao.escopo.EscopoColecao;
import model.Carta;
import model.FiltroCartas;
import model.Usuario;

import java.sql.SQLException;
import java.util.List;

public interface IConsulta {

    List<Carta> buscar(Usuario usuario, EscopoColecao escopo, FiltroCartas filtro) throws SQLException;

}
