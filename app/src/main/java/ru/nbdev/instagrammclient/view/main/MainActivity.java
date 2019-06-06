package ru.nbdev.instagrammclient.view.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;

import ru.nbdev.instagrammclient.Constants;
import ru.nbdev.instagrammclient.presenter.MainPresenter;
import ru.nbdev.instagrammclient.R;
import ru.nbdev.instagrammclient.view.detail.DetailActivity;

public class MainActivity extends MvpAppCompatActivity implements MainView {
    private static final int RECYCLER_COLUMNS = 2;
    private MainAdapter mainAdapter;

    @InjectPresenter
    MainPresenter presenter;

    @ProvidePresenter
    MainPresenter provideMainPresenter() {
        return new MainPresenter(getResources());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerInit();
    }

    private void recyclerInit() {
        RecyclerView recyclerView = findViewById(R.id.main_recycler);
        GridLayoutManager layoutManager = new GridLayoutManager(this, RECYCLER_COLUMNS);
        recyclerView.setLayoutManager(layoutManager);
        mainAdapter = new MainAdapter(this, presenter.getRecyclerPresenter());
        recyclerView.setAdapter(mainAdapter);
    }

    @Override
    public void runDetailActivity(String detailURL) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(Constants.EXTRA_DETAIL_URL_STRING, detailURL);
        startActivity(intent);
    }

    @Override
    public void updateRecyclerView() {
        mainAdapter.notifyDataSetChanged();
    }

    @Override
    public void showMessage(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
