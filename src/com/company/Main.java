package com.company;

/*
 * Created by Oussama BONNOR on 19/01/2017
 * The usage of this software is under MIT license
 * All legal ownership of this software is to JetLight studio
 * */

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXSlider;
import javafx.application.Application;
import javafx.beans.binding.StringBinding;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.Reflection;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.*;
import java.nio.file.Paths;
import java.util.*;

public class Main extends Application implements EventHandler<ActionEvent> {

    private Stage stage;
    private Scene scene;
    private Pane mainPain;
    private Pane listPane;

    private Label title;
    private Label song;
    private Label totalTime;
    private Label currentTime;

    private JFXSlider slider;
    private JFXSlider volumeSlider;
    private JFXButton play;
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
    private ImageView imageView;
    private Image img;
    private Media hit;
    private MediaPlayer mediaPlayer;
    private Timer timer;
    private TimerTask timerTask;

    private ArrayList<File> musicList = new ArrayList<>();
    private int musicIndex = 0;
    private String[] colors = {"115245", "554455", "224687", "9C27B0", "42A5F5", "80D8FF", "FF9800", "9E9E9E", "#212121"};
    private boolean isMute;
    private boolean isRandom;

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
        stage.initStyle(StageStyle.DECORATED);
        try {
            img = new Image(new FileInputStream(new File(String.valueOf(Paths.get("res/music.jpg")))));
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.getIcons().add(img);

        mainPain = new Pane();

        //setting the GUI components's parameters and attributes (java only)
        title = new Label("Choose a song to play");
        title.setTextFill(Paint.valueOf("FFFFFF"));
        title.setTranslateY(40);
        title.setAlignment(Pos.CENTER);
        title.setTextAlignment(TextAlignment.CENTER);
        title.setPrefWidth(450);
        title.setFont(Font.font("FangSong", FontWeight.BOLD, 35));

        totalTime = new Label("");
        totalTime.setTextFill(Paint.valueOf("FFFFFF"));
        totalTime.setTranslateY(520);
        totalTime.setTranslateX(350);
        totalTime.setAlignment(Pos.BOTTOM_CENTER);
        totalTime.setTextAlignment(TextAlignment.CENTER);
        totalTime.setFont(Font.font("FangSong", FontWeight.BOLD, 20));

        currentTime = new Label("");
        currentTime.setTextFill(Paint.valueOf("FFFFFF"));
        currentTime.setTranslateY(520);
        currentTime.setTranslateX(50);
        currentTime.setAlignment(Pos.BOTTOM_CENTER);
        currentTime.setTextAlignment(TextAlignment.CENTER);
        currentTime.setFont(Font.font("FangSong", FontWeight.BOLD, 20));

        song = new Label("");
        song.setTextFill(Paint.valueOf("FFFFFF"));
        song.setTranslateY(120);
        song.setAlignment(Pos.CENTER);
        song.setTextAlignment(TextAlignment.CENTER);
        song.setPrefWidth(450);
        song.setWrapText(true);
        song.setFont(Font.font("FangSong", FontWeight.BOLD, 22));

        play = new JFXButton("Start");
        play.setTextFill(Paint.valueOf("#0F9D58"));
        play.setBackground(new Background(new BackgroundFill(Paint.valueOf("#FFFFFF"), null, null)));
        play.setFont(Font.font("FangSong", FontWeight.BOLD, 18));
        play.setTranslateY(600);
        play.setTranslateX(165);
        play.setPrefWidth(120);
        play.setOnAction(this);

        mute = new JFXCheckBox();
        mute.setTranslateX(90);
        mute.setTranslateY(660);
        mute.setText("Mute");
        mute.setTextFill(Paint.valueOf("#FFFFFF"));
        mute.setFont(Font.font("FangSong", FontWeight.BOLD, 20));
        mute.setOnAction(this);

        next = new JFXButton("Next");
        next.setTextFill(Paint.valueOf("0F9D58"));
        next.setBackground(new Background(new BackgroundFill(Paint.valueOf("FFFFFF"), null, null)));
        next.setFont(Font.font("FangSong", FontWeight.BOLD, 18));
        next.setTranslateY(600);
        next.setTranslateX(300);
        next.setOnAction(this);

        previous = new JFXButton("Prev");
        previous.setTextFill(Paint.valueOf("0F9D58"));
        previous.setBackground(new Background(new BackgroundFill(Paint.valueOf("FFFFFF"), null, null)));
        previous.setFont(Font.font("FangSong", FontWeight.BOLD, 18));
        previous.setTranslateY(600);
        previous.setTranslateX(90);
        previous.setOnAction(this);

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

        volumeUp = new JFXButton("+");
        volumeUp.setTextFill(Paint.valueOf("0F9D58"));
        volumeUp.setBackground(new Background(new BackgroundFill(Paint.valueOf("FFFFFF"), null, null)));
        volumeUp.setTranslateY(650);
        volumeUp.setTranslateX(325);
        volumeUp.setPrefSize(40, 40);
        volumeUp.setOnAction(this);

        volumeDown = new JFXButton("-");
        volumeDown.setTextFill(Paint.valueOf("0F9D58"));
        volumeDown.setBackground(new Background(new BackgroundFill(Paint.valueOf("FFFFFF"), null, null)));
        volumeDown.setTranslateY(650);
        volumeDown.setTranslateX(275);
        volumeDown.setPrefSize(40, 40);
        volumeDown.setOnAction(this);

        slider = new JFXSlider(0, 100, 0);
        slider.setTranslateX(50);
        slider.setTranslateY(490);
        slider.setPrefSize(350, 50);
        //this next lambda expression is in charge of changing time of song when dragging mouse
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

        goToPlayer = new JFXButton("Player");
        goToPlayer.setTextFill(Paint.valueOf("212121"));
        goToPlayer.setBackground(new Background(new BackgroundFill(Paint.valueOf("FFFFFF"), null, null)));
        goToPlayer.setFont(Font.font("FangSong", FontWeight.BOLD, 18));
        goToPlayer.setTranslateY(650);
        goToPlayer.setTranslateX(150);
        goToPlayer.setOnAction(this);

        random = new JFXCheckBox();
        random.setTranslateX(250);
        random.setTranslateY(660);
        random.setText("Random");
        random.setTextFill(Paint.valueOf("#FFFFFF"));
        random.setFont(Font.font("FangSong", FontWeight.BOLD, 20));
        random.setOnAction(this);

        volumeSlider = new JFXSlider(0, 1, 0);
        volumeSlider.setTranslateX(340);
        volumeSlider.setTranslateY(620);
        volumeSlider.setPrefSize(100, 50);
        volumeSlider.setRotate(270);
        //these next lambda expressions are in charge of setting volume of the song
        volumeSlider.setOnMouseReleased(event -> mediaPlayer.setVolume(volumeSlider.getValue()));
        volumeSlider.setOnMouseDragged(event -> mediaPlayer.setVolume(volumeSlider.getValue()));

        //setting the image view to a default picture assigned by us
        imageView = new ImageView(img);
        imageView.setY(230);
        imageView.setX(150);
        imageView.setFitWidth(150);
        imageView.setFitHeight(150);
        Reflection reflection = new Reflection();
        reflection.setFraction(0.35);
        imageView.setEffect(reflection);

        timer = null;
        mediaPlayer = null;

        mainPain.getChildren().addAll(title, play, previous, volumeSlider, next, select, goToList, volumeDown, volumeUp, song, slider, mute, totalTime, currentTime, imageView);
        changingTheme();
        scene = new Scene(mainPain, 450, 750);

        listPane = new Pane();
        listPane.getChildren().addAll(listView, goToPlayer, random);
        listPane.setBackground(new Background(new BackgroundFill(Paint.valueOf("212121"), null, null)));

        stage.setScene(scene);
        stage.show();
        startJetTunes();
    }

