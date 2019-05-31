package ru.nbdev.instagrammclient.view;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;

import ru.nbdev.instagrammclient.Constants;
import ru.nbdev.instagrammclient.R;
import ru.nbdev.instagrammclient.presenter.DetailPresenter;

public class DetailActivity extends MvpAppCompatActivity implements DetailView {

    private ImageView detailImage;

    @InjectPresenter
    DetailPresenter presenter;

    @ProvidePresenter
    DetailPresenter provideDetailsPresenter() {
        return new DetailPresenter(getResources());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        int position = getIntent().getIntExtra(Constants.EXTRA_POSITION_INT, -1);
        presenter.setPosition(position);

        detailImage = findViewById(R.id.image_detail);
        detailImage.setOnClickListener(v -> presenter.onImageClick());

        presenter.onCreate();
    }


    @Override
    public void setDetailImage(Drawable image) {
        detailImage.setImageDrawable(image);
    }
}
