package Launcher;

import ToolBox.DbConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MusicPlayerLauncher extends Application {
    private Stage stage;


    public static void main(String[] args) {
        // write your code here
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        //setting the stage's parameters (icon, frame type, resizable..)
        DbConnection.createConnection("JetTunes");
        DbConnection.createTable("musicList");
        stage = primaryStage;
        stage.setTitle("JetTunes");
        stage.setResizable(false);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.getIcons().add(new Image("res/images/logoCircle.png"));
        Parent root = FXMLLoader.load(getClass().getResource("/View/musicPlayer.fxml"));
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }
}
