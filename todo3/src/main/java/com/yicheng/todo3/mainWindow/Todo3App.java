package com.yicheng.todo3.mainWindow;

import com.yicheng.todo3.alertWindow.AlterWindowController;
import com.yicheng.todo3.sql.SqlConfig;
import com.yicheng.todo3.taskDetail.TaskDetail;
import com.yicheng.todo3.taskModel.Emergency;
import com.yicheng.todo3.taskModel.Task;
import com.yicheng.todo3.util.AlertWindow;
import com.yicheng.todo3.util.RingAlert;
import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javafx.util.Duration;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


import static com.yicheng.todo3.taskModel.Emergency.*;

public class Todo3App extends Application {

    private Stage primaryStage;

    private Stage floatingStage;

    private FXMLLoader secondStageLoader;

    @Override
    public void start(Stage stage) throws Exception {
        this.primaryStage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/yicheng/todo3/todo3.fxml"));
        Parent root = fxmlLoader.load();

        ContextMenu contextMenu = new ContextMenu();
        MenuItem showItem = new MenuItem("Show");
        showItem.setOnAction(event -> Platform.runLater(() -> primaryStage.show()));
        MenuItem exitItem = new MenuItem("Exit");
        exitItem.setOnAction(event -> System.exit(0));
        contextMenu.getItems().addAll(showItem, new SeparatorMenuItem(), exitItem);

        primaryStage.setResizable(false);
        primaryStage.setTitle("TODO-LIST");
        Scene scene = new Scene(root);
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/todo_list.png")));
        initView(primaryStage,scene);
        primaryStage.setScene(scene);
        systemTray(primaryStage);
        primaryStage.getScene().getStylesheets().add("org/kordamp/bootstrapfx/bootstrapfx.css");
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch();
    }

