package grungesoft.com.stopmotioncamera;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ActionMenuView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;

import grungesoft.com.stopmotioncamera.Utilities.MMediaMuxer;
import grungesoft.com.stopmotioncamera.Utilities.SavingPicture;
import grungesoft.com.stopmotioncamera.Utilities.Util;

import static grungesoft.com.stopmotioncamera.MainActivity.context;

public class MovieConverterActivity extends AppCompatActivity {

    public static final String LOG_TAG = "MovieConverterActivityTag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_converter);

        setupMovieButton();
    }

    TextView feedbackText;
    Handler handler = new Handler();
    MMediaMuxer myMuxer = new MMediaMuxer();

    /**
     *
     */
    private void setupMovieButton()
    {
        final Activity test = this;
        Button startButton = (Button)findViewById(R.id.convert_movie_button);
        feedbackText = (TextView)findViewById(R.id.convert_movie_feedback_text);

        setFeedback("test");

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.log(LOG_TAG,"Convert Movie Clicked");


create a service!
                Thread t = new Thread() {

                    public void run() {
                        handler.post(new Runnable() {

                            public void run() {
                                setFeedback("test loop");


                                int index;
                                for(index = 1; index <= 15; index++) {
                                    setFeedback("Getting Frame ( " + index + ") ");
                                    byte[] bytes =getFrame(index);

                                    setFeedback("Adding Frame ( " + index + ") ");
  //                                  myMuxer.AddFrame(bytes);
                                }
                                setFeedback("Done - Creating video");
//                                myMuxer.CreateVideo();

                            }

                        });
                    }

                };
                t.start();


                setFeedback("Starting");


            }
        });
    }

    /**
     *
     * @param str
     */
    private void setFeedback(final String str)
    {
        Util.log(LOG_TAG,str);

//        runOnUiThread(new Runnable()
//        {
//            @Override
//            public void run()
//            {
                feedbackText.setText(str);
//            }
//        });

    }

    private byte[] getFrame(int index)
    {
        String path = SavingPicture.getInstance().getPictureFileDir().getPath();
        String fileName = index + ".jpg";

        String fullPath = path + "/" + fileName;

        Bitmap bmp = BitmapFactory.decodeFile(fullPath);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        bmp.recycle();


        return byteArray;

    }
}
