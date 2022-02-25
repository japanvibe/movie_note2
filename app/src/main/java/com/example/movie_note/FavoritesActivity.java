package com.example.movie_note;

import android.os.Bundle;
import android.widget.GridView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.movie_note.adapters.MovieGridAdapter;
import com.example.movie_note.entity.Movie;
import com.example.movie_note.modal.Details;

import java.util.ArrayList;
import java.util.List;

public class FavoritesActivity extends AppCompatActivity {
    private TextView tvHeader;
    private GridView gvFavoriteMovies;
    private List<Movie> favoriteMovies;
    private MovieGridAdapter adapter;
    private Details details;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        initElements();
    }
    private void initElements(){
        gvFavoriteMovies=findViewById(R.id.gvFavoriteMovies);
        favoriteMovies=new ArrayList<>();
        favoriteMovies=MainActivity.getMovieDao().getMovies();
        details=new Details(FavoritesActivity.this);
        adapter = new MovieGridAdapter(FavoritesActivity.this, favoriteMovies, true);
        gvFavoriteMovies.setAdapter(adapter);
        details.setMovieList(favoriteMovies);
        details.setAllGenres(MainActivity.getAllGenres());
        gvFavoriteMovies.setOnItemClickListener(details.setListener());
    }
}
