package service;

import dao.IUsuario;
import dao.UsuarioDAO;
import model.Usuario;

import java.sql.SQLException;
import java.util.List;

/**
 * SERVICE LAYER — regras de negócio de usuário: validação de credenciais, autenticação,
 * cadastro, atualização e exclusão. Conversa com o DAO; o Controller só conversa com esta classe.
 */
public class UsuarioService {
    private final IUsuario usuarioDAO;

    public UsuarioService() {
        this(new UsuarioDAO());
    }

    public UsuarioService(IUsuario usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

    public Usuario autenticar(String nome, String senha) throws ServicoException {
        validarCredenciais(nome, senha);

        Usuario usuario;
        try {
            usuario = usuarioDAO.acessarUsuario(nome.trim());
        } catch (SQLException e) {
            throw new ServicoException("Erro no sistema: tente novamente mais tarde!", e);
        }

        if (usuario == null) {
            throw new ServicoException("Usuário não cadastrado!");
        }

        if (!usuario.getSenha().equals(senha)) {
            throw new ServicoException("Senha incorreta!");
        }

        return usuario;
    }

    public Usuario cadastrar(String nome, String senha) throws ServicoException {
        validarCredenciais(nome, senha);

        Usuario novo = new Usuario();
        novo.setNomeUsuario(nome.trim());
        novo.setSenha(senha);

        try {
            novo.setId(usuarioDAO.cadastrarUsuario(novo));
            return novo;
        } catch (SQLException e) {
            throw ServicoException.de(e, "Já existe um usuário com esse nome.", "Erro ao cadastrar o usuário.");
        }
    }

    public Usuario atualizar(int id, String nome, String senha) throws ServicoException {
        validarCredenciais(nome, senha);

        Usuario atualizado = new Usuario(id, nome.trim(), senha);

        try {
            usuarioDAO.atualizarUsuario(atualizado);
            return atualizado;
        } catch (SQLException e) {
            throw ServicoException.de(e, "Já existe um usuário com esse nome.", "Erro ao atualizar o usuário.");
        }
    }

    public void excluir(Usuario usuario) throws ServicoException {
        try {
            usuarioDAO.deletarUsuario(usuario);
        } catch (SQLException e) {
            throw new ServicoException("Erro ao excluir o usuário.", e);
        }
    }

    public List<Usuario> listar() throws ServicoException {
        try {
            return usuarioDAO.listarUsuarios();
        } catch (SQLException e) {
            throw new ServicoException("Não foi possível carregar os usuários.", e);
        }
    }

    private void validarCredenciais(String nome, String senha) throws ServicoException {
        if (nome == null || nome.isBlank() || senha == null || senha.isEmpty()) {
            throw new ServicoException("Preencha o nome e a senha.");
        }
    }

}
