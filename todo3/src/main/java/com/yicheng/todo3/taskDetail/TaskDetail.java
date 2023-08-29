package com.yicheng.todo3.taskDetail;

import com.yicheng.todo3.taskModel.Task;
import com.yicheng.todo3.util.AlertWindow;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class TaskDetail{

    public void openTaskDetail(Stage parentStage, Task task) {
        Parent parent = null;
        try{
            parent = FXMLLoader.load(getClass().getResource("/com/yicheng/todo3/taskDetail.fxml"));
        }catch (IOException ioe){
            AlertWindow.alertError("加载窗口失败！");
        }
        Scene scene = new Scene(parent);
        if(task != null){
            Node gridPane = scene.lookup("#gridPaneId");
            TextField taskIdInput = (TextField) gridPane.lookup("#taskIDInput");
            TextField taskInput = (TextField) gridPane.lookup("#taskInput");
            TextField dayInput = (TextField) gridPane.lookup("#dayInput");
            TextField timeInput = (TextField) gridPane.lookup("#timeInput");
            TextField ddlInput = (TextField) gridPane.lookup("#ddlInput");
            TextField courseInput = (TextField) gridPane.lookup("#courseInput");
            TextField taskDesInput = (TextField) gridPane.lookup("#taskDesInput");

            taskIdInput.setText(task.getTaskID());
            taskInput.setText(task.getTask());
            dayInput.setText(task.getDay());
            timeInput.setText(task.getTime());
            ddlInput.setText(task.getDeadline());
            courseInput.setText(task.getCourse());
            taskDesInput.setText(task.getTaskDescription());
        }
        scene.getStylesheets().add("org/kordamp/bootstrapfx/bootstrapfx.css");
        Stage stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(parentStage);
        stage.setScene(scene);
        stage.setTitle("任务详细");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/todo_list.png")));
        stage.showAndWait();
    }
}
