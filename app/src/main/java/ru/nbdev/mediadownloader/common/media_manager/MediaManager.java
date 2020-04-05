package ru.nbdev.mediadownloader.common.media_manager;

import java.io.File;

import io.reactivex.Single;
import ru.nbdev.mediadownloader.model.entity.Photo;

public interface MediaManager {

    Single<File> downloadPhoto(Photo photo);

    void sharePhoto(Photo photo);

    void updateAndroidGallery(File file);
}
