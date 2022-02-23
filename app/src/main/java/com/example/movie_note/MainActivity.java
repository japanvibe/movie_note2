package com.example.movie_note;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.movie_note.adapters.MovieGridAdapter;
import com.example.movie_note.api.ApiData;
import com.example.movie_note.api.MovieApi;
import com.example.movie_note.api.NetworkService;
import com.example.movie_note.database.App;
import com.example.movie_note.database.FavoriteMovie;
import com.example.movie_note.database.MovieDao;
import com.example.movie_note.entity.Genre;
import com.example.movie_note.entity.GenresArray;
import com.example.movie_note.entity.Movie;
import com.example.movie_note.entity.Result;
import com.example.movie_note.modal.Details;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private GridView gvMovies;
    private MovieGridAdapter adapter;
    private List<Movie> movieList;
    private List<Genre> allGenres;
    private Button btnFind;
    private Button btnPrev;
    private Button btnNext;
    private EditText etSearch;
    private MovieApi movieApi;;
    private int page;
    private Details details;
    public static MovieDao movieDao;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private int totalPages;
    private Toolbar toolbar;
    private boolean trendingSearch=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initElements();
        Call<Result> call = movieApi.getTrending(
                ApiData.getApiKey(),ApiData.getLangRu(),String.valueOf(page));
        sendRequest(call);
    }
    private void initElements(){
        movieApi= NetworkService.getInstance().getMovieApi();
        page=ApiData.getInitialPage();
        movieDao = App.getInstance().getDatabase().movieDao();
        movieList=new ArrayList<>();
        allGenres=new ArrayList<>();
        getAllGenres();
        etSearch=findViewById(R.id.etSearch);
        btnFind=findViewById(R.id.btnFind);
        btnPrev=findViewById(R.id.btnPrev);
        btnNext=findViewById(R.id.btnNext);
        if(trendingSearch){
            btnNext.setOnClickListener(v->getTrending(v));
            btnPrev.setOnClickListener(v->getTrending(v));
        }
        else {
            btnNext.setOnClickListener(this::findMovies);
            btnPrev.setOnClickListener(this::findMovies);
        }
        btnFind.setOnClickListener(this::findMovies);
        gvMovies=findViewById(R.id.gvMovies);
        drawerLayout=findViewById(R.id.drawerLayout);
        navigationView=findViewById(R.id.drawer);
        toolbar=findViewById(R.id.tbAppbar);
        ActionBarDrawerToggle actionBarDrawerToggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
}
    private void getTrending(View view){
        if(view.getId()==R.id.btnPrev&&page>1) page--;
        if(view.getId()==R.id.btnNext&&page<totalPages) page++;
        if(page>0&&page<=totalPages) {
        Call<Result> call = movieApi.getTrending(
                ApiData.getApiKey(),ApiData.getLangRu(),String.valueOf(page));
        sendRequest(call);
        }
    }
    private void findMovies(View view) {
        if(trendingSearch) {
            trendingSearch = false;
            page=1;
            btnNext.setOnClickListener(this::findMovies);
            btnPrev.setOnClickListener(this::findMovies);
        }
        closeKeyboard();
        if(view.getId()==R.id.btnPrev&&page>1) page--;
        if(view.getId()==R.id.btnNext&&page<totalPages) page++;
        if(page>0&&page<=totalPages) {
            Call<Result> call = movieApi.getMoviesByTitle(
                    ApiData.getApiKey(),
                    ApiData.getLangRu(),
                    ApiData.getAdult(),
                    String.valueOf(page),
                    etSearch.getText().toString());
            sendRequest(call);
        }
    }
    private void closeKeyboard(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void getAllGenres(){
        if(allGenres.isEmpty()){
            Call<GenresArray> genresArrayCall=movieApi.getGenres(ApiData.getApiKey(),ApiData.getLangRu());
            genresArrayCall.enqueue(new Callback<GenresArray>() {
                @Override
                public void onResponse(Call<GenresArray> call, Response<GenresArray> response) {
                    if(response.isSuccessful())
                        allGenres=response.body().getGenres();
                }

                @Override
                public void onFailure(Call<GenresArray> call, Throwable t) {
                }
            });
        }
    }

    private void sendRequest(Call call){
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                if(response.isSuccessful()){
                    movieList.clear();
                    movieList=response.body().getMovieList();
                    totalPages=response.body().getTotalPages();
                        for (Movie movie : movieList) {
                            for (FavoriteMovie movie1 : movieDao.getMovies()) {
                                if(movie.getMovieId()==movie1.getMovieId())
                                    movie.setFavorite(true);
                            }
                        }
                        adapter = new MovieGridAdapter(MainActivity.this, movieList);
                        gvMovies.setAdapter(adapter);
                        details=new Details(MainActivity.this,movieList,allGenres);
                        gvMovies.setOnItemClickListener(details.setListener());
                    }
            }
            @Override
            public void onFailure(Call<Result> call, Throwable t) {

            }
        });
    }
}