    public void initView(Stage stage,Scene scene){

        TableView<Task> tableView = (TableView<Task>) scene.lookup("#tableId");

        ContextMenu contextMenu = new ContextMenu();
        MenuItem editMenuItem = new MenuItem("修改任务");
        MenuItem deleteMenuItem = new MenuItem("删除任务");
        MenuItem finishItem = new MenuItem("任务结束");


        Menu emergencyMenu = new Menu("紧急程度");
        MenuItem black = new MenuItem("低级");
        MenuItem blue = new MenuItem("中级");
        MenuItem pink = new MenuItem("高级");
        MenuItem red = new MenuItem("最高级");
        emergencyMenu.getItems().addAll(red,pink,blue,black);

        contextMenu.getItems().addAll(editMenuItem, emergencyMenu, finishItem, deleteMenuItem);
        tableView.setContextMenu(contextMenu);

        tableView.setRowFactory(new Callback<TableView<Task>, TableRow<Task>>() {
            @Override
            public TableRow<Task> call(TableView<Task> taskTableView) { // 这里的taskTableView代表当前的TableView对象

                return new TableRow<Task>(){

                    @Override
                    protected void updateItem(Task task,boolean empty){
                        super.updateItem(task, empty);

                        if(task != null){
                            if(task.getFinished()){
                                setStyle("-fx-font-size: 15px;");
                            }
                            else{
                                // 根据权重不同，渲染不同颜色
                                switch (task.getEmergency()){
                                    case BLACK -> setStyle("-fx-background-color: #71d071 ; -fx-font-size: 15px; ");
                                    case RED -> setStyle("-fx-background-color: #f83e61; -fx-font-size: 15px;");
                                    case BLUE -> setStyle("-fx-background-color: #77C3F2; -fx-font-size: 15px;");
                                    case PINK -> setStyle("-fx-background-color: #fd6bca ; -fx-font-size: 15px;");
                                }
                            }
                        }
                        else{
                            setStyle("-fx-background-color: transparent;");
                        }

                        setOnMouseClicked(event -> {
                            if (!isEmpty() && event.getButton() == MouseButton.SECONDARY) {
                                contextMenu.show(taskTableView, event.getScreenX(), event.getScreenY());
                            }
                        });

                        editMenuItem.setOnAction(event -> {
                            TaskDetail taskDetail = new TaskDetail();
                            try {
                                Task selectedTask = taskTableView.getSelectionModel().getSelectedItem();
                                taskDetail.openTaskDetail(stage,selectedTask);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });

                        deleteMenuItem.setOnAction(event -> {
                            Connection connection = SqlConfig.connectDB();
                            if(connection != null){
                                try{
                                    PreparedStatement sql = connection.prepareStatement(SqlConfig.deleteTaskById);
                                    sql.setString(1,taskTableView.getSelectionModel().getSelectedItem().getTaskID());
                                    if(sql.executeUpdate() == 1){
                                        AlertWindow.alertInfo("任务已删除！");
                                        taskTableView.getItems().remove(taskTableView.getSelectionModel().getSelectedIndex());
                                    }else{
                                        AlertWindow.alertWarning("任务删除失败，请检查网络");
                                    }
                                }catch (SQLException e){
                                    e.printStackTrace();
                                }
                            }


                        });

                    }
                };
            }

        });

        tableView.setOnMouseClicked(mouseEvent -> {
            if(mouseEvent.getButton() != MouseButton.SECONDARY && contextMenu.isShowing()){
                contextMenu.hide();
            }
        });

        scene.setOnMouseClicked(mouseEvent -> {
            contextMenu.hide();
        });

        Connection connection = SqlConfig.connectDB();
        if(connection != null){
            ResultSet resultSet = SqlConfig.getResult(connection,"SELECT * FROM TASK WHERE TASK_FINISHED = 0;");
            try{
                while (resultSet.next()){
                    String taskID = resultSet.getString("TASK_ID");
                    String task = resultSet.getString("TASK_NAME");
                    String day = resultSet.getString("TASK_DAY");
                    String time = resultSet.getString("TASK_TIME");
                    String ddl = resultSet.getString("TASK_DEADLINE");
                    String course = resultSet.getString("TASK_COURSE");
                    String description = resultSet.getString("TASK_DESCRIPTION");
                    int emergency = resultSet.getInt("TASK_EMERGENCY");
                    switch (emergency) {
                        case 1 -> tableView.getItems().add(new Task(taskID, task, day, time, ddl, course, description, RED, false));
                        case 2 -> tableView.getItems().add(new Task(taskID, task, day, time, ddl, course, description, PINK, false));
                        case 3 -> tableView.getItems().add(new Task(taskID, task, day, time, ddl, course, description, BLUE, false));
                        case 4 -> tableView.getItems().add(new Task(taskID, task, day, time, ddl, course, description, BLACK, false));
                    }
                }
            } catch (SQLException e) {
                AlertWindow.alertError(e.getMessage());
            }
        }
        SqlConfig.releaseConnect(connection);

        Stage floating = floatingWindow(tableView);
        this.floatingStage = floating;

        finishItem.setOnAction(event -> {
            Task selectedTask = tableView.getSelectionModel().getSelectedItem();
            Connection finishConnection = SqlConfig.connectDB();
            if(finishConnection != null){
                PreparedStatement sql;
                try{
                    sql = finishConnection.prepareStatement(SqlConfig.getUpdateTaskFinishedStatusSql());
                    sql.setInt(1,1);
                    sql.setString(2,selectedTask.getTaskID());
                    int cnt = sql.executeUpdate();
                    if(cnt == 1){
                        AlertWindow.alertInfo(selectedTask.getTaskID() + "任务已完成");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }else{
                AlertWindow.alertError("请检查网络");
            }
            try{
                finishConnection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            tableView.getItems().remove(selectedTask);

        });

        black.setOnAction(event -> {
            int selectedIndex = tableView.getSelectionModel().getSelectedIndex();
            updateEmergency(selectedIndex,tableView,BLACK);
        });

        blue.setOnAction(event -> {
            int selectedIndex = tableView.getSelectionModel().getSelectedIndex();
            updateEmergency(selectedIndex,tableView,BLUE);
        });

        pink.setOnAction(event -> {
            int selectedIndex = tableView.getSelectionModel().getSelectedIndex();
            updateEmergency(selectedIndex,tableView,PINK);
        });

        red.setOnAction(event -> {
            int selectedIndex = tableView.getSelectionModel().getSelectedIndex();
            updateEmergency(selectedIndex,tableView,RED);
        });

        primaryStage.setOnCloseRequest(windowEvent -> {
            windowEvent.consume();  // 阻止默认关闭行为
            primaryStage.hide();
        });

    }

    public void updateEmergency(int selectedIndex,TableView<Task> tableView,Emergency emergency){
        String taskID = tableView.getSelectionModel().getSelectedItem().getTaskID();
        Task task = tableView.getItems().get(selectedIndex);
        Connection con = SqlConfig.connectDB();

        PreparedStatement sql;
        try{
            sql = con.prepareStatement(SqlConfig.getUpdateTaskEmergencySql());
            switch (emergency){
                case BLUE -> sql.setInt(1,3);
                case BLACK -> sql.setInt(1,4);
                case PINK -> sql.setInt(1,2);
                case RED -> sql.setInt(1,1);
            }
            sql.setString(2,taskID);
            if(sql.executeUpdate()==1){
                tableView.getItems().remove(selectedIndex);
                task.setEmergency(emergency);
                tableView.getItems().add(selectedIndex,task);
            }
            else{
                AlertWindow.alertError("紧急程度修改失败!");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        SqlConfig.releaseConnect(con);
    }

    private void systemTray(Stage primaryStage) {
        try {
            //设置为false时点击关闭按钮程序不会退出
            Platform.setImplicitExit(false);
            //加载系统托盘图标
            URL url = getClass().getResource("/todo_list.png");
            //使用awt的组件制作系统托盘按钮
            java.awt.Image image = Toolkit.getDefaultToolkit().getImage(url);
            PopupMenu trayMenu = new PopupMenu();
            java.awt.MenuItem show = new java.awt.MenuItem("show");
            java.awt.MenuItem floating = new java.awt.MenuItem("float window");
            java.awt.MenuItem exit = new java.awt.MenuItem("exit");
            trayMenu.add(show);
            trayMenu.add(floating);
            trayMenu.add(exit);
            //加载系统托盘组件
            TrayIcon trayIcon = new TrayIcon(image, "TODO-LIST", trayMenu);
            //系统托盘图片自适应
            trayIcon.setImageAutoSize(true);
            //将系统托盘组件加载到系统托盘中
            SystemTray systemTray = SystemTray.getSystemTray();
            systemTray.add(trayIcon);
            //绑定系统托盘事件
            show.addActionListener(actionListener -> {
                Platform.runLater(() -> {
                    primaryStage.show();
                    floatingStage.hide();

                });
            });
            floating.addActionListener(actionListener ->{
                Platform.runLater(() ->{
                    primaryStage.hide();
                    AlterWindowController alterWindowController = secondStageLoader.getController();
                    alterWindowController.refreshLists();
                    floatingStage.show();
                });
            });
            exit.addActionListener(actionListener -> {
                try {
                    Platform.runLater(() ->{
                        Platform.exit();
                    });
                    systemTray.remove(trayIcon);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Stage floatingWindow(TableView<Task> tableView){
        AlterWindowController alterWindowController = new AlterWindowController(tableView);
        Parent parent = null;
        try{
            secondStageLoader = new FXMLLoader(getClass().getResource("/com/yicheng/todo3/alertWindow.fxml"));
            secondStageLoader.setController(alterWindowController);
            parent = secondStageLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Scene scene = new Scene(parent);
        Stage stage = new Stage(StageStyle.TRANSPARENT);
        scene.setFill(Color.TRANSPARENT);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/todo_list.png")));
        stage.setAlwaysOnTop(true);
        stage.setScene(scene);

        return stage;
    }
}
