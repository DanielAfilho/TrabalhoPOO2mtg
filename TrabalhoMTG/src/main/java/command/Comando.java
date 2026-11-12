package command;

import java.sql.SQLException;

public interface Comando {

    void executar() throws SQLException;

    void desfazer() throws SQLException;

    String descricao();

}
