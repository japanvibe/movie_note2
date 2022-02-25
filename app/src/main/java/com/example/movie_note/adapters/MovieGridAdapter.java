package com.example.movie_note.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.movie_note.MainActivity;
import com.example.movie_note.R;
import com.example.movie_note.api.ApiData;
import com.example.movie_note.entity.Movie;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MovieGridAdapter extends BaseAdapter {
    private List<Movie> movies;
    private LayoutInflater inflater;
    private Toast toast;
    private boolean favorites;
    public MovieGridAdapter(Context context, List<Movie> movies, boolean favorites) {
        this.movies=movies;
        this.favorites=favorites;
        inflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return movies.size();
    }

    @Override
    public Object getItem(int i) {
        return movies.get(i);
    }

    @Override
    public long getItemId(int i) {
        return movies.get(i).getMovieId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.movie_item,null);
        ImageView ivPoster = view.findViewById(R.id.ivPoster);
        TextView tvTitle=view.findViewById(R.id.tvTitle);
        TextView tvDate=view.findViewById(R.id.tvDate);
        TextView tvRating=view.findViewById(R.id.tvRating);
        Button btnAddRemove=view.findViewById(R.id.btnAddRemove);
        Picasso.get().load(ApiData.getPosterBaseUrl()+movies.get(i).getImageUrl()).error(R.drawable.no_poster).into(ivPoster);
        tvTitle.setText(movies.get(i).getTitle());
        DateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
        Date dateObject=new Date();
        try {
            dateObject = sdf.parse(movies.get(i).getDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        sdf=new SimpleDateFormat("yyyy");
        tvDate.setText(sdf.format(dateObject));
        tvRating.setText(String.valueOf(movies.get(i).getRating()));
        if(movies.get(i).isFavorite()) {
            btnAddRemove.setText(R.string.remove);
            btnAddRemove.setBackgroundColor(Color.MAGENTA);
        }
        else btnAddRemove.setText(R.string.add);
        btnAddRemove.setOnClickListener(v->{
            if(!movies.get(i).isFavorite()) {
                movies.get(i).setFavorite(true);
                MainActivity.getMovieDao().insert(movies.get(i));
                btnAddRemove.setText(R.string.remove);
                toast = Toast.makeText(inflater.getContext(),
                        R.string.add_movie, Toast.LENGTH_SHORT);
            } else {
                MainActivity.getMovieDao().deleteById(movies.get(i).getMovieId());
                movies.get(i).setFavorite(false);
                if(favorites)movies.remove(i);
                toast = Toast.makeText(inflater.getContext(),
                        R.string.remove_movie, Toast.LENGTH_SHORT);
            }
            toast.show();
            notifyDataSetChanged();
        });
        return view;
    }
}
