package dao;

import model.carta.Cor;
import model.carta.Subtipo;
import model.carta.Supertipo;
import model.carta.Tipo;

import java.sql.SQLException;
import java.util.List;

public interface IFiltro {

    public List<Cor> listarCores() throws SQLException;

    public List<Tipo> listarTipos() throws SQLException;

    public List<Supertipo> listarSupertipos() throws SQLException;

    public List<Subtipo> listarSubtipos() throws SQLException;

}
