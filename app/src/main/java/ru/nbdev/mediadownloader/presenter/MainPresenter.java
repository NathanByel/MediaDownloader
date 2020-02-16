package ru.nbdev.mediadownloader.presenter;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.nbdev.mediadownloader.R;
import ru.nbdev.mediadownloader.model.entity.Photo;
import ru.nbdev.mediadownloader.model.entity.SearchRequest;
import ru.nbdev.mediadownloader.model.entity.pixabay.PixabayFilter;
import ru.nbdev.mediadownloader.model.entity.pixabay.PixabaySearchRequest;
import ru.nbdev.mediadownloader.model.repository.PhotoRepository;
import ru.nbdev.mediadownloader.view.main.MainRecyclerAdapter;
import ru.nbdev.mediadownloader.view.main.MainView;
import timber.log.Timber;

@InjectViewState
public class MainPresenter extends MvpPresenter<MainView> {

    private List<Photo> photosList;
    private final MainRecyclerPresenter mainRecyclerPresenter;
    private PixabayFilter pixabayFilter;
    private String lastQuery;

    @Inject
    PhotoRepository photoRepository;

    public MainPresenter() {
        lastQuery = "";
        mainRecyclerPresenter = new MainRecyclerPresenter();
        pixabayFilter = new PixabayFilter();
    }

    public void onRefresh() {
        clearRecycler();
        searchPhotos(lastQuery, pixabayFilter);
    }

    public void onSearch(String query) {
        lastQuery = query;
        clearRecycler();
        searchPhotos(query, pixabayFilter);
    }

    public void onFilterIconClick() {
        getViewState().showFilterDialog(pixabayFilter);
    }

    public void onFiltersClosed(PixabayFilter filter) {
        pixabayFilter = filter;
    }

    @Override
    protected void onFirstViewAttach() {
        searchPhotos("", pixabayFilter);
    }

    private void searchPhotos(String query, PixabayFilter filter) {
        getViewState().showProgress();
        SearchRequest request = new PixabaySearchRequest(query, filter);
        Disposable disposable = photoRepository.searchPhotos(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(photos -> {
                    getViewState().hideProgress();
                    photosList = photos;
                    updateRecycler();
                }, throwable -> {
                    getViewState().hideProgress();
                    Timber.e("searchPhotos() from repository error. %s", throwable.getMessage());
                    getViewState().showMessage(R.string.load_error);
                });
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
        public void bindView(MainRecyclerAdapter.MainRecyclerViewHolder mainRecyclerViewHolder) {
            Photo photo = photosList.get(mainRecyclerViewHolder.getAdapterPosition());
            mainRecyclerViewHolder.setPhotoData(photo);
            mainRecyclerViewHolder.setOnImageClickListener(v -> onItemClick((int) photo.id));
        }

        @Override
        public void onItemClick(int photoId) {
            getViewState().runDetailActivity(photoId);
        }
    }
}
