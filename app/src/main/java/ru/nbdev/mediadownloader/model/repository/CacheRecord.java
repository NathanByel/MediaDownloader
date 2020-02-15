package ru.nbdev.mediadownloader.model.repository;

import java.util.List;

import ru.nbdev.mediadownloader.model.entity.Photo;
import ru.nbdev.mediadownloader.model.entity.SearchRequest;

public class CacheRecord {
    SearchRequest searchRequest;
    String key;
    List<Photo> photos;
}
