package com.yicheng.todo3.taskDetail;

import com.yicheng.todo3.sql.SqlConfig;
import com.yicheng.todo3.taskModel.Emergency;
import com.yicheng.todo3.taskModel.Task;
import com.yicheng.todo3.util.AlertWindow;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.UUID;

public class TaskDetailController implements Initializable {

    @FXML
    private TextField taskIDInput;

    @FXML
    private TextField taskInput;

    @FXML
    private TextField dayInput;

    @FXML
    private TextField timeInput;

    @FXML
    private TextField ddlInput;

    @FXML
    private TextField courseInput;

    @FXML
    private TextField taskDesInput;

    @FXML
    private Button cancelBtn;

    @FXML
    private Button finishBtn;

    public void handleFinishBtn(MouseEvent mouseEvent){     // 点击Finish按钮
        if(taskInput.getText().isEmpty() || dayInput.getText().isEmpty() || timeInput.getText().isEmpty() || ddlInput.getText().isEmpty() || courseInput.getText().isEmpty()){
            AlertWindow.alertWarning("星号(*)信息必须填写!");
        }
        else{
            Connection connection = SqlConfig.connectDB();
            PreparedStatement sql = null;
            if(connection != null){
                if(!taskIDInput.getText().isEmpty()){       // 修改任务
                    try{
                        sql = connection.prepareStatement(SqlConfig.getTaskById);
                        sql.setString(1,taskIDInput.getText());
                        ResultSet resultSet = sql.executeQuery();
                        resultSet.next();
                        int emergency = resultSet.getInt("TASK_EMERGENCY");

                        sql = connection.prepareStatement(SqlConfig.updateTaskSql);
                        sql.setString(1,taskInput.getText());
                        sql.setString(2,dayInput.getText());
                        sql.setString(3,timeInput.getText());
                        sql.setString(4,ddlInput.getText());
                        sql.setString(5,courseInput.getText());
                        sql.setString(6,taskDesInput.getText());
                        sql.setInt(7,4);
                        sql.setInt(8,0);
                        sql.setString(9,taskIDInput.getText());


                        Stage currentStage = (Stage) finishBtn.getScene().getWindow();
                        Stage parentStage = (Stage) currentStage.getOwner();
                        TableView<Task> tableView = (TableView<Task>)parentStage.getScene().lookup("#tableId");
                        currentStage.close();
                        if(sql.executeUpdate() == 1){
                            int i = 0;
                            boolean flag = true;
                            while(flag && i < tableView.getItems().size()){
                                if(tableView.getItems().get(i).getTaskID().equals(taskIDInput.getText())){
                                    tableView.getItems().remove(i);
                                    Task task = null;
                                    switch (emergency){
                                        case 1 -> task = new Task(taskIDInput.getText(),taskInput.getText(),dayInput.getText(),timeInput.getText(),ddlInput.getText(),courseInput.getText(),taskDesInput.getText(), Emergency.RED,false);
                                        case 2 -> task = new Task(taskIDInput.getText(),taskInput.getText(),dayInput.getText(),timeInput.getText(),ddlInput.getText(),courseInput.getText(),taskDesInput.getText(), Emergency.PINK,false);
                                        case 3 -> task = new Task(taskIDInput.getText(),taskInput.getText(),dayInput.getText(),timeInput.getText(),ddlInput.getText(),courseInput.getText(),taskDesInput.getText(), Emergency.BLUE,false);
                                        case 4 -> task = new Task(taskIDInput.getText(),taskInput.getText(),dayInput.getText(),timeInput.getText(),ddlInput.getText(),courseInput.getText(),taskDesInput.getText(), Emergency.BLACK,false);
                                    }
                                    tableView.getItems().add(i,task);
                                    flag = false;
                                }
                                ++i;
                            }
                            AlertWindow.alertInfo("任务修改成功！");
                        }
                        else{
                            AlertWindow.alertError("任务修改失败");
                        }
                    }catch (SQLException e){
                        e.printStackTrace();
                    }

                }
                else{       // 添加任务
                    try{
                        sql = connection.prepareStatement(SqlConfig.getTaskById);

                        boolean isExist = true;
                        String taskID = "";
                        while(isExist){
                            taskID = UUID.randomUUID().toString().substring(0,5);
                            sql.setString(1,taskID);
                            ResultSet resultSet = sql.executeQuery();
                            if(!resultSet.next()){
                                isExist = false;
                            }
                        }

                        sql = connection.prepareStatement(SqlConfig.insertTaskSql);
                        sql.setString(1,taskID);
                        sql.setString(2,taskInput.getText());
                        sql.setString(3,dayInput.getText());
                        sql.setString(4,timeInput.getText());
                        sql.setString(5,ddlInput.getText());
                        sql.setString(6,courseInput.getText());
                        sql.setString(7,taskDesInput.getText());
                        sql.setInt(8,4);
                        sql.setInt(9,0);

                        Stage currentStage = (Stage) finishBtn.getScene().getWindow();
                        Stage parentStage = (Stage) currentStage.getOwner();
                        TableView<Task> tableView = (TableView<Task>)parentStage.getScene().lookup("#tableId");
                        currentStage.close();

                        if(sql.executeUpdate() == 1){
                            tableView.getItems().add(new Task(taskID,taskInput.getText(),dayInput.getText(),timeInput.getText(),ddlInput.getText(),courseInput.getText(),taskDesInput.getText(), Emergency.BLACK,false));
                            AlertWindow.alertInfo("任务添加成功!");
                        }else{
                            AlertWindow.alertError("任务添加失败!");
                        }

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                }
            }
            else{
                Stage currentStage = (Stage) finishBtn.getScene().getWindow();
                currentStage.close();
                AlertWindow.alertError("连接失败 请检查网络!");
            }
        }
    }

    public void handleCancelBtn(MouseEvent mouseEvent) {    // 点击Cancel按钮后，关闭任务详细信息窗口
        Stage currentStage = (Stage) cancelBtn.getScene().getWindow();
        currentStage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
