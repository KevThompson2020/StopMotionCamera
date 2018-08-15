package grungesoft.com.stopmotioncamera.Gallary;


/**
 * Created by Kev on 06/02/2017.
 */

public class GallaryItem {

    private String name;
    private int numOfSongs;
    private int thumbnail;

    /**
     *
     */
    public GallaryItem() {
    }

    /**
     *
     * @param name
     * @param numOfSongs
     * @param thumbnail
     */
    public GallaryItem(String name, int numOfSongs, int thumbnail) {
        this.name = name;
        this.numOfSongs = numOfSongs;
        this.thumbnail = thumbnail;
    }


    public String getName() {
        return name;
    }

    public int getThumbnail() {
        return thumbnail;
    }

    /**
     *
     * @return
     */
    public int getNumOfSongs() {
        return numOfSongs;
    }

    /**
     *
     * @param numOfSongs
     */
    public void setNumOfSongs(int numOfSongs) {
        this.numOfSongs = numOfSongs;
    }
}
