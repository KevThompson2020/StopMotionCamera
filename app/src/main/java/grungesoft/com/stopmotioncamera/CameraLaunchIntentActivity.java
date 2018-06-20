package grungesoft.com.stopmotioncamera;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;

import grungesoft.com.stopmotioncamera.Utilities.Util;

public class CameraLaunchIntentActivity extends AppCompatActivity {

    TextView timeCountDownTextView;
    int TAKE_PHOTO_CODE = 0;
    public static int count = 0;
    private final int SECONDS_IN_MINUTE = 60;
    private final int MILLIS_IN_SECOND = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Util.log(MainActivity.TAG, "CameraLaunchIntentActivity - GetDate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        Bundle extras = getIntent().getExtras();
        int waitTime = extras.getInt("delayDuration");

        timeCountDownTextView = (TextView)findViewById(R.id.minutes_readout);
        timeCountDownTextView.setText("Next Picture in: " + waitTime + " Minute");

        new CountDownTimer((waitTime * SECONDS_IN_MINUTE) * MILLIS_IN_SECOND, MILLIS_IN_SECOND) {

            public void onTick(long millisUntilFinished) {

                int seconds = (int) (millisUntilFinished / 1000) % 60 ;
                int minutes = (int) ((millisUntilFinished / (1000*60)) % 60);
                int hours   = (int) ((millisUntilFinished / (1000*60*60)) % 24);

                timeCountDownTextView.setText("Next Picture in: " + hours + ":" + minutes + ":" + seconds);
            }

            public void onFinish() {
                Util.log(MainActivity.TAG, "CameraLaunchIntentActivity - Timer onFinish");
                triggerPhoto();
            }
        }.start();



    }

    private void triggerPhoto()
    {

        Util.log(MainActivity.TAG, "CameraLaunchIntentActivity - triggerPhoto");
        final String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/picFolder/";
        File newdir = new File(dir);
        newdir.mkdirs();


        count++;
        String file = dir+count+".jpg";
        File newfile = new File(file);
        try {
            newfile.createNewFile();
        }
        catch (IOException e)
        {
        }

        Uri outputFileUri = Uri.fromFile(newfile);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

        startActivityForResult(cameraIntent, TAKE_PHOTO_CODE);

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == TAKE_PHOTO_CODE && resultCode == RESULT_OK) {
            Log.d("CameraDemo", "Pic saved");
        }
    }

}
