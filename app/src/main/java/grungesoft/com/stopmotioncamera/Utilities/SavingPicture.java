package grungesoft.com.stopmotioncamera.Utilities;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import grungesoft.com.stopmotioncamera.MainActivity;
import grungesoft.com.stopmotioncamera.R;

public class SavingPicture {

    private static volatile SavingPicture sSoleInstance = new SavingPicture();
    File pictureFileDir_;

    public final String FOLDER_NAME = "StopMotionCamera";

    //private constructor.
    private SavingPicture(){}

    /**
     * get the instance of the singleton
     * @return
     */
    public static SavingPicture getInstance() {
        return sSoleInstance;
    }

    /**
     *
     */
    public void FolderChecks() {
        if(!Util.checkIfFolderExists(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), FOLDER_NAME)) {
            Util.createFolder(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), FOLDER_NAME);
        }

        pictureFileDir_ = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
    }


    /**
     *
     */
    public void savePicture(Context context, byte[] data)
    {
        String name = createSaveFileName();
        File pictureFile = new File(name);

        try {
            Util.log(MainActivity.TAG, "PhotoHandler - SaveStart");
            FileOutputStream fileOutStream = new FileOutputStream(pictureFile);
            fileOutStream.write(data);
            fileOutStream.close();
            Util.log(MainActivity.TAG, "PhotoHandler - SaveEnd");
            Toast.makeText(context, context.getResources().getString(R.string.new_image) + pictureFile.getPath(),Toast.LENGTH_LONG).show();
        } catch (Exception error) {
            Log.d(MainActivity.TAG, "File" + pictureFile.getPath() + "not saved: " + error.getMessage());
            Toast.makeText(context, "Image could not be saved.", Toast.LENGTH_LONG).show();
        }
    }

    private String createSaveFileName()
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss");
        String date = dateFormat.format(new Date());
        String photoFile = "Picture_" + date + ".jpg";

        String filename = pictureFileDir_.getPath() + File.separator + FOLDER_NAME +  File.separator + photoFile;
        return filename;
    }

}
