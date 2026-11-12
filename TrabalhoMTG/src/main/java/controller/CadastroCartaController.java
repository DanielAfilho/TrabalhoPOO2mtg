package controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import main.GerenciadorTelas;
import model.Carta;
import model.atributos.Cor;
import model.atributos.Subtipo;
import model.atributos.Supertipo;
import model.atributos.Tipo;
import service.AtributoService;
import service.CartaService;
import service.CatalogoAtributos;
import service.ServicoException;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

public class CadastroCartaController {

    @FXML
    private TextField txtNome;

    @FXML
    private TextField txtResistencia;

    @FXML
    private TextField txtDano;

    @FXML
    private ListView<Tipo> lvTipo;

    @FXML
    private ListView<Supertipo> lvSupertipo;

    @FXML
    private ListView<Subtipo> lvSubtipo;

    @FXML private ListView<Cor> lvCor;

    @FXML
    private ImageView imgCarta;

    @FXML
    private Label lblMensagem;

    private final CartaService cartaService = new CartaService();
    private final AtributoService atributoService = new AtributoService();
    private Carta carta;
    private byte[] imagemSelecionada;

    @FXML
    private void initialize() {
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
        }
        catch (ServicoException e) {
            erro(e.getMessage());
        }

    }

    private boolean lerFormulario() {
        int resistencia;
        int dano;

        try {
            resistencia = Integer.parseInt(txtResistencia.getText().trim());
            dano = Integer.parseInt(txtDano.getText().trim());
        } catch (NumberFormatException e) {
            erro("Resistência e dano devem ser números inteiros.");
            return false;
        }

        String nome = txtNome.getText().trim();

        if (carta == null) {
            carta = new Carta(0, imagemSelecionada, nome, resistencia, dano);
        } else {
            carta.setNomeCarta(nome);
            carta.setResistencia(resistencia);
            carta.setDano(dano);
            carta.setImagem(imagemSelecionada);
        }

        carta.setTipos(new ArrayList<>(lvTipo.getSelectionModel().getSelectedItems()));
        carta.setSupertipos(new ArrayList<>(lvSupertipo.getSelectionModel().getSelectedItems()));
        carta.setSubtipos(new ArrayList<>(lvSubtipo.getSelectionModel().getSelectedItems()));
        carta.setCores(new ArrayList<>(lvCor.getSelectionModel().getSelectedItems()));
        return true;
    }

    protected void limparFormulario() {
        carta = null;
        imagemSelecionada = null;
        txtNome.clear();
        txtResistencia.clear();
        txtDano.clear();
        lvTipo.getSelectionModel().clearSelection();
        lvSupertipo.getSelectionModel().clearSelection();
        lvSubtipo.getSelectionModel().clearSelection();
        lvCor.getSelectionModel().clearSelection();
        imgCarta.setImage(null);
    }

    private void mostrarImagem() {
        if (imagemSelecionada == null) {
            imgCarta.setImage(null);
        } else {
            imgCarta.setImage(new Image(new ByteArrayInputStream(imagemSelecionada)));
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

    public void setCarta(Carta carta) {
        limparFormulario();
        this.carta = carta;

        if (carta == null) {
            return;
        }

        txtNome.setText(carta.getNomeCarta());
        txtResistencia.setText(String.valueOf(carta.getResistencia()));
        txtDano.setText(String.valueOf(carta.getDano()));

        imagemSelecionada = carta.getImagem();
        mostrarImagem();

        carta.getTipos().forEach(t -> lvTipo.getSelectionModel().select(t));
        carta.getSupertipos().forEach(s -> lvSupertipo.getSelectionModel().select(s));
        carta.getSubtipos().forEach(s -> lvSubtipo.getSelectionModel().select(s));
        carta.getCores().forEach(c -> lvCor.getSelectionModel().select(c));
    }

    @FXML
    private void btnExcluirOnAction() {

        try {
            cartaService.excluir(carta);
            limparFormulario();
            sucesso("Carta excluída!");
        } catch (ServicoException e) {
            erro(e.getMessage());
        }

    }

    @FXML
    private void btnBuscarOnAction() {

        try {
            setCarta(cartaService.buscarPorNome(txtNome.getText()));
        } catch (ServicoException e) {
            erro(e.getMessage());
        }

    }

    @FXML
    private void btnImagemOnAction() {
        FileChooser escolhedor = new FileChooser();
        escolhedor.getExtensionFilters().add(new FileChooser.ExtensionFilter("Imagens", "*.png", "*.jpg", "*.jpeg"));

        File arquivo = escolhedor.showOpenDialog(imgCarta.getScene().getWindow());
        if (arquivo == null) {
            return;
        }

        try {
            imagemSelecionada = Files.readAllBytes(arquivo.toPath());
            mostrarImagem();
        } catch (IOException e) {
            erro("Não foi possível ler a imagem.");
        }

    }

    @FXML
    private void btnAtualizarOnAction() {

        if (carta == null) {
            erro("Carta não existe.");
            return;
        }

        if (!lerFormulario()) {
            return;
        }

        try {
            cartaService.atualizar(carta);
            sucesso("Carta atualizada!");
        }
        catch (ServicoException e) {
            erro(e.getMessage());
        }

    }

    @FXML
    private void btnVoltarOnAction() {

        try {
            GerenciadorTelas.trocarTela("/view/Colecao.fxml", "Cadastro de cartas");
        } catch(Exception e) {
            erro("Erro na aplicação");
        }

    }

    @FXML
    private void btnNovoOnAction() {
        limparFormulario();
        lblMensagem.setText("");
    }

    @FXML
    private void btnSalvarOnAction() {
        if (carta != null) {
            erro("Carta já existe.");
            return;
        }

        if (!lerFormulario()) {
            carta = null;
            return;
        }

        try {
            cartaService.cadastrar(carta);
            sucesso("Carta cadastrada!");
        }
        catch (ServicoException e) {
            carta = null;
            erro(e.getMessage());
        }

    }

}
