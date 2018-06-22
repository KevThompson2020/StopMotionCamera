package grungesoft.com.stopmotioncamera;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.hardware.Camera;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import grungesoft.com.stopmotioncamera.Utilities.SavingPicture;
import grungesoft.com.stopmotioncamera.Utilities.Util;

public class PhotoHandler implements Camera.PictureCallback {

    private final Context context;

    public PhotoHandler(Context context) {
        Util.log(MainActivity.TAG, "PhotoHandler - Constructor");
        this.context = context;
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {


        SavingPicture.getInstance().savePicture(context, data);
    }

    /**
     *
     * @return
     */
    private File getDir() {
        Util.log(MainActivity.TAG, "PhotoHandler - GetDir");
        File sdDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        return new File(sdDir, "CameraAPIDemo");
    }
}
