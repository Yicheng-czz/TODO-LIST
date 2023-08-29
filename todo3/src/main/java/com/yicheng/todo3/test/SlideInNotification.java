package com.yicheng.todo3.test;

import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;


public class SlideInNotification extends Application {
    @Override
    public void start(Stage primaryStage) {
        Parent root = null;
        try{
            root = FXMLLoader.load(getClass().getResource("/com/yicheng/todo3/alertWindow.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);

        // 创建一个透明的无边框 Stage
        Stage stage = new Stage(StageStyle.TRANSPARENT);
        stage.setAlwaysOnTop(true);
        stage.setScene(scene);

        // 设置初始位置（屏幕左外侧）
        double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();
        System.out.println("wdith = " + scene.getWidth() + "; height = " + scene.getHeight());
        stage.setX(Screen.getPrimary().getVisualBounds().getWidth() - 265 );   // 窗口运动完之后的实际位置
        stage.setY(screenHeight - 341);

        // 创建一个滑动动画
        TranslateTransition slideInAnimation = new TranslateTransition(Duration.millis(1000), root);
        slideInAnimation.setFromX(Screen.getPrimary().getBounds().getWidth());  // 开始运动的位置
        slideInAnimation.setToX(0); // 相对于实际位置的相对位置

        // 显示并播放动画
        stage.show();
        slideInAnimation.play();

        Media media = new Media(getClass().getResource("/mp3/y2040.mp3").toString());
        MediaPlayer player = new MediaPlayer(media);
        player.setCycleCount(1);
        // 播放音频
        player.play();
    }

    public static void main(String[] args) {
        launch(args);
    }
}


