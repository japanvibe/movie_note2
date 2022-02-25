package com.example.movie_note.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.movie_note.entity.Movie;

import java.util.List;

@Dao
public interface MovieDao {
    @Insert
    void insert(Movie movie);

    @Delete
    void delete(Movie movie);

    @Query("DELETE FROM Movie WHERE movieId=:id")
    void deleteById(long id);

    @Query("SELECT * FROM Movie")
    List<Movie> getMovies();

}
