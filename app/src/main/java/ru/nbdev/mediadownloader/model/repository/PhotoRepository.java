package ru.nbdev.mediadownloader.model.repository;

import java.util.List;

import io.reactivex.Single;
import ru.nbdev.mediadownloader.model.entity.Photo;
import ru.nbdev.mediadownloader.model.entity.SearchRequest;

public interface PhotoRepository {

    String getServiceName();

    Single<List<Photo>> getRandomPhotos();

    Single<List<Photo>> searchPhotos(SearchRequest request);

    Single<Photo> getPhotoById(long id);
}
