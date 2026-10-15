package dao;

import model.Carta;

public interface ICarta {

    public void cadastrarCarta(Carta carta);

    public Carta acessarCarta(String nome);

    public void deletarCarta(Carta carta);

}
