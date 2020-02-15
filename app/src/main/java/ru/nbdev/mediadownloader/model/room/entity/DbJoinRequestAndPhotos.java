package ru.nbdev.mediadownloader.model.room.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "join_request_and_photos_table")
public class DbJoinRequestAndPhotos {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    public long id;

    @ColumnInfo(name = "search_id")
    public long searchId;

    @ColumnInfo(name = "photo_id")
    public long photoId;

    public DbJoinRequestAndPhotos(long searchId, long photoId) {
        this.searchId = searchId;
        this.photoId = photoId;
    }
}
