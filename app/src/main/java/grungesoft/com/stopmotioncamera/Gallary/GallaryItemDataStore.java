package grungesoft.com.stopmotioncamera.Gallary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import grungesoft.com.stopmotioncamera.GallaryViewActivity;

/**
 * Created by Kev on 23/02/2017.
 */

public class GallaryItemDataStore {

    static public Map<String, GallaryItem> items = new LinkedHashMap<String, GallaryItem>();

    /**
     * Constructor
     */
    void MusicItemDataStore()
    {

    }

    /**
     *
     * @param id
     * @param item
     */
    public static void addItem(String id, GallaryItem item)
    {
        items.put(id, item);
    }
}
