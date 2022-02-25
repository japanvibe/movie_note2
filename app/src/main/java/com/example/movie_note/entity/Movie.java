package com.example.movie_note.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
@Entity
public class Movie implements Serializable {
    @PrimaryKey(autoGenerate = false)
    @SerializedName("id")
    private long movieId;

    @SerializedName("title")
    private String title;

    @SerializedName("poster_path")
    private String imageUrl;

    @SerializedName("overview")
    private String overview;

    @SerializedName("vote_average")
    private float rating;

    @SerializedName("release_date")
    private String date;

    @SerializedName("genre_ids")
    private int[] genres;

    private boolean favorite;

    public Movie(long movieId, String title, String imageUrl, String overview, float rating, String date, int[] genres) {
        this.movieId = movieId;
        this.title = title;
        this.imageUrl = imageUrl;
        this.overview = overview;
        this.rating = rating;
        this.date = date;
        this.genres=genres;
        this.favorite=false;
    }


    public long getMovieId() {
        return movieId;
    }

    public void setMovieId(long movieId) {
        this.movieId = movieId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int[] getGenres() {
        return genres;
    }

    public void setGenres(int[] genres) {
        this.genres = genres;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "title='" + title + '\'' +
                ", favorite=" + favorite +
                '}';
    }
}
