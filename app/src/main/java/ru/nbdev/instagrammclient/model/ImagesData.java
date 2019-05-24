package ru.nbdev.instagrammclient.model;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.List;

import ru.nbdev.instagrammclient.R;
import ru.nbdev.instagrammclient.ScaledBitmapLoader;

public class ImagesData {
    private List<Drawable> images = new ArrayList<>();
    private int clickCount = 0;

    public ImagesData(Resources resources) {
        Bitmap thumb = ScaledBitmapLoader.decodeSampledBitmapFromResource(
                resources, R.drawable.fruits1, 100, 100);
        Drawable previewThumb = new BitmapDrawable(resources, thumb);
        previewThumb.setBounds(0, 0, previewThumb.getMinimumWidth(), previewThumb.getMinimumHeight());

        for (int i = 0; i < 10; i++) {
            images.add(previewThumb);
        }
    }

    public List<Drawable> getImagesList() {
        return images;
    }

    public void setClickCount(int clickCount) {
        this.clickCount = clickCount;
    }

    public int getClickCount() {
        return clickCount;
    }
}
