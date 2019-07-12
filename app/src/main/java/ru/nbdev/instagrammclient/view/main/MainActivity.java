package ru.nbdev.instagrammclient.view.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;

import ru.nbdev.instagrammclient.Constants;
import ru.nbdev.instagrammclient.PixabaySearchFilter;
import ru.nbdev.instagrammclient.R;
import ru.nbdev.instagrammclient.presenter.MainPresenter;
import ru.nbdev.instagrammclient.view.detail.DetailActivity;

public class MainActivity extends MvpAppCompatActivity implements MainView, SwipeRefreshLayout.OnRefreshListener {
    private static final int RECYCLER_COLUMNS = 2;
    private MainAdapter mainAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    private Spinner orderFilterSpinner;
    private Spinner imageTypeFilterSpinner;
    private Spinner categoryFilterSpinner;

    @InjectPresenter
    MainPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbarInit();
        searchBarInit();
        swipeRefreshInit();
        recyclerInit();
    }

    private void searchBarInit() {
        ImageView searchBarStartIcon = findViewById(R.id.search_bar_start_icon);
        ImageView searchBarFilterIcon = findViewById(R.id.search_bar_filter_icon);
        EditText editText = findViewById(R.id.search_bar_edit_text);

        searchBarStartIcon.setOnClickListener(v -> {
            editText.clearFocus();
            hideKeyboard(MainActivity.this);
        });

        editText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                searchBarStartIcon.setImageResource(R.drawable.ic_arrow_back_black_24dp);
            } else {
                searchBarStartIcon.setImageResource(R.drawable.ic_search_black_24dp);
            }
        });

        editText.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN &&
                    (keyCode == KeyEvent.KEYCODE_ENTER)) {

                editText.clearFocus();
                hideKeyboard(MainActivity.this);
                presenter.onSearch(editText.getText().toString());
                return true;
            }
            return false;
        });

        searchBarFilterIcon.setOnClickListener(v -> {
            View view = getLayoutInflater().inflate(R.layout.main_bottom_sheet, null);
            bottomSpinnersInit(view);
            presenter.onFiltersOpened();

            BottomSheetDialog dialog = new BottomSheetDialog(MainActivity.this);
            dialog.setContentView(view);
            dialog.show();
            dialog.setOnDismissListener(dialog1 -> presenter.onFiltersClosed(
                    (int) orderFilterSpinner.getSelectedItemId(),
                    (int) imageTypeFilterSpinner.getSelectedItemId(),
                    (int) categoryFilterSpinner.getSelectedItemId()
            ));
        });
    }

    private void toolbarInit() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void swipeRefreshInit() {
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    private void bottomSpinnersInit(View v) {
        orderFilterSpinner = v.findViewById(R.id.spinner_order);
        imageTypeFilterSpinner = v.findViewById(R.id.spinner_image_type);
        categoryFilterSpinner = v.findViewById(R.id.spinner_image_category);
    }


    @Override
    public void fillFilterFields(PixabaySearchFilter filter) {
        ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                filter.getOrderTranslatedKeys().toArray(new String[0])
        );
        orderFilterSpinner.setAdapter(adapter);
        orderFilterSpinner.setSelection(filter.getSelectedOrderId());

        adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                filter.getImageTypeTranslatedKeys().toArray(new String[0])
        );
        imageTypeFilterSpinner.setAdapter(adapter);
        imageTypeFilterSpinner.setSelection(filter.getSelectedImageTypeId());

        adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                filter.getCategoryTranslatedKeys().toArray(new String[0])
        );
        categoryFilterSpinner.setAdapter(adapter);
        categoryFilterSpinner.setSelection(filter.getSelectedCategoryId());
    }

    // TODO сделать в начале закрытие клавиатуры, а потом выход, если была активирована панель поиска
    /*@Override
    public void onBackPressed() {
        super.onBackPressed();
    }*/

    private void recyclerInit() {
        RecyclerView recyclerView = findViewById(R.id.main_recycler);
        GridLayoutManager layoutManager = new GridLayoutManager(this, RECYCLER_COLUMNS);
        recyclerView.setLayoutManager(layoutManager);
        mainAdapter = new MainAdapter(this, presenter.getRecyclerPresenter());
        recyclerView.setAdapter(mainAdapter);
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


    private void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(false);
        presenter.onRefresh();
    }


    public static void hideKeyboard(Activity activity) {
        if (activity != null) {

            // Check if no view has focus
            View view = activity.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            } else {
                activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
            }
        }
    }
}
