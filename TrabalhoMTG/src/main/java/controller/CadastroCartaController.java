package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import model.carta.Tipo;
import model.carta.Supertipo;
import model.carta.Subtipo;
import javafx.scene.control.TextField;
import dao.CartaDAO;
import dao.FiltroDAO;
import javafx.scene.control.Alert;
import model.carta.Carta;
import java.util.ArrayList;
import java.util.List;

public class CadastroCartaController {
    @FXML
    private TextField txtNome;
    @FXML
    private TextField txtVida;
    @FXML
    private TextField txtDano;

    @FXML
    private ComboBox<Tipo> cbTipo;
    @FXML
    private ComboBox<Supertipo> cbSupertipo;
    @FXML
    private ComboBox<Subtipo> cbSubtipo;

    private CartaDAO cartaDAO = new CartaDAO();
    private FiltroDAO filtroDAO = new FiltroDAO();

    public void initialize() {
        cbTipo.getItems().setAll(filtroDAO.listarTipos());
        cbSupertipo.getItems().setAll(filtroDAO.listarSupertipos());
        cbSubtipo.getItems().setAll(filtroDAO.listarSubtipos());
    }

    @FXML
    public void btnNovoOnAction(ActionEvent actionEvent) {
        String nome = txtNome.getText().trim();
        String textoVida = txtVida.getText().trim();
        String textoDano = txtDano.getText().trim();

        if (nome.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Informe o nome da carta.").showAndWait();
            return;
        }

        if (cbTipo.getValue() == null) {
            new Alert(Alert.AlertType.WARNING, "Escolha o tipo da carta.").showAndWait();
            return;
        }

        int vida;
        int dano;
        try {
            vida = Integer.parseInt(textoVida);
            dano = Integer.parseInt(textoDano);
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.WARNING, "Vida e dano devem ser números inteiros.").showAndWait();
            return;
        }

        if (vida < 0 || dano < 0) {
            new Alert(Alert.AlertType.WARNING, "Vida e dano não podem ser negativos.").showAndWait();
            return;
        }

        Carta carta = new Carta();
        carta.setNomeCarta(nome);
        carta.setResistencia(vida);
        carta.setDano(dano);

        List<Tipo> tipos = new ArrayList<>();
        tipos.add(cbTipo.getValue());
        carta.setTipos(tipos);

        List<Supertipo> supertipos = new ArrayList<>();
        if(cbSupertipo.getValue() != null){
            supertipos.add(cbSupertipo.getValue());
        }
        carta.setSupertipos(supertipos);

        List<Subtipo> subtipos = new ArrayList<>();
        if(cbSubtipo.getValue() != null){
            subtipos.add(cbSubtipo.getValue());
        }
        carta.setSubtipos(subtipos);

    }

    @FXML
    public void btnExcluirOnAction(ActionEvent actionEvent) {
    }

    @FXML
    public void btnAtualizarOnAction(ActionEvent actionEvent) {
    }
}
