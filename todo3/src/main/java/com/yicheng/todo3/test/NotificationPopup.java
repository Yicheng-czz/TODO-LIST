package com.yicheng.todo3.test;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class NotificationPopup extends Application {
    @Override
    public void start(Stage primaryStage) {
        Label messageLabel = new Label("This is a notification message");
        messageLabel.setStyle("-fx-background-color: #333333; -fx-padding: 10px; -fx-text-fill: white;");

        StackPane root = new StackPane(messageLabel);
        root.setAlignment(Pos.BOTTOM_LEFT);
        root.setPrefSize(300, 100);

        Scene scene = new Scene(root);

        // 创建一个透明的无边框 Stage
        Stage stage = new Stage(StageStyle.TRANSPARENT);
        stage.setScene(scene);

        // 设置初始位置（屏幕左下角）
        stage.setX(Screen.getPrimary().getVisualBounds().getWidth() - 310);
        stage.setY(Screen.getPrimary().getVisualBounds().getHeight() -  110);

        // 添加弹出动画效果
        KeyValue fadeInKeyValue = new KeyValue(stage.getScene().getRoot().opacityProperty(), 1);
        KeyFrame fadeInFrame = new KeyFrame(Duration.millis(3000), fadeInKeyValue);
        Timeline fadeInTimeline = new Timeline(fadeInFrame);

        KeyValue fadeOutKeyValue = new KeyValue(stage.getScene().getRoot().opacityProperty(), 0);
        KeyFrame fadeOutFrame = new KeyFrame(Duration.millis(3000), fadeOutKeyValue);
        Timeline fadeOutTimeline = new Timeline(fadeOutFrame);

        // 显示并播放动画
        stage.show();
        fadeInTimeline.play();

        // 设置自动关闭
        fadeOutTimeline.setDelay(Duration.seconds(10)); // 2秒后开始关闭动画
        fadeOutTimeline.setOnFinished(event -> stage.close()); // 关闭时关闭 Stage

        fadeOutTimeline.play();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

