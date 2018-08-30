package grungesoft.com.stopmotioncamera.Utilities;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

import grungesoft.com.stopmotioncamera.Gallary.AnimalItem;
import grungesoft.com.stopmotioncamera.R;

public class Util {

    static String TAG = "Utils";

    /**
     * Log out to the console
     * @param tag
     * @param message
     */
    public static void log(String tag, String message)
    {
            Log.i(tag, message);
    }

    /**
     * Log out to the console with standard tag.
     * @param message
     */
    public static void log(String message)
    {
        Log.i(TAG, message);
    }

    /**
     * write the String to a file,
     * @param data
     * @param context
     */
    public static void writeToFile(String fileName, String data, Context context) {
        try {
            File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            File file = new File(path, fileName);
            FileOutputStream stream = new FileOutputStream(file);
            stream.write(data.getBytes());
            stream.close();
        }
        catch (IOException e) {
            Util.log("Exception", "File write failed: " + e.toString());
        }
    }

    /**
     * write some bytes to a file on storage.
     * @param data
     * @param context
     */
    public static void writeToFileBytes(String fileName, byte[] data, Context context) {
        try {
            File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            File file = new File(path, fileName);
            FileOutputStream stream = new FileOutputStream(file);
            stream.write(data);
            stream.close();
        }
        catch (IOException e) {
            Util.log("Exception", "File write failed: " + e.toString());
        }
    }


    /**
     *
     * @param directoryFile
     * @param FolderName
     * @return
     */
    public static boolean checkIfFolderExists(File directoryFile, String FolderName)
    {
        File dir = new File(directoryFile + "/" + FolderName);
        if(dir.exists() && dir.isDirectory()) {
            return true;
        }
        return false;
    }


    /**
     * create a folder at the designated file directory with the foldername spceified
     * @param directoryFile
     * @param FolderName
     */
    public static void createFolder(File directoryFile, String FolderName)
    {
        File dir = new File(directoryFile + "/" + FolderName);
        dir.mkdir();
    }


    /**
     *
     * @param directoryFile
     * @param FolderName
     * @return
     */
    public static File [] getAllImagePaths(File directoryFile, String folderName)
    {
        File [] files = null;
        File dir = new File(directoryFile + "/" + folderName);
        if(dir!=null && dir.isDirectory()) {
            int size = dir.listFiles().length;
            files = dir.listFiles();
        }
        return files;
    }
    /**
     *
     * @param context
     * @return
     */

    public static ArrayList<AnimalItem> generateAnimalItems(Context context) {


        File [] paths = getAllImagePaths(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), SavingPicture.getInstance().getFolderName());

        ArrayList<AnimalItem> animalItems = new ArrayList<>();

        animalItems.add(new AnimalItem("Dog", context.getString(R.string.dog_blurb), "https://c1.staticflickr.com/1/188/417924629_6832e79c98_z.jpg?zz=1", AnimalItem.LOCATION_URL));

        for(int index = 0; index < paths.length; index++) {
            animalItems.add(new AnimalItem("image " + index, context.getString(R.string.dog_blurb), paths[index].getAbsolutePath(), AnimalItem.LOCATION_LOCAL_STORE));
        }





        animalItems.add(new AnimalItem("Penguin", context.getString(R.string.penguin_blurb), "https://c1.staticflickr.com/9/8616/16237154608_c5489cae31_z.jpg", AnimalItem.LOCATION_URL));
        animalItems.add(new AnimalItem("Eagle", context.getString(R.string.eagle_blurb), "https://c1.staticflickr.com/5/4010/4210875342_7cb06a9b62_z.jpg?zz=1", AnimalItem.LOCATION_URL));
        animalItems.add(new AnimalItem("Rabbit", context.getString(R.string.rabbit_blurb), "https://c2.staticflickr.com/4/3285/2819978026_175072995a_z.jpg?zz=1", AnimalItem.LOCATION_URL));
        animalItems.add(new AnimalItem("Dolphin", context.getString(R.string.dolphin_blurb), "https://c1.staticflickr.com/8/7619/16124006043_60bc4d8ca5_z.jpg", AnimalItem.LOCATION_URL));
        animalItems.add(new AnimalItem("Snek", context.getString(R.string.snek_blurb), "https://c1.staticflickr.com/9/8796/17158681740_a6caa5099f_z.jpg", AnimalItem.LOCATION_URL));
        animalItems.add(new AnimalItem("Seal", context.getString(R.string.seal_blurb), "https://c1.staticflickr.com/4/3852/14729534910_62b338dd72_z.jpg", AnimalItem.LOCATION_URL));
        animalItems.add(new AnimalItem("Rhino", context.getString(R.string.rhino_blurb), "https://c1.staticflickr.com/1/335/18040640224_f56f05f8dc_z.jpg", AnimalItem.LOCATION_URL));
        animalItems.add(new AnimalItem("Leopard", context.getString(R.string.leopard_blurb), "https://c1.staticflickr.com/9/8678/16645189230_b0e96e7af9_z.jpg", AnimalItem.LOCATION_URL));
        animalItems.add(new AnimalItem("Hippo", context.getString(R.string.hippo_blurb), "https://c2.staticflickr.com/4/3774/9377370000_6a57d1cfec_z.jpg", AnimalItem.LOCATION_URL));
        return animalItems;
    }

}
