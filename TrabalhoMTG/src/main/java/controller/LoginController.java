package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import main.GerenciadorTelas;
import model.Usuario;
import service.ServicoException;
import service.UsuarioService;

public class LoginController {
    private final UsuarioService usuarioService = new UsuarioService();

    @FXML
    private TextField txfNome;

    @FXML
    private PasswordField psfSenha;

    @FXML
    private Label lblMensagem;

    @FXML
    public void btnAcessarOnAction() {
        Usuario usuario;

        try {
            usuario = usuarioService.autenticar(txfNome.getText(), psfSenha.getText());
        } catch (ServicoException e) {
            erro("Login Inválido: " + e.getMessage());
            return;
        }

        try {
            GerenciadorTelas.setUsuarioLogado(usuario);
            GerenciadorTelas.trocarTela("/view/Colecao.fxml", "Coleção");
        } catch (Exception e) {
            e.printStackTrace();
            erro("Erro na aplicação: verifique a integridade do programa");
        }
    }

    @FXML
    public void btnCadastrarOnAction() {
        try {
            usuarioService.cadastrar(txfNome.getText(), psfSenha.getText());
            sucesso("Cadastro válido: cadastro concluído");
        } catch (ServicoException e) {
            erro("Cadastro Inválido: " + e.getMessage());
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
