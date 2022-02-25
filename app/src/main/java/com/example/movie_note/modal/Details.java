package com.example.movie_note.modal;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.movie_note.R;
import com.example.movie_note.adapters.GenreGridAdapter;
import com.example.movie_note.api.ApiData;
import com.example.movie_note.entity.Genre;
import com.example.movie_note.entity.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Details {
    private Dialog details;
    private ScrollView svDetailsScrollView;
    private ImageView ivDetailsPoster;
    private TextView tvDetailsTitle;
    private TextView tvDetailsOverview;
    private GridView gvGenres;
    private List<Movie> movieList;
    private List<Genre> allGenres;
    private List<Genre> movieGenres;

    public Details(Context context) {
        details=new Dialog(context);
        details.setContentView(R.layout.details_dialog);
        svDetailsScrollView=details.findViewById(R.id.svDetailsScrollView);
        ivDetailsPoster=details.findViewById(R.id.ivDetailsPoster);
        gvGenres=details.findViewById(R.id.gvGenres);
        tvDetailsTitle=details.findViewById(R.id.tvDetailsTitle);
        tvDetailsOverview=details.findViewById(R.id.tvDetailsOverview);
        movieGenres=new ArrayList<>();
    }

    public void setMovieList(List<Movie> movieList) {
        this.movieList = movieList;
    }

    public void setAllGenres(List<Genre> allGenres) {
        this.allGenres = allGenres;
    }

    public AdapterView.OnItemClickListener setListener(){
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                svDetailsScrollView.scrollTo(0,0);
                            Picasso.get().load(ApiData.getPosterBaseUrl()+movieList.get(i).getImageUrl()).error(R.drawable.no_poster).into(ivDetailsPoster);
                            movieGenres.clear();
                            for (Genre genre : allGenres) {
                                for (int id : movieList.get(i).getGenres()) {
                                    if(genre.getId()==id)movieGenres.add(genre);
                                }
                            }
                            GenreGridAdapter genreGridAdapter=new GenreGridAdapter(adapterView.getContext(), movieGenres);
                            gvGenres.setAdapter(genreGridAdapter);
                            tvDetailsTitle.setText(movieList.get(i).getTitle());
                            if(!movieList.get(i).getOverview().isEmpty()) {
                                tvDetailsOverview.setText(movieList.get(i).getOverview());
                            }
                            else tvDetailsOverview.setText("Нет описания");
                            details.show();
                        }
        };
    }
}
