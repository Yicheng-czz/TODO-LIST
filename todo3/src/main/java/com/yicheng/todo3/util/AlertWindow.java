package com.yicheng.todo3.util;


import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.util.Timer;

public class AlertWindow {

    public static void alertInfo(String info){
        Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);
        infoAlert.setTitle("信息");
        infoAlert.setContentText(info);
        infoAlert.setHeaderText("");
        infoAlert.setResizable(false);
        infoAlert.showAndWait();
    }

    public static void alertWarning(String warning){
        Alert warningAlert = new Alert(Alert.AlertType.WARNING);
        warningAlert.setTitle("警告");
        warningAlert.setContentText(warning);
        warningAlert.setHeaderText("");
        warningAlert.setResizable(false);
        warningAlert.showAndWait();
    }

    public static void alertError(String error){
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setTitle("错误");
        errorAlert.setContentText(error);
        errorAlert.setHeaderText("");
        errorAlert.setResizable(false);
        errorAlert.showAndWait();
    }

    public static void alertConfirm(Stage stage){
        // 显示确认对话框
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("确认");
        confirmAlert.setHeaderText("确定要关闭窗口吗？");

        // 添加“确定”和“取消”按钮
        ButtonType confirmButton = new ButtonType("确定");
        ButtonType cancelButton = new ButtonType("取消");
        confirmAlert.getButtonTypes().setAll(confirmButton, cancelButton);

        confirmAlert.showAndWait().ifPresent(buttonType -> {
            if (buttonType == confirmButton) {
                Platform.runLater(() ->{
                    stage.hide();
                });
            }
        });

    }



}
