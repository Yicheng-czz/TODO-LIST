package com.yicheng.todo3.mainWindow;

import com.yicheng.todo3.sql.SqlConfig;
import com.yicheng.todo3.taskDetail.TaskDetail;
import com.yicheng.todo3.taskModel.Emergency;
import com.yicheng.todo3.taskModel.Task;

import com.yicheng.todo3.util.AlertWindow;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class Todo3Controller{

    @FXML
    private MenuItem appearanceId;

    @FXML
    private MenuItem tutorialId;

    @FXML
    private MenuItem authorId;

    @FXML
    private TableView<Task> tableId;

    @FXML
    private ToggleButton toggleBtnId;

    @FXML
    private Button addBtn;

    @FXML
    public void appearanceClicked(){

    }

    @FXML
    public void tutorialId(){

    }

    @FXML
    public void authorIdClicked(){

    }

    @FXML
    public void addBtnClicked(){
        TaskDetail taskDetail = new TaskDetail();
        taskDetail.openTaskDetail((Stage) addBtn.getScene().getWindow(),null);
    }

    @FXML
    public void toggleHandle(MouseEvent mouseEvent){
        Connection connection = SqlConfig.connectDB();
        PreparedStatement sql;
        if(connection != null){
            try{
                if(toggleBtnId.isSelected()){   // 开启
                    sql = connection.prepareStatement(SqlConfig.getGetAllTasksFinishedSql());
                    ResultSet result = sql.executeQuery();
                    Task task = null;
                    while(result.next()){
                        String taskId = result.getString("TASK_ID");
                        String taskName = result.getString("TASK_NAME");
                        String day = result.getString("TASK_DAY");
                        String time = result.getString("TASK_TIME");
                        String ddl = result.getString("TASK_DEADLINE");
                        String course = result.getString("TASK_COURSE");
                        String des = result.getString("TASK_DESCRIPTION");
                        int emergency = result.getInt("TASK_EMERGENCY");
                        switch (emergency){
                            case 1 -> task = new Task(taskId,taskName,day,time,ddl,course,des, Emergency.RED,true);
                            case 2 -> task = new Task(taskId,taskName,day,time,ddl,course,des, Emergency.PINK,true);
                            case 3 -> task = new Task(taskId,taskName,day,time,ddl,course,des, Emergency.BLUE,true);
                            case 4 -> task = new Task(taskId,taskName,day,time,ddl,course,des, Emergency.BLACK,true);
                        }
                        tableId.getItems().add(0,task);

                    }
                }
                else{                           // 关闭
                    int i = 0;
                    while(i < tableId.getItems().size()){
                        if(tableId.getItems().get(i).getFinished()){
                            tableId.getItems().remove(i);
                        }
                        else{
                            ++i;
                        }

                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        else{
            AlertWindow.alertError("请检查网络连接");
        }
        SqlConfig.releaseConnect(connection);
    }

}
