package ru.nbdev.mediadownloader.common.image_loader;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;

import java.util.HashMap;
import java.util.Map;

import timber.log.Timber;

public class GlideImageLoader implements ImageLoader {

    private final Context context;
    private final Map<String, CancelableTarget> cancelableTargets = new HashMap<>();


    public GlideImageLoader(Context context) {
        this.context = context;
    }

    @Override
    public void loadImageFromUrl(String url, String tag, @NonNull OnReadyListener listener) {
        CancelableTarget target = new CancelableTarget(tag, listener);
        cancelableTargets.put(tag, target);

        Glide
                .with(context)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(target);
    }

    @Override
    public void cancelLoading(String tag) {
        Target target = cancelableTargets.get(tag);
        if (target != null) {
            cancelableTargets.remove(tag);
            Glide
                    .with(context)
                    .clear(target);
        }
    }


    private class CancelableTarget extends CustomTarget<Drawable> {

        private final OnReadyListener listener;
        private final String tag;


        public CancelableTarget(String tag, OnReadyListener listener) {
            this.listener = listener;
            this.tag = tag;
        }

        @Override
        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
            cancelableTargets.remove(tag);
            listener.onSuccess(resource);
        }

        @Override
        public void onLoadCleared(@Nullable Drawable placeholder) {
            Timber.d("onLoadCleared");
        }

        @Override
        public void onLoadFailed(@Nullable Drawable errorDrawable) {
            cancelableTargets.remove(tag);
            listener.onError();
        }
    }
}
