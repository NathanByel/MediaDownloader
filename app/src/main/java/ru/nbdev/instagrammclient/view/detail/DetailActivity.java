package ru.nbdev.instagrammclient.view.detail;

import android.os.Bundle;
import android.widget.ImageView;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;

import ru.nbdev.instagrammclient.Constants;
import ru.nbdev.instagrammclient.R;
import ru.nbdev.instagrammclient.model.GlideLoader;
import ru.nbdev.instagrammclient.presenter.DetailPresenter;

public class DetailActivity extends MvpAppCompatActivity implements DetailView {

    private ImageView detailImage;
    private GlideLoader glideLoader;

    @InjectPresenter
    DetailPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        int photoId = getIntent().getIntExtra(Constants.EXTRA_PHOTO_ID_INT, -1);
        presenter.onNewPhotoId(photoId);

        detailImage = findViewById(R.id.image_detail);
        detailImage.setOnClickListener(v -> presenter.onImageClick());

        glideLoader = new GlideLoader(this);
    }

    @Override
    public void setDetailPhoto(String url) {
        glideLoader.loadImage(url, detailImage);
    }
}
