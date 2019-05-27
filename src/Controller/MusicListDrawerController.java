package Controller;

import Model.Song;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URL;
import java.util.ResourceBundle;

public class MusicListDrawerController implements Initializable {

    @FXML
    private ListView<Song> musicList;
    @FXML
    private JFXTextField searchBar;

    public MusicPlayerController controller;
    ObservableList<Song> musicListObservableList = FXCollections.observableArrayList();
    MediaPlayer mediaPlayer;
    String songName, artistName, songLength;
    Image albumImage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        controller = MusicPlayerController.instance;
        settingUpMusicList();
        musicList.setItems(musicListObservableList);
        musicList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (musicList.getSelectionModel().getSelectedIndex() >= 0) {
                controller.playSong(musicList.getSelectionModel().getSelectedItem().getSongIndex());
            }
        });
        musicList.setCellFactory(param -> new SongCellController());
        searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.matches("")) musicList.setItems(musicListObservableList);
            else musicList.setItems(songSearch(newValue));
        });
    }

    ObservableList<Song> songSearch(String newValue) {
        ObservableList<Song> tempList = FXCollections.observableArrayList();
        for (Song song : musicListObservableList) {
            if (song.getSongName().toLowerCase().contains(newValue.toLowerCase())) {
                tempList.add(song);
            }
        }
        return tempList;
    }

    void settingUpMusicList() {
        for (int i = 0; i < MusicPlayerController.musicList.size(); i++) {
            Media hit = new Media(MusicPlayerController.musicList.get(i).toURI().toString());
            mediaPlayer = new MediaPlayer(hit); //using media player to determine when media meta data is loaded (refactor it if you can)
            int songIndex = i;
            mediaPlayer.setOnReady(() -> {
                //assigning song name
                if (hit.getMetadata().get("songArtist") == null) {
                    songName = hit.getSource().split("/")[hit.getSource().split("/").length - 1].replace("%20", " ");
                } else songName = "" + hit.getMetadata().get("songArtist");
                //assigning song album name
                if (hit.getMetadata().get("artist") == null) {
                    artistName = "Unknown artist";
                } else artistName = "" + hit.getMetadata().get("artist");
                //assigning song length
                int seconds = (int) hit.getDuration().toSeconds() % 60;
                int minutes = (int) hit.getDuration().toSeconds() / 60;
                songLength = String.format("%02d:%02d", minutes, seconds);
                //assigning song album image
                albumImage = (Image) hit.getMetadata().get("image");

                musicListObservableList.add(new Song(songIndex, songName, artistName, songLength, albumImage));
            });

        }
    }

    @FXML
    void closeDrawer(MouseEvent event) {
        controller.deactivateDrawer(null);
    }

    @FXML
    void updateMusicList(MouseEvent event) {
        controller.deletingMusicList();
        controller.fillingTheList();
        settingUpMusicList();
    }

}
