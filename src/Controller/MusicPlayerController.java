package Controller;

/*
 * Created by Oussama BONNOR on 19/01/2017
 * The usage of this software is under MIT license
 * All legal ownership of this software is to JetLight studio
 * */

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXSlider;
import javafx.beans.binding.StringBinding;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

import java.io.*;
import java.net.URL;
import java.nio.file.Paths;
import java.util.*;

public class MusicPlayerController implements EventHandler<ActionEvent>, Initializable {

    private Stage stage;
    private Scene scene;
    private Pane mainPain;
    private Pane listPane;

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

    private JFXButton select;
    private JFXButton volumeUp;
    private JFXButton volumeDown;
    private JFXButton next;
    private JFXButton previous;
    private JFXButton goToList;
    private JFXButton goToPlayer;
    private JFXCheckBox mute;
    private JFXCheckBox random;
    private JFXListView listView;
    @FXML
    private ImageView albumImage;
    private Image img;
    private Media hit;
    private MediaPlayer mediaPlayer;
    private Timer timer;
    private TimerTask timerTask;

    private ArrayList<File> musicList = new ArrayList<>();
    private int musicIndex = 0;
    private String[] colors = {"115245", "554455", "224687", "9C27B0", "42A5F5", "80D8FF", "FF9800", "9E9E9E", "#212121"};
    private boolean isMute;
    private boolean isPlaying;
    private boolean isRandom;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //setting the stage's parameters (icon, frame type, resizable..)
        //region refactoring UI
       /* mainPain = new Pane();

        mute = new JFXCheckBox();
        mute.setTranslateX(90);
        mute.setTranslateY(660);
        mute.setText("Mute");
        mute.setTextFill(Paint.valueOf("#FFFFFF"));
        mute.setFont(Font.font("FangSong", FontWeight.BOLD, 20));
        mute.setOnAction(this);

        select = new JFXButton("Open");
        select.setTextFill(Paint.valueOf("0F9D58"));
        select.setBackground(new Background(new BackgroundFill(Paint.valueOf("FFFFFF"), null, null)));
        select.setFont(Font.font("FangSong", FontWeight.BOLD, 18));
        select.setTranslateY(650);
        select.setTranslateX(190);
        select.setOnAction(this);

        goToList = new JFXButton("Open Music List");
        goToList.setTextFill(Paint.valueOf("0F9D58"));
        goToList.setBackground(new Background(new BackgroundFill(Paint.valueOf("FFFFFF"), null, null)));
        goToList.setFont(Font.font("FangSong", FontWeight.BOLD, 18));
        goToList.setTranslateY(700);
        goToList.setTranslateX(150);
        goToList.setOnAction(this);

        */
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
        volumeSlider.setOnMouseReleased(event -> mediaPlayer.setVolume(volumeSlider.getValue()));
        volumeSlider.setOnMouseDragged(event -> mediaPlayer.setVolume(volumeSlider.getValue()));

        /*
        listView = new JFXListView();
        listView.setTranslateX(50);
        listView.setTranslateY(50);
        listView.setPrefSize(350, 550);
        listView.setOnMouseClicked(event -> {
            mediaPlayer.stop();
            musicIndex = listView.getSelectionModel().getSelectedIndex();
            hit = new Media(musicList.get(musicIndex).toURI().toString());
            mediaPlayer = new MediaPlayer(hit);
            mediaPlayer.setOnReady(this::playMusic);
            savingParameters();
            mediaPlayer.setMute(isMute);
        });

        //setting the image view to a default picture assigned by us
        albumImage = new ImageView(img);
        albumImage.setY(230);
        albumImage.setX(150);
        albumImage.setFitWidth(150);
        albumImage.setFitHeight(150);
        Reflection reflection = new Reflection();
        reflection.setFraction(0.35);
        albumImage.setEffect(reflection);

        timer = null;
        mediaPlayer = null;

        //endregion
        */

