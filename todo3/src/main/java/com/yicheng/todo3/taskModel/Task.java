package com.yicheng.todo3.taskModel;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

;

public class Task {
    private StringProperty taskID;
    private StringProperty task;
    private StringProperty  day;
    private StringProperty  time;
    private StringProperty  deadline;
    private StringProperty  course;
    private StringProperty  taskDescription;

    private Emergency emergency;

    private boolean finished;

    public Task(String taskID, String task, String day, String time, String deadline, String course, String taskDescription, Emergency emergency, boolean finished) {
        this.taskID  = new SimpleStringProperty(taskID);
        this.task = new SimpleStringProperty(task);
        this.day = new SimpleStringProperty(day);
        this.time = new SimpleStringProperty(time);
        this.deadline = new SimpleStringProperty(deadline);
        this.course = new SimpleStringProperty(course);
        this.taskDescription = new SimpleStringProperty(taskDescription);
        this.emergency = emergency;
        this.finished = finished;
    }

    public boolean getFinished(){
        return finished;
    }

    public String getTaskID() {
        return taskID.get();
    }



    public void setTaskID(String taskID) {
        this.taskID.set(taskID);
    }

    public String getTaskDescription() {
        return taskDescription.get();
    }

    public Emergency getEmergency() {
        return emergency;
    }

    public void setEmergency(Emergency urgence) {
        this.emergency = urgence;
    }

    public String getDeadline() {
        return deadline.get();
    }

    public String getTask() {
        return task.get();
    }

    public String getCourse() {
        return course.get();
    }

    public String getDay() {
        return day.get();
    }

    public String getTime() {
        return time.get();
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription.set(taskDescription);
    }

    public void setTime(String time) {
        this.time.set(time);
    }

    public void setDeadline(String deadline) {
        this.deadline.set(deadline);
    }

    public void setDay(String day) {
        this.day.set(day);
    }

    public void setCourse(String course) {
        this.course.set(course);
    }

    public void setTask(String task) {
        this.task.set(task);
    }
}
