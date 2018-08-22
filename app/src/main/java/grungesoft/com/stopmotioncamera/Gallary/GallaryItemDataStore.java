package grungesoft.com.stopmotioncamera.Gallary;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Kev on 23/02/2017.
 */

public class GallaryItemDataStore {

    static public Map<String, AnimalItem> items = new LinkedHashMap<String, AnimalItem>();

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
    public static void addItem(String id, AnimalItem item)
    {
        items.put(id, item);
    }
}
