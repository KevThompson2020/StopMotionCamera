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

        SavingPicture.getInstance().savePicture(data);

        Util.log(MainActivity.TAG, "PhotoHandler - onPictureTaken");
        File pictureFileDir = getDir();

        if (!pictureFileDir.exists() && !pictureFileDir.mkdirs()) {

            String errorString = context.getResources().getString(R.string.folder_creation_error);
            Log.d(MainActivity.TAG,errorString);
            Toast.makeText(context, errorString, Toast.LENGTH_LONG).show();
            return;

        }

        Util.log(MainActivity.TAG, "PhotoHandler - GetDate");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss");
        String date = dateFormat.format(new Date());
        String photoFile = "Picture_" + date + ".jpg";

        String filename = pictureFileDir.getPath() + File.separator + photoFile;

        File pictureFile = new File(filename);

        try {
            Util.log(MainActivity.TAG, "PhotoHandler - SaveStart");
            FileOutputStream fileOutStream = new FileOutputStream(pictureFile);
            fileOutStream.write(data);
            fileOutStream.close();
            Util.log(MainActivity.TAG, "PhotoHandler - SaveEnd");
            Toast.makeText(context, context.getResources().getString(R.string.new_image) + photoFile,Toast.LENGTH_LONG).show();
        } catch (Exception error) {
            Log.d(MainActivity.TAG, "File" + filename + "not saved: " + error.getMessage());
            Toast.makeText(context, "Image could not be saved.", Toast.LENGTH_LONG).show();
        }
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
