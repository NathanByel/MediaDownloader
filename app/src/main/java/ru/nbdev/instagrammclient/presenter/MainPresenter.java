package ru.nbdev.instagrammclient.presenter;

import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.nbdev.instagrammclient.PixabaySearchFilter;
import ru.nbdev.instagrammclient.R;
import ru.nbdev.instagrammclient.app.App;
import ru.nbdev.instagrammclient.model.entity.Photo;
import ru.nbdev.instagrammclient.model.retrofit.PixabayApiHelper;
import ru.nbdev.instagrammclient.model.room.AppDatabase;
import ru.nbdev.instagrammclient.view.main.MainAdapter;
import ru.nbdev.instagrammclient.view.main.MainView;

@InjectViewState
public class MainPresenter extends MvpPresenter<MainView> {
    private static final String TAG = "MainPresenter";
    private RecyclerPresenter recyclerPresenter;
    private List<Photo> photosList;
    private PixabayApiHelper pixabayApiHelper;
    private PixabaySearchFilter pixabaySearchFilter;

    @Inject
    AppDatabase database;

    public MainPresenter() {
        App.getAppComponent().inject(this);
        recyclerPresenter = new RecyclerPresenter();
        pixabayApiHelper = new PixabayApiHelper();
        pixabaySearchFilter = new PixabaySearchFilter();
    }

    public void onRefresh() {
        clearRecycler();
        loadPhotosList();
    }

    public void onSearch(String query) {
        clearRecycler();
        searchPhotos(query, pixabaySearchFilter);
    }

    public void onFiltersOpened() {
        getViewState().fillFilterFields(pixabaySearchFilter);
    }

    public void onFiltersClosed(int orderId, int typeId, int categoryId) {
        pixabaySearchFilter.setSelectedOrderById(orderId);
        pixabaySearchFilter.setSelectedImageTypeById(typeId);
        pixabaySearchFilter.setSelectedCategoryById(categoryId);
    }

    @Override
    protected void onFirstViewAttach() {
        loadPhotosList();
    }

    private void loadPhotosList() {
        Disposable disposable = database.pixabayDao().getPhotosList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(photos -> {
                    // TODO Надо придумать по каким правилам брать из базы или обновлять там данные, чтобы ссылки не протухали.
                    //if (photos.size() == 0) {
                        loadPhotosListFromInternet();
                    //} else {
                    //    photosList = photos;
                    //    updateRecycler();
                    //}
                }, throwable -> {
                    Log.e(TAG, "Not found. " + throwable.toString());
                });
    }

    private void loadPhotosListFromInternet() {
        Disposable disposable = pixabayApiHelper.requestRandomPhotosList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(photos -> {
                    photosList = photos.hits;
                    savePhotosListToDatabase();
                    updateRecycler();
                }, throwable -> {
                    Log.e(TAG, "onError " + throwable);
                    getViewState().showMessage(R.string.load_error);
                });
    }

    private void searchPhotos(String query, PixabaySearchFilter filter) {
        Disposable disposable = pixabayApiHelper.requestPhotosList(query, filter)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(photos -> {
                    photosList = photos.hits;
                    savePhotosListToDatabase();
                    updateRecycler();
                }, throwable -> {
                    Log.e(TAG, "onError " + throwable);
                    getViewState().showMessage(R.string.load_error);
                });
    }

    private void savePhotosListToDatabase() {
        Disposable disposable = Single.fromCallable(() -> database.pixabayDao().insertList(photosList))
                .subscribeOn(Schedulers.io())
                //.observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        items -> Log.d(TAG, "Added records to DB, " + items.size()),
                        throwable -> Log.e(TAG, "Error save to DB, " + throwable)
                );
    }

    private void updateRecycler() {
        if (photosList != null) {
            getViewState().showPhotosCount(photosList.size());
            getViewState().updateRecyclerView();
        }
    }

    private void clearRecycler() {
        photosList.clear();
        getViewState().updateRecyclerView();
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
            mainViewHolder.setOnClickListener(v -> onItemClick(photo.id));
        }

        @Override
        public void onItemClick(int photoId) {
            getViewState().runDetailActivity(photoId);
        }
    }
}
