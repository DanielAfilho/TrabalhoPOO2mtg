package service;

import dao.AtributoDAO;
import dao.IAtributo;

import java.sql.SQLException;

public class AtributoService {
    private final IAtributo atributoDAO;

    public AtributoService() {
        this(new AtributoDAO());
    }

    public AtributoService(IAtributo atributoDAO) {
        this.atributoDAO = atributoDAO;
    }

    public CatalogoAtributos carregarCatalogo() throws ServicoException {
        try {
            return new CatalogoAtributos(
                    atributoDAO.listarTipos(),
                    atributoDAO.listarSupertipos(),
                    atributoDAO.listarSubtipos(),
                    atributoDAO.listarCores());
        } catch (SQLException e) {
            throw new ServicoException("Não foi possível carregar tipos e cores.", e);
        }
    }

}
