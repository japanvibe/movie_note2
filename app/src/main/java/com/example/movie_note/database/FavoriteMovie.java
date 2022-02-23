package com.example.movie_note.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class FavoriteMovie {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String title;
    private long movieId;

    public FavoriteMovie(String title, long movieId) {
        this.title = title;
        this.movieId = movieId;
    }

    public FavoriteMovie() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getMovieId() {
        return movieId;
    }

    public void setMovieId(long movieId) {
        this.movieId = movieId;
    }

    @Override
    public String toString() {
        return "FavoriteMovie{" +
                "title='" + title + '\'' +
                ", movieId=" + movieId +
                '}';
    }
}
