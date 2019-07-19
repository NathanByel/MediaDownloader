package ru.nbdev.instagrammclient.view.detail;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.github.chrisbanes.photoview.PhotoView;

import java.io.File;

import ru.nbdev.instagrammclient.Constants;
import ru.nbdev.instagrammclient.R;
import ru.nbdev.instagrammclient.app.App;
import ru.nbdev.instagrammclient.model.GlideLoader;
import ru.nbdev.instagrammclient.model.entity.Photo;
import ru.nbdev.instagrammclient.presenter.DetailPresenter;

public class DetailActivity extends MvpAppCompatActivity implements DetailView {
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 123;

    private PhotoView detailImage;
    private LinearLayout topPanelLayout;
    private ImageView iconDownload;
    private ImageView iconShare;
    private ImageView imageStatus;

    private Animation buttonsAnimation;
    private GlideLoader glideLoader;

    @InjectPresenter
    DetailPresenter presenter;

    @ProvidePresenter
    DetailPresenter provideDetailPresenter() {
        int photoId = getIntent().getIntExtra(Constants.EXTRA_PHOTO_ID_INT, Constants.WRONG_PHOTO_ID);

        DetailPresenter detailPresenter = new DetailPresenter(photoId);
        App.getAppComponent().inject(detailPresenter);
        return detailPresenter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        initViews();
        initAnimations();
        setupListeners();

        glideLoader = new GlideLoader(this);
        setTopPanelVisibility(false);
    }

    private void setupListeners() {
        iconDownload.setOnClickListener(v -> {
            iconDownload.startAnimation(buttonsAnimation);
            presenter.onSaveClick();
        });

        iconShare.setOnClickListener(v -> {
            iconShare.startAnimation(buttonsAnimation);
            presenter.onShareClick();
        });
    }

    private void initViews() {
        imageStatus = findViewById(R.id.detail_image_status);
        topPanelLayout = findViewById(R.id.detail_top_panel);
        iconDownload = findViewById(R.id.detail_download_icon);
        iconShare = findViewById(R.id.detail_share_icon);
        detailImage = findViewById(R.id.detail_image);
    }

    private void initAnimations() {
        buttonsAnimation = AnimationUtils.loadAnimation(DetailActivity.this, R.anim.scale_zoom_in_out);
    }

    private void setTopPanelVisibility(boolean visibility) {
        topPanelLayout.setVisibility(visibility ? View.VISIBLE : View.INVISIBLE);
    }

    private void requestWriteStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_EXTERNAL_STORAGE);
        }
    }

    @Override
    public void showPhoto(Photo photo) {
        setTopPanelVisibility(false);
        detailImage.setVisibility(View.INVISIBLE);
        imageStatus.setImageResource(R.drawable.progress_animation);
        imageStatus.setVisibility(View.VISIBLE);

        glideLoader.loadImage(photo.largeImageURL, detailImage, new GlideLoader.OnImageLoadedListener() {
            @Override
            public void onError() {
                setTopPanelVisibility(false);
                imageStatus.setImageResource(R.drawable.ic_error_outline_48dp);
                showMessage(getResources().getString(R.string.load_error));
            }

            @Override
            public void onSuccess() {
                imageStatus.setVisibility(View.INVISIBLE);
                detailImage.setVisibility(View.VISIBLE);
                setTopPanelVisibility(true);
            }
        });
    }

    @Override
    public void savePhoto(Photo photo) {
        String saveFileName = photo.id + "_L";
        saveFileName += photo.largeImageURL.substring(photo.largeImageURL.lastIndexOf('.'));

        glideLoader.saveImage(photo.largeImageURL, saveFileName, new GlideLoader.OnImageSavedListener() {
            @Override
            public void onError() {
                runOnUiThread(() -> showMessage(getResources().getString(R.string.save_error)));
            }

            @Override
            public void onSuccess(File file) {
                runOnUiThread(() -> showMessage(getResources().getString(R.string.saved_to) + " " + file.toString()));
            }
        });
    }

    @Override
    public void checkWriteStoragePermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                presenter.onWriteStoragePermissionGranted();
            } else {
                if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    final String message = "Storage permission is needed to save files";
                    Snackbar.make(detailImage, message, Snackbar.LENGTH_LONG)
                            .setAction("GRANT", v -> requestWriteStoragePermission())
                            .show();
                } else {
                    requestWriteStoragePermission();
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_WRITE_EXTERNAL_STORAGE && grantResults.length == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                presenter.onWriteStoragePermissionGranted();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void showMessage(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void sharePhoto(Photo photo) {
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");

        //share.putExtra(Intent.EXTRA_SUBJECT, "Title Of The Post");
        share.putExtra(Intent.EXTRA_TEXT, photo.largeImageURL);

        startActivity(Intent.createChooser(share, getResources().getString(R.string.send_url)));
    }
}