        startJetTunes();

    }

    @FXML
    void startJetTunes() {
        if (loadingFile()) {
            playButton.setImage(getUiImage("pauseWhite"));
        } else {
            fillingTheList();
        }
        loadingParam();
    }

    @Override
    public void handle(ActionEvent event) {

        if (event.getSource() == select) {
            fillingTheList();
        }

        if (event.getSource() == previous) {
            previousSong();
        }

        if (event.getSource() == mute)

        {
            isMute = !mediaPlayer.isMute();
            mediaPlayer.setMute(isMute);
        }
        if (event.getSource() == volumeUp && volumeSlider.getValue() <= 0.8)

        {
            mediaPlayer.setVolume(mediaPlayer.getVolume() + 0.2);
            volumeSlider.setValue(volumeSlider.getValue() + 0.2);
        }
        if (event.getSource() == volumeDown && volumeSlider.getValue() >= 0.2)

        {
            mediaPlayer.setVolume(mediaPlayer.getVolume() - 0.2);
            volumeSlider.setValue(volumeSlider.getValue() - 0.2);
        }
        if (event.getSource() == goToList) {
            changingView(true);
        }
        if (event.getSource() == goToPlayer) {
            changingView(false);
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
        if (isRandom) musicIndex = new Random().nextInt(musicList.size());
        else ++musicIndex;
        if (musicIndex == musicList.size()) musicIndex = 0;
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
    public void randomButton(MouseEvent event) {
        isRandom = !isRandom;
        savingParameters();
    }

    @FXML
    public void repeatButton(MouseEvent event) {

    }

    @FXML
    public void closeApp(MouseEvent event) {
        System.exit(0);
    }


    private void changingView(boolean list) {
        if (list) {
            scene.setRoot(listPane);
            listView.getSelectionModel().select(musicIndex);
            listView.scrollTo(musicIndex);
        } else {
            scene.setRoot(mainPain);
        }
        stage.setScene(scene);
        stage.show();
    }

    //region music functions

    Image getUiImage(String name) {
        return new Image("res/images/" + name + ".png");
    }

    private void savingParameters() {
        try {
            FileWriter fw = new FileWriter(new File("C://Users//oussama//IdeaProjects//Music_Player_material_design_javaFX//src\\res\\data\\parameters"), false);
            fw.write("" + isRandom + "\n");
            fw.write("" + musicIndex);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean loadingFile() {

        File f = new File("C:\\Users\\oussama\\IdeaProjects\\Music_Player_material_design_javaFX\\src\\res\\data\\MusicList");
        Scanner scn;
        try {
            scn = new Scanner(f);
            if (!scn.hasNextLine()) return false;

            while (scn.hasNextLine()) {
                musicList.add(new File(scn.nextLine()));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < musicList.size(); i++) {
            if (isFileExists(musicList.get(i).toURI().toString())) {
                Media hit = new Media(musicList.get(i).toURI().toString());
                String song = hit.getSource().split("/")[hit.getSource().split("/").length - 1].replace("%20", " ");
                //listView.getItems().add(songTitle);
            }
        }
        return true;
    }

    private void loadingParam() {
        File paramFile = new File("C:\\Users\\oussama\\IdeaProjects\\Music_Player_material_design_javaFX\\src\\res\\data\\Parameters");
        Scanner sc;
        try {
            sc = new Scanner(paramFile);
            if (sc.hasNextLine()) {
                isRandom = Boolean.valueOf(sc.nextLine());
                musicIndex = Integer.valueOf(sc.nextLine());
            }
            hit = new Media(musicList.get(musicIndex).toURI().toString());

            settingUpMediaPlayer(hit);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void fillingTheList() {
        playButton.setImage(getUiImage("pauseWhite"));
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Music File");
        File file = fileChooser.showOpenDialog(stage);
        if (fileChooser.initialDirectoryProperty().getName().matches("initialDirectory")
                && file.getParentFile().isDirectory()) {
            File parent = file.getParentFile();
            FileWriter fw;
            try {
                fw = new FileWriter(new File("res/data/MusicList"), true);
                for (int i = 0; i < parent.listFiles().length; i++) {
                    String filename = parent.listFiles()[i].toURI().toString();
                    if (filename.endsWith(".mp3")) {
                        musicList.add(parent.listFiles()[i]);
                        fw.write(parent.listFiles()[i].toString() + "\n");
                        //unnecessary hard coding
                        Media hit = new Media(parent.listFiles()[i].toURI().toString());
                        String song = hit.getSource().split("/")[hit.getSource().split("/").length - 1].replace("%20", " ");
                        listView.getItems().add(song);
                    }
                }
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            listView.scrollTo(0);
        }

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
    }

    private void playMusic() {
        //setting up the sliders (volume and time)
        slider.setValue(0);
        slider.setMax(hit.getDuration().toSeconds());
        //volumeSlider.setValue(mediaPlayer.getVolume());
        //      volumeSlider.setMax(mediaPlayer.getVolume());

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
            try {
                img = new Image(new FileInputStream(new File(String.valueOf(Paths.get("C:\\Users\\oussama\\IdeaProjects\\Music_Player_material_design_javaFX\\src\\res\\images\\music.jpg")))));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        albumImage.setImage(img);

        //playing the songTitle and starting running the time slider
        mediaPlayer.play();
        mediaPlayer.setMute(isMute);
        sliderClock(true);
    }

    private boolean isFileExists(String url) {
        return new File(url).exists();
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
        XYChart.Data[] series1Data = new XYChart.Data[100];
        for (int i = 0; i < series1Data.length; i++) {
            series1Data[i] = new XYChart.Data<>(Integer.toString(i + 1), 25);
            series1.getData().add(series1Data[i]);
        }

        mediaPlayer.setAudioSpectrumListener((timestamp, duration, magnitudes, phases) -> {
            for (int i = 0; i < series1Data.length; i++) {
                series1Data[i].setYValue(magnitudes[i] - mediaPlayer.getAudioSpectrumThreshold());

            }
        });

        areaChart.getData().clear();
        areaChart.getData().add(series1);
        mediaPlayer.setOnReady(this::playMusic);

        mediaPlayer.setOnEndOfMedia(() -> nextSong(null));

        return mediaPlayer;
    }

    //endregion
}
