package dao;

import model.atributos.Cor;
import model.atributos.Subtipo;
import model.atributos.Supertipo;
import model.atributos.Tipo;

import java.sql.SQLException;
import java.util.List;

public interface IAtributo {

    public List<Cor> listarCores() throws SQLException;

    public List<Tipo> listarTipos() throws SQLException;

    public List<Supertipo> listarSupertipos() throws SQLException;

    public List<Subtipo> listarSubtipos() throws SQLException;

}
