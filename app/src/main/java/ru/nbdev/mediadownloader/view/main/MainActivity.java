package ru.nbdev.mediadownloader.view.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.analytics.FirebaseAnalytics;

import moxy.MvpAppCompatActivity;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import ru.nbdev.mediadownloader.R;
import ru.nbdev.mediadownloader.app.App;
import ru.nbdev.mediadownloader.common.Constants;
import ru.nbdev.mediadownloader.common.Helpers;
import ru.nbdev.mediadownloader.model.entity.pixabay.PixabayFilter;
import ru.nbdev.mediadownloader.presenter.MainPresenter;
import ru.nbdev.mediadownloader.view.detail.DetailActivity;

public class MainActivity extends MvpAppCompatActivity implements MainView {

    private Toolbar toolbar;
    private RecyclerView recycler;
    private MainRecyclerAdapter recyclerAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ImageView ivStatus;

    private ImageView searchBarStartIcon;
    private ImageView searchBarFilterIcon;
    private EditText editSearchQuery;

    private PixabayFilterSheet pixabayFilterSheet;

    @InjectPresenter
    MainPresenter presenter;


    @ProvidePresenter
    MainPresenter provideMainPresenter() {
        MainPresenter mainPresenter = new MainPresenter();
        App.getAppComponent().inject(mainPresenter);
        return mainPresenter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pixabayFilterSheet = new PixabayFilterSheet(this);
        findViews();
        toolbarInit();
        recyclerInit();
        setupListeners();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (editSearchQuery.isFocused()) {
            clearSearchBarFocus();
        } else {
            super.onBackPressed();
        }
    }

    private void findViews() {
        ivStatus = findViewById(R.id.imageview_status);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout_main);
        toolbar = findViewById(R.id.toolbar_main);
        recycler = findViewById(R.id.recycler_main);

        searchBarStartIcon = findViewById(R.id.imageview_search_bar_start);
        searchBarFilterIcon = findViewById(R.id.imageview_search_bar_filter);
        editSearchQuery = findViewById(R.id.edittext_search_bar_query);
    }

    private void setupListeners() {
        searchBarStartIcon.setOnClickListener(v -> clearSearchBarFocus());

        editSearchQuery.setOnFocusChangeListener((v, hasFocus) -> {
            int rId = hasFocus ? R.drawable.ic_arrow_back_black_24dp : R.drawable.ic_search_black_24dp;
            searchBarStartIcon.setImageResource(rId);
        });

        editSearchQuery.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                clearSearchBarFocus();
                String query = editSearchQuery.getText().toString();
                presenter.onSearch(query);

                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.SEARCH_TERM, query);
                App.getFirebaseAnalytics().logEvent(FirebaseAnalytics.Event.SEARCH, bundle);
                return true;
            }
            return false;
        });

        searchBarFilterIcon.setOnClickListener(v -> presenter.onFilterIconClick());

        swipeRefreshLayout.setOnRefreshListener(() -> {
            swipeRefreshLayout.setRefreshing(false);
            presenter.onRefresh();
        });

        pixabayFilterSheet.setOnDismissListener(filter -> presenter.onFiltersClosed(filter));

        pixabayFilterSheet.setOnShowResultClickListener(filter -> presenter.onFiltersShowResultClick(editSearchQuery.getText().toString(), filter));
    }

    private void clearSearchBarFocus() {
        Helpers.hideKeyboard(MainActivity.this);
        editSearchQuery.clearFocus();
    }

    private void toolbarInit() {
        setSupportActionBar(toolbar);
    }

    private void recyclerInit() {
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(
                Constants.RECYCLER_COLUMNS,
                StaggeredGridLayoutManager.VERTICAL
        );
        recycler.setLayoutManager(layoutManager);
        recyclerAdapter = new MainRecyclerAdapter(this, presenter.getMainRecyclerPresenter());
        recycler.setAdapter(recyclerAdapter);
    }

    private void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showFilterDialog(PixabayFilter filter) {
        pixabayFilterSheet.show(filter);
    }

    @Override
    public void hideFilterDialog() {
        pixabayFilterSheet.dismiss();
    }

    @Override
    public void showProgress() {
        recycler.setVisibility(View.INVISIBLE);

        // Android API 23, 24 have "animated-rotate" bug in xml.
        // So, we use animation.
        ivStatus.clearAnimation();
        ivStatus.setImageResource(R.drawable.ic_progress);
        ivStatus.startAnimation(AnimationUtils.loadAnimation(this, R.anim.progress_animation));
        ivStatus.setVisibility(View.VISIBLE);
    }

    @Override
    public void showResult() {
        ivStatus.clearAnimation();
        ivStatus.setVisibility(View.INVISIBLE);
        recycler.setVisibility(View.VISIBLE);
        updatePhotosList();
    }

    @Override
    public void showError() {
        recycler.setVisibility(View.INVISIBLE);
        ivStatus.clearAnimation();
        ivStatus.setImageResource(R.drawable.ic_broken_image_black_50dp);
        ivStatus.setVisibility(View.VISIBLE);
    }

    @Override
    public void updatePhotosList() {
        recyclerAdapter.notifyDataSetChanged();
    }

    @Override
    public void runDetailActivity(long photoId) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(Constants.EXTRA_PHOTO_ID_LONG, photoId);
        startActivity(intent);
    }

    @Override
    public void showMessage(int textId) {
        showToast(getResources().getString(textId));
    }

    @Override
    public void showMessage(int textId, String text) {
        showToast(getResources().getString(textId) + text);
    }
}
