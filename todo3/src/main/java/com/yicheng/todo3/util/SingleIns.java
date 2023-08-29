package com.yicheng.todo3.util;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class SingleIns extends Application {


    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/yicheng/todo3/singleIns.fxml"));
        Parent root = fxmlLoader.load();

        Scene scene = new Scene(root);
        scene.getStylesheets().add("org/kordamp/bootstrapfx/bootstrapfx.css");
        Stage singleInstance = new Stage();
        singleInstance.getIcons().add(new Image(getClass().getResourceAsStream("/todo_list.png")));
        singleInstance.setTitle("TODO-LIST");
        singleInstance.setResizable(false);
        singleInstance.setScene(scene);
        singleInstance.showAndWait();
    }

    public static void main(String[] args) {
        launch();
    }
}
