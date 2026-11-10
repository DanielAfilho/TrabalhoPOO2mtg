package command;

import dao.IColecao;

import java.sql.SQLException;

/** PADRÃO GoF: COMMAND (comando concreto) — remove uma carta da coleção; desfazer = adicionar de volta. */
public class RemoverCartaComando implements Comando {
    private final IColecao colecaoDAO; // receiver
    private final int usuarioId;
    private final int cartaId;

    public RemoverCartaComando(IColecao colecaoDAO, int usuarioId, int cartaId) {
        this.colecaoDAO = colecaoDAO;
        this.usuarioId = usuarioId;
        this.cartaId = cartaId;
    }

    @Override
    public void executar() throws SQLException {
        colecaoDAO.removerCarta(usuarioId, cartaId);
    }

    @Override
    public void desfazer() throws SQLException {
        colecaoDAO.adicionarCarta(usuarioId, cartaId);
    }

    @Override
    public String descricao() {
        return "remoção de carta";
    }

}
