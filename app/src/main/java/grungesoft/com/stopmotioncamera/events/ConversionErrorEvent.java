package grungesoft.com.stopmotioncamera.events;


public class ConversionErrorEvent
{
    public String framePath_;
    public String errMsg_;
    public int frameIndex_;

    public ConversionErrorEvent(String framePath, String errMsg, int frameIndex)
    {
        this.framePath_ = framePath;
        this.errMsg_ = errMsg;
        this.frameIndex_ = frameIndex;
    }
}