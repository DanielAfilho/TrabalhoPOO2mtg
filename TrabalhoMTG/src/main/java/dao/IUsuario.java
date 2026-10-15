package dao;

import model.Usuario;

import java.sql.SQLException;

public interface IUsuario {

    public int cadastrarUsuario(Usuario usuario) throws SQLException;

    public Usuario acessarUsuario(String nome) throws SQLException;

    public void updateUsuario(Usuario usuario) throws SQLException;

    public void deletarUsuario(Usuario usuario) throws SQLException;

}
