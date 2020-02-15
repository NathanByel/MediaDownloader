package ru.nbdev.mediadownloader.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.io.File;
import java.io.IOException;

import ru.nbdev.mediadownloader.R;
import ru.nbdev.mediadownloader.common.Helpers;
import timber.log.Timber;

public class GlideLoader {
    private Context context;

    public GlideLoader(Context context) {
        this.context = context;
    }

    public void loadImage(String url, ImageView imageView, @Nullable OnImageLoadedListener listener) {
        Glide
                .with(context)
                .load(url)
                .placeholder(R.drawable.ic_progress_animated)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        if (listener != null) {
                            listener.onError();
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

    public void saveImage(String url, String saveFileName, OnImageSavedListener listener) {
        Glide.with(context)
                .downloadOnly()
                .load(url)
                .listener(new RequestListener<File>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<File> target, boolean isFirstResource) {
                        listener.onError();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(File resource, Object model, Target<File> target, DataSource dataSource, boolean isFirstResource) {
                        File file = copyFromCacheToDownloadDir(resource, saveFileName);
                        if (file != null) {
                            listener.onSuccess(file);
                        } else {
                            listener.onError();
                        }
                        return false;
                    }
                })
                .submit();
    }

    private File getSaveDir() {
        return new File(Environment.getExternalStorageDirectory() + File.separator + Environment.DIRECTORY_DOWNLOADS);
    }

    private File copyFromCacheToDownloadDir(File cacheFile, String saveFileName) {
        File saveDir = getSaveDir();
        if (saveDir.exists() || saveDir.mkdir()) {
            try {
                File file = new File(saveDir, saveFileName);
                Helpers.copyFileUsingStream(cacheFile, file);
                return file;
            } catch (IOException e) {
                Timber.e(e);
            }
        }
        return null;
    }

    public interface OnImageSavedListener {
        void onError();

        void onSuccess(File file);
    }

    public interface OnImageLoadedListener {
        void onError();

        void onSuccess();
    }
}
