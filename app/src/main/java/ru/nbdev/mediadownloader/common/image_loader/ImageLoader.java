package ru.nbdev.mediadownloader.common.image_loader;

import android.graphics.drawable.Drawable;

public interface ImageLoader {

    void loadImageFromUrl(String url, String tag, OnReadyListener listener);

    void cancelLoading(String tag);


    interface OnReadyListener {

        void onSuccess(Drawable image);

        void onError();
    }
}
