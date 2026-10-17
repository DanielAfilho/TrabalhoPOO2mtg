package main;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        GerenciadorTelas.setStage(stage);
        stage.setResizable(false);
        GerenciadorTelas.trocarTela("/br/unicentro/appestudantes/view/Login.fxml","Login");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
