package ru.nbdev.instagrammclient.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import ru.nbdev.instagrammclient.presenter.MainPresenter;
import ru.nbdev.instagrammclient.R;

public class MainActivity extends AppCompatActivity {
    private static final int RECYCLER_COLUMNS = 2;
    private MainPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        presenter = new MainPresenter(getResources());
        recyclerInit();
    }

    private void recyclerInit() {
        RecyclerView recyclerView = findViewById(R.id.main_recycler);
        GridLayoutManager layoutManager = new GridLayoutManager(this, RECYCLER_COLUMNS);
        recyclerView.setLayoutManager(layoutManager);
        MainAdapter adapter = new MainAdapter(presenter.getRecyclerPresenter());
        recyclerView.setAdapter(adapter);
    }
}
