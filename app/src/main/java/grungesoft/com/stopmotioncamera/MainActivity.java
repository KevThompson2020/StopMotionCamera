package grungesoft.com.stopmotioncamera;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    private int minutesSelected = 1;  // default to 60 mintues
    private TextView minutesReadout;
    private SeekBar minutesBar;
    public static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (shouldAskPermissions()) {
            askPermissions();
            return;
        }


        if (checkCameraHardware(this))
        {
            setupSeekBar();
            setupStartButton();
        }
        else
        {
            Log.d(TAG, "No Camera detected");
        }

        context = this;

    }

    /**
     *
     */
    private void setupStartButton()
    {
        Button startButton = (Button)findViewById(R.id.start_button);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MainActivity.this, CameraActivity.class);
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
                Toast.makeText(MainActivity.this,"seek bar progress:"+progressChanged,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }


    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    protected boolean shouldAskPermissions() {

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1)
        {
            String permission = "android.permission.WRITE_EXTERNAL_STORAGE";
            int res = checkCallingOrSelfPermission(permission);
            if(res == PackageManager.PERMISSION_GRANTED){
                return false;
            }
            else
            {
                return true;
            }


        }

        return false;
    }

    @TargetApi(23)
    protected void askPermissions() {
        String[] permissions = {
                "android.permission.READ_EXTERNAL_STORAGE",
                "android.permission.WRITE_EXTERNAL_STORAGE",
                "android.permission.CAMERA",
                "android.permission.ACCESS_FINE_LOCATION"
        };
        int requestCode = 200;
        requestPermissions(permissions, requestCode);
    }

}
