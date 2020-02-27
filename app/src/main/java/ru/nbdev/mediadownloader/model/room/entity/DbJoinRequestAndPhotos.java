package ru.nbdev.mediadownloader.model.room.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;
import static androidx.room.ForeignKey.NO_ACTION;

@Entity(
        tableName = "join_request_and_photos_table",
        indices = {
                @Index(value = "search_id"),
                @Index(value = "photo_id")
        },
        foreignKeys = {
                @ForeignKey(entity = DbSearchRequest.class, parentColumns = "_id", childColumns = "search_id", onDelete = CASCADE),
                @ForeignKey(entity = DbPhoto.class, parentColumns = "_id", childColumns = "photo_id", onDelete = NO_ACTION)
        }
)
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
