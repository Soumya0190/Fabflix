package edu.uci.ics.fabflixmobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class ListViewActivity extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview);
        ArrayList<MovieItem> movies = new ArrayList<>();

        Intent moviePage = getIntent();
        String movieLst = moviePage.getStringExtra("movielist");
        JSONArray jsonArr = null;
        try {
            jsonArr = new JSONArray(movieLst);
        } catch (JSONException e) {
            Log.d("parse movie array", String.valueOf(e.getStackTrace()));
        }
        if (jsonArr != null) {
            for (int i = 0; i < jsonArr.length(); i++) {
                MovieItem mItem = new MovieItem();
                try {
                    JSONObject jObj = (JSONObject) (jsonArr.get(i));
                    mItem.setTitle(jObj.getString("movie_title"));
                    mItem.setDirector(jObj.getString("movie_director"));
                    mItem.setMovieid(jObj.getString("movie_id"));
                    mItem.setYear(jObj.getString("movie_year"));
                    mItem.setRating(jObj.getString("movie_rating"));
                    mItem.setPrice(jObj.getString("price"));

                    String genres = jObj.getString("movie_genres");
                    if (genres == null) genres ="";
                    genres = genres.replace("[", "");
                    genres = genres.replace("]", "");
                    mItem.setMovie_genres(genres);
                    String stars = jObj.getString("movie_stars");

                    if (stars != null && stars.length()>0) {
                        stars = stars.replace("[", "");
                        stars = stars.replace("]", "");
                        String[] starArr = stars.split(",");
                        for (int x=1; x < starArr.length; x=x+2)
                        {
                            if (x==1) stars = starArr[x];
                            else stars = stars + ", " + starArr[x];
                        }
                    } else stars ="";

                    mItem.setMovie_stars(stars);
                    movies.add(mItem);
                } catch (JSONException e) {
                    //e.printStackTrace();
                    Log.d("JSON parse movie array", String.valueOf(e.getStackTrace()));
                }

                MovieListViewAdapter adapter = new MovieListViewAdapter(movies, this);

                ListView listView = findViewById(R.id.list);
                listView.setAdapter(adapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        MovieItem movie = movies.get(position);
                        //  String message = String.format("Clicked on position: %d, name: %s, %d", position, movie.getTitle(), movie.getYear());
                        //Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        Log.d("list view activity: movieid = ",movie.getMovieid());
                        Intent singleMoviePage = new Intent(ListViewActivity.this, SingleMovieActivity.class);
                        singleMoviePage.putExtra("movieid", movie.getMovieid());

                        startActivity(singleMoviePage);

                    }
                });
            }
        }
    }}