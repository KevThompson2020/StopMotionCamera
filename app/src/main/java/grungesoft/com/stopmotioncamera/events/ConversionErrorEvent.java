package grungesoft.com.stopmotioncamera.events;


public class ConversionErrorEvent
{
    public String typeId_;
    public String errMsg_;
    public int frameIndex_;

    public ConversionErrorEvent(String id, String errMsg, int frameIndex)
    {
        this.typeId_ = id;
        this.errMsg_ = errMsg;
        this.frameIndex_ = frameIndex;
    }
}