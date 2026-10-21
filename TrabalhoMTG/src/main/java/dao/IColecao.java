package dao;

import java.sql.SQLException;

public interface IColecao {

    public void adicionarCarta(int usuarioId, int cartaId) throws SQLException;

    public void removerCarta(int usuarioId, int cartaId) throws SQLException;

}