package com.example.movie_note.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.movie_note.R;
import com.example.movie_note.entity.Genre;

import java.util.List;

public class GenreGridAdapter extends BaseAdapter {
    private List<Genre> genres;
    private LayoutInflater inflater;


    public GenreGridAdapter(Context context, List<Genre> genres) {
        this.genres=genres;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return genres.size();
    }

    @Override
    public Object getItem(int i) {
        return genres.get(i);
    }

    @Override
    public long getItemId(int i) {
        return genres.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.genres_item,null);
        TextView tvGenre=view.findViewById(R.id.tvGenre);
        tvGenre.setText(genres.get(i).getName());
        return view;
    }
}
