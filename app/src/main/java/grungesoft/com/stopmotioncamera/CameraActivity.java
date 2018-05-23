package grungesoft.com.stopmotioncamera;

import android.hardware.Camera;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class CameraActivity extends AppCompatActivity {
    private Camera mCamera;
    private final String TAG = "CameraActivity";
    private int cameraId = 0;

    TextView timeCountDownTextView;
    int TAKE_PHOTO_CODE = 0;
    public static int count = 0;
    private final int SECONDS_IN_MINUTE = 60;
    private final int MILLIS_IN_SECOND = 1000;
    private int waitmillis = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        Bundle extras = getIntent().getExtras();
        waitmillis = extras.getInt("delayDuration");

        timeCountDownTextView = (TextView)findViewById(R.id.minutes_readout);
        timeCountDownTextView.setText("Next Picture in: " + waitmillis + " Minute");

        triggerTimer();
    }

    private void triggerTimer()
    {
        new CountDownTimer((waitmillis * SECONDS_IN_MINUTE) * MILLIS_IN_SECOND, MILLIS_IN_SECOND) {

            public void onTick(long millisUntilFinished) {

                int seconds = (int) (millisUntilFinished / 1000) % 60 ;
                int minutes = (int) ((millisUntilFinished / (1000*60)) % 60);
                int hours   = (int) ((millisUntilFinished / (1000*60*60)) % 24);


                timeCountDownTextView.setText("Next Picture in: " + hours + ":" + minutes + ":" + seconds);
            }

            public void onFinish() {
                triggerPhoto();
                triggerTimer();
            }
        }.start();

    }

    /**
     *
     */
    private void triggerPhoto()
    {
        // Create an instance of Camera
        mCamera = getCameraInstance();

        if(mCamera!=null)
        {
            mCamera.stopPreview();
            mCamera.startPreview();
            mCamera.takePicture(null, null, new PhotoHandler(getApplicationContext()));

        }

    }

    /**
     *
     * @return
     */
    public Camera getCameraInstance(){
        Camera c = null;

        cameraId = findRearFacingCamera();

        try
        {
            c = Camera.open(cameraId); // attempt to get a Camera instance
        }
        catch (Exception e)
        {
            // Camera is not available (in use or does not exist)
            e.printStackTrace();
        }
        return c; // returns null if camera is unavailable
    }

    /**
     *
     * @return
     */
    private int findRearFacingCamera()
    {
        int cameraId = -1;
        // Search for the front facing camera
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++)
        {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK)
            {
                Log.d(MainActivity.TAG, "Camera found");
                cameraId = i;
                break;
            }
        }
        return cameraId;
    }

    /**
     *
     */
    @Override
    protected void onPause()
    {
        if (mCamera != null)
        {
            mCamera.release();
            mCamera = null;
        }
        super.onPause();
    }

}
