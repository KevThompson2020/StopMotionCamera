package grungesoft.com.stopmotioncamera.Utilities;

import android.os.Environment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

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
    public void savePicture(byte[] data)
    {

    }

    private String createSaveFileName()
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss");
        String date = dateFormat.format(new Date());
        String photoFile = "Picture_" + date + ".jpg";

        String filename = pictureFileDir_.getPath() + File.separator + photoFile;
        return "";
    }

}
