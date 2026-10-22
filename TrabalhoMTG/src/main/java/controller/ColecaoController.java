package controller;

import dao.*;
import javafx.collections.FXCollections;
import javafx.scene.Cursor;
import javafx.scene.control.SelectionMode;
import main.GerenciadorTelas;
import model.Carta;
import model.Usuario;
import model.atributos.*;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.ByteArrayInputStream;

import model.atributos.Tipo;

import java.sql.SQLException;
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

    private final IAtributo atributoDAO = new AtributoDAO();
    private final IColecao colecaoDAO = new ColecaoDAO();
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
            lvTipo.setItems(FXCollections.observableArrayList(atributoDAO.listarTipos()));
            lvSupertipo.setItems(FXCollections.observableArrayList(atributoDAO.listarSupertipos()));
            lvSubtipo.setItems(FXCollections.observableArrayList(atributoDAO.listarSubtipos()));
            lvCor.setItems(FXCollections.observableArrayList(atributoDAO.listarCores()));
        } catch (SQLException e) {
            lblMensagem.setStyle("-fx-text-fill: red;");
            lblMensagem.setText("Não foi possível carregar os filtros.");
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
            colecaoDAO.adicionarCarta(usuarioLogado.getId(), carta.getId());
            carregarCartas();
        } catch (SQLException e) {
            erro("Não foi possível adquirir a carta.");
        }
    }

    private void remover(Carta carta) {
        try {
            colecaoDAO.removerCarta(usuarioLogado.getId(), carta.getId());
            carregarCartas();
        } catch (SQLException e) {
            erro("Não foi possível remover a carta.");
        }
    }

    private void carregarCartas() {
        try {
            IConsulta consulta = new ConsultaDAO();

            aplicarFiltros(consulta);
            List<Carta> minhas = consulta.consultaTem(usuarioLogado);

            aplicarFiltros(consulta);
            List<Carta> disponiveis = consulta.consultaFalta(usuarioLogado);

            preencher(flowMinhasCartas, minhas, this::remover);
            preencher(flowDisponiveis, disponiveis, this::adquirir);
        } catch (SQLException e) {
            lblMensagem.setStyle("-fx-text-fill: red;");
            lblMensagem.setText("Não foi possível carregar as cartas.");
        }
    }

    private void aplicarFiltros(IConsulta consulta) {
        String nome = txtNome.getText().trim();
        if (!nome.isEmpty()) {
            consulta.consultaNome(nome);
        }

        consulta.consultaTipo(new ArrayList<>(lvTipo.getSelectionModel().getSelectedItems()));
        consulta.consultaSupertipo(new ArrayList<>(lvSupertipo.getSelectionModel().getSelectedItems()));
        consulta.consultaSubtipo(new ArrayList<>(lvSubtipo.getSelectionModel().getSelectedItems()));
        consulta.consultaCor(new ArrayList<>(lvCor.getSelectionModel().getSelectedItems()));

        String resistencia = txtResistencia.getText().trim();
        if (!resistencia.isEmpty()) {
            consulta.consultaResistencia(Integer.parseInt(resistencia));
        }

        String dano = txtDano.getText().trim();
        if (!dano.isEmpty()) {
            consulta.consultaDano(Integer.parseInt(dano));
        }
    }

    @FXML
    private void btnFiltrarOnAction() {
        try {
            if (!txtResistencia.getText().isBlank()) Integer.parseInt(txtResistencia.getText().trim());
            if (!txtDano.getText().isBlank()) Integer.parseInt(txtDano.getText().trim());
        } catch (NumberFormatException e) {
            lblMensagem.setStyle("-fx-text-fill: red;");
            lblMensagem.setText("Resistência e dano devem ser números inteiros.");
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
