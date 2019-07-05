package ru.nbdev.instagrammclient.model;

import android.content.Context;
import android.support.v4.widget.CircularProgressDrawable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import ru.nbdev.instagrammclient.R;

public class GlideLoader {

    private Context context;

    public GlideLoader(Context context) {
        this.context = context;
    }

    public void loadImage(String url, ImageView imageView) {
        CircularProgressDrawable circularProgressDrawable =
                new CircularProgressDrawable(context);
        circularProgressDrawable.setStrokeWidth(5f);
        circularProgressDrawable.setCenterRadius(30f);
        circularProgressDrawable.start();

        Glide
                .with(context)
                .load(url)
                .placeholder(R.drawable.progress_animation)
                //.placeholder(circularProgressDrawable)
                //.diskCacheStrategy(DiskCacheStrategy.NONE)
                //.skipMemoryCache(true)
                //.centerCrop()
                .into(imageView);
    }
}
