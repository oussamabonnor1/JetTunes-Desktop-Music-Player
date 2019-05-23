package Model;

import javafx.scene.image.Image;

public class Song {
    private String songName;
    private String artistName;
    private String songLength;
    private Image albumImage;

    public Song(String songName, String albumName, String songLength, Image albumImage) {
        this.songName = songName;
        this.artistName = albumName;
        this.songLength = songLength;
        if (albumImage == null) this.albumImage = new Image("res/images/trackWhite.png");
        else this.albumImage = albumImage;
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

    public String getSongLength() {
        return songLength;
    }

    public void setSongLength(String songLength) {
        this.songLength = songLength;
    }
}
