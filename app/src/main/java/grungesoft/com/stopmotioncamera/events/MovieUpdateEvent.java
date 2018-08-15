package grungesoft.com.stopmotioncamera.events;


public class MovieUpdateEvent
{
    public String framePath_;
    public int frameIndex_;
    public int frameCount_;

    public MovieUpdateEvent(final String path, final int frameIndex, final int frameCount)
    {
        this.framePath_=path;
        this.frameIndex_ = frameIndex;
        this.frameCount_ = frameCount;
    }
}