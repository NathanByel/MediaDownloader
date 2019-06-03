package ru.nbdev.instagrammclient.presenter;

import android.content.res.Resources;
import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import ru.nbdev.instagrammclient.model.ImagesData;
import ru.nbdev.instagrammclient.view.DetailView;

@InjectViewState
public class DetailPresenter extends MvpPresenter<DetailView> {
    private static final String TAG = "DetailPresenter";

    private ImagesData imagesData;
    private int position = -1;

    public DetailPresenter(Resources resources) {
        imagesData = new ImagesData(resources);
    }

    public void onImageClick() {
        Log.d(TAG, "Image id = " + position);
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void onCreate() {
        if ((position >= 0) && (position < imagesData.getImagesList().size())) {
            getViewState().setDetailImage(imagesData.getImagesList().get(position));
        }
    }
}
