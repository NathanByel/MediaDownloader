package ru.nbdev.instagrammclient.presenter;

import android.content.res.Resources;
import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import ru.nbdev.instagrammclient.R;
import ru.nbdev.instagrammclient.model.entity.Photo;
import ru.nbdev.instagrammclient.model.entity.PhotosList;
import ru.nbdev.instagrammclient.model.retrofit.ApiHelper;
import ru.nbdev.instagrammclient.view.main.MainAdapter;
import ru.nbdev.instagrammclient.view.main.MainView;

@InjectViewState
public class MainPresenter extends MvpPresenter<MainView> {
    private static final String TAG = "MainPresenter";
    private RecyclerPresenter recyclerPresenter;
    private Resources resources;
    private ApiHelper apiHelper;
    private List<Photo> photosList;

    public MainPresenter(Resources resources) {
        this.resources = resources;
        apiHelper = new ApiHelper();
        recyclerPresenter = new RecyclerPresenter();
    }

    @Override
    protected void onFirstViewAttach() {
        loadPhotosList();
    }

    private void loadPhotosList() {
        Observable<PhotosList> single = apiHelper.requestPhotosList();

        Disposable disposable = single.observeOn(AndroidSchedulers.mainThread()).subscribe(photos -> {
            photosList = photos.hits;
            getViewState().showMessage("Photos count: " + photosList.size());
            getViewState().updateRecyclerView();
        }, throwable -> {
            Log.e(TAG, "onError " + throwable);
            getViewState().showMessage(resources.getString(R.string.load_error));
        });
    }

    public RecyclerPresenter getRecyclerPresenter() {
        return recyclerPresenter;
    }

    private class RecyclerPresenter implements ru.nbdev.instagrammclient.presenter.RecyclerPresenter {
        @Override
        public int getItemCount() {
            if (photosList != null) {
                return photosList.size();
            }
            return 0;
        }

        @Override
        public void bindView(MainAdapter.MainViewHolder mainViewHolder) {
            Photo photo = photosList.get(mainViewHolder.getAdapterPosition());
            mainViewHolder.setImage(photo.previewURL);
            mainViewHolder.setOnClickListener(v -> onItemClick(photo.largeImageURL));
        }

        @Override
        public void onItemClick(String detailURL) {
            getViewState().runDetailActivity(detailURL);
        }
    }
}
