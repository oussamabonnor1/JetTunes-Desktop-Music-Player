package Controller;

import Model.Song;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.io.IOException;


public class SongCellController extends ListCell<Song> {

    @FXML
    private VBox root;

    @FXML
    private ImageView albumImage;

    @FXML
    private Label songName;

    @FXML
    private Label artistName;

    @FXML
    private Label songLength;

    @Override
    protected void updateItem(Song song, boolean empty) {
        super.updateItem(song, empty);
        if (empty || song == null) {
            setText(null);
            setGraphic(null);
        } else {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/View/songCustomLayout.fxml"));
            fxmlLoader.setController(this);
            try {
                fxmlLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            songName.setText(String.valueOf(song.getSongName()));
            artistName.setText(String.valueOf(song.getArtistName()));
            albumImage.setImage(song.getAlbumImage());
            songLength.setText(song.getSongLength());
            setGraphic(root);
        }
    }
}
