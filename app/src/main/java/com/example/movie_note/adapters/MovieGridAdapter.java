package com.example.movie_note.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.movie_note.MainActivity;
import com.example.movie_note.R;
import com.example.movie_note.api.ApiData;
import com.example.movie_note.database.FavoriteMovie;
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
    public MovieGridAdapter(Context context, List<Movie> movies) {
        this.movies=movies;
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
        if(movies.get(i).isFavorite())btnAddRemove.setText("удалить");
        else btnAddRemove.setText("добавить");
        btnAddRemove.setOnClickListener(v->{
            if(!movies.get(i).isFavorite()) {
                MainActivity.movieDao.insert(new FavoriteMovie(movies.get(i).getTitle(),movies.get(i).getMovieId()));
                movies.get(i).setFavorite(true);
                btnAddRemove.setText("удалить");
                toast = Toast.makeText(inflater.getContext(),
                        "Фильм добавлен в избранные", Toast.LENGTH_SHORT);
            } else {
                MainActivity.movieDao.deleteById(movies.get(i).getMovieId());
                movies.get(i).setFavorite(false);
                btnAddRemove.setText("add");
                toast = Toast.makeText(inflater.getContext(),
                        "Фильм удален из избранных", Toast.LENGTH_SHORT);
            }
            toast.show();
            notifyDataSetChanged();
        });
        return view;
    }
}
