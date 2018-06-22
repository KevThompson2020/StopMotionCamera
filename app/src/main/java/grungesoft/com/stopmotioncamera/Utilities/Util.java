package grungesoft.com.stopmotioncamera.Utilities;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

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


}
