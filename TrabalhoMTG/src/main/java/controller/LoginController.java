package controller;

import dao.UsuarioDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.Usuario;

public class LoginController {
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
        lblMensagem.setStyle("-fx-text-fill: red;");

        if (nome.isEmpty() || senha.isEmpty()) {
            lblMensagem.setText("Login Invalido: campo(s) não preenchido(s)!");
            return;
        }

        UsuarioDAO banco = new UsuarioDAO();
        Usuario usuario;

        try {
            usuario = banco.acessarUsuario(nome);
        }
        catch(RuntimeException e) {
            lblMensagem.setText("Erro no sistema: tente novamente mais tarde!");
            return;
        }

        if (usuario.getId() == -1) {
            lblMensagem.setText("Login Invalido: usuario não cadastrado!");
        }
        else {

            if (usuario.getSenha().equals(senha)) {

            }
            else {
                lblMensagem.setText("Login Invalido: senha incorreta!");
            }

        }


    }

    @FXML
    public void btnCadastrarOnAction() {
        String nome = txfNome.getText().trim();
        String senha = psfSenha.getText();
        lblMensagem.setStyle("-fx-text-fill: red;");

        if (nome.isEmpty() || senha.isEmpty()) {
            lblMensagem.setText("Cadastro Invalido: campo(s) não preenchido(s)!");
            return;
        }

        UsuarioDAO banco = new UsuarioDAO();
        Usuario usuario;

        try {
            usuario = banco.acessarUsuario(nome);
        }
        catch(RuntimeException e) {
            lblMensagem.setText("Erro no sistema: tente novamente mais tarde!");
            return;
        }

        if (usuario.getId() != -1) {
            lblMensagem.setText("Cadastro Invalido: usuario já existe!");
        }
        else {
            usuario.setNome(nome);
            usuario.setSenha(senha);

            try {
                usuario.setId(banco.cadastrarUsuario(usuario));
            }
            catch(RuntimeException e) {
                lblMensagem.setText("Erro no sistema: tente novamente mais tarde!");
                return;
            }

            lblMensagem.setStyle("-fx-text-fill: green;");
            lblMensagem.setText("Cadastro válido: cadastro concluído");
        }

    }
}
