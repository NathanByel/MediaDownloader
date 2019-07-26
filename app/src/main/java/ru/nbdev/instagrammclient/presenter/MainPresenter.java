package ru.nbdev.instagrammclient.presenter;

import android.os.Build;
import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import ru.nbdev.instagrammclient.R;
import ru.nbdev.instagrammclient.model.entity.Photo;
import ru.nbdev.instagrammclient.model.entity.PhotosList;
import ru.nbdev.instagrammclient.model.entity.SearchRequest;
import ru.nbdev.instagrammclient.model.retrofit.PixabayApiHelper;
import ru.nbdev.instagrammclient.model.room.AppDatabase;
import ru.nbdev.instagrammclient.view.main.MainAdapter;
import ru.nbdev.instagrammclient.view.main.MainView;

@InjectViewState
public class MainPresenter extends MvpPresenter<MainView> {

    private static final String TAG = "MainPresenter";

    private MainRecyclerPresenter mainRecyclerPresenter;
    private List<Photo> photosList;
    private PixabayApiHelper pixabayApiHelper;
    private PixabaySearchFilter pixabaySearchFilter;

    @Inject
    AppDatabase database;

    public MainPresenter() {
        mainRecyclerPresenter = new MainRecyclerPresenter();
        pixabayApiHelper = new PixabayApiHelper();
        pixabaySearchFilter = new PixabaySearchFilter();
    }

    public void onRefresh() {
        clearRecycler();
        loadPhotosListFromInternet();
    }

    public void onSearch(String query) {
        clearRecycler();
        searchPhotos(query, pixabaySearchFilter);
    }

    public void onFilterIconClick() {
        getViewState().showFilterDialog(pixabaySearchFilter);
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
        Disposable disposable = pixabayApiHelper.requestPhotosList(query, filter, 1)
                //.observeOn(AndroidSchedulers.mainThread())
                .subscribe(photos -> {
                    photosList.clear();
                    photosList.addAll(photos.hits);
                    //photosList = photos.hits;

                    int pages = (int)Math.ceil(photos.totalHits / 200f);

                    if (pages > 1) {
                        List<Observable<?>> requests = new ArrayList<>();

                        // Make a collection of all requests you need to call at once, there can be any number of requests, not only 3.
                        // You can have 2 or 5, or 100.
                        for (int i = 2; i <= pages; i++) {
                            requests.add(pixabayApiHelper.requestPhotosList(query, filter, i));
                        }

                        // Zip all requests with the Function, which will receive the results.
                        Disposable d = Observable.zip(
                                requests,

                                new Function<Object[], List<Photo>>() {
                                    @Override
                                    public List<Photo> apply(Object[] objects) throws Exception {
                                        // Objects[] is an array of combined results of completed requests
                                        List<Photo> list = new ArrayList<>();

                                        for (Object photosList : objects) {
                                            list.addAll(((PhotosList)photosList).hits);
                                        }
                                        return list;
                                    }
                                })
                                .observeOn(AndroidSchedulers.mainThread())
                                // After all requests had been performed the next observer will receive the Object, returned from Function
                                .subscribe(
                                        // Will be triggered if all requests will end successfully
                                        new Consumer<List<Photo>>() {
                                            @Override
                                            public void accept(List<Photo> photos) throws Exception {
                                                //Do something on successful completion of all requests
                                                photosList.addAll(photos);

                                                //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                                //    photosList.sort((o1, o2) -> Integer.compare(o1.id, o2.id));
                                                //}

                                                savePhotosListToDatabase();
                                                saveRequest(query, photosList);
                                                updateRecycler();
                                                return;
                                            }
                                        },

                                        // Will be triggered if any error during requests will happen
                                        new Consumer<Throwable>() {
                                            @Override
                                            public void accept(Throwable e) throws Exception {
                                                //Do something on error completion of requests
                                                Log.e(TAG, "onError " + e);
                                                getViewState().showMessage(R.string.load_error);
                                                return;
                                            }
                                        }
                                );
                    }

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

    private void saveRequest(String request, List<Photo> photosList) {
        Disposable disposable = Single.fromCallable(() -> database.pixabayDao().insert(
                new SearchRequest(request, new Date(), photosList.toArray(new Photo[0]))
        ))
                .subscribeOn(Schedulers.io())
                //.observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        items -> {
                            Log.d(TAG, "Added records to DB, " + items);
                        },
                        throwable -> {
                            Log.e(TAG, "Error save to DB, " + throwable);
                        }
                );
    }


    private void updateRecycler() {
        if (photosList != null) {
            getViewState().showPhotosCount(photosList.size());
            getViewState().updateRecyclerView();
        }
    }

    private void clearRecycler() {
        if (photosList != null) {
            photosList.clear();
            getViewState().updateRecyclerView();
        }
    }

    public MainRecyclerPresenter getMainRecyclerPresenter() {
        return mainRecyclerPresenter;
    }

    private class MainRecyclerPresenter implements RecyclerPresenter {
        @Override
        public int getItemCount() {
            if (photosList != null) {
                return photosList.size();
            }
            return 0;
        }

        @Override
        public void bindView(MainAdapter.MainRecyclerViewHolder mainRecyclerViewHolder) {
            Photo photo = photosList.get(mainRecyclerViewHolder.getAdapterPosition());
            mainRecyclerViewHolder.setPhotoData(photo);
            mainRecyclerViewHolder.setOnClickListener(v -> onItemClick(photo.id));
        }

        @Override
        public void onItemClick(int photoId) {
            getViewState().runDetailActivity(photoId);
        }
    }
}
