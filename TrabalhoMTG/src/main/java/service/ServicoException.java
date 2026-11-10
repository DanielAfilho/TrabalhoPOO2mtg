package service;

import java.sql.SQLException;

/**
 * Erro da camada de serviços. A mensagem já é a que deve ser exibida ao usuário,
 * então o Controller não precisa conhecer SQLException nem códigos de erro do banco.
 */
public class ServicoException extends Exception {

    public ServicoException(String mensagem) {
        super(mensagem);
    }

    public ServicoException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }

    /** Traduz um erro de banco: violação de unicidade (SQLState 23505) ou erro genérico. */
    static ServicoException de(SQLException e, String mensagemDuplicidade, String mensagemGenerica) {
        if ("23505".equals(e.getSQLState())) {
            return new ServicoException(mensagemDuplicidade, e);
        }
        return new ServicoException(mensagemGenerica, e);
    }

}
