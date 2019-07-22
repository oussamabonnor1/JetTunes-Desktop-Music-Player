package Controller;

/*
 * Created by Oussama BONNOR on 19/01/2017
 * The usage of this software is under MIT license
 * All legal ownership of this software is to JetLight studio
 * */

import ToolBox.DbConnection;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.events.JFXDrawerEvent;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import javafx.beans.binding.StringBinding;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

import java.io.*;
import java.net.URL;
import java.nio.file.Paths;
import java.util.*;

public class MusicPlayerController implements Initializable {

    @FXML
    private Label songArtist;
    @FXML
    private Label songTitle;
    @FXML
    private Label totalTime;
    @FXML
    private Label currentTime;
    @FXML
    private LineChart<String, Number> areaChart;

    @FXML
    private JFXSlider slider;
    @FXML
    private JFXSlider volumeSlider;

    @FXML
    private ImageView playButton;

    @FXML
    private ImageView mute;

    @FXML
    private ImageView randomButton;

    @FXML
    private ImageView albumImage;

    @FXML
    private ImageView repeatButton;

    @FXML
    private JFXHamburger hamburger;

    @FXML
    private JFXDrawer musicListDrawer;

    @FXML
    private VBox progressPanel;

    private HamburgerBackArrowBasicTransition transition;
    private Image img;
    private Media hit;
    private MediaPlayer mediaPlayer;
    private Timer timer;
    private TimerTask timerTask;
    static ArrayList<File> musicList = new ArrayList<>();
    static ArrayList<Media> musicMediaList = new ArrayList<>();
    String pathTillProject = System.getProperty("user.dir");
    private int musicIndex = 0;
    private boolean isMute;
    private boolean isPlaying;
    private boolean isRandom;
    private boolean isRepeat;

