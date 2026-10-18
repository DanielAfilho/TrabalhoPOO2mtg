package controller;

import dao.UsuarioDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import main.GerenciadorTelas;
import model.Usuario;

import java.sql.SQLException;

public class LoginController {
    UsuarioDAO usuarioDAO = new UsuarioDAO();
    Usuario usuario = new Usuario();

    @FXML
    private TextField txfNome;

    @FXML
    private PasswordField psfSenha;

    @FXML
    private Label lblMensagem;

    @FXML
    public void btnAcessarOnAction() {
        String nome = txfNome.getText().trim();
        String senha = psfSenha.getText();

        if (nome.isEmpty() || senha.isEmpty()) {
            erro("Login Invalido: campo(s) não preenchido(s)!");
            return;
        }

        try {
            usuario = usuarioDAO.acessarUsuario(nome);
        }
        catch(SQLException  e) {
            erro("Erro no sistema: tente novamente mais tarde!");
            return;
        }

        if (usuario == null ){
            erro("Login Invalido: usuario não cadastrado!");
        }
        else {

            if (usuario.getSenha().equals(senha)) {

                try {
                    GerenciadorTelas.setUsuarioLogado(usuario);
                    GerenciadorTelas.trocarTela("/view/CadastroCarta.fxml", "Cadastro de cartas");
                }
                catch (Exception e) {
                    e.printStackTrace();
                    erro("Erro na aplicação: verifique a integridade do programa");
                }

            }
            else {
                erro("Login Invalido: senha incorreta!");
            }

        }

    }

    @FXML
    public void btnCadastrarOnAction() {
        String nome = txfNome.getText().trim();
        String senha = psfSenha.getText();

        if (nome.isEmpty() || senha.isEmpty()) {
            erro("Cadastro Invalido: campo(s) não preenchido(s)!");
            return;
        }

        usuario.setNomeUsuario(nome);
        usuario.setSenha(senha);

        try {
            usuario.setId(usuarioDAO.cadastrarUsuario(usuario));
            sucesso("Cadastro válido: cadastro concluído");
        }
        catch(SQLException e) {

            if ("23505".equals(e.getSQLState())) {
                erro("Esse usuario já está cadastrada.");
            } else {
                erro("Erro no sistema.");
            }

        }


    }

    private void erro(String texto) {
        lblMensagem.setStyle("-fx-text-fill: red;");
        lblMensagem.setText(texto);
    }

    private void sucesso(String texto) {
        lblMensagem.setStyle("-fx-text-fill: green;");
        lblMensagem.setText(texto);
    }

}
