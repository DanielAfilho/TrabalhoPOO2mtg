package dao;

import model.Carta;
import model.Usuario;
import model.atributos.Cor;
import model.atributos.Subtipo;
import model.atributos.Supertipo;
import model.atributos.Tipo;

import java.sql.SQLException;
import java.util.List;

public interface IConsulta {

    public List<Carta> consultaFalta(Usuario usuario) throws SQLException;

    public List<Carta> consultaTem(Usuario usuario) throws SQLException;

    public void consultaTipo(List<Tipo> lista);

    public void consultaSubtipo(List<Subtipo> lista);

    public void consultaSupertipo(List<Supertipo> lista);

    public void consultaCor(List<Cor> lista);

    public void consultaDano(int valor);

    public void consultaResistencia(int valor);

    public void consultaNome(String nome);

}
