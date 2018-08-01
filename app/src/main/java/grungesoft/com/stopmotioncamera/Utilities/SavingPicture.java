package grungesoft.com.stopmotioncamera.Utilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
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
     * @return
     */
    public File getPictureFileDir() {
        return pictureFileDir_;
    }

    /**
     *
     */
    public void FolderChecks() {

        if(!Util.checkIfFolderExists(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), FOLDER_NAME)) {
            Util.createFolder(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), FOLDER_NAME);
        }

        pictureFileDir_ = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator +  FOLDER_NAME);
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

            Bitmap realImage = BitmapFactory.decodeByteArray(data, 0, data.length);
            ExifInterface exif=new ExifInterface(pictureFile.toString());

            Log.d("EXIF value", exif.getAttribute(ExifInterface.TAG_ORIENTATION));
            if(exif.getAttribute(ExifInterface.TAG_ORIENTATION).equalsIgnoreCase("6")){
                realImage= rotate(realImage, 90);
            } else if(exif.getAttribute(ExifInterface.TAG_ORIENTATION).equalsIgnoreCase("8")){
                realImage= rotate(realImage, 270);
            } else if(exif.getAttribute(ExifInterface.TAG_ORIENTATION).equalsIgnoreCase("3")){
                realImage= rotate(realImage, 180);
            } else if(exif.getAttribute(ExifInterface.TAG_ORIENTATION).equalsIgnoreCase("0")){
                realImage= rotate(realImage, 90);
            }

            boolean bo = realImage.compress(Bitmap.CompressFormat.JPEG, 100, fileOutStream);

            fileOutStream.write(data);
            fileOutStream.close();
            Util.log(MainActivity.TAG, "PhotoHandler - SaveEnd");
            Toast.makeText(context, context.getResources().getString(R.string.new_image) + pictureFile.getPath(),Toast.LENGTH_LONG).show();
        } catch (Exception error) {
            Log.d(MainActivity.TAG, "File" + pictureFile.getPath() + "not saved: " + error.getMessage());
            Toast.makeText(context, "Image could not be saved.", Toast.LENGTH_LONG).show();
        }
    }


    public static Bitmap rotate(Bitmap bitmap, int degree) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        Matrix mtx = new Matrix();
        //       mtx.postRotate(degree);
        mtx.setRotate(degree);

        return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);
    }

    private String createSaveFileName()
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss");
        String date = dateFormat.format(new Date());
        String photoFile = "Picture_" + date + ".jpg";

        String filename = pictureFileDir_.getPath() +  File.separator + photoFile;
        return filename;
    }

}
