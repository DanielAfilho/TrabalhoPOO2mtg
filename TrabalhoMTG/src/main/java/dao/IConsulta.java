package dao;

import dao.escopo.EscopoColecao;
import model.Carta;
import model.FiltroCartas;
import model.Usuario;

import java.sql.SQLException;
import java.util.List;

public interface IConsulta {

    /**
     * Busca cartas (id e imagem) aplicando o escopo escolhido (STRATEGY: cartas do usuário ou faltantes)
     * e os filtros recebidos (BUILDER: {@link FiltroCartas}).
     */
    List<Carta> buscar(Usuario usuario, EscopoColecao escopo, FiltroCartas filtro) throws SQLException;

}
