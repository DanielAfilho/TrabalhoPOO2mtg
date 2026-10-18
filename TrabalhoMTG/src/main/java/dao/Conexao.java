package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao implements IConst {

    public static Connection getConexao() throws SQLException {
        return DriverManager.getConnection(
                stringDeConexao,
                usuario,
                senha
        );
    }

}
