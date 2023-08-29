package com.yicheng.todo3.sql;

import com.yicheng.todo3.util.AlertWindow;

import javax.print.DocFlavor;
import java.sql.*;

public class SqlConfig {
    private static String dbUrl = "jdbc:firebirdsql://172.30.198.50:3050/C:\\Users\\YC_9_\\Desktop\\TODO?charSet=utf8";
    private static String user = "SYSDBA";
    private static String pwd = "masterkey";

    private static String[] attributes = {"TASK_ID","TASK_NAME","TASK_DAY","TASK_TIME","TASK_DEADLINE","TASK_COURSE","TASK_DESCRIPTION","TASK_EMERGENCY","TASK_FINISHED"};

    public static String getAllTasksUnfinishedSql = "SELECT * FROM TASK WHERE TASK_FINISHED = 0;";
    public static String getAllTasksFinishedSql = "SELECT * FROM TASK WHERE TASK_FINISHED = 1;";
    public static String getAllTasksSql = "SELECT * FROM TASK;";
    public static String getTaskById = "SELECT * FROM TASK WHERE TASK_ID = ?;";
    public static String insertTaskSql = "INSERT INTO TASK(TASK_ID, TASK_NAME, TASK_DAY, TASK_TIME, TASK_DEADLINE, TASK_COURSE, TASK_DESCRIPTION, TASK_EMERGENCY, TASK_FINISHED) VALUES (?,?,?,?,?,?,?,?,?);";
    public static String updateTaskSql = "UPDATE TASK SET TASK_NAME = ?, TASK_DAY = ?, TASK_TIME = ?, TASK_DEADLINE = ?, TASK_COURSE = ?, TASK_DESCRIPTION = ?, TASK_EMERGENCY = ?, TASK_FINISHED = ? WHERE TASK_ID = ?;";

    public static String updateTaskEmergencySql = "UPDATE TASK SET TASK_EMERGENCY = ? WHERE TASK_ID = ?;";

    public static String updateTaskFinishedStatusSql = "UPDATE TASK SET TASK_FINISHED = ? WHERE TASK_ID = ?";
    public static String deleteTaskById = "DELETE FROM TASK WHERE TASK_ID = ?;";

    public static String getDbUrl() {
        return dbUrl;
    }

    public static String getPwd() {
        return pwd;
    }

    public static String getUser() {
        return user;
    }

    public static String getUpdateTaskFinishedStatusSql() {
        return updateTaskFinishedStatusSql;
    }

    public static String[] getAttributes() {
        return attributes;
    }

    public static String getGetTaskById() {
        return getTaskById;
    }

    public static String getDeleteTaskById() {
        return deleteTaskById;
    }

    public static String getGetAllTasksUnfinishedSql() {
        return getAllTasksUnfinishedSql;
    }

    public static String getGetAllTasksFinishedSql(){
        return getAllTasksFinishedSql;
    }

    public static String getUpdateTaskEmergencySql() {
        return updateTaskEmergencySql;
    }

    public static Connection connectDB() {
        try{
            return DriverManager.getConnection(getDbUrl(),getUser(),getPwd());
        }catch (SQLException e){
            AlertWindow.alertError("连接失败");
            return null;
        }
    }

    public static ResultSet getResult(Connection connection, String sql){
        try{
            Statement statement = connection.createStatement();
            return statement.executeQuery(sql);
        }catch (SQLException e){
            AlertWindow.alertError("获取数据集失败");
            return null;
        }
    }

    public static boolean judge(Connection connection, String sql){
        try{
            Statement statement = connection.createStatement();
            return statement.execute(sql);
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    public static void releaseConnect(Connection connection){
        try{
            if(connection != null && !connection.isClosed()){
                connection.close();
            }
        }catch (SQLException e){
            connection = null;
            AlertWindow.alertError("关闭数据库连接异常，已强制关闭!");
        }
    }
}
