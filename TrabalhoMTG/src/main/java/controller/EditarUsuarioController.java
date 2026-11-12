package controller;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import main.GerenciadorTelas;
import model.Usuario;
import service.ServicoException;
import service.UsuarioService;

public class EditarUsuarioController {
    @FXML
    private TextField txtNome;
    @FXML private PasswordField psfSenha;
    @FXML private Label lblMensagem;

    @FXML private TableView<Usuario> tabela;
    @FXML private TableColumn<Usuario, Integer> colId;
    @FXML private TableColumn<Usuario, String> colNome;

    // SERVICE LAYER: o controller não conhece mais o DAO
    private final UsuarioService usuarioService = new UsuarioService();
    private Usuario usuarioSelecionado;

    @FXML
    private void initialize() {
        colId.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getId()));
        colNome.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNomeUsuario()));

        tabela.getSelectionModel().selectedItemProperty().addListener((obs, anterior, atual) -> {
            usuarioSelecionado = atual;

            if (atual != null) {
                txtNome.setText(atual.getNomeUsuario());
                psfSenha.setText(atual.getSenha());
                lblMensagem.setText("");
            }
        });

        carregarTabela();
    }

    private void carregarTabela() {
        try {
            tabela.getItems().setAll(usuarioService.listar());
        } catch (ServicoException e) {
            erro(e.getMessage());
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

    private void limparFormulario() {
        usuarioSelecionado = null;
        tabela.getSelectionModel().clearSelection();
        txtNome.clear();
        psfSenha.clear();
        lblMensagem.setText("");
    }

    @FXML
    private void btnVoltarOnAction() {

        try {
            GerenciadorTelas.trocarTela("/view/Colecao.fxml", "Coleção");
        } catch(Exception e) {
            erro("Não foi possível voltar para a coleção");
        }

    }

    @FXML
    private void btnNovoOnAction() {
        limparFormulario();
    }

    @FXML
    private void btnSalvarOnAction() {
        if (usuarioSelecionado != null) {
            erro("Há um usuário selecionado. Use Atualizar, ou clique em Novo.");
            return;
        }

        try {
            usuarioService.cadastrar(txtNome.getText(), psfSenha.getText());
            limparFormulario();
            carregarTabela();
            sucesso("Usuário cadastrado!");
        } catch (ServicoException e) {
            erro(e.getMessage());
        }
    }

    @FXML
    private void btnAtualizarOnAction() {
        if (usuarioSelecionado == null) {
            erro("Selecione um usuário na tabela.");
            return;
        }

        try {
            Usuario atualizado = usuarioService.atualizar(
                    usuarioSelecionado.getId(), txtNome.getText(), psfSenha.getText());
            
            Usuario logado = GerenciadorTelas.getUsuarioLogado();
            if (logado != null && logado.getId() == atualizado.getId()) {
                GerenciadorTelas.setUsuarioLogado(atualizado);
            }

            limparFormulario();
            carregarTabela();
            sucesso("Usuário atualizado!");
        } catch (ServicoException e) {
            erro(e.getMessage());
        }
    }

    @FXML
    private void btnExcluirOnAction() {
        if (usuarioSelecionado == null) {
            erro("Selecione um usuário na tabela.");
            return;
        }

        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION,
                "Excluir o usuário \"" + usuarioSelecionado.getNomeUsuario() + "\" e a coleção dele?",
                ButtonType.YES, ButtonType.NO);

        if (confirmacao.showAndWait().orElse(ButtonType.NO) != ButtonType.YES) {
            return;
        }

        Usuario logado = GerenciadorTelas.getUsuarioLogado();
        boolean excluiuASiMesmo = logado != null && logado.getId() == usuarioSelecionado.getId();

        try {
            usuarioService.excluir(usuarioSelecionado);
        } catch (ServicoException e) {
            erro(e.getMessage());
            return;
        }

        if (excluiuASiMesmo) {
            try {
                GerenciadorTelas.setUsuarioLogado(null);
                GerenciadorTelas.trocarTela("/view/Login.fxml", "Login");
            } catch (Exception e) {
                erro("Usuário excluído, mas não foi possível voltar ao login.");
            }
            return;
        }

        limparFormulario();
        carregarTabela();
        sucesso("Usuário excluído!");
    }
}
