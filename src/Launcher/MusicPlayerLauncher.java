package Launcher;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;

public class MusicPlayerLauncher extends Application {
    private Stage stage;


    public static void main(String[] args) {
        // write your code here
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        //setting the stage's parameters (icon, frame type, resizable..)
        stage = primaryStage;
        stage.setTitle("JetTunes");
        stage.setResizable(false);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.getIcons().add(new Image("res/images/logo.png"));
        Parent root = FXMLLoader.load(getClass().getResource("../View/musicPlayer.fxml"));
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }
}
