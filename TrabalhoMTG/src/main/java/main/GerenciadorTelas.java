package main;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Usuario;

public class GerenciadorTelas {
    private static Usuario usuarioLogado;

    public static Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    public static void setUsuarioLogado(Usuario usuario) {
        usuarioLogado = usuario;
    }

    private static Stage stage;

    public static void setStage(Stage stagePrincipal) {
        stage = stagePrincipal;
    }

    public static void trocarTela(String arquivoFXML, String titulo) throws IOException {
        FXMLLoader loader = new FXMLLoader(GerenciadorTelas.class.getResource(arquivoFXML));
        Scene scene = new Scene(loader.load());
        stage.setTitle(titulo);
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
    }

}
