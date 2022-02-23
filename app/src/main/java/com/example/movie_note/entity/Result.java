package com.example.movie_note.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Result implements Serializable {
    @SerializedName("results")
    private List<Movie> movieList;

    @SerializedName("total_pages")
    private int totalPages;

    public Result(List<Movie> movieList, int totalPages) {
        this.movieList = movieList;
        this.totalPages = totalPages;
    }

    public List<Movie> getMovieList() {
        return movieList;
    }

    public void setMovieList(List<Movie> movieList) {
        this.movieList = movieList;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
