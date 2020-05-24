package edu.uci.ics.fabflixmobile;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.android.volley.Response;

import java.util.ArrayList;

    public class SingleMovieAdapter extends ArrayAdapter<MovieItem>
    {
        private ArrayList<MovieItem> movies;

        public SingleMovieAdapter(ArrayList<MovieItem> movies, Response.Listener<String> context)
        {
            super((Context) context, R.layout.singlemovie, movies);
            this.movies = movies;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
        {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View view = inflater.inflate(R.layout.row, parent, false);

            MovieItem movie = movies.get(position);

            TextView titleView = view.findViewById(R.id.title);
            TextView yearView = view.findViewById(R.id.year);
            TextView movieidView = view.findViewById(R.id.movieid);
            TextView directorView = view.findViewById(R.id.director);
            TextView genreView = view.findViewById(R.id.genres);
            TextView starsView = view.findViewById(R.id.stars);
            TextView ratingView = view.findViewById(R.id.rating);
            TextView pricingView = view.findViewById(R.id.pricing);
            titleView.setText(movie.getTitle());
            yearView.setText(movie.getYear());
            movieidView.setText(movie.getMovieid());
            directorView.setText(movie.getDirector());
            genreView.setText(movie.getMovie_genres());
            starsView.setText(movie.getMovie_stars());
            ratingView.setText(movie.getRating());
            pricingView.setText(movie.getPrice());
            return view;
        }
    }
