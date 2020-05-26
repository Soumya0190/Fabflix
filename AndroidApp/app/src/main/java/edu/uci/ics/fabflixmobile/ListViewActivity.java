package edu.uci.ics.fabflixmobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class ListViewActivity extends Activity
{
    private int totalPg = 0;
    private int currentPg = 0;
    private int numOfRecordsPerPg = 20;
    private int itemsRemaining =0;
    Button nextBtn, prevBtn; ListView movieListView; TextView msg;
    JSONArray jsonArr = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview);

        //movieListView = (ListView) findViewById(R.id.list);
        nextBtn = (Button)findViewById(R.id.nextBtn);
        prevBtn = (Button)findViewById(R.id.prevBtn);
        prevBtn.setEnabled(false);
        msg = (TextView)findViewById(R.id.lstmessage);
        ListView listView = findViewById(R.id.list);

        Intent moviePage = getIntent();
        String movieLst = moviePage.getStringExtra("movielist");

        try {
            jsonArr = new JSONArray(movieLst);
        } catch (JSONException e) {
            Log.d("parse movie array", String.valueOf(e.getStackTrace()));
        }
        if (jsonArr != null) {

            totalPg = (int) jsonArr.length() / numOfRecordsPerPg;
            itemsRemaining = jsonArr.length() % numOfRecordsPerPg;
            if (itemsRemaining >0) totalPg = totalPg + 1;
            currentPg = 1;
            ArrayList<MovieItem> movies =  generatePage(currentPg, numOfRecordsPerPg, totalPg, itemsRemaining);
            MovieListViewAdapter adapter = new MovieListViewAdapter(movies, this);

            listView.setAdapter(adapter);
            // listView.setAdapter(new ArrayAdapter<String>(ListViewActivity.this, ));

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    MovieItem movie = movies.get(position);

                    Log.d("list view activity: movieid = ", movie.getMovieid());
                    Intent singleMoviePage = new Intent(ListViewActivity.this, SingleMovieActivity.class);
                    singleMoviePage.putExtra("movieid", movie.getMovieid());

                    startActivity(singleMoviePage);

                }
            });

            nextBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    currentPg += 1;
                    ArrayList<MovieItem> movies =  generatePage(currentPg, numOfRecordsPerPg, totalPg, itemsRemaining);
                    MovieListViewAdapter adapter = new MovieListViewAdapter(movies, ListViewActivity.this);
                    ListView listView = findViewById(R.id.list);
                    listView.setAdapter(adapter);
                    SetButtonStatus(currentPg, totalPg);
                }
            });
            prevBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    currentPg -= 1;
                    ArrayList<MovieItem> movies =  generatePage(currentPg, numOfRecordsPerPg, totalPg, itemsRemaining);
                    MovieListViewAdapter adapter = new MovieListViewAdapter(movies, ListViewActivity.this);
                    ListView listView = findViewById(R.id.list);
                    listView.setAdapter(adapter);
                    SetButtonStatus(currentPg, totalPg);
                }
            });

        }

    } //else msg.setText("No Data Found");




    public ArrayList<MovieItem> generatePage(int currentPage, int itemsPerPage, int lastpage, int itemsRemaining) {
        ArrayList<MovieItem> newMovieLst = new ArrayList<>();
        int startItem = (currentPage-1) * itemsPerPage;
        int lastItem = 0;

        if (currentPage == lastpage && itemsRemaining > 0) lastItem = startItem + itemsRemaining;
        else lastItem = startItem + numOfRecordsPerPg;

        if (jsonArr != null) {
            for (int i = startItem; i < lastItem; i++) {
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
                    if (genres == null) genres = "";
                    genres = genres.replace("[", "").replace("]", "");
                    mItem.setMovie_genres(genres);

                    String stars = jObj.getString("movie_stars");
                    if (stars != null && stars.length() > 0) {
                        stars = stars.replace("[", "").replace("]", "");
                        String[] starArr = stars.split(",");
                        for (int x = 1; x < starArr.length; x = x + 2) {
                            if (x == 1) stars = starArr[x];
                            else stars = stars + ", " + starArr[x];
                        }
                    } else stars = "";

                    mItem.setMovie_stars(stars);
                    newMovieLst.add(mItem);
                } catch (JSONException e) {
                    //e.printStackTrace();
                    Log.d("JSON parse movie array", String.valueOf(e.getStackTrace()));
                }
            }
        }
        return newMovieLst;
    }


    private void SetButtonStatus(int currentPg, int totalPg) {
        prevBtn.setEnabled(true);
        nextBtn.setEnabled(true);
        if (currentPg == 1) prevBtn.setEnabled(false);
        if ((totalPg == 1) ||(currentPg == totalPg)) nextBtn.setEnabled(false);

    }

}