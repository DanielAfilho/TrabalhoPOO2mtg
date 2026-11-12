package service;

import command.AdquirirCartaComando;
import command.Comando;
import command.HistoricoComandos;
import command.RemoverCartaComando;
import dao.ColecaoDAO;
import dao.ConsultaDAO;
import dao.IColecao;
import dao.IConsulta;
import dao.escopo.CartasDoUsuario;
import dao.escopo.CartasFaltantes;
import model.Carta;
import model.Colecao;
import model.FiltroCartas;
import model.Usuario;

import java.sql.SQLException;
import java.util.List;

public class ColecaoService {
    private final IColecao colecaoDAO;
    private final IConsulta consultaDAO;
    private final HistoricoComandos historico = new HistoricoComandos();

    public ColecaoService() {
        this.colecaoDAO = new ColecaoDAO();
        this.consultaDAO = new ConsultaDAO();
    }

    public ColecaoService(IColecao colecaoDAO, IConsulta consultaDAO) {
        this.colecaoDAO = colecaoDAO;
        this.consultaDAO = consultaDAO;
    }

    public Colecao carregar(Usuario usuario, FiltroCartas filtro) throws ServicoException {
        try {
            List<Carta> minhas = consultaDAO.buscar(usuario, new CartasDoUsuario(), filtro);     // STRATEGY
            List<Carta> disponiveis = consultaDAO.buscar(usuario, new CartasFaltantes(), filtro); // STRATEGY
            return new Colecao(minhas, disponiveis);
        } catch (SQLException e) {
            throw new ServicoException("Não foi possível carregar as cartas.", e);
        }
    }

    public void adquirir(Usuario usuario, Carta carta) throws ServicoException {
        Comando comando = new AdquirirCartaComando(colecaoDAO, usuario.getId(), carta.getId()); // COMMAND

        try {
            historico.executar(comando);
        } catch (SQLException e) {
            throw ServicoException.de(e, "Você já possui esta carta.", "Não foi possível adquirir a carta.");
        }
    }

    public void remover(Usuario usuario, Carta carta) throws ServicoException {
        Comando comando = new RemoverCartaComando(colecaoDAO, usuario.getId(), carta.getId()); // COMMAND

        try {
            historico.executar(comando);
        } catch (SQLException e) {
            throw new ServicoException("Não foi possível remover a carta.", e);
        }
    }

    public boolean podeDesfazer() {
        return historico.podeDesfazer();
    }

    public String desfazer() throws ServicoException {
        if (!historico.podeDesfazer()) {
            throw new ServicoException("Não há nenhuma ação para desfazer.");
        }

        try {
            return historico.desfazer().descricao();
        } catch (SQLException e) {
            throw new ServicoException("Não foi possível desfazer a última ação.", e);
        }
    }

}
