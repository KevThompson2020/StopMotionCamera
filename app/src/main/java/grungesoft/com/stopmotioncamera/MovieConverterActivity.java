package grungesoft.com.stopmotioncamera;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.provider.CalendarContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.squareup.otto.Subscribe;

import grungesoft.com.stopmotioncamera.Services.MovieConverterService;
import grungesoft.com.stopmotioncamera.Utilities.Util;
import grungesoft.com.stopmotioncamera.events.ConversionErrorEvent;
import grungesoft.com.stopmotioncamera.events.Events;
import grungesoft.com.stopmotioncamera.events.MovieFinishEvent;
import grungesoft.com.stopmotioncamera.events.MovieUpdateEvent;

public class MovieConverterActivity extends AppCompatActivity {

    public static final String LOG_TAG = "MovieConverterActivityTag";
    private Activity mActivity_;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_converter);

        mActivity_ = this;

        Events.eventBus.register(this);

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


    /**
     * Receive the message to say the movie has been completed.
     * @param event
     */
    @Subscribe
    public void receiveEvent(final MovieFinishEvent event)
    {
        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                TextView text = (TextView)findViewById(R.id.convert_movie_feedback_text);
                if(text!=null) {

                    if(event.typeId.contains("conversion_error")) {
                        text.setText("Error");
                    }
                    else {
                        text.setText("Complete");
                    }

                }
            }
        });
    }

    /**
     * Receive the message to say the movie has been completed.
     * @param event
     */
    @Subscribe
    public void receiveEvent(final MovieUpdateEvent event)
    {
        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
                //progressBar.setMax(event.frameCount_);
                ObjectAnimator animation = ObjectAnimator.ofInt(progressBar, "progress", event.frameIndex_, event.frameCount_); // see this max value coming back here, we animate towards that value
                animation.setDuration(10); // in milliseconds
                animation.setInterpolator(new DecelerateInterpolator());
                animation.start();


                TextView text = (TextView)findViewById(R.id.convert_movie_feedback_text);
                if(text!=null) {
                    text.setText("Converting Frame " + event.frameIndex_ + " of " +  event.frameCount_) ;
                }
            }
        });
    }
    /**
     * Receive the message to say the movie has been completed.
     * @param event
     */
    @Subscribe
    public void receiveEvent(final ConversionErrorEvent event)
    {
        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                LinearLayout linearLayout = (LinearLayout)findViewById(R.id.convert_movie_error_zone);

                TextView text = new TextView(mActivity_);
                text.setText(event.framePath_ + " " + event.errMsg_);

                linearLayout.addView(text);
            }
        });
    }

}
