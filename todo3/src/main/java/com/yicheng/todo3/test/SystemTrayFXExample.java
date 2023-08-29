package com.yicheng.todo3.test;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class SystemTrayFXExample extends Application {

    private Stage primaryStage;
    private TrayIcon trayIcon;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        Button closeButton = new Button("Close");
        closeButton.setOnAction(event -> closePrimaryStage());

        primaryStage.setTitle("JavaFX App");
        primaryStage.setScene(new Scene(closeButton, 300, 200));
        primaryStage.setOnCloseRequest(event -> hidePrimaryStage()); // Hide instead of close
        primaryStage.show();

        setupSystemTray(primaryStage);
    }

    private void setupSystemTray(Stage primaryStage) {
        if (SystemTray.isSupported()) {
            SystemTray systemTray = SystemTray.getSystemTray();

            try {
                BufferedImage trayIconImage = ImageIO.read(getClass().getResourceAsStream("/todo_list.png"));
                trayIcon = new TrayIcon(trayIconImage);
                trayIcon.addActionListener(event -> Platform.runLater(this::togglePrimaryStageVisibility));

                PopupMenu popupMenu = new PopupMenu();
                MenuItem openMenuItem = new MenuItem("Open");
                openMenuItem.addActionListener(event -> Platform.runLater(this::showPrimaryStage));
                MenuItem exitMenuItem = new MenuItem("Exit");
                exitMenuItem.addActionListener(event -> Platform.exit());

                popupMenu.add(openMenuItem);
                popupMenu.add(exitMenuItem);

                trayIcon.setPopupMenu(popupMenu);
                systemTray.add(trayIcon);
            } catch (IOException | AWTException e) {
                e.printStackTrace();
            }
        }
    }

    private void togglePrimaryStageVisibility() {
        if (primaryStage.isShowing()) {
            hidePrimaryStage();
        } else {
            showPrimaryStage();
        }
    }

    private void showPrimaryStage() {
        Platform.runLater(() -> {
            primaryStage.show();
            primaryStage.toFront();
        });
    }

    private void hidePrimaryStage() {
        Platform.runLater(() -> {
            primaryStage.hide();
        });
    }

    private void closePrimaryStage() {
        SystemTray.getSystemTray().remove(trayIcon);
        Platform.exit();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

