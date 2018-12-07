package ru.startandroid.audiocross;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button audio1, audio2, play;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        audio1 = (Button) findViewById(R.id.audio1);
        audio2 = (Button) findViewById(R.id.audio2);
        play = (Button) findViewById(R.id.play);

        audio1.setOnClickListener(this);
        audio2.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.audio1 :
                try {
                    OpenFileDialog fileDialog = new OpenFileDialog(this);
                    fileDialog.show();
                } catch (RuntimeException exc) {
                    Toast.makeText(this, exc.toString(), Toast.LENGTH_LONG).show();
                };

                break;
        }
    }
}
