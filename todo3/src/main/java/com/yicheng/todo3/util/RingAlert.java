package com.yicheng.todo3.util;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class RingAlert {
    public void ringAlert(){
        Media media = new Media(getClass().getResource("/mp3/y2040.mp3").toString());
        MediaPlayer player = new MediaPlayer(media);
        player.setCycleCount(1);
        // 播放音频
        player.play();
    }
}
