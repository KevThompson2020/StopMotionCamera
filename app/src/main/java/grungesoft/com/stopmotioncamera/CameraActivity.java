package grungesoft.com.stopmotioncamera;

import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.CountDownTimer;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.support.v7.app.AppCompatActivity;
import java.io.IOException;

import grungesoft.com.stopmotioncamera.Utilities.Util;

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

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        Bundle extras = getIntent().getExtras();
        waitmillis = extras.getInt("delayDuration");

        timeCountDownTextView = (TextView)findViewById(R.id.minutes_readout);
        timeCountDownTextView.setText("Next Picture in: " + waitmillis + " Minute");

        triggerTimer();
        Util.log(MainActivity.TAG, "CameraActivty - OnCreate");
    }

    /**
     *
     */
    private void triggerTimer()
    {

        Util.log(MainActivity.TAG, "CameraActivty - triggerTimer");
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

        if(mCamera!=null) {
            Util.log(MainActivity.TAG, "CameraActivty - triggerPhoto");

            try {
                mCamera.setPreviewTexture(new SurfaceTexture(10));
            } catch (IOException e1) {
                Log.e(MainActivity.TAG, e1.getMessage());
            }

            Camera.Parameters params = mCamera.getParameters();
            params.setPreviewSize(640, 480);
            params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            params.setPictureFormat(ImageFormat.JPEG);
            params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            mCamera.setParameters(params);
            mCamera.startPreview();
            mCamera.takePicture(null, null, null, new PhotoHandler(getApplicationContext()));
        }


//        SurfaceView surface = new SurfaceView(this);
//        if(mCamera!=null)
//        {
//            Util.log(MainActivity.TAG, "CameraActivty - triggerPhoto");
//
//            //Util.log(MainActivity.TAG, "CameraActivty - stopPreview");
//            //mCamera.stopPreview();
//
//            try {
//                mCamera.setPreviewDisplay(surface.getHolder());
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            Util.log(MainActivity.TAG, "CameraActivty - startPreview");
//            mCamera.startPreview();
//
//            Util.log(MainActivity.TAG, "CameraActivty - takePicture");
//            mCamera.takePicture(null, null, new PhotoHandler(getApplicationContext()));
//
////            mCamera.takePicture(null, null, jpegCallback);
//
//
//            //mCamera.release();
//        }

    }

    /**
     *
     * @return
     */
    public Camera getCameraInstance(){
        Camera c = null;

        Util.log(MainActivity.TAG, "getCameraInstance - start");

        cameraId = findRearFacingCamera();

        try
        {
            Util.log(MainActivity.TAG, "getCameraInstance - Camera.open");
            c = Camera.open(cameraId); // attempt to get a Camera instance

            // WORKS
//            try {
//                c.setPreviewTexture(new SurfaceTexture(10));
//            } catch (IOException e1) {
//                Log.e(MainActivity.TAG, e1.getMessage());
//            }
//
//            Camera.Parameters params = c.getParameters();
//            params.setPreviewSize(640, 480);
//            params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
//            params.setPictureFormat(ImageFormat.JPEG);
//            c.setParameters(params);
//            c.startPreview();
//            c.takePicture(null, null, null, new Camera.PictureCallback() {
//                @Override
//                public void onPictureTaken(byte[] data, Camera camera) {
//                    Log.i(MainActivity.TAG, "picture-taken");
//                }
//            });


        }
        catch (Exception e)
        {
            Util.log(MainActivity.TAG, "getCameraInstance - Camera not available");
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

//    /** picture call back */
//    Camera.PictureCallback jpegCallback = new Camera.PictureCallback() {
//        public void onPictureTaken(byte[] data, Camera camera)
//        {
//            FileOutputStream outStream = null;
//            try {
//                String dir_path = "";// set your directory path here
//                outStream = new FileOutputStream(dir_path+ File.separator+"hello"+"Hello"+".jpg");
//                outStream.write(data);
//                outStream.close();
//                Log.d(TAG, "onPictureTaken - wrote bytes: " + data.length);
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            } finally
//            {
//                camera.stopPreview();
//                camera.release();
//                camera = null;
//                //Toast.makeText(getApplicationContext(), "Image snapshot Done",Toast.LENGTH_LONG).show();
//
//
//            }
//            Log.d(TAG, "onPictureTaken - jpeg");
//        }
//    };

}
