package com.example.movie_note.database;

import android.app.Application;

import androidx.room.Room;

public class App extends Application {
    private static App instance;
    private MovieDb database;

    @Override
    public void onCreate() {
        super.onCreate();
        database = Room.databaseBuilder(
                this,
                MovieDb.class,
                "movieDb.db").allowMainThreadQueries().build();
        instance = this;
    }

    public static App getInstance() {
        return instance;
    }

    public MovieDb getDatabase() {
        return database;
    }
}
