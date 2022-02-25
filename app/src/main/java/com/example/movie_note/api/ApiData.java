package com.example.movie_note.api;

public class ApiData {

    private static final String BASE_API_URL="https://api.themoviedb.org/3/";
    private static final String LANG_RU="ru-RU";
    private static final String ADULT="false";
    private static final String POSTER_BASE_URL="https://image.tmdb.org/t/p/w600_and_h900_bestv2";
    private static final int INITIAL_PAGE=1;

    public static String getApiKey(){
        return API_KEY;
    }

    public static String getPosterBaseUrl(){
        return POSTER_BASE_URL;
    }

    public static String getLangRu(){
        return LANG_RU;
    }

    public static String getAdult() {
        return ADULT;
    }

    public static String getBaseApiUrl() { return BASE_API_URL; }

    public static int getInitialPage() {
        return INITIAL_PAGE;
    }
}