    private void startJetTunes() {
        if (loadingFile()) {
            play.setText("Pause");
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
        if (event.getSource() == play)

        {
            if (Objects.equals(play.getText(), "Pause")) {
                play.setText("Play");
                mediaPlayer.pause();
                sliderClock(false); //must not forget to stop the time slider
            } else {
                play.setText("Pause");
                mediaPlayer.play();
                sliderClock(true); //must not forget to start the time slider
            }
        }

        if (event.getSource() == next) {
            nextSong();
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
        if (event.getSource() == random) {
            isRandom = !isRandom;
            savingParameters();
        }
    }

    private void loadingParam() {
        File paramFile = new File("res/Parameters");
        Scanner sc;
        try {
            sc = new Scanner(paramFile);
            if (sc.hasNextLine()) {
                isRandom = Boolean.valueOf(sc.nextLine());
                musicIndex = Integer.valueOf(sc.nextLine());
            }
            if (isFileExists(musicList.get(musicIndex).toURI().toString())) {
                hit = new Media(musicList.get(musicIndex).toURI().toString());
                mediaPlayer = new MediaPlayer(hit);
                mediaPlayer.setOnReady(this::playMusic);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void fillingTheList() {
        play.setText("Pause");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Music File");
        File file = fileChooser.showOpenDialog(stage);
        if (fileChooser.initialDirectoryProperty().getName().matches("initialDirectory")
                && file.getParentFile().isDirectory()) {
            File parent = file.getParentFile();
            FileWriter fw;
            try {
                fw = new FileWriter(new File("res/MusicList"), true);
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

        //stopping the previous song and its data
        if (timer != null) {
            sliderClock(false);
            timer = null;
            timerTask = null;
        }
        if (mediaPlayer != null) mediaPlayer.stop();

        hit = new Media(musicList.get(musicIndex).toURI().toString());

        mediaPlayer = new MediaPlayer(hit);

        //this is used to delay the media player enough for the song to be loaded
        //this doesn't influence play time (milli-seconds scale)
        mediaPlayer.setOnReady(this::playMusic);
    }

    private void nextSong() {
        changingTheme();
        mediaPlayer.stop();
        if (isRandom) musicIndex = new Random().nextInt(musicList.size());
        else ++musicIndex;
        if (musicIndex == musicList.size()) musicIndex = 0;
        hit = new Media(musicList.get(musicIndex).toURI().toString());
        mediaPlayer = new MediaPlayer(hit);
        mediaPlayer.setOnReady(this::playMusic);
        savingParameters();
        mediaPlayer.setMute(isMute);
    }

    private void previousSong() {
        changingTheme();
        mediaPlayer.stop();
        --musicIndex;
        if (musicIndex < 0) musicIndex = musicList.size() - 1;
        hit = new Media(musicList.get(musicIndex).toURI().toString());
        mediaPlayer = new MediaPlayer(hit);
        savingParameters();
        mediaPlayer.setOnReady(this::playMusic);
    }

    private void playMusic() {
        //setting up the sliders (volume and time)
        slider.setValue(0);
        slider.setMax(hit.getDuration().toSeconds());
        volumeSlider.setValue(mediaPlayer.getVolume());
        volumeSlider.setMax(mediaPlayer.getVolume());
        mediaPlayer = new MediaPlayer(hit);
        mediaPlayer.setOnEndOfMedia(this::nextSong);

        //setting basic song info (artist name, title)
        title.setText("" + hit.getMetadata().get("artist"));
        if (hit.getMetadata().get("artist") == null) {
            title.setText("Now Playing");
        }
        song.setText("" + hit.getMetadata().get("title"));
        if (hit.getMetadata().get("title") == null) {
            song.setText(hit.getSource().split("/")[hit.getSource().split("/").length - 1].replace("%20", " "));
        }

        sliderClock(false);
        //choosing an album picture (if null then we provide one)
        img = (Image) hit.getMetadata().get("image");
        if (img == null) {
            try {
                img = new Image(new FileInputStream(new File(String.valueOf(Paths.get("res/music.jpg")))));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        imageView.setImage(img);

        //playing the song and starting running the time slider
        mediaPlayer.play();
        mediaPlayer.setMute(isMute);
        sliderClock(true);
    }

    private boolean loadingFile() {
        File f = new File("res/MusicList");
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
                listView.getItems().add(song);
            }
        }
        listView.scrollTo(0);
        return true;
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

    private void savingParameters() {

        try {
            FileWriter fw = new FileWriter(new File("res/Parameters"), false);
            fw.write("" + isRandom + "\n");
            fw.write("" + musicIndex);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void changingView(boolean list) {
        if (list) {
            scene.setRoot(listPane);
            listView.getSelectionModel().select(musicIndex);
            listView.scrollTo(musicIndex);
        } else {
            mainPain = new Pane(title, play, previous, volumeSlider, next, select, goToList, volumeDown, volumeUp, song, slider, mute, totalTime, currentTime, imageView);
            changingTheme();
            scene.setRoot(mainPain);
        }
        stage.setScene(scene);
        stage.show();
    }

    private void changingTheme() {
        int random = new Random().nextInt(colors.length);
        mainPain.setBackground(new Background(new BackgroundFill(Paint.valueOf(colors[random]), null, null)));
    }
}
