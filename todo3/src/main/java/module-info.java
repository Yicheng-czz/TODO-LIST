module com.yicheng.todo3 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;

    requires org.controlsfx.controls;
    requires java.desktop;
    requires java.sql;


    opens com.yicheng.todo3 to javafx.fxml;
    opens com.yicheng.todo3.taskModel to javafx.base;
    opens com.yicheng.todo3.taskDetail to javafx.graphics,javafx.fxml;
    opens com.yicheng.todo3.util to javafx.graphics;
    opens com.yicheng.todo3.test to javafx.graphics;
    opens com.yicheng.todo3.alertWindow to javafx.fxml;
    exports com.yicheng.todo3;
    exports com.yicheng.todo3.mainWindow;
    opens com.yicheng.todo3.mainWindow to javafx.fxml;
}