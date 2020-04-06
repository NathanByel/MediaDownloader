package ru.nbdev.mediadownloader.view.detail;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.material.snackbar.Snackbar;

import javax.inject.Inject;

import moxy.MvpAppCompatActivity;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import ru.nbdev.mediadownloader.R;
import ru.nbdev.mediadownloader.app.App;
import ru.nbdev.mediadownloader.common.Constants;
import ru.nbdev.mediadownloader.common.image_loader.ImageLoader;
import ru.nbdev.mediadownloader.model.entity.Photo;
import ru.nbdev.mediadownloader.presenter.DetailPresenter;
import timber.log.Timber;

public class DetailActivity extends MvpAppCompatActivity implements DetailView {

    private final String IMAGE_LOADER_TAG = "DetailPresenter";
    private PhotoView photoView;
    private LinearLayout layoutTopPanel;
    private ImageView iconDownload;
    private ImageView iconShare;
    private ImageView imageStatus;
    private Animation animationZoomInOut;
    private final ImageLoader imageLoader = App.getAppComponent().getImageLoader();

    @InjectPresenter
    DetailPresenter presenter;


    @ProvidePresenter
    DetailPresenter provideDetailPresenter() {
        long photoId = getIntent().getLongExtra(Constants.EXTRA_PHOTO_ID_LONG, Constants.WRONG_PHOTO_ID);

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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        imageLoader.cancelLoading(IMAGE_LOADER_TAG);
        presenter.onDestroy();
    }

    @Override
    public void showProgress() {
        setTopPanelVisibility(false);
        photoView.setVisibility(View.INVISIBLE);

        imageStatus.clearAnimation();
        imageStatus.setImageResource(R.drawable.ic_progress);
        imageStatus.startAnimation(AnimationUtils.loadAnimation(this, R.anim.progress_animation));
        imageStatus.setVisibility(View.VISIBLE);
    }

    @Override
    public void showPhoto(Photo photo) {
        showProgress();
        imageLoader.loadImageFromUrl(photo.fullSizeURL, IMAGE_LOADER_TAG, new ImageLoader.OnReadyListener() {

            @Override
            public void onSuccess(Drawable image) {
                imageStatus.clearAnimation();
                imageStatus.setVisibility(View.INVISIBLE);

                setTopPanelVisibility(true);
                photoView.setImageDrawable(image);
                photoView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError() {
                Timber.e("imageLoader.loadImage() error.");
                showError();
                showMessage(R.string.load_error);
            }
        });
    }

    @Override
    public void showError() {
        setTopPanelVisibility(false);
        photoView.setVisibility(View.INVISIBLE);

        imageStatus.clearAnimation();
        imageStatus.setImageResource(R.drawable.ic_broken_image_black_50dp);
        imageStatus.setVisibility(View.VISIBLE);
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
        } else {
            presenter.onWriteStoragePermissionReady(true);
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
        Toast.makeText(this, getResources().getString(textId), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showMessage(int textId, String text) {
        Toast.makeText(this, getResources().getString(textId) + text, Toast.LENGTH_SHORT).show();
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

    private void requestWriteStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.REQUEST_WRITE_EXTERNAL_STORAGE);
        }
    }
}
