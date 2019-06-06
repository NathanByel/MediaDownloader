package ru.nbdev.instagrammclient.presenter;

import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import ru.nbdev.instagrammclient.view.detail.DetailView;

@InjectViewState
public class DetailPresenter extends MvpPresenter<DetailView> {
    private static final String TAG = "DetailPresenter";

    private String detailURL;

    public void onImageClick() {
        Log.d(TAG, "Photo URL = " + detailURL);
    }

    @Override
    protected void onFirstViewAttach() {
        if (detailURL != null) {
            getViewState().setDetailPhoto(detailURL);
        }
    }

    public void setDetailURL(String detailURL) {
        this.detailURL = detailURL;
    }
}
