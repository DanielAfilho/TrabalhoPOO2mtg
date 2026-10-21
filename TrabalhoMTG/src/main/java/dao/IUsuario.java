package dao;

import model.Usuario;

import java.sql.SQLException;
import java.util.List;

public interface IUsuario {

    public int cadastrarUsuario(Usuario usuario) throws SQLException;

    public Usuario acessarUsuario(String nome) throws SQLException;

    public void atualizarUsuario(Usuario usuario) throws SQLException;

    public void deletarUsuario(Usuario usuario) throws SQLException;

    public List<Usuario> listarUsuarios() throws SQLException;

}
