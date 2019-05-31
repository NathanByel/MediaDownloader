package ru.nbdev.instagrammclient.presenter;

import android.content.res.Resources;
import android.util.Log;

import ru.nbdev.instagrammclient.model.ImagesData;
import ru.nbdev.instagrammclient.view.MainAdapter;

public class MainPresenter {
    private static final String TAG = "MainPresenter";
    private RecyclerPresenter recyclerPresenter;
    private Resources resources;

    public MainPresenter(Resources resources) {
        this.resources = resources;
        recyclerPresenter = new RecyclerPresenter();
    }

    public RecyclerPresenter getRecyclerPresenter() {
        return recyclerPresenter;
    }

    private class RecyclerPresenter implements MvpRecyclerPresenter {
        private ImagesData imagesData = new ImagesData(resources);

        @Override
        public int getItemCount() {
            return imagesData.getImagesList().size();
        }

        @Override
        public void bindView(MainAdapter.MainViewHolder mainViewHolder) {
            mainViewHolder.setImage(imagesData.getImagesList().get(mainViewHolder.getAdapterPosition()));
        }

        @Override
        public void onItemClick() {
            int count = imagesData.getClickCount() + 1;
            imagesData.setClickCount(count);
            Log.d(TAG, "Click count = " + count);
        }
    }
}
