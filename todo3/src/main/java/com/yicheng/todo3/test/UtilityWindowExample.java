package com.yicheng.todo3.test;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class UtilityWindowExample extends Application {

    @Override
    public void start(Stage primaryStage) {
        Button showUtilityWindowButton = new Button("Show Utility Window");
        showUtilityWindowButton.setOnAction(event -> showUtilityWindow());

        primaryStage.setTitle("Main Window");
        primaryStage.setScene(new Scene(showUtilityWindowButton, 300, 200));
        primaryStage.show();
    }

    private void showUtilityWindow() {
        Stage utilityWindow = new Stage(StageStyle.UTILITY);
        utilityWindow.setTitle("Utility Window");
        utilityWindow.setScene(new Scene(new Button("Utility Window Content"), 200, 100));
        utilityWindow.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

