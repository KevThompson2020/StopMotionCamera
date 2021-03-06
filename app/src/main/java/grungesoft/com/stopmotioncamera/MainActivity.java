package grungesoft.com.stopmotioncamera;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.opencv.android.OpenCVLoader;

import grungesoft.com.stopmotioncamera.Gallary.GallaryViewActivity;
import grungesoft.com.stopmotioncamera.Utilities.SavingPicture;
import grungesoft.com.stopmotioncamera.Utilities.Util;


public class MainActivity extends AppCompatActivity {

    public static final String TAG = "StopMotionCameraApp";
    public static final String LOG_TAG = "MainActivity";

    public static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PermissionChecks();
        SavingPicture.getInstance().FolderChecks();


        OpenCVLoader.initDebug();



        if (checkCameraHardware(this)) {
            setupStartButton();
            setupGallaryButton();
            setupMovieButton();
        }
        else {
            Util.log(LOG_TAG, "No Camera detected");
        }

        context = this;

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
                Intent pictureSetupIntent = new Intent(MainActivity.this, PictureSetupActivity.class);
                startActivity(pictureSetupIntent);
            }
        });
    }

    /**
     *
     */
    private void setupGallaryButton()
    {
        Button startButton = (Button)findViewById(R.id.gallary_button);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.log(LOG_TAG, "Movie clicked");
                Intent gallaryIntent = new Intent(MainActivity.this, GallaryViewActivity.class);
                startActivity(gallaryIntent);
            }
        });
    }


    /**
     *
     */
    private void setupMovieButton()
    {
        Button startButton = (Button)findViewById(R.id.movie_button);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.log(LOG_TAG,"Start clicked");
                Intent movieIntent = new Intent(MainActivity.this, MovieConverterActivity.class);
                startActivity(movieIntent);
            }
        });
    }

    /**
     *
     * @param context
     * @return
     */
    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }


    /**
     *
     */
    private void PermissionChecks()
    {
        if (shouldAskPermissions()) {
            askPermissions();
            return;
        }
    }

    /**
     *
     * @return
     */
    protected boolean shouldAskPermissions() {

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1)
        {
            String permission = "android.permission.WRITE_EXTERNAL_STORAGE";
            int res = checkCallingOrSelfPermission(permission);
            if(res == PackageManager.PERMISSION_GRANTED){
                return false;
            }
            else {
                return true;
            }
        }
        return false;
    }

    /**
     *
     */
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
