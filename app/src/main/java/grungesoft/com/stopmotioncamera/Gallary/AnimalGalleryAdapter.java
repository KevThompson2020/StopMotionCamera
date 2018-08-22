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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Map;

import grungesoft.com.stopmotioncamera.R;


public class AnimalGalleryAdapter extends RecyclerView.Adapter<AnimalGalleryAdapter.ImageViewHolder> {

    private final AnimalItemClickListener animalItemClickListener;
    private ArrayList<AnimalItem> animalItems;

    public AnimalGalleryAdapter(ArrayList<AnimalItem> animalItems, AnimalItemClickListener animalItemClickListener) {
        this.animalItems = animalItems;
        this.animalItemClickListener = animalItemClickListener;
    }

    ....

    @Override
    public void onBindViewHolder(final ImageViewHolder holder, int position) {
        final AnimalItem animalItem = animalItems.get(position);

        Picasso.with(holder.itemView.getContext())
                .load(animalItem.imageUrl)
                .into(holder.animalImageView);

        ViewCompat.setTransitionName(holder.animalImageView, animalItem.name);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animalItemClickListener.onAnimalItemClick(holder.getAdapterPosition(), animalItem, holder.animalImageView);
            }
        });
    }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            animalItemClickListener.onAnimalItemClick(position, animalItem, holder.animalImageView);
        }
    });
}
}