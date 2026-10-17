package main;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GerenciadorTelas {

    private static Stage stage;

    public static void setStage(Stage stagePrincipal) {
        stage = stagePrincipal;
    }

    public static void trocarTela(String arquivoFXML, String titulo) throws IOException {
        FXMLLoader loader = new FXMLLoader(GerenciadorTelas.class.getResource("/view/Login.fxml"));
        Scene scene = new Scene(loader.load(), 600, 400);
        stage.setTitle(titulo);
        stage.setScene(scene);
        stage.show();
    }
}
