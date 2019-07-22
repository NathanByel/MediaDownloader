package ru.nbdev.instagrammclient.model.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import ru.nbdev.instagrammclient.model.entity.Photo;

@Database(entities = {Photo.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract PixabayDao pixabayDao();
}
