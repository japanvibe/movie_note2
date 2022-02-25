package com.example.movie_note.database;

import androidx.room.TypeConverter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Converters {
    @TypeConverter
    public String fromGenres(int[] genres) {
        return new Gson().toJson(genres);
    }

    @TypeConverter
    public int[] toGenres(String data) {
        Type listType = new TypeToken<int[]>() {}.getType();
        return new Gson().fromJson(data,listType);
    }
}
