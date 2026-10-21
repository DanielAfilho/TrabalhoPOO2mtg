package controller;

import dao.IUsuario;
import dao.UsuarioDAO;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import main.GerenciadorTelas;
import model.Usuario;

import java.sql.SQLException;

public class EditarUsuarioController {
    @FXML
    private TextField txtNome;
    @FXML private PasswordField psfSenha;
    @FXML private Label lblMensagem;

    @FXML private TableView<Usuario> tabela;
    @FXML private TableColumn<Usuario, Integer> colId;
    @FXML private TableColumn<Usuario, String> colNome;

    private final IUsuario usuarioDAO = new UsuarioDAO();
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
            tabela.getItems().setAll(usuarioDAO.listarUsuarios());
        } catch (SQLException e) {
            erro("Não foi possível carregar os usuários.");
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

        String nome = txtNome.getText().trim();
        String senha = psfSenha.getText();

        if (nome.isEmpty() || senha.isEmpty()) {
            erro("Preencha o nome e a senha.");
            return;
        }

        Usuario novo = new Usuario();
        novo.setNomeUsuario(nome);
        novo.setSenha(senha);

        try {
            novo.setId(usuarioDAO.cadastrarUsuario(novo));
            limparFormulario();
            carregarTabela();
            sucesso("Usuário cadastrado!");
        } catch (SQLException e) {
            if ("23505".equals(e.getSQLState())) {
                erro("Já existe um usuário com esse nome.");
            } else {
                erro("Erro ao cadastrar o usuário.");
            }
        }
    }
    @FXML
    private void btnAtualizarOnAction() {
        if (usuarioSelecionado == null) {
            erro("Selecione um usuário na tabela.");
            return;
        }

        String nome = txtNome.getText().trim();
        String senha = psfSenha.getText();

        if (nome.isEmpty() || senha.isEmpty()) {
            erro("Preencha o nome e a senha.");
            return;
        }

        Usuario atualizado = new Usuario(usuarioSelecionado.getId(), nome, senha);

        try {
            usuarioDAO.atualizarUsuario(atualizado);

            // se o usuário alterou a si mesmo, a sessão precisa do nome novo
            Usuario logado = GerenciadorTelas.getUsuarioLogado();
            if (logado != null && logado.getId() == atualizado.getId()) {
                GerenciadorTelas.setUsuarioLogado(atualizado);
            }

            limparFormulario();
            carregarTabela();
            sucesso("Usuário atualizado!");
        } catch (SQLException e) {
            if ("23505".equals(e.getSQLState())) {
                erro("Já existe um usuário com esse nome.");
            } else {
                erro("Erro ao atualizar o usuário.");
            }
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

        boolean excluiuASiMesmo = false;
        Usuario logado = GerenciadorTelas.getUsuarioLogado();
        if (logado != null && logado.getId() == usuarioSelecionado.getId()) {
            excluiuASiMesmo = true;
        }

        try {
            usuarioDAO.deletarUsuario(usuarioSelecionado);
        } catch (SQLException e) {
            erro("Erro ao excluir o usuário.");
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
