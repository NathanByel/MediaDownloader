package ru.nbdev.instagrammclient.presenter;

import android.content.res.Resources;
import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import ru.nbdev.instagrammclient.model.ImagesData;
import ru.nbdev.instagrammclient.view.MainAdapter;
import ru.nbdev.instagrammclient.view.MainView;

@InjectViewState
public class MainPresenter extends MvpPresenter<MainView> {
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
            int position = mainViewHolder.getAdapterPosition();
            mainViewHolder.setImage(imagesData.getImagesList().get(position));
            mainViewHolder.setOnClickListener(v -> onItemClick(position));
        }

        @Override
        public void onItemClick(int position) {
            int count = imagesData.getClickCount() + 1;
            imagesData.setClickCount(count);
            Log.d(TAG, "Click count = " + count);

            getViewState().runDetailActivity(position);
        }
    }
}
