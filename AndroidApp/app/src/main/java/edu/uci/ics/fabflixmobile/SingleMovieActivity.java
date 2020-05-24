package edu.uci.ics.fabflixmobile;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SingleMovieActivity extends ActionBarActivity
{

    private TextView message;
    private TextView mID;
    private Button navSrchBtn;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);


        mID = findViewById(R.id.movieid);
       message = findViewById(R.id.message);
        navSrchBtn = findViewById(R.id.navSrchBtn);
        Intent moviePage = getIntent();
        String movieId= moviePage.getStringExtra("movieid");
        mID.setText(movieId);

        url = "http://10.0.2.2:8080/cs122b_spring20_team_3_war_exploded/api/single-movie?id="+movieId;

        displayMovie();
        navSrchBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent srchPage = new Intent(SingleMovieActivity.this, Search.class);
                startActivity(srchPage);
            }
        });
    }

    public void displayMovie()
    {
        ArrayList<MovieItem> movies = new ArrayList<>();
        final RequestQueue queue = NetworkManager.sharedManager(this).queue;
        final StringRequest loginRequest = new StringRequest(Request.Method.POST, url , new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                //TODO should parse the json response to redirect to appropriate function

                try
                {
                    JSONObject jObj = new JSONObject(response);

                    Log.d("parse movie data", jObj.get("title").toString());
                    MovieItem mItem = new MovieItem();
                        mItem.setTitle(jObj.getString("movie_title"));
                        mItem.setDirector(jObj.getString("movie_director"));
                        mItem.setMovie_genres(jObj.getString("movie_genres"));
                        mItem.setMovie_stars(jObj.getString("movie_stars"));
                        mItem.setMovieid(jObj.getString("movie_id"));
                        mItem.setYear(jObj.getString("movie_year"));
                        mItem.setRating(jObj.getString("movie_rating"));
                        mItem.setPrice(jObj.getString("price"));

                        movies.add(mItem);
                    SingleMovieAdapter adapter = new SingleMovieAdapter(movies, this);

                    ListView listView = findViewById(R.id.smovielist);
                    listView.setAdapter(adapter);


                    //  System.out.println("JSON OBJECT " + jsonObj.get("movieArray"));
                    // Log.d("parse movie data", jsonObj.get("movieArray").toString());

                }
                catch(Exception ex)
                {
                    Log.d("search.parsedata", String.valueOf(ex.getStackTrace()));
                    message.setText("Error in displaying Single Movie");
                }



            }
        },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("search.error", error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                final Map<String, String> params = new HashMap<>();
                params.put("id", mID.getText().toString());
                //   params.put("searchTitle", mTitle.getText().toString());
                //params.put("recordsPerPage", "20");
                Log.d("single movie params =" , params.toString());
                return params;
            }
        };
        queue.add(loginRequest);
    }
}