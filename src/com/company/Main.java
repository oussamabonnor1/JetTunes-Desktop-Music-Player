package com.company;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXSlider;
import javafx.application.Application;
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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Timer;
import java.util.TimerTask;

public class Main extends Application implements EventHandler<ActionEvent> {

    Stage stage;
    Scene scene;
    Pane mainlayout;

    Label title;
    Label song;
    Label totalTime;
    Label currentTime;

    JFXSlider slider;
    JFXButton play;
    JFXButton next;
    JFXButton previous;
    JFXButton select;
    JFXCheckBox mute;
    ImageView imageView;
    Image img;
    MediaPlayer mediaPlayer;
    Timer timer;
    TimerTask timerTask;

    public static void main(String[] args) {
        // write your code here
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        stage.setTitle("JetTunes");
        stage.setResizable(false);

        mainlayout = new Pane();

        title = new Label("Choose a song to play");
        title.setTextFill(Paint.valueOf("FFFFFF"));
        title.setTranslateY(70);
        title.setAlignment(Pos.CENTER);
        title.setTextAlignment(TextAlignment.CENTER);
        title.setPrefWidth(450);
        title.setFont(Font.font("FangSong", FontWeight.BOLD, 35));

        totalTime = new Label("");
        totalTime.setTextFill(Paint.valueOf("FFFFFF"));
        totalTime.setTranslateY(520);
        totalTime.setTranslateX(400);
        totalTime.setAlignment(Pos.BOTTOM_CENTER);
        totalTime.setTextAlignment(TextAlignment.CENTER);
        totalTime.setFont(Font.font("FangSong", FontWeight.BOLD, 15));

        currentTime = new Label("");
        currentTime.setTextFill(Paint.valueOf("FFFFFF"));
        currentTime.setTranslateY(520);
        currentTime.setTranslateX(50);
        currentTime.setAlignment(Pos.BOTTOM_CENTER);
        currentTime.setTextAlignment(TextAlignment.CENTER);
        currentTime.setFont(Font.font("FangSong", FontWeight.BOLD, 20));

        song = new Label("");
        song.setTextFill(Paint.valueOf("FFFFFF"));
        song.setTranslateY(170);
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
        mute.setTranslateX(80);
        mute.setTranslateY(650);
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

        select = new JFXButton("...");
        select.setTextFill(Paint.valueOf("0F9D58"));
        select.setBackground(new Background(new BackgroundFill(Paint.valueOf("FFFFFF"), null, null)));
        select.setFont(Font.font("FangSong", FontWeight.BOLD, 18));
        select.setTranslateY(650);
        select.setTranslateX(205);
        select.setOnAction(this);

        slider = new JFXSlider(0, 100, 0);
        slider.setTranslateX(50);
        slider.setTranslateY(490);
        slider.setPrefSize(350, 50);
        slider.setMouseTransparent(true);


        try {
            img = new Image(new FileInputStream(new File(String.valueOf(Paths.get("res/music.jpg")))));
        } catch (IOException e) {
            e.printStackTrace();
        }
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

        mainlayout.getChildren().addAll(title, play, previous, next, select, song, slider, mute, totalTime, currentTime, imageView);
        mainlayout.setBackground(new Background(new BackgroundFill(Paint.valueOf("#224687"), null, null)));

        scene = new Scene(mainlayout, 450, 700);

        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void handle(ActionEvent event) {

        if (event.getSource() == select) {
            play.setText("Pause");
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Music File");
            File file = fileChooser.showOpenDialog(stage);

            //stopping the previous song and its data
            if (timer != null) {
                sliderClock(false);
                timer = null;
                timerTask = null;
            }
            if (mediaPlayer != null) mediaPlayer.stop();

            javafx.scene.media.Media hit = new javafx.scene.media.Media(file.toURI().toString());
            mediaPlayer = new MediaPlayer(hit);

            mediaPlayer.setOnReady(new Runnable() {

                @Override
                public void run() {
                    title.setText("" + hit.getMetadata().get("artist"));
                    if (hit.getMetadata().get("artist") == null) {
                        title.setText("Now Playing");
                    }
                    song.setText("" + hit.getMetadata().get("title"));
                    if (hit.getMetadata().get("title") == null) {
                        song.setText(file.getName().split("mp3")[0]);
                    }
                    mediaPlayer.play();

                    Image img = (Image) hit.getMetadata().get("image");
                    if (img != null) {
                        imageView.setImage(img);
                    }
                    slider.setValue(0);
                    slider.setMax(hit.getDuration().toSeconds());
                    sliderClock(true);
                }
            });
        }
        if (event.getSource() == play) {
            if (play.getText() == "Pause") {
                play.setText("Play");
                mediaPlayer.pause();
                sliderClock(true);
            } else {
                play.setText("Pause");
                mediaPlayer.play();
                sliderClock(false);
            }
        }
    }

    public void sliderClock(boolean state) {

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
            timerTask.cancel();
            timer.cancel();
            timer.purge();
        }
    }
    /* */
}
