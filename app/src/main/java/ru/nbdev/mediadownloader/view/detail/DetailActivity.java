package ru.nbdev.mediadownloader.view.detail;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.load.engine.GlideException;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.material.snackbar.Snackbar;

import moxy.MvpAppCompatActivity;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import ru.nbdev.mediadownloader.R;
import ru.nbdev.mediadownloader.app.App;
import ru.nbdev.mediadownloader.common.Constants;
import ru.nbdev.mediadownloader.model.entity.Photo;
import ru.nbdev.mediadownloader.presenter.DetailPresenter;
import ru.nbdev.mediadownloader.view.GlideLoader;
import timber.log.Timber;

public class DetailActivity extends MvpAppCompatActivity implements DetailView {

    private PhotoView photoView;
    private LinearLayout layoutTopPanel;
    private ImageView iconDownload;
    private ImageView iconShare;
    private ImageView imageStatus;

    private Animation animationZoomInOut;
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

        findViews();
        initAnimations();
        setupListeners();

        glideLoader = new GlideLoader(this);
        setLoadMode();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    private void setupListeners() {
        iconDownload.setOnClickListener(v -> {
            iconDownload.startAnimation(animationZoomInOut);
            presenter.onSaveClick();
        });

        iconShare.setOnClickListener(v -> {
            iconShare.startAnimation(animationZoomInOut);
            presenter.onShareClick();
        });
    }

    private void findViews() {
        layoutTopPanel = findViewById(R.id.linearlayout_detail_toppanel);
        iconDownload = findViewById(R.id.imageview_detail_download);
        iconShare = findViewById(R.id.imageview_detail_share);
        photoView = findViewById(R.id.photoview_detail_photo);
        imageStatus = findViewById(R.id.imageview_status);
    }

    private void initAnimations() {
        animationZoomInOut = AnimationUtils.loadAnimation(this, R.anim.animation_zoom_in_out);
    }

    private void setTopPanelVisibility(boolean visibility) {
        layoutTopPanel.setVisibility(visibility ? View.VISIBLE : View.INVISIBLE);
    }

    private void setLoadMode() {
        setTopPanelVisibility(false);
        imageStatus.setImageResource(R.drawable.ic_progress_animated);
        imageStatus.setVisibility(View.VISIBLE);
        photoView.setVisibility(View.INVISIBLE);
    }

    private void setErrorMode() {
        setTopPanelVisibility(false);
        imageStatus.setImageResource(R.drawable.ic_broken_image_black_50dp);
        imageStatus.setVisibility(View.VISIBLE);
        photoView.setVisibility(View.INVISIBLE);
    }

    private void setReadyMode() {
        setTopPanelVisibility(true);
        imageStatus.setVisibility(View.INVISIBLE);
        photoView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showPhoto(Photo photo) {
        setLoadMode();
        glideLoader.loadImage(photo.fullSizeURL, photoView, new GlideLoader.OnImageReadyListener() {
            @Override
            public void onError(GlideException e) {
                Timber.e(e, "Detail view glideLoader.loadImage() error.");
                setErrorMode();
                showMessage(R.string.load_error);
            }

            @Override
            public void onSuccess() {
                setReadyMode();
            }
        });
    }

    private void requestWriteStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.REQUEST_WRITE_EXTERNAL_STORAGE);
        }
    }

    @Override
    public void checkWriteStoragePermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                presenter.onWriteStoragePermissionReady(true);
            } else {
                if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    final String message = getResources().getString(R.string.storage_permission_needed);
                    Snackbar.make(photoView, message, Snackbar.LENGTH_LONG)
                            .setAction(getResources().getString(R.string.grant), v -> requestWriteStoragePermission())
                            .show();
                } else {
                    requestWriteStoragePermission();
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == Constants.REQUEST_WRITE_EXTERNAL_STORAGE && grantResults.length == 1) {
            presenter.onWriteStoragePermissionReady(grantResults[0] == PackageManager.PERMISSION_GRANTED);
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void showMessage(int textId) {
        showToast(getResources().getString(textId));
    }

    @Override
    public void showMessage(int textId, String text) {
        showToast(getResources().getString(textId) + text);
    }

    @Override
    public void showError() {
        setErrorMode();
        showMessage(R.string.load_error);
    }

    private void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
