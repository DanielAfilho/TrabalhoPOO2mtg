package service;

import dao.CartaDAO;
import dao.ICarta;
import model.Carta;

import java.sql.SQLException;
import java.util.ArrayList;

public class CartaService {
    private final ICarta cartaDAO;

    public CartaService() {
        this(new CartaDAO());
    }

    public CartaService(ICarta cartaDAO) {
        this.cartaDAO = cartaDAO;
    }

    public Carta cadastrar(Carta carta) throws ServicoException {
        validar(carta);

        try {
            carta.setId(cartaDAO.cadastrarCarta(carta));
            return carta;
        } catch (SQLException e) {
            throw ServicoException.de(e, "Essa carta já está cadastrada.", "Erro ao salvar a carta.");
        }
    }

    public void atualizar(Carta carta) throws ServicoException {
        validar(carta);

        try {
            cartaDAO.atualizarCarta(carta);
        } catch (SQLException e) {
            throw ServicoException.de(e, "Este nome é usado.", "Erro ao atualizar a carta.");
        }
    }

    public void excluir(Carta carta) throws ServicoException {
        if (carta == null) {
            throw new ServicoException("Busque uma carta antes de excluir.");
        }

        try {
            cartaDAO.deletarCarta(carta);
        } catch (SQLException e) {
            throw new ServicoException("Erro ao excluir a carta.", e);
        }
    }

    public Carta buscarPorNome(String nome) throws ServicoException {
        if (nome == null || nome.isBlank()) {
            throw new ServicoException("Você precisa buscar por um nome de carta.");
        }

        try {
            Carta encontrada = cartaDAO.acessarCarta(nome.trim());

            if (encontrada == null) {
                throw new ServicoException("Carta não encontrada!");
            }

            return encontrada;
        } catch (SQLException e) {
            throw new ServicoException("Erro ao buscar a carta.", e);
        }
    }

    private void validar(Carta carta) throws ServicoException {
        if (carta.getNomeCarta() == null || carta.getNomeCarta().isBlank()) {
            throw new ServicoException("Campo vazio: informe o nome da carta.");
        }

        if (carta.getImagem() == null) {
            throw new ServicoException("Campo vazio: escolha uma imagem.");
        }

        if (carta.getTipos() == null || carta.getTipos().isEmpty()) {
            throw new ServicoException("Campo vazio: escolha ao menos um tipo.");
        }

        if (carta.getSupertipos() == null) carta.setSupertipos(new ArrayList<>());
        if (carta.getSubtipos() == null) carta.setSubtipos(new ArrayList<>());
        if (carta.getCores() == null) carta.setCores(new ArrayList<>());
    }

}
