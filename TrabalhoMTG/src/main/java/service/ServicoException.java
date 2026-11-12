package service;

import java.sql.SQLException;

public class ServicoException extends Exception {

    public ServicoException(String mensagem) {
        super(mensagem);
    }

    public ServicoException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }

    static ServicoException de(SQLException e, String mensagemDuplicidade, String mensagemGenerica) {
        if ("23505".equals(e.getSQLState())) {
            return new ServicoException(mensagemDuplicidade, e);
        }
        return new ServicoException(mensagemGenerica, e);
    }

}
