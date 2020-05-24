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
        //movies.add(new MovieItem("The Terminal", "" 2004));
        // movies.add(new MovieItem("The Final Season", (short) 2007));
        Intent moviePage = getIntent();
        String movieLst = moviePage.getStringExtra("movielist");
        JSONArray jsonArr = null;
        try {
            jsonArr = new JSONArray(movieLst);
        } catch (JSONException e) {
            Log.d("parse movie array", String.valueOf(e.getStackTrace()));
        }
        if (jsonArr != null) {
            System.out.println("JSON ARRAY HELLO " + jsonArr);
            for (int i = 0; i < jsonArr.length(); i++) {
                MovieItem mItem = new MovieItem();
                try {
                    JSONObject jObj = (JSONObject) (jsonArr.get(i));
                    mItem.setTitle(jObj.getString("movie_title"));
                    mItem.setDirector(jObj.getString("movie_director"));
                    mItem.setMovie_genres(jObj.getString("movie_genres"));
                    mItem.setMovie_stars(jObj.getString("movie_stars"));
                    mItem.setMovieid(jObj.getString("movie_id"));
                    mItem.setYear(jObj.getString("movie_year"));
                    mItem.setRating(jObj.getString("movie_rating"));
                    mItem.setPrice(jObj.getString("price"));
                    System.out.println("JSON OBJECT " + jsonArr.get(i));
                    movies.add(mItem);
                } catch (JSONException e) {
                    e.printStackTrace();
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
                        Intent singleMoviePage = new Intent(ListViewActivity.this, SingleMovieActivity.class);
                        singleMoviePage.putExtra("movieid", movie.getMovieid());
                        startActivity(singleMoviePage);

                    }
                });
            }
        }
    }}