package ru.nbdev.mediadownloader.view.detail;

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

import ru.nbdev.mediadownloader.R;
import ru.nbdev.mediadownloader.app.App;
import ru.nbdev.mediadownloader.common.Constants;
import ru.nbdev.mediadownloader.model.entity.Photo;
import ru.nbdev.mediadownloader.presenter.DetailPresenter;
import ru.nbdev.mediadownloader.view.GlideLoader;

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
        imageStatus = findViewById(R.id.imageview_detail_status);
        layoutTopPanel = findViewById(R.id.linearlayout_detail_toppanel);
        iconDownload = findViewById(R.id.icon_detail_download);
        iconShare = findViewById(R.id.icon_detail_share);
        photoView = findViewById(R.id.photoview_detail_photo);
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

    private void setLoadMode() {
        setTopPanelVisibility(false);
        photoView.setVisibility(View.VISIBLE);
        imageStatus.setImageResource(R.drawable.ic_progress_animated);
        imageStatus.setVisibility(View.VISIBLE);
    }

    private void setErrorMode(String text) {
        setTopPanelVisibility(false);
        photoView.setVisibility(View.INVISIBLE);
        imageStatus.setImageResource(R.drawable.ic_error_outline_48dp);
        showToast(text);
    }

    private void setPhotoMode() {
        setTopPanelVisibility(true);
        imageStatus.setVisibility(View.INVISIBLE);
        photoView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showPhoto(Photo photo) {
        setLoadMode();
        glideLoader.loadImage(photo.fullSizeURL, photoView, new GlideLoader.OnImageLoadedListener() {
            @Override
            public void onError() {
                setErrorMode(getResources().getString(R.string.load_error));
            }

            @Override
            public void onSuccess() {
                setPhotoMode();
            }
        });
    }

    @Override
    public void savePhoto(Photo photo) {
        String saveFileName = photo.id + "_L";
        saveFileName += photo.fullSizeURL.substring(photo.fullSizeURL.lastIndexOf('.'));

        glideLoader.saveImage(photo.fullSizeURL, saveFileName, new GlideLoader.OnImageSavedListener() {
            @Override
            public void onError() {
                runOnUiThread(() -> showToast(getResources().getString(R.string.save_error)));
            }

            @Override
            public void onSuccess(File file) {
                runOnUiThread(() -> showToast(getResources().getString(R.string.saved_to) + " " + file.toString()));
            }
        });
        /*FileDownloader downloader = new FileDownloader();
        String fileName = "123.jpg";
        File file = new File(Environment.getExternalStorageDirectory().toString() + File.separator + Environment.DIRECTORY_DOWNLOADS, fileName);
        Disposable disposable = downloader.download(photo.fullSizeURL, file)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    showToast(getResources().getString(R.string.saved_to) + " ");
                }, throwable -> {
                    showToast(getResources().getString(R.string.save_error));
                });*/
    }

    @Override
    public void checkWriteStoragePermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                presenter.onWriteStoragePermissionGranted();
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
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                presenter.onWriteStoragePermissionGranted();
            } else {
                showToast(getResources().getString(R.string.storage_permission_needed));
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void sharePhoto(Photo photo) {
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_TEXT, photo.fullSizeURL);

        startActivity(Intent.createChooser(share, getResources().getString(R.string.send_url)));
    }

    @Override
    public void showMessage(int textId) {
        showToast(getResources().getString(textId));
    }

    private void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
