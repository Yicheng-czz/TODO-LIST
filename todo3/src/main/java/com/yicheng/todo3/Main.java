package com.yicheng.todo3;

import com.yicheng.todo3.mainWindow.Todo3App;
import com.yicheng.todo3.util.AlertWindow;
import com.yicheng.todo3.util.SingleIns;

import java.io.File;
import java.io.IOException;


public class Main {

    private static String filePath = "./lock.txt";

    public static void main(String[] args) {
        File file = new File(filePath);
        if(!file.exists()){
            try{
                if(file.createNewFile()){
                    Todo3App.main(args);
                    file.delete();
                }
                else{
                    AlertWindow.alertError("初始化文件锁失败");
                }
            }catch (IOException e){
                AlertWindow.alertError(e.getMessage());
            }
        }
        else{
            SingleIns.main(args);
        }
    }
}
