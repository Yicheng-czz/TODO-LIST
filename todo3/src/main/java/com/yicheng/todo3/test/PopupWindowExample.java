package com.yicheng.todo3.test;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;

public class PopupWindowExample extends Application {

    private Stage smallWindow;
    private Timeline popupTimeline;

    @Override
    public void start(Stage primaryStage) {
        Button closeButton = new Button("Close");
        closeButton.setOnAction(event -> primaryStage.close());

        primaryStage.setTitle("Main Window");
        primaryStage.setScene(new Scene(closeButton, 300, 200));
        primaryStage.show();

        setupPopupWindow(primaryStage);
    }

    private void setupPopupWindow(Stage primaryStage) {
        smallWindow = new Stage();
        smallWindow.initStyle(StageStyle.UNDECORATED);

        Button closeButton = new Button("Close");
        closeButton.setOnAction(event -> smallWindow.close());

        Scene scene = new Scene(closeButton, 200, 100);
        smallWindow.setScene(scene);

        popupTimeline = new Timeline(
                new KeyFrame(Duration.hours(1), event -> showPopupWindow(primaryStage)),
                new KeyFrame(Duration.seconds(5), event -> hidePopupWindow())
        );
        popupTimeline.setCycleCount(Timeline.INDEFINITE);
        popupTimeline.play();
    }

    private void showPopupWindow(Stage primaryStage) {
        if (!primaryStage.isShowing()) {
            return;
        }

        smallWindow.setX(primaryStage.getX() + 100);
        smallWindow.setY(primaryStage.getY() + 100);
        smallWindow.show();
    }

    private void hidePopupWindow() {
        smallWindow.hide();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

