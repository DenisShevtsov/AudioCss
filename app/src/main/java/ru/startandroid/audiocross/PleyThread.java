package ru.startandroid.audiocross;

import android.media.AudioManager;
import android.media.MediaPlayer;

import java.io.IOException;

public class PleyThread implements Runnable {

    MediaPlayer mediaPlayer1, mediaPlayer2;
    private String mu1, mu2;
    Thread thrd;
    int timer;

    PleyThread(String musik1, String musik2, int mix, String name){
        mu1 = musik1;
        mu2 = musik2;
        timer = mix*1000;
        thrd = new Thread(this, name);
        thrd.start();
    }

    @Override
    public void run() {
        try {
            mediaPlayer1 = new MediaPlayer();
            mediaPlayer2 = new MediaPlayer();
            mediaPlayer1.setDataSource(mu1);
            mediaPlayer2.setDataSource(mu2);
            mediaPlayer1.setLooping(true);
            mediaPlayer2.setLooping(true);
            mediaPlayer1.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer2.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer1.prepare();
            mediaPlayer2.prepare();

            mediaPlayer1.start();
            for (; ; ) {
                thrd.sleep(timer);
                mediaPlayer1.pause();
                mediaPlayer2.start();
                thrd.sleep(timer);
                mediaPlayer2.pause();
                mediaPlayer1.start();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
