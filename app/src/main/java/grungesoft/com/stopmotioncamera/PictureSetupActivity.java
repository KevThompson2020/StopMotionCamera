package grungesoft.com.stopmotioncamera;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import grungesoft.com.stopmotioncamera.Gallary.GallaryViewActivity;
import grungesoft.com.stopmotioncamera.Utilities.Util;

public class PictureSetupActivity extends AppCompatActivity {
    public static final String LOG_TAG = "PictureSetupActivity";

    private int minutesSelected = 1;  // default to 60 mintues
    private TextView minutesReadout;
    private SeekBar minutesBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_setup);

        setupSeekBar();
        setupStartButton();
    }


    /**
     *
     */
    private void setupStartButton()
    {
        Button startButton = (Button)findViewById(R.id.picture_setup_button);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.log(LOG_TAG, "Galllary clicked");
                Intent cameraIntent = new Intent(PictureSetupActivity.this, CameraActivity.class);
                cameraIntent.putExtra("delayDuration", minutesSelected);
                startActivity(cameraIntent);

            }

        });
    }


    /**
     *
     */
    private void setupSeekBar()
    {
        minutesBar = (SeekBar)findViewById(R.id.seek_bar_minutes);
        minutesBar.setMinimumHeight(1);

        minutesReadout = (TextView)findViewById(R.id.minutes_readout);
        minutesReadout.setText("Minutes: " + minutesSelected);
        minutesBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChanged = 0;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
                int normalised = (int) ((float)progress * 0.6f);
                progressChanged = 1 + normalised;
                minutesSelected = progressChanged;
                minutesReadout.setText("Minutes:" + minutesSelected);
            }


            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                Toast.makeText(PictureSetupActivity.this,"seek bar progress:"+progressChanged,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

}
