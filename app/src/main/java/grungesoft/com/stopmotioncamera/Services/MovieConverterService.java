package grungesoft.com.stopmotioncamera.Services;

import android.app.IntentService;
import android.content.Intent;
import android.os.Environment;

import java.io.File;
import java.util.ArrayList;

import grungesoft.com.stopmotioncamera.Gallary.AnimalItem;
import grungesoft.com.stopmotioncamera.R;
import grungesoft.com.stopmotioncamera.Utilities.EncodeDecode;
import grungesoft.com.stopmotioncamera.Utilities.SavingPicture;
import grungesoft.com.stopmotioncamera.Utilities.Util;
import grungesoft.com.stopmotioncamera.events.Events;
import grungesoft.com.stopmotioncamera.events.MovieFinishEvent;

public class MovieConverterService extends IntentService {

    public static final String LOG_TAG = "MEDIA_CONVERTER";

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

        String name = "test";
        String location = SavingPicture.getInstance().getPictureFileDir().getAbsolutePath();
        File directory = new File(location);
        if (!directory.exists())
        {
            directory.mkdir();
        }
        File file = new File(directory, name + ".mp4");

        addFileList();


        try
        {
            EncodeDecode encodeDecoder = new EncodeDecode(images, file);
            encodeDecoder.encodeDecodeVideoFromBufferToSurface(480, 368,80000);
        } catch (Throwable e)
        {
            e.printStackTrace();
            Events.eventBus.post(new MovieFinishEvent("conversion_error"));
            return;
        }


        Events.eventBus.post(new MovieFinishEvent("conversion_finished"));

        //return file.getAbsolutePath();
        Util.log(LOG_TAG, "MOVIE CONVERTER COMPLETE");
    }

    private void addFileList()
    {

        //        ArrayList<File> images = new ArrayList();
//        for( int index = 0; index < 152; index++ )
//        {
//            File f = new File(getFramePath(index));
//            if(f.exists()) {
//                Util.log(LOG_TAG, "adding file " + f.getAbsolutePath());
//                images.add(f);
//            }
//            else {
//                Util.log(LOG_TAG, "NO SUCH file " + f.getAbsolutePath());
//            }
//        }


        File [] paths = Util.getAllImagePaths(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), SavingPicture.getInstance().getFolderName());

        ArrayList<AnimalItem> animalItems = new ArrayList<>();

        animalItems.add(new AnimalItem("Dog", context.getString(R.string.dog_blurb), "https://c1.staticflickr.com/1/188/417924629_6832e79c98_z.jpg?zz=1", AnimalItem.LOCATION_URL));

        for(int index = 0; index < paths.length; index++) {
            animalItems.add(new AnimalItem("image " + index, context.getString(R.string.dog_blurb), paths[index].getAbsolutePath(), AnimalItem.LOCATION_LOCAL_STORE));
        }

    }

    private String getFramePath(int index)
    {

        String formatted = String.format("%02d", index);
        String path = SavingPicture.getInstance().getPictureFileDir().getPath();
        //String fileName = "frame_" + formatted + "_delay-0.11s.gif";
        String fileName = "frame_" + formatted +"_delay-0.05s.jpg";


        String fullPath = path + "/" + fileName;
        Util.log("MEDIA_CONVERTER", fullPath);
        return fullPath;
    }
}
