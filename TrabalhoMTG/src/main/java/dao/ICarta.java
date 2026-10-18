package dao;

import model.Carta;

import java.sql.SQLException;

public interface ICarta {

    public int cadastrarCarta(Carta carta) throws SQLException;

    public Carta acessarCarta(String nome) throws SQLException;

    public void atualizarCarta(Carta carta) throws SQLException;

    public void deletarCarta(Carta carta) throws SQLException;

}
