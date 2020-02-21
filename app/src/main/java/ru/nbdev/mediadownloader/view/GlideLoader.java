package ru.nbdev.mediadownloader.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

public class GlideLoader {

    private final Context context;

    public GlideLoader(Context context) {
        this.context = context;
    }

    public void loadImage(String url, ImageView imageView, @Nullable OnImageReadyListener listener) {
        Glide
                .with(context)
                .load(url)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        if (listener != null) {
                            listener.onError(e);
                        }
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        if (listener != null) {
                            listener.onSuccess();
                        }
                        return false;
                    }
                })
                .into(imageView);
    }

    public interface OnImageReadyListener {
        void onError(GlideException e);

        void onSuccess();
    }
}
