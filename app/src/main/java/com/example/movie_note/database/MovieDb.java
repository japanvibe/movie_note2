package com.example.movie_note.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {FavoriteMovie.class}, version = 1)
public abstract class MovieDb extends RoomDatabase {
    public abstract MovieDao movieDao();
}
