package com.example.movie_note;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.movie_note.adapters.MovieGridAdapter;
import com.example.movie_note.api.ApiData;
import com.example.movie_note.api.MovieApi;
import com.example.movie_note.api.NetworkService;
import com.example.movie_note.database.App;
import com.example.movie_note.database.MovieDao;
import com.example.movie_note.entity.Genre;
import com.example.movie_note.entity.GenresArray;
import com.example.movie_note.entity.Movie;
import com.example.movie_note.entity.Result;
import com.example.movie_note.modal.Details;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static MovieDao movieDao;
    private static MovieApi movieApi;
    private static List<Genre> allGenres;
    private GridView gvMovies;
    private MovieGridAdapter adapter;
    private List<Movie> movieList;
    private Button btnFind;
    private Button btnPrev;
    private Button btnNext;
    private EditText etSearch;
    private int page;
    private Details details;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private int totalPages;
    private Toolbar toolbar;
    private boolean trendingSearch=true;
    private boolean genresSearch;
    private String[] stringGenres;
    private List<Genre> filteredGenres;
    private String strGenres;
    private AlertDialog.Builder builder;
    private AlertDialog genresDialog;
    private boolean[] clickedGenres;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initElements();
        Call<Result> call = movieApi.getTrending(
                ApiData.getApiKey(),ApiData.getLangRu(),String.valueOf(page));
        sendRequest(call);
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        List<Movie> dbMovies=movieDao.getMovies();
        for (Movie movie : movieList) {
            movie.setFavorite(false);
            for (Movie movie1 : movieDao.getMovies()) {
                if(movie.getMovieId()==movie1.getMovieId())
                    movie.setFavorite(true);
            }
        }
        adapter.notifyDataSetChanged();
    }
    private void initElements(){
        movieApi= NetworkService.getInstance().getMovieApi();
        page=ApiData.getInitialPage();
        movieDao = App.getInstance().getDatabase().movieDao();
        movieList=new ArrayList<>();
        allGenres=new ArrayList<>();
        filteredGenres=new ArrayList<>();
        getGenres();
        etSearch=findViewById(R.id.etSearch);
        btnFind=findViewById(R.id.btnFind);
        btnPrev=findViewById(R.id.btnPrev);
        btnNext=findViewById(R.id.btnNext);
        btnPrev.setOnClickListener(v->getTrending(v));
        btnNext.setOnClickListener(v->getTrending(v));
        btnFind.setOnClickListener(this::findMovies);
        gvMovies=findViewById(R.id.gvMovies);
        drawerLayout=findViewById(R.id.drawerLayout);
        navigationView=findViewById(R.id.drawer);
        toolbar=findViewById(R.id.tbAppbar);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle actionBarDrawerToggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        details=new Details(MainActivity.this);
    }
    private void getTrending(View view){
        trendingSearch=true;
        if(view.getId()==R.id.btnPrev&&page>1) page--;
        if(view.getId()==R.id.btnNext&&page<totalPages) page++;
        if(page>0&&page<=totalPages) {
        Call<Result> call = movieApi.getTrending(
                ApiData.getApiKey(),ApiData.getLangRu(),String.valueOf(page));
        sendRequest(call);
        }
    }
    private void getMoviesByGenres(View view){
        genresSearch=true;
        if(view.getId()==R.id.btnPrev&&page>1) page--;
        if(view.getId()==R.id.btnNext&&page<totalPages) page++;
        if(page>0&&page<=totalPages) {
            Call<Result> call = movieApi.getMoviesByGenres(
                    ApiData.getApiKey(),strGenres,ApiData.getLangRu(),String.valueOf(page));
            sendRequest(call);
        }
    }
    private void findMovies(View view) {
        if(trendingSearch||genresSearch) {
            trendingSearch = false;
            genresSearch=false;
            page=ApiData.getInitialPage();
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
    private void getGenres(){
            Call<GenresArray> genresArrayCall=movieApi.getGenres(ApiData.getApiKey(),ApiData.getLangRu());
            genresArrayCall.enqueue(new Callback<GenresArray>() {
                @Override
                public void onResponse(Call<GenresArray> call, Response<GenresArray> response) {
                    if(response.isSuccessful())
                        allGenres=response.body().getGenres();
                    stringGenres=new String[allGenres.size()];
                    for (int i = 0; i < allGenres.size(); i++) {
                        stringGenres[i]=allGenres.get(i).getName();
                    }
                    clickedGenres=new boolean[stringGenres.length];
                }
                @Override
                public void onFailure(Call<GenresArray> call, Throwable t) {
                }
            });
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
                            for (Movie movie1 : movieDao.getMovies()) {
                                if(movie.getMovieId()==movie1.getMovieId())
                                    movie.setFavorite(true);
                            }
                        }
                        adapter = new MovieGridAdapter(MainActivity.this, movieList, false);
                        gvMovies.setAdapter(adapter);
                        details.setMovieList(movieList);
                        details.setAllGenres(allGenres);
                        gvMovies.setOnItemClickListener(details.setListener());
                    }
            }
            @Override
            public void onFailure(Call<Result> call, Throwable t) {

            }
        });
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_favorite:
                Intent intent=new Intent(MainActivity.this,FavoritesActivity.class);
                startActivity(intent);
                drawerLayout.close();
                break;
            case R.id.nav_trending:
                trendingSearch=true;
                btnNext.setOnClickListener(v->getTrending(v));
                btnPrev.setOnClickListener(v->getTrending(v));
                closeKeyboard();
                drawerLayout.close();
                page=ApiData.getInitialPage();
                Call<Result> call = movieApi.getTrending(
                        ApiData.getApiKey(),ApiData.getLangRu(),String.valueOf(page));
                sendRequest(call);
                break;
            case R.id.nav_filters:
                filteredGenres.clear();
                closeKeyboard();
                drawerLayout.close();
                builder=new AlertDialog.Builder(MainActivity.this);
                builder.setPositiveButton(R.string.button_find, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        genresSearch = true;
                        btnNext.setOnClickListener(v -> getMoviesByGenres(v));
                        btnPrev.setOnClickListener(v -> getMoviesByGenres(v));
                        page = ApiData.getInitialPage();
                        List<String> filteredGenresStr=new ArrayList<>();
                        for (int j = 0; j < filteredGenres.size(); j++) {
                            filteredGenresStr.add(String.valueOf(filteredGenres.get(j).getId()));
                        }
                        System.out.println(filteredGenresStr);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            strGenres = filteredGenresStr.stream().map(Object::toString).collect(Collectors.joining(","));
                            System.out.println(strGenres);
                            Call<Result> call = movieApi.getMoviesByGenres(
                                    ApiData.getApiKey(), strGenres, ApiData.getLangRu(), String.valueOf(page));
                            sendRequest(call);
                        }
                    }
                }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        genresDialog.cancel();
                    }
                }).setTitle(R.string.choose_genres).setMultiChoiceItems(stringGenres, clickedGenres, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                        clickedGenres[i] = b;
                        filteredGenres.add(allGenres.get(i));
                    }
                });
                genresDialog=builder.create();
                genresDialog.show();
                break;
        }
        return false;
    }
    public static MovieDao getMovieDao() {
        return movieDao;
    }
    public static MovieApi getMovieApi() {
        return movieApi;
    }
    public static List<Genre> getAllGenres() {
        return allGenres;
    }
}