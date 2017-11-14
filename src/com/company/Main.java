package com.company;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSlider;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.File;
import java.util.Map;
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
    JFXButton stop;

    public static void main(String[] args) {
        // write your code here
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        stage.setTitle("JetReads");
        stage.setResizable(false);

        mainlayout = new Pane();

        title = new Label("Current reading page");
        title.setTextFill(Paint.valueOf("FFFFFF"));
        title.setTranslateY(100);
        title.setTranslateX(230);
        title.setAlignment(Pos.BOTTOM_CENTER);
        title.setTextAlignment(TextAlignment.CENTER);
        title.setFont(Font.font("FangSong", FontWeight.BOLD, 35));

        totalTime = new Label("3:54");
        totalTime.setTextFill(Paint.valueOf("FFFFFF"));
        totalTime.setTranslateY(350);
        totalTime.setTranslateX(600);
        totalTime.setAlignment(Pos.BOTTOM_CENTER);
        totalTime.setTextAlignment(TextAlignment.CENTER);
        totalTime.setFont(Font.font("FangSong", FontWeight.BOLD, 15));

        currentTime = new Label("1:54");
        currentTime.setTextFill(Paint.valueOf("FFFFFF"));
        currentTime.setTranslateY(350);
        currentTime.setTranslateX(190);
        currentTime.setAlignment(Pos.BOTTOM_CENTER);
        currentTime.setTextAlignment(TextAlignment.CENTER);
        currentTime.setFont(Font.font("FangSong", FontWeight.BOLD, 15));

        song = new Label("Evanecense the other side");
        song.setTextFill(Paint.valueOf("FFFFFF"));
        song.setTranslateY(700);
        song.setTranslateX(100);
        song.setAlignment(Pos.BOTTOM_CENTER);
        song.setTextAlignment(TextAlignment.CENTER);
        song.setPrefWidth(600);
        song.setFont(Font.font("FangSong", FontWeight.BOLD, 35));

        play = new JFXButton("Play");
        play.setTextFill(Paint.valueOf("006064"));
        play.setBackground(new Background(new BackgroundFill(Paint.valueOf("FFFFFF"), null, null)));
        play.setFont(Font.font("FangSong", FontWeight.BOLD, 40));
        play.setTranslateY(500);
        play.setTranslateX(320);
        play.setPrefWidth(170);
        play.setOnAction(this);


        next = new JFXButton(">");
        next.setTextFill(Paint.valueOf("006064"));
        next.setBackground(new Background(new BackgroundFill(Paint.valueOf("FFFFFF"), null, null)));
        next.setFont(Font.font("FangSong", FontWeight.BOLD, 40));
        next.setTranslateY(500);
        next.setTranslateX(550);
        next.setMaxWidth(150);
        next.setOnAction(this);

        previous = new JFXButton("<");
        previous.setTextFill(Paint.valueOf("006064"));
        previous.setBackground(new Background(new BackgroundFill(Paint.valueOf("FFFFFF"), null, null)));
        previous.setFont(Font.font("FangSong", FontWeight.BOLD, 40));
        previous.setTranslateY(500);
        previous.setTranslateX(190);
        previous.setMaxWidth(150);
        previous.setOnAction(this);

        stop = new JFXButton("<>");
        stop.setTextFill(Paint.valueOf("006064"));
        stop.setBackground(new Background(new BackgroundFill(Paint.valueOf("FFFFFF"), null, null)));
        stop.setFont(Font.font("FangSong", FontWeight.BOLD, 40));
        stop.setTranslateY(600);
        stop.setTranslateX(360);
        stop.setMaxWidth(150);
        stop.setOnAction(this);

        slider = new JFXSlider(0, 100, 0);
        slider.setTranslateX(20);
        slider.setTranslateY(300);
        slider.setPrefSize(350, 50);


        mainlayout.getChildren().addAll(title, play, next, previous, song, stop, slider, totalTime, currentTime);
        mainlayout.setBackground(new Background(new BackgroundFill(Paint.valueOf("006064"), null, null)));
        //mainlayout.setBackground(new Background(new BackgroundImage(img,null,null,null, null)));

        scene = new Scene(mainlayout, 600, 700);

        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void handle(ActionEvent event) {
        if (event.getSource() == play) {
            if (play.getText() == "Play") {
                play.setText("Pause");
                String bip = "C://Users//oussama//Music//GoPro_HERO3_Almost_as_Epic_as_the_HERO3_.hd.mp3";
                javafx.scene.media.Media hit = new javafx.scene.media.Media(new File(bip).toURI().toString());
                MediaPlayer mediaPlayer = new MediaPlayer(hit);
                mediaPlayer.setOnReady(new Runnable() {

                    @Override
                    public void run() {

                        System.out.println("Duration: " + hit.getDuration().toSeconds());

                        // play if you want
                        mediaPlayer.play();

                        slider.setMax(hit.getDuration().toSeconds());
                        sliderClock();
                    }
                });
                //mediaPlayer.play();
            } else play.setText("Play");
        }
    }

    public void sliderClock() {
       /* try {
            Thread.sleep(1000);
            slider.setValue(slider.getValue() + 1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                javafx.application.Platform.runLater(() -> {
                    slider.setValue(slider.getValue() + 1);
                });
            }
        }, 0, 1000);
    }
    /* */
}
