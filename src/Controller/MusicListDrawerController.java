package Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class MusicListDrawerController implements Initializable {

    @FXML
    private ListView<String> musicList;
    public MusicPlayerController controller;
    ObservableList<String> musicListObservableList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        controller = MusicPlayerController.instance;
        settingUpMusicList();
        musicList.setItems(musicListObservableList);
        musicList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            controller.playSong(musicList.getSelectionModel().getSelectedIndex());
        });
    }

    void settingUpMusicList() {
        for (File file : MusicPlayerController.musicList) {
            Media hit = new Media(file.toURI().toString());
            if (hit.getMetadata().get("songArtist") == null) {
                musicListObservableList.add(hit.getSource().split("/")[hit.getSource().split("/").length - 1].replace("%20", " "));
            } else musicListObservableList.add("" + hit.getMetadata().get("songArtist"));
        }
    }

    @FXML
    void closeDrawer(MouseEvent event){
        controller.deactivateDrawer(null);
    }


}
