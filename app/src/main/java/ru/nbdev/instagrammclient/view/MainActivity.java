package ru.nbdev.instagrammclient.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;

import ru.nbdev.instagrammclient.Constants;
import ru.nbdev.instagrammclient.presenter.MainPresenter;
import ru.nbdev.instagrammclient.R;

public class MainActivity extends MvpAppCompatActivity implements MainView {
    private static final int RECYCLER_COLUMNS = 2;

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
        MainAdapter adapter = new MainAdapter(presenter.getRecyclerPresenter());
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void runDetailActivity(int position) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(Constants.EXTRA_POSITION_INT, position);
        startActivity(intent);
    }
}
