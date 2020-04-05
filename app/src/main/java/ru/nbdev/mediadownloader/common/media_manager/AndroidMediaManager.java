package ru.nbdev.mediadownloader.common.media_manager;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import java.io.File;

import io.reactivex.Single;
import ru.nbdev.mediadownloader.R;
import ru.nbdev.mediadownloader.common.FileDownloader;
import ru.nbdev.mediadownloader.model.entity.Photo;

public class AndroidMediaManager implements MediaManager {

    private static final String DOWNLOAD_DIR =
            Environment.getExternalStorageDirectory() + File.separator + Environment.DIRECTORY_DOWNLOADS;

    private final Context context;


    public AndroidMediaManager(Context context) {
        this.context = context;
    }

    @Override
    public Single<File> downloadPhoto(Photo photo) {
        String fileName = photo.id + "_L";
        fileName += photo.fullSizeURL.substring(photo.fullSizeURL.lastIndexOf('.'));
        File file = new File(DOWNLOAD_DIR, fileName);

        FileDownloader downloader = new FileDownloader();
        return downloader.download(photo.fullSizeURL, file);
    }

    @Override
    public void sharePhoto(Photo photo) {
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_TEXT, photo.fullSizeURL);

        Intent chooser = Intent.createChooser(share, context.getResources().getString(R.string.send_url));
        chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(chooser);
    }

    @Override
    public void updateAndroidGallery(File file) {
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.fromFile(file));
        context.sendBroadcast(intent);
    }
}
