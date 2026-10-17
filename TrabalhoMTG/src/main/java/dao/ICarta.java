package dao;

import model.carta.Carta;

import java.sql.SQLException;

public interface ICarta {

    public Carta cadastrarCarta(Carta carta) throws SQLException;

    public Carta acessarCarta(String nome) throws SQLException;

    public Carta atualizarCarta(String carta) throws SQLException;

    public void deletarCarta(Carta carta) throws SQLException;

}
