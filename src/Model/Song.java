package Model;

import javafx.scene.image.Image;

public class Song {
    private String songName;
    private String artistName;
    private Image albumImage;

    public Song(String songName, String albumName) {
        this.songName = songName;
        this.artistName = albumName;
        this.albumImage = new Image("res/images/albumWhite.png");
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public Image getAlbumImage() {
        return albumImage;
    }

    public void setAlbumImage(Image albumImage) {
        this.albumImage = albumImage;
    }
}
