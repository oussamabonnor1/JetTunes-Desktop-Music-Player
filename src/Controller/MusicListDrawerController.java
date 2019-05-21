package Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MusicListDrawerController implements Initializable {

    @FXML
    private ListView<String> musicList;
    ArrayList<File> musicListObservableList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        musicListObservableList = MusicPlayerController.musicList;
        musicList.setItems(musicListObservableList);
    }



}
