package com.example.movie_note.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MovieDao {
    @Insert
    void insert(FavoriteMovie movie);

    @Delete
    void delete(FavoriteMovie movie);

    @Query("DELETE FROM FavoriteMovie WHERE movieId=:id")
    void deleteById(long id);

    @Query("SELECT * FROM FavoriteMovie")
    List<FavoriteMovie> getMovies();

}
