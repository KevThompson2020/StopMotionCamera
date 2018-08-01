package grungesoft.com.stopmotioncamera;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import grungesoft.com.stopmotioncamera.Services.MovieConverterService;
import grungesoft.com.stopmotioncamera.Utilities.Util;

public class MovieConverterActivity extends AppCompatActivity {

    public static final String LOG_TAG = "MovieConverterActivityTag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_converter);

        setupMovieButton();
    }

    TextView feedbackText;

    /**
     *
     */
    private void setupMovieButton()
    {
        final Activity test = this;
        Button startButton = (Button)findViewById(R.id.convert_movie_button);
        feedbackText = (TextView)findViewById(R.id.convert_movie_feedback_text);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.log(LOG_TAG,"Convert Movie Clicked");
                startServiceNow();
            }
        });
    }


    /**
     *
     */
    private void startServiceNow()
    {
        Util.log(LOG_TAG,"startServiceNow - set intent");
        Intent movieConverterService = new Intent(this, MovieConverterService.class);

        Util.log(LOG_TAG,"startServiceNow - start");
        startService(movieConverterService);

    }
}
