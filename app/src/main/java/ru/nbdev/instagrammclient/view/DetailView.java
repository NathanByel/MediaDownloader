package ru.nbdev.instagrammclient.view;

import android.graphics.drawable.Drawable;

import com.arellomobile.mvp.MvpView;

public interface DetailView extends MvpView {
    void setDetailImage(Drawable image);
}
