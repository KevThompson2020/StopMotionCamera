package grungesoft.com.stopmotioncamera.Services;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

import grungesoft.com.stopmotioncamera.Utilities.MMediaMuxer;
import grungesoft.com.stopmotioncamera.Utilities.SavingPicture;
import grungesoft.com.stopmotioncamera.Utilities.Util;

public class MovieConverterService extends IntentService {

    MMediaMuxer myMuxer_ = new MMediaMuxer();

    /**
     * Constructor
     */
    public MovieConverterService()
    {
        super("MovieConverterService");
    }

    /**
     * Thread Where we make the movie!
     * @param workIntent
     */
    protected void onHandleIntent(Intent workIntent)
    {
        Util.log("MovieConverter", "Starting MovieConversion:");


        int index;
        for(index = 1; index <= 2; index++) {
            Util.log("MovieConverter", "Get Frame " + index);
            byte[] bytes =getFrame(index);

            Util.log("MovieConverter", "Add Frame " + index + " of (" + bytes.length +") bytes");
            myMuxer_.AddFrame(bytes);

        }
        myMuxer_.CreateVideo();


    }


    private byte[] getFrame(int index)
    {
        String path = SavingPicture.getInstance().getPictureFileDir().getPath();
        String fileName = index + ".jpg";

        String fullPath = path + "/" + fileName;

        Bitmap bmp = BitmapFactory.decodeFile(fullPath);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        bmp.recycle();


        return byteArray;

    }
}
