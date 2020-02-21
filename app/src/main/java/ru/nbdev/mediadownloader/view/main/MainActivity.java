package ru.nbdev.mediadownloader.view.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;

import ru.nbdev.mediadownloader.R;
import ru.nbdev.mediadownloader.app.App;
import ru.nbdev.mediadownloader.common.Constants;
import ru.nbdev.mediadownloader.common.Helpers;
import ru.nbdev.mediadownloader.model.entity.pixabay.PixabayFilter;
import ru.nbdev.mediadownloader.presenter.MainPresenter;
import ru.nbdev.mediadownloader.view.detail.DetailActivity;

public class MainActivity extends MvpAppCompatActivity implements MainView, SwipeRefreshLayout.OnRefreshListener {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private MainRecyclerAdapter mainRecyclerAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ImageView imageViewStatus;

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
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(false);
        presenter.onRefresh();
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
        imageViewStatus = findViewById(R.id.imageview_status);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout_main);
        toolbar = findViewById(R.id.toolbar_main);
        recyclerView = findViewById(R.id.recycler_main);

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
                presenter.onSearch(editSearchQuery.getText().toString());
                return true;
            }
            return false;
        });

        searchBarFilterIcon.setOnClickListener(v -> presenter.onFilterIconClick());

        swipeRefreshLayout.setOnRefreshListener(this);

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
        GridLayoutManager layoutManager = new GridLayoutManager(this, Constants.RECYCLER_COLUMNS);
        recyclerView.setLayoutManager(layoutManager);
        mainRecyclerAdapter = new MainRecyclerAdapter(this, presenter.getMainRecyclerPresenter());
        recyclerView.setAdapter(mainRecyclerAdapter);
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
        imageViewStatus.setImageResource(R.drawable.ic_progress_animated);
        imageViewStatus.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        imageViewStatus.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showError() {
        imageViewStatus.setImageResource(R.drawable.ic_broken_image_black_50dp);
        imageViewStatus.setVisibility(View.VISIBLE);
    }

    @Override
    public void runDetailActivity(int photoId) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(Constants.EXTRA_PHOTO_ID_INT, photoId);
        startActivity(intent);
    }

    @Override
    public void updateRecyclerView() {
        mainRecyclerAdapter.notifyDataSetChanged();
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
