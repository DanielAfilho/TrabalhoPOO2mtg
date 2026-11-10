package command;

import java.sql.SQLException;

/**
 * PADRÃO GoF: COMMAND (interface do comando).
 *
 * Encapsula uma ação sobre a coleção do usuário como um objeto, de forma que ela possa
 * ser executada e também DESFEITA depois.
 */
public interface Comando {

    void executar() throws SQLException;

    void desfazer() throws SQLException;

    String descricao();

}
