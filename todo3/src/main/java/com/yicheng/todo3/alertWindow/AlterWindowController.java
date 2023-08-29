package com.yicheng.todo3.alertWindow;

import com.yicheng.todo3.sql.SqlConfig;
import com.yicheng.todo3.taskModel.Task;
import com.yicheng.todo3.util.AlertWindow;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static com.yicheng.todo3.taskModel.Emergency.*;
import static com.yicheng.todo3.taskModel.Emergency.BLACK;

public class AlterWindowController implements Initializable {
    @FXML
    private ListView<String> urgentList;

    @FXML
    private ListView<String> importantList;

    @FXML
    private ListView<String> normalList;
    @FXML
    private ImageView closeAlertWindowBtn;

    private TableView<Task> primaryStageTableview = null;

    private boolean mouseIsPressed = false;
    private double mouseSceneXOffset;
    private double mouseSceneYOffset;

    public AlterWindowController(TableView<Task> tableView){
        primaryStageTableview = tableView;
    }

    @FXML
    public void closeAlertWindow(MouseEvent mouseEvent){
        Stage alertStage = (Stage) closeAlertWindowBtn.getScene().getWindow();
        Platform.runLater(() ->{
            alertStage.close();
        });
    }

    public void refreshLists(){
        urgentList.getItems().remove(0,urgentList.getItems().size());
        importantList.getItems().remove(0,importantList.getItems().size());
        normalList.getItems().remove(0,normalList.getItems().size());

        Connection connection = SqlConfig.connectDB();
        if(connection != null){
            ResultSet resultSet = SqlConfig.getResult(connection,"SELECT * FROM TASK WHERE TASK_FINISHED = 0;");
            try{
                while (resultSet.next()){
                    String task = resultSet.getString("TASK_NAME");
                    int emergency = resultSet.getInt("TASK_EMERGENCY");
                    switch (emergency) {
                        case 1 -> urgentList.getItems().add(task);
                        case 2 -> importantList.getItems().add(task);
                        case 3 -> normalList.getItems().add(task);
                    }
                }
            } catch (SQLException e) {
                AlertWindow.alertError(e.getMessage());
            }
        }
        SqlConfig.releaseConnect(connection);
    }

    @FXML
    public void beginDrag(MouseEvent mouseEvent){
        mouseIsPressed = true;
    }

    @FXML
    public void getOffset(MouseEvent mouseEvent){
        mouseSceneXOffset = mouseEvent.getSceneX();
        mouseSceneYOffset = mouseEvent.getSceneY();
    }

    @FXML
    public void dragging(MouseEvent mouseEvent){
        if(mouseIsPressed && mouseEvent.getButton() == MouseButton.PRIMARY){
            Stage stage = (Stage) closeAlertWindowBtn.getScene().getWindow();
            stage.setX(mouseEvent.getScreenX() - mouseSceneXOffset);
            stage.setY(mouseEvent.getScreenY() - mouseSceneYOffset);
        }
    }

    @FXML
    public void endDrag(MouseEvent mouseEvent){
        mouseIsPressed = false;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(primaryStageTableview == null){
            return;
        }
        for (Task task : primaryStageTableview.getItems()
        ) {
            switch (task.getEmergency()){
                case BLUE -> normalList.getItems().add(task.getTask());
                case PINK -> importantList.getItems().add(task.getTask());
                case RED -> urgentList.getItems().add(task.getTask());
            }
        }
    }
}
