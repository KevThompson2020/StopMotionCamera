package grungesoft.com.stopmotioncamera;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ActionMenuView;
import android.widget.Button;
import android.widget.ImageView;

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


    /**
     *
     */
    private void setupMovieButton()
    {
        final Activity test = this;
        Button startButton = (Button)findViewById(R.id.convert_movie_button);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.log(LOG_TAG,"Convert Movie Clicked");

                MMediaMuxer myMuxer = new MMediaMuxer();

                int index;
                for(index = 1; index <= 15; index++) {
                    myMuxer.AddFrame(getFrame(index));
                }

                myMuxer.CreateVideo();
            }
        });
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