    public static MusicPlayerController instance;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //this next lambda expression is in charge of changing time of songTitle when dragging mouse
        slider.setOnMouseReleased(event -> mediaPlayer.seek(Duration.seconds(slider.getValue())));
        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            int seconds = (int) slider.getValue() % 60;
            int minutes = (int) slider.getValue() / 60;
            String time = String.format("%02d:%02d", minutes, seconds);
            currentTime.setText(time);
        });
        slider.valueFactoryProperty().setValue(param -> new StringBinding() {
            @Override
            protected String computeValue() {
                return "*";
            }
        });
        //these next lambda expressions are in charge of setting volume of the songTitle
        volumeSlider.setOnMouseReleased(event -> mediaPlayer.setVolume(volumeSlider.getValue() / 100));
        volumeSlider.setOnMouseDragged(event -> mediaPlayer.setVolume(volumeSlider.getValue() / 100));

        startJetTunes();

    }

    @FXML
    void startJetTunes() {
        if (loadingMusic()) {
            playButton.setImage(getUiImage("pauseWhite"));
            loadingParam();
            initializingMusicDrawer();
        } else {
            fillingTheList();
        }
    }

    @FXML
    public void play(MouseEvent event) {
        if (!isPlaying) {
            playButton.setImage(getUiImage("playWhiteCircle"));
            mediaPlayer.pause();
            sliderClock(false); //must not forget to stop the time slider
        } else {
            playButton.setImage(getUiImage("pauseWhite"));
            mediaPlayer.play();
            sliderClock(true); //must not forget to start the time slider
        }
        isPlaying = !isPlaying;
    }

    @FXML
    public void nextSong(MouseEvent event) {
        mediaPlayer.stop();
        if (!isRepeat) {
            if (isRandom) musicIndex = new Random().nextInt(musicList.size());
            else ++musicIndex;
            if (musicIndex == musicList.size()) musicIndex = 0;
        }
        hit = new Media(musicList.get(musicIndex).toURI().toString());
        settingUpMediaPlayer(hit);
        savingParameters();
        mediaPlayer.setMute(isMute);
    }

    void playSong(int musicIndex) {
        mediaPlayer.stop();
        this.musicIndex = musicIndex;
        hit = new Media(musicList.get(musicIndex).toURI().toString());
        settingUpMediaPlayer(hit);
        savingParameters();
        mediaPlayer.setMute(isMute);
    }

    @FXML
    private void previousSong() {
        mediaPlayer.stop();
        --musicIndex;
        if (musicIndex < 0) musicIndex = musicList.size() - 1;
        hit = new Media(musicList.get(musicIndex).toURI().toString());
        settingUpMediaPlayer(hit);
        savingParameters();
        mediaPlayer.setOnReady(this::playMusic);
    }

    @FXML
    void hamburgerClicked(MouseEvent event) {
        musicListDrawer.setDisable(false);
        transition.setRate(transition.getCurrentRate() * -1);
        transition.play();
        if (musicListDrawer.isShown()) musicListDrawer.close();
        else musicListDrawer.open();
    }

    @FXML
    void deactivateDrawer(JFXDrawerEvent jfxDrawerEvent) {
        if (musicListDrawer.isShown()) musicListDrawer.close();
    }

    @FXML
    public void randomButton(MouseEvent event) {
        isRandom = !isRandom;
        randomButton.setImage(getUiImage(isRandom ? "shuffleOnWhite" : "ShuffleOFFGreen"));
        savingParameters();
    }

    public void mute(MouseEvent event) {
        isMute = !isMute;
        mediaPlayer.setMute(isMute);
        mute.setImage(getUiImage(isMute ? "volumeOffWhite" : "volumeOnWhite"));
        savingParameters();
    }

    @FXML
    public void repeatButton(MouseEvent event) {
        isRepeat = !isRepeat;
        repeatButton.setImage(getUiImage(isRepeat ? "repeatOnWhite" : "repeatOffWhite"));
    }

    @FXML
    public void closeApp(MouseEvent event) {
        System.exit(0);
    }

    @FXML
    void minimizeApp(MouseEvent event) {
        ((Stage) ((ImageView) event.getSource()).getScene().getWindow()).setIconified(true);
    }


    //region music functions

    void initializingMusicDrawer() {
        instance = this;
        //Music list drawer setup
        try {
            //loading music list custom layout
            VBox vBox = FXMLLoader.load(getClass().getResource("/View/musicListDrawer.fxml"));
            musicListDrawer.setSidePane(vBox);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //transition is responsible for drawer animation
        transition = new HamburgerBackArrowBasicTransition(hamburger);
        transition.setRate(-1);
        musicListDrawer.setOnDrawerClosed(event -> musicListDrawer.setDisable(true));
    }

    Image getUiImage(String name) {
        return new Image("res/images/" + name + ".png");
    }

    private void savingParameters() {
        try {
            File file = new File(pathTillProject + "/JetTunes/data/Parameters");
            FileWriter fw = new FileWriter(file);
            fw.write("" + isRandom + "\n");
            fw.write("" + musicIndex + "\n");
            fw.write("" + isMute + "\n");
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private boolean loadingMusic() {
        /*InputStream is = getClass().getResourceAsStream("/res/data/MusicList");
        InputStreamReader sReader = new InputStreamReader(is);
        Scanner scn;
        scn = new Scanner(sReader);

        if (!scn.hasNextLine()) return false;
        int i = 0;
        while (scn.hasNextLine()) {
            File file = new File(scn.nextLine());
            if (file.exists()) {
                musicList.add(file);
                musicMediaList.add(new Media(musicList.get(i).toURI().toString()));
                i++;
            }
        }
        return true;
        */
        ArrayList<String> musicListPaths = DbConnection.getMusicList();
        if (musicListPaths.size() == 0) {
            return false;
        } else {
            for (int i = 0; i < musicListPaths.size(); i++) {
                String temp = musicListPaths.get(i);
                if (musicListPaths.get(i).contains("`"))
                    temp = musicListPaths.get(i).replaceAll("`", "'");
                musicList.add(new File(temp));
            }
            return true;
        }
    }

    private void loadingParam() {
        String filePath = String.valueOf(Paths.get(pathTillProject + "/JetTunes/data/Parameters"));
        File paramFile = new File(filePath);
        if (!paramFile.exists()) {
            try {
                new File(pathTillProject + "/JetTunes/data").mkdirs();
                PrintWriter writer = new PrintWriter(pathTillProject + "/JetTunes/data/Parameters", "UTF-8");
                isRandom = false;
                musicIndex = 0;
                isMute = false;
                writer.println(false);
                writer.println(0);
                writer.println(false);
                writer.close();
                randomButton.setImage(getUiImage(isRandom ? "shuffleOnWhite" : "ShuffleOFFGreen"));
                mute.setImage(getUiImage(isMute ? "volumeOffWhite" : "volumeOnWhite"));
            } catch (FileNotFoundException | UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {
            try {
                Scanner sc = new Scanner(paramFile);
                if (sc.hasNextLine()) {
                    isRandom = Boolean.valueOf(sc.nextLine());
                    musicIndex = Integer.valueOf(sc.nextLine());
                    isMute = Boolean.valueOf(sc.nextLine());
                    randomButton.setImage(getUiImage(isRandom ? "shuffleOnWhite" : "ShuffleOFFGreen"));
                    mute.setImage(getUiImage(isMute ? "volumeOffWhite" : "volumeOnWhite"));
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        hit = new Media(musicList.get(musicIndex).toURI().toString());
        settingUpMediaPlayer(hit);
        //setting media player after initializing it (avoiding null pointer exception)
        mediaPlayer.setMute(isMute);
    }

    void deletingMusicList() {
        DbConnection.dropTable();
    }

    void fillingTheList() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Choose a music directory");
        File file = directoryChooser.showDialog(new Stage());

        if (file != null) {
            progressPanel.setVisible(true);

            //clearing music to avoid duplicated songs
            musicList.clear();
            if (directoryChooser.initialDirectoryProperty().getName().matches("initialDirectory")
                    && file.getParentFile().isDirectory()) {
                fillingMusicDataBaseList(file);
            }
        }
    }

    private void fillingMusicDataBaseList(File file) {
        Task task = new Task() {
            @Override
            protected Object call() {
                File[] files = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    String filename = files[i].toURI().toString();
                    if (filename.endsWith(".mp3")) {
                        musicList.add(files[i]);
                        DbConnection.addSong("musicList", files[i].toString());
                    }
                }
                return null;
            }
        };
        task.setOnSucceeded(event -> {
            progressPanel.setVisible(false);
            //stopping the previous songTitle and its data
            if (timer != null) {
                sliderClock(false);
                timer = null;
                timerTask = null;
            }
            if (mediaPlayer != null) mediaPlayer.stop();
            hit = new Media(musicList.get(musicIndex).toURI().toString());
            settingUpMediaPlayer(hit);

            //this is used to delay the media player enough for the songTitle to be loaded
            //this doesn't influence play time (milli-seconds scale)
            mediaPlayer.setOnReady(this::playMusic);
            loadingParam();
            initializingMusicDrawer();
        });
        new Thread(task).start();
    }

    private void playMusic() {
        //setting up the sliders (volume and time)
        slider.setValue(0);
        slider.setMax(hit.getDuration().toSeconds());

        //setting basic songTitle info (artist name, songArtist)
        songArtist.setText("" + hit.getMetadata().get("artist"));
        if (hit.getMetadata().get("artist") == null) {
            songArtist.setText("Now Playing");
        }
        songTitle.setText("" + hit.getMetadata().get("songArtist"));
        if (hit.getMetadata().get("songArtist") == null) {
            songTitle.setText(hit.getSource().split("/")[hit.getSource().split("/").length - 1].replace("%20", " "));
        }

        sliderClock(false);
        //choosing an album picture (if null then we provide one)
        img = (Image) hit.getMetadata().get("image");
        if (img == null) {
            albumImage.setVisible(false);
            areaChart.setVisible(true);
        } else {
            areaChart.setVisible(false);
            albumImage.setVisible(true);
            albumImage.setImage(img);
        }

        //playing the songTitle and starting running the time slider
        mediaPlayer.play();
        mediaPlayer.setMute(isMute);
        sliderClock(true);
    }

    private void sliderClock(boolean state) {
        if (state) {
            totalTime.setText(String.format("%02d:%02d", (int) slider.getMax() / 60, (int) slider.getMax() % 60));
            timer = new Timer();
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    javafx.application.Platform.runLater(() -> {
                        slider.setValue(slider.getValue() + 1);
                        int seconds = (int) slider.getValue() % 60;
                        int minutes = (int) slider.getValue() / 60;
                        String time = String.format("%02d:%02d", minutes, seconds);
                        currentTime.setText(time);
                    });
                }
            };
            timer.scheduleAtFixedRate(timerTask, 0, 1000);
        } else {
            if (timer != null) {
                timerTask.cancel();
                timer.cancel();
                timer.purge();
            }
        }
    }

    private MediaPlayer settingUpMediaPlayer(Media hit) {
        mediaPlayer = new MediaPlayer(hit);

        XYChart.Series<String, Number> series1 = new XYChart.Series<>();
        XYChart.Data[] series1Data = new XYChart.Data[60];
        for (int i = 0; i < series1Data.length; i++) {
            series1Data[i] = new XYChart.Data<>(Integer.toString(i + 1), 25);
            series1.getData().add(series1Data[i]);
        }
        mediaPlayer.setAudioSpectrumListener((timestamp, duration, magnitudes, phases) -> {
            for (int i = 0; i < series1Data.length; i++) {
                float tempValue = magnitudes[i] - mediaPlayer.getAudioSpectrumThreshold();
                if (tempValue < 10) tempValue *= 2;
                if (tempValue < 20) tempValue *= 1.5f;
                series1Data[(i + 25) % 60].setYValue(tempValue);
            }
        });

        areaChart.getData().clear();
        areaChart.getData().add(series1);
        mediaPlayer.setVolume(volumeSlider.getValue() / 100);
        mediaPlayer.setOnReady(this::playMusic);

        mediaPlayer.setOnEndOfMedia(() -> nextSong(null));

        return mediaPlayer;
    }

    //endregion
}
