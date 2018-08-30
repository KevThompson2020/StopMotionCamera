package grungesoft.com.stopmotioncamera.Gallary;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

import grungesoft.com.stopmotioncamera.MainActivity;
import grungesoft.com.stopmotioncamera.R;


public class AnimalGalleryAdapter extends RecyclerView.Adapter<AnimalGalleryAdapter.ImageViewHolder> {

    private final AnimalItemClickListener animalItemClickListener;
    private ArrayList<AnimalItem> animalItems;
    private int lastPosition = -1;


    public AnimalGalleryAdapter(ArrayList<AnimalItem> animalItems, AnimalItemClickListener animalItemClickListener) {
        this.animalItems = animalItems;
        this.animalItemClickListener = animalItemClickListener;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ImageViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_animal_square, parent, false));
    }

    @Override
    public int getItemCount() {
        return animalItems.size();
    }


    @Override
    public void onViewDetachedFromWindow(AnimalGalleryAdapter.ImageViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }

    @Override
    public void onBindViewHolder(final ImageViewHolder holder, int position) {
        final AnimalItem animalItem = animalItems.get(position);

        if(animalItem.locationType == AnimalItem.LOCATION_URL) {
            Picasso.with(holder.itemView.getContext())
                    .load(animalItem.imageLocation)
                    .into(holder.animalImageView);
        }
        else if(animalItem.locationType == AnimalItem.LOCATION_LOCAL_STORE) {
            File f = new File(animalItem.imageLocation);
            Picasso.with(holder.itemView.getContext())
                    .load(f)
                    .into(holder.animalImageView);

        }




        Animation animation = AnimationUtils.loadAnimation(MainActivity.context,
                (position > lastPosition) ? R.anim.up_from_bottom_anim
                        : R.anim.down_from_top_anim);
        holder.itemView.startAnimation(animation);
        lastPosition = position;

        ViewCompat.setTransitionName(holder.animalImageView, animalItem.name);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animalItemClickListener.onAnimalItemClick(holder.getAdapterPosition(), animalItem, holder.animalImageView);
            }
        });
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {

        private ImageView animalImageView;

        public ImageViewHolder(View itemView) {
            super(itemView);
            animalImageView = (ImageView) itemView.findViewById(R.id.item_animal_square_image);
        }


    }
}