package controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import main.GerenciadorTelas;
import model.Carta;
import model.Colecao;
import model.FiltroCartas;
import model.Usuario;
import model.atributos.Cor;
import model.atributos.Subtipo;
import model.atributos.Supertipo;
import model.atributos.Tipo;
import service.AtributoService;
import service.CatalogoAtributos;
import service.ColecaoService;
import service.ServicoException;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;


public class ColecaoController {
    @FXML private Label lblUsuario;
    @FXML private Label lblMensagem;

    @FXML private TextField txtNome;
    @FXML private TextField txtResistencia;
    @FXML private TextField txtDano;

    @FXML private ListView<Tipo> lvTipo;
    @FXML private ListView<Supertipo> lvSupertipo;
    @FXML private ListView<Subtipo> lvSubtipo;
    @FXML private ListView<Cor> lvCor;

    @FXML private FlowPane flowMinhasCartas;
    @FXML private FlowPane flowDisponiveis;

    // SERVICE LAYER: o controller não conhece mais os DAOs
    private final AtributoService atributoService = new AtributoService();
    private final ColecaoService colecaoService = new ColecaoService();
    private Usuario usuarioLogado;

    @FXML
    private void initialize() {
        usuarioLogado = GerenciadorTelas.getUsuarioLogado();
        lblUsuario.setText("Coleção de " + usuarioLogado.getNomeUsuario());

        lvTipo.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        lvSupertipo.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        lvSubtipo.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        lvCor.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        try {
            CatalogoAtributos catalogo = atributoService.carregarCatalogo();
            lvTipo.setItems(FXCollections.observableArrayList(catalogo.tipos()));
            lvSupertipo.setItems(FXCollections.observableArrayList(catalogo.supertipos()));
            lvSubtipo.setItems(FXCollections.observableArrayList(catalogo.subtipos()));
            lvCor.setItems(FXCollections.observableArrayList(catalogo.cores()));
        } catch (ServicoException e) {
            erro("Não foi possível carregar os filtros.");
        }

        carregarCartas();
    }

    private ImageView criarImagem(Carta carta, Consumer<Carta> aoClicar) {
        ImageView imagem = new ImageView(new Image(new ByteArrayInputStream(carta.getImagem())));
        imagem.setFitWidth(150);
        imagem.setPreserveRatio(true);
        imagem.setCursor(Cursor.HAND);
        imagem.setOnMouseClicked(e -> aoClicar.accept(carta));
        return imagem;
    }

    private void preencher(FlowPane area, List<Carta> cartas, Consumer<Carta> aoClicar) {
        area.getChildren().clear();

        for (Carta carta : cartas) {
            area.getChildren().add(criarImagem(carta, aoClicar));
        }
    }

    private void adquirir(Carta carta) {
        try {
            colecaoService.adquirir(usuarioLogado, carta);
            carregarCartas();
        } catch (ServicoException e) {
            erro(e.getMessage());
        }
    }

    private void remover(Carta carta) {
        try {
            colecaoService.remover(usuarioLogado, carta);
            carregarCartas();
        } catch (ServicoException e) {
            erro(e.getMessage());
        }
    }

    private void carregarCartas() {
        try {
            Colecao colecao = colecaoService.carregar(usuarioLogado, montarFiltro());

            preencher(flowMinhasCartas, colecao.getCartasUsuario(), this::remover);
            preencher(flowDisponiveis, colecao.getCartasDisponiveis(), this::adquirir);
        } catch (ServicoException e) {
            erro(e.getMessage());
        }
    }

    /** PADRÃO BUILDER: monta o filtro só com o que o usuário preencheu na tela. */
    private FiltroCartas montarFiltro() {
        return FiltroCartas.builder()
                .nome(txtNome.getText())
                .tipos(new ArrayList<>(lvTipo.getSelectionModel().getSelectedItems()))
                .supertipos(new ArrayList<>(lvSupertipo.getSelectionModel().getSelectedItems()))
                .subtipos(new ArrayList<>(lvSubtipo.getSelectionModel().getSelectedItems()))
                .cores(new ArrayList<>(lvCor.getSelectionModel().getSelectedItems()))
                .resistencia(lerInteiro(txtResistencia))
                .dano(lerInteiro(txtDano))
                .build();
    }

    /** @return o número digitado, ou null se o campo estiver vazio ou inválido */
    private Integer lerInteiro(TextField campo) {
        try {
            return Integer.valueOf(campo.getText().trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @FXML
    private void btnFiltrarOnAction() {
        try {
            if (!txtResistencia.getText().isBlank()) Integer.parseInt(txtResistencia.getText().trim());
            if (!txtDano.getText().isBlank()) Integer.parseInt(txtDano.getText().trim());
        } catch (NumberFormatException e) {
            erro("Resistência e dano devem ser números inteiros.");
            return;
        }

        lblMensagem.setText("");
        carregarCartas();
    }

    @FXML
    private void btnLimparFiltrosOnAction() {
        txtNome.clear();
        txtResistencia.clear();
        txtDano.clear();
        lvTipo.getSelectionModel().clearSelection();
        lvSupertipo.getSelectionModel().clearSelection();
        lvSubtipo.getSelectionModel().clearSelection();
        lvCor.getSelectionModel().clearSelection();
        lblMensagem.setText("");
        carregarCartas();
    }

    /** PADRÃO COMMAND: desfaz a última aquisição ou remoção de carta. */
    @FXML
    private void btnDesfazerOnAction() {
        try {
            String desfeito = colecaoService.desfazer();
            carregarCartas();
            lblMensagem.setStyle("-fx-text-fill: green;");
            lblMensagem.setText("Desfeito: " + desfeito + ".");
        } catch (ServicoException e) {
            erro(e.getMessage());
        }
    }

    @FXML
    private void btnCadastrarCartaOnAction() {
        try {
            GerenciadorTelas.trocarTela("/view/CadastroCarta.fxml", "Cadastro de cartas");
        } catch (Exception e) {
            erro("Não foi possível abrir a tela de cadastro.");
        }
    }

    @FXML
    private void btnSairOnAction() {
        try {
            GerenciadorTelas.setUsuarioLogado(null);
            GerenciadorTelas.trocarTela("/view/Login.fxml", "Login");
        } catch (Exception e) {
            erro("Não foi possível voltar ao login.");
        }
    }

    private void erro(String texto) {
        lblMensagem.setStyle("-fx-text-fill: red;");
        lblMensagem.setText(texto);
    }

    @FXML private void btnEditarUsuarioOnAction() {
        try {
            GerenciadorTelas.trocarTela("/view/EditarUsuario.fxml", "Editor de usuários");
        } catch (Exception e) {
            erro("Não foi possível abrir o gerenciador de usuários");
        }
    }

}
