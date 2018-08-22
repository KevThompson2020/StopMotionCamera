package grungesoft.com.stopmotioncamera;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import grungesoft.com.stopmotioncamera.Gallary.AnimalItem;

public class AnimalDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal_detail);
        supportPostponeEnterTransition();

        Bundle extras = getIntent().getExtras();
        AnimalItem animalItem = extras.getParcelable(GallaryViewActivity.EXTRA_ANIMAL_ITEM);

        ImageView imageView = (ImageView) findViewById(R.id.animal_detail_image_view);
        TextView textView = (TextView) findViewById(R.id.animal_detail_text);
        textView.setText(animalItem.detail);

        String imageUrl = animalItem.imageUrl;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            String imageTransitionName = extras.getString(GallaryViewActivity.EXTRA_ANIMAL_IMAGE_TRANSITION_NAME);
            imageView.setTransitionName(imageTransitionName);
        }

        Picasso.with(this)
                .load(imageUrl)
                .noFade()
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        supportStartPostponedEnterTransition();
                    }

                    @Override
                    public void onError() {
                        supportStartPostponedEnterTransition();
                    }
                });
    }
}