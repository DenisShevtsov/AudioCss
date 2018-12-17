package ru.startandroid.audiocross;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    Button audio1, audio2, play;
    private String musik1, musik2;
    TextView timer;
    SeekBar sBar;
    int mix = 2;
    MediaPlayer mediaPlayer1, mediaPlayer2;
    AudioManager am;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        audio1 = (Button) findViewById(R.id.audio1);
        audio2 = (Button) findViewById(R.id.audio2);
        sBar = (SeekBar) findViewById(R.id.seekBar);
        play = (Button) findViewById(R.id.play);
        timer = (TextView) findViewById(R.id.timer);
        am = (AudioManager) getSystemService(AUDIO_SERVICE);


        audio1.setOnClickListener(this);
        audio2.setOnClickListener(this);
        sBar.setOnSeekBarChangeListener(this);
        play.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        try {
        switch (view.getId()) {
            case R.id.audio1:
                OpenFileDialog fileDialog = new OpenFileDialog(this)
                        .setFilter(".*\\.mp3")
                        .setOpenDialogListener(new OpenFileDialog.OpenDialogListener() {
                            @Override
                            public void OnSelectedFile(String fileName) {
                                musik1 = fileName;
                            }
                        });
                fileDialog.show();
                break;

            case R.id.audio2:
                OpenFileDialog fileDialog2 = new OpenFileDialog(this)
                        .setFilter(".*\\.mp3")
                        .setOpenDialogListener(new OpenFileDialog.OpenDialogListener() {
                            @Override
                            public void OnSelectedFile(String fileName) {
                                musik2 = fileName;
                            }
                        });
                fileDialog2.show();
                break;

            case R.id.play:
                mediaPlayer1 = new MediaPlayer();
                mediaPlayer2 = new MediaPlayer();
                mediaPlayer1.setDataSource(musik1);
                mediaPlayer2.setDataSource(musik2);
                mediaPlayer1.setLooping(true);
                mediaPlayer2.setLooping(true);
                mediaPlayer1.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer2.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer1.prepare();
                mediaPlayer2.prepare();


                mediaPlayer1.start();
                for (int g = mix;;){
                    if(mediaPlayer1.getCurrentPosition() > g*1000){
                        mediaPlayer1.pause();
                        mediaPlayer2.start();
                        if (mediaPlayer2.getCurrentPosition() > g*1000){
                            mediaPlayer2.pause();
                            mediaPlayer1.start();
                            g = g + mix;
                        }
                    }
                }
        }
        } catch (IOException exc) {
            Toast.makeText(this, exc.toString(), Toast.LENGTH_LONG).show();
        }catch (RuntimeException exc) {
            Toast.makeText(this, exc.toString(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        int progress = i;
        int st = sBar.getMax()/9;

        for (int r = 1; r < 10; r++){
            if (progress < r * st) {
                mix = r + 1;
                break;
            }
        }
        timer.setText(mix + "c");
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
