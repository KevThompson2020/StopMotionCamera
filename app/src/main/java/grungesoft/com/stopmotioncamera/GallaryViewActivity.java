package grungesoft.com.stopmotioncamera;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;

import grungesoft.com.stopmotioncamera.Gallary.AnimalGalleryAdapter;
import grungesoft.com.stopmotioncamera.Gallary.AnimalItem;
import grungesoft.com.stopmotioncamera.Gallary.AnimalItemClickListener;
import grungesoft.com.stopmotioncamera.Gallary.GallaryAdapter;
import grungesoft.com.stopmotioncamera.Gallary.GallaryItemDataStore;

public class GallaryViewActivity extends AppCompatActivity implements AnimalItemClickListener {

    private RecyclerView recyclerView;
    private static AnimalGalleryAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallary_view);


        setupAdapter();
        dataUpdated();

    }


    //Code to setup the RecyclerView and Adapter

    @Override
    public void onAnimalItemClick(int pos, AnimalItem animalItem, ImageView sharedImageView) {
        Intent intent = new Intent(this, AnimalDetailActivity.class);
        intent.putExtra(EXTRA_ANIMAL_ITEM, animalItem);
        intent.putExtra(EXTRA_ANIMAL_IMAGE_TRANSITION_NAME, ViewCompat.getTransitionName(sharedImageView));

        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this,
                sharedImageView,
                ViewCompat.getTransitionName(sharedImageView));

        startActivity(intent, options.toBundle());
    }


    /**
     *
     */
    private void setupAdapter()
    {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mAdapter = new AnimalGalleryAdapter(this, GallaryItemDataStore.items);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

    }



    /**
     *
     */
    public static void dataUpdated()
    {
        if(mAdapter!=null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    protected class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    protected int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}

