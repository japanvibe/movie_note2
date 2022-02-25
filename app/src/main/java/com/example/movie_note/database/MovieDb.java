package com.example.movie_note.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.movie_note.entity.Movie;

@Database(entities = {Movie.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class MovieDb extends RoomDatabase {
    public abstract MovieDao movieDao();
}
