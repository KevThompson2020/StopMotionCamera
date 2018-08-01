package grungesoft.com.stopmotioncamera.Utilities;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioFormat;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import static android.provider.MediaStore.MediaColumns.MIME_TYPE;


public class ConvertToMovie {

    private static volatile ConvertToMovie sSoleInstance = new ConvertToMovie();
    File pictureFileDir_;
    public final String FOLDER_NAME = "StopMotionCamera";

    //private constructor.
    private ConvertToMovie(){}

    /**
     * get the instance of the singleton
     * @return
     */
    public static ConvertToMovie getInstance() {
        return sSoleInstance;
    }

    /**
     *
     */
    public void folderChecks() {

        if(!Util.checkIfFolderExists(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), FOLDER_NAME)) {
            Util.createFolder(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), FOLDER_NAME);
        }

        pictureFileDir_ = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator +  FOLDER_NAME);
    }

    public void convert()
    {
        loadFiles();
    }


    private void loadFiles()
    {
        folderChecks();
        bufferEncoder();
    }


    Runnable runnable;
    boolean mRunning;
    int WIDTH = 1280;
    int HEIGHT = 720;
    int BIT_RATE = 6000000;
    int generateIndex;
    MediaCodec mediaCodec;
    MediaMuxer mediaMuxer;
    MediaCodec.BufferInfo mBufferInfo;
    MediaFormat mediaFormat;

    private static final int FRAME_RATE = 15;               // 15fps
    private static final int IFRAME_INTERVAL = 10;
    final int TIMEOUT_USEC = 10000;

    /**
     *
     */
    private void bufferEncoder() {
        runnable = new Runnable() {
            @Override
            public void run() {
                prepareEncoder();
                try {
                    while (mRunning) {
                        encode();
                    }
                    encode();
                } finally {
                    release();
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }





    /**
     *
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void prepareEncoder() {
        try {
            mBufferInfo = new MediaCodec.BufferInfo();

            mediaCodec = MediaCodec.createEncoderByType(MIME_TYPE);
            mediaFormat = MediaFormat.createVideoFormat(MIME_TYPE, WIDTH, HEIGHT);
            mediaFormat.setInteger(MediaFormat.KEY_BIT_RATE, BIT_RATE);
            mediaFormat.setInteger(MediaFormat.KEY_FRAME_RATE, FRAME_RATE);
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
                mediaFormat.setInteger(MediaFormat.KEY_COLOR_FORMAT, MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420SemiPlanar);
            }else{
                mediaFormat.setInteger(MediaFormat.KEY_COLOR_FORMAT, MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420Flexible);
            }
            //2130708361, 2135033992, 21
            mediaFormat.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, IFRAME_INTERVAL);

//            final MediaFormat audioFormat = MediaFormat.createAudioFormat(MIME_TYPE_AUDIO, SAMPLE_RATE, 1);
//            audioFormat.setInteger(MediaFormat.KEY_AAC_PROFILE, MediaCodecInfo.CodecProfileLevel.AACObjectLC);
//            audioFormat.setInteger(MediaFormat.KEY_CHANNEL_MASK, AudioFormat.CHANNEL_IN_MONO);
//            audioFormat.setInteger(MediaFormat.KEY_BIT_RATE, BIT_RATE);
//            audioFormat.setInteger(MediaFormat.KEY_CHANNEL_COUNT, 1);

            mediaCodec.configure(mediaFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
            mediaCodec.start();

//            mediaCodecForAudio = MediaCodec.createEncoderByType(MIME_TYPE_AUDIO);
//            mediaCodecForAudio.configure(audioFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
//            mediaCodecForAudio.start();

            try {
                String outputPath = new File(Environment.getExternalStorageDirectory(),
                        "test.mp4").toString();
                mediaMuxer = new MediaMuxer(outputPath, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
            } catch (IOException ioe) {
                throw new RuntimeException("MediaMuxer creation failed", ioe);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void encode() {
        while (true) {
            if (!mRunning) {
                break;
            }
            int inputBufIndex = mediaCodec.dequeueInputBuffer(TIMEOUT_USEC);
            long ptsUsec = computePresentationTime(generateIndex);
            if (inputBufIndex >= 0) {

                Bitmap image = loadBitmapFromView();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                image.recycle();

                image = Bitmap.createScaledBitmap(image, WIDTH, HEIGHT, false);
                //byte[] input = getNV21(WIDTH, HEIGHT, image);
                byte[] input = byteArray;
                final ByteBuffer inputBuffer = mediaCodec.getInputBuffer(inputBufIndex);
                inputBuffer.clear();
                inputBuffer.put(input);
                mediaCodec.queueInputBuffer(inputBufIndex, 0, input.length, ptsUsec, 0);
                generateIndex++;
            }
            int encoderStatus = mediaCodec.dequeueOutputBuffer(mBufferInfo, TIMEOUT_USEC);
            if (encoderStatus == MediaCodec.INFO_TRY_AGAIN_LATER) {
                // no output available yet
                Log.d("CODEC", "no output from encoder available");
            } else if (encoderStatus == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
                // not expected for an encoder
                MediaFormat newFormat = mediaCodec.getOutputFormat();
                //mTrackIndex = mediaMuxer.addTrack(newFormat);
                mediaMuxer.start();
            } else if (encoderStatus < 0) {
                Log.i("CODEC", "unexpected result from encoder.dequeueOutputBuffer: " + encoderStatus);
            } else if (mBufferInfo.size != 0) {
                ByteBuffer encodedData = mediaCodec.getOutputBuffer(encoderStatus);
                if (encodedData == null) {
                    Log.i("CODEC", "encoderOutputBuffer " + encoderStatus + " was null");
                } else {
                    encodedData.position(mBufferInfo.offset);
                    encodedData.limit(mBufferInfo.offset + mBufferInfo.size);
                    //mediaMuxer.writeSampleData(mTrackIndex, encodedData, mBufferInfo);
                    mediaCodec.releaseOutputBuffer(encoderStatus, false);
                }
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void release() {
        if (mediaCodec != null) {
            mediaCodec.stop();
            mediaCodec.release();
            mediaCodec = null;
            Log.i("CODEC", "RELEASE CODEC");
        }
        if (mediaMuxer != null) {
            mediaMuxer.stop();
            mediaMuxer.release();
            mediaMuxer = null;
            Log.i("CODEC", "RELEASE MUXER");
        }
    }

    /**
     * Generates the presentation time for frame N, in microseconds.
     */
    private static long computePresentationTime(int frameIndex) {
        return 132 + frameIndex * 1000000 / FRAME_RATE;
    }


    /**
     *
     * @return
     */
    private Bitmap loadBitmapFromView()
    {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        return BitmapFactory.decodeFile(pictureFileDir_ + "//Testload.jpg", options);
    }
}
