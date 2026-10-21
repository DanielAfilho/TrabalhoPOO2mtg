package controller;

import dao.AtributoDAO;
import dao.CartaDAO;
import dao.IAtributo;
import dao.ICarta;
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
import model.Colecao;
import model.atributos.Cor;
import model.atributos.Subtipo;
import model.atributos.Supertipo;
import model.atributos.Tipo;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;
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

    private final ICarta cartaDAO = new CartaDAO();
    private final IAtributo atributoDAO = new AtributoDAO();
    private Carta carta;
    private byte[] imagemSelecionada;

    @FXML
    private void initialize() {
        lvTipo.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        lvSupertipo.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        lvSubtipo.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        lvCor.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        try {
            lvTipo.setItems(FXCollections.observableArrayList(atributoDAO.listarTipos()));
            lvSupertipo.setItems(FXCollections.observableArrayList(atributoDAO.listarSupertipos()));
            lvSubtipo.setItems(FXCollections.observableArrayList(atributoDAO.listarSubtipos()));
            lvCor.setItems(FXCollections.observableArrayList(atributoDAO.listarCores()));
        }
        catch (SQLException e) {
            erro("Não foi possível carregar tipos e cores.");
        }

    }

    private String validarFormulario() {
        if (txtNome.getText().isBlank() || txtResistencia.getText().isBlank() || txtDano.getText().isBlank() || imagemSelecionada == null || lvTipo.getSelectionModel().isEmpty()) {
            return "Campo vazio.";
        }

        try {
            Integer.parseInt(txtResistencia.getText().trim());
            Integer.parseInt(txtDano.getText().trim());
        } catch (NumberFormatException e) {
            return "Resistência e dano devem ser números inteiros.";
        }

        return null;
    }

    private void lerFormulario() {
        String nome = txtNome.getText().trim();
        int resistencia = Integer.parseInt(txtResistencia.getText().trim());
        int dano = Integer.parseInt(txtDano.getText().trim());

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

        if (carta == null) {
            erro("Busque uma carta antes de excluir.");
            return;
        }

        try {
            cartaDAO.deletarCarta(carta);
            limparFormulario();
            sucesso("Carta excluída!");
        } catch (SQLException e) {
            erro("Erro ao excluir a carta.");
        }

    }

    @FXML
    private void btnBuscarOnAction() {
        if (carta == null) {
            erro("Você precisa buscar por um nome de carta.");
            return;
        }
        String nome = txtNome.getText().trim();
        try {
            Carta encontrada = cartaDAO.acessarCarta(nome);
            if (encontrada == null) {
                erro("Carta não encontrada!");
                return;
            }

            setCarta(encontrada);
        } catch (SQLException e) {
            erro("Erro ao buscar a carta.");
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

        String problema = validarFormulario();
        if (problema != null) {
            erro(problema);
            return;
        }

        lerFormulario();

        try {
            cartaDAO.atualizarCarta(carta);
            sucesso("Carta atualizada!");
        }
        catch (SQLException e) {

            if ("23505".equals(e.getSQLState())) {
                erro("Este nome é usado.");
            } else {
                erro("Erro ao atualizar a carta.");
            }

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

        String problema = validarFormulario();
        if (problema != null) {
            erro(problema);
            return;
        }

        lerFormulario();

        try {
            carta.setId(cartaDAO.cadastrarCarta(carta));
            sucesso("Carta cadastrada!");
        }
        catch (SQLException e) {
            carta = null;

            if ("23505".equals(e.getSQLState())) {
                erro("Essa carta já está cadastrada.");
            } else {
                erro("Erro ao salvar a carta.");
            }

        }

    }

}