package com.example.movie_note.api;

import com.example.movie_note.entity.GenresArray;
import com.example.movie_note.entity.Movie;
import com.example.movie_note.entity.Result;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieApi {
    @GET("search/movie")
    Call<Result> getMoviesByTitle(@Query("api_key") String apiKey,
                                  @Query("language") String lang,
                                  @Query("adult") String adult,
                                  @Query("page") String page,
                                  @Query("query") String title);
    @GET("genre/movie/list")
    Call<GenresArray> getGenres(@Query("api_key") String apiKey,
                                @Query("language") String lang);

    @GET("trending/movie/week")
    Call<Result> getTrending(@Query("api_key") String apiKey,
                             @Query("language") String lang,
                             @Query("page") String page);

    @GET("movie/{movie_id}")
    Call<Movie> getMovieById(@Path("movie_id") String movieId,
                             @Query("api_key") String apiKey,
                             @Query("language") String lang);

    @GET("discover/movie")
    Call<Result> getMoviesByGenres(@Query("api_key") String apiKey,
                                  @Query("with_genres") String genres,
                                  @Query("language") String lang,
                                  @Query("page") String page);
}
