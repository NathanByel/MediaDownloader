package ru.nbdev.instagrammclient.view.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;

import ru.nbdev.instagrammclient.model.Constants;
import ru.nbdev.instagrammclient.model.Helpers;
import ru.nbdev.instagrammclient.presenter.PixabaySearchFilter;
import ru.nbdev.instagrammclient.R;
import ru.nbdev.instagrammclient.app.App;
import ru.nbdev.instagrammclient.presenter.MainPresenter;
import ru.nbdev.instagrammclient.view.detail.DetailActivity;

public class MainActivity extends MvpAppCompatActivity implements MainView, SwipeRefreshLayout.OnRefreshListener {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private MainAdapter mainAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    private View bottomSheet;
    private ImageView searchBarStartIcon;
    private ImageView searchBarFilterIcon;
    private EditText editSearchQuery;

    private BottomSheetDialog filterDialog;
    private ArrayAdapter<String> orderFilterAdapter;
    private ArrayAdapter<String> imageTypeFilterAdapter;
    private ArrayAdapter<String> categoryFilterAdapter;

    private Spinner orderFilterSpinner;
    private Spinner imageTypeFilterSpinner;
    private Spinner categoryFilterSpinner;

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

        findViews();
        toolbarInit();
        recyclerInit();
        initFilters();
        setupListeners();
    }

    private void findViews() {
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout_main);
        toolbar = findViewById(R.id.toolbar_main);
        recyclerView = findViewById(R.id.recycler_main);

        searchBarStartIcon = findViewById(R.id.icon_search_bar_start);
        searchBarFilterIcon = findViewById(R.id.icon_search_bar_filter);
        editSearchQuery = findViewById(R.id.edit_search_bar_query);

        bottomSheet = getLayoutInflater().inflate(R.layout.bottom_sheet_main, null);
        orderFilterSpinner = bottomSheet.findViewById(R.id.spinner_order);
        imageTypeFilterSpinner = bottomSheet.findViewById(R.id.spinner_image_type);
        categoryFilterSpinner = bottomSheet.findViewById(R.id.spinner_image_category);
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

        filterDialog.setOnDismissListener(dialog ->
                presenter.onFiltersClosed(
                        (int) orderFilterSpinner.getSelectedItemId(),
                        (int) imageTypeFilterSpinner.getSelectedItemId(),
                        (int) categoryFilterSpinner.getSelectedItemId()
                )
        );
    }

    private void clearSearchBarFocus() {
        Helpers.hideKeyboard(MainActivity.this);
        editSearchQuery.clearFocus();
    }

    private void toolbarInit() {
        setSupportActionBar(toolbar);
    }

    private void initFilters() {
        orderFilterAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item);
        orderFilterSpinner.setAdapter(orderFilterAdapter);

        imageTypeFilterAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item);
        imageTypeFilterSpinner.setAdapter(imageTypeFilterAdapter);

        categoryFilterAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item);
        categoryFilterSpinner.setAdapter(categoryFilterAdapter);

        filterDialog = new BottomSheetDialog(MainActivity.this);
        filterDialog.setContentView(bottomSheet);
    }

    private void recyclerInit() {
        GridLayoutManager layoutManager = new GridLayoutManager(this, Constants.RECYCLER_COLUMNS);
        recyclerView.setLayoutManager(layoutManager);
        mainAdapter = new MainAdapter(this, presenter.getMainRecyclerPresenter());
        recyclerView.setAdapter(mainAdapter);
    }

    private void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    private String[] getFilterNamesFromRes(PixabaySearchFilter.FilterValue[] values) {
        String[] names = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            names[i] = getResources().getString(values[i].getValueNameResId());
        }
        return names;
    }

    @Override
    public void showFilterDialog(PixabaySearchFilter filter) {
        orderFilterAdapter.clear();
        orderFilterAdapter.addAll(getFilterNamesFromRes(filter.getOrderValues()));
        orderFilterSpinner.setSelection(filter.getSelectedOrderId());

        imageTypeFilterAdapter.clear();
        imageTypeFilterAdapter.addAll(getFilterNamesFromRes(filter.getImageTypeValues()));
        imageTypeFilterSpinner.setSelection(filter.getSelectedImageTypeId());

        categoryFilterAdapter.clear();
        categoryFilterAdapter.addAll(getFilterNamesFromRes(filter.getCategoryValues()));
        categoryFilterSpinner.setSelection(filter.getSelectedCategoryId());

        filterDialog.show();
    }

    @Override
    public void onBackPressed() {
        if (editSearchQuery.isFocused()) {
            clearSearchBarFocus();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void runDetailActivity(int photoId) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(Constants.EXTRA_PHOTO_ID_INT, photoId);
        startActivity(intent);
    }

    @Override
    public void updateRecyclerView() {
        mainAdapter.notifyDataSetChanged();
    }

    @Override
    public void showMessage(int textId) {
        showToast(getResources().getString(textId));
    }

    @Override
    public void showPhotosCount(int count) {
        showToast(getResources().getString(R.string.photos_count) + count);
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(false);
        presenter.onRefresh();
    }
}
