package edu.uci.ics.fabflixmobile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
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
        setContentView(R.layout.singlemovieview);


        mID = findViewById(R.id.sMovieid);
        message = findViewById(R.id.smmessage);
        navSrchBtn = findViewById(R.id.navSrchBtn);
        Intent moviePage = getIntent();
        String movieId= moviePage.getStringExtra("movieid");
        // mID.setText(movieId);

        //   url="https://10.0.2.2:8443/cs122b_spring20_team_3_war_exploded/api/single-movie?id="+movieId;
        url ="https://ec2-18-222-19-74.us-east-2.compute.amazonaws.com:8443/cs122b-spring20-team-3/api/single-movie?id="+movieId;
        // url = "http://10.0.2.2:8080/cs122b_spring20_team_3_war_exploded/api/single-movie?id="+movieId;
        //url = "https://10.0.2.2:8443/122bwebproject_war/api/single-movie?id="+movieId;

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
        final StringRequest singleMovieRequest = new StringRequest(Request.Method.GET, url , new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                //TODO should parse the json response to redirect to appropriate function

                try
                {
                    JSONObject jObj = new JSONObject(response);

                    Log.d("parse movie data", response);
                    MovieItem mItem = new MovieItem();
                    TextView titleView = findViewById(R.id.smtitle);
                    TextView yearView = findViewById(R.id.smyear);
                    // TextView movieidView = view.findViewById(R.id.movieid);
                    TextView directorView = findViewById(R.id.smdirector);
                    TextView genreView = findViewById(R.id.smgenres);
                    TextView starsView = findViewById(R.id.smstars);
                    //TextView ratingView = view.findViewById(R.id.rating);
                    // TextView pricingView = view.findViewById(R.id.pricing);
                    titleView.setText(jObj.getString("title"));
                    yearView.setText("Year :" + jObj.getString("year"));
                    // movieidView.setText(movie.getMovieid());
                    directorView.setText("Director :" +jObj.getString("director"));
                    String genres = jObj.getString("genres");
                    if (genres == null) genres ="";
                    genres = genres.replace("[", "");
                    genres = genres.replace("]", "");
                    genreView.setText("Genre :" + genres);

                    String stars = jObj.getString("stars");
                    if (stars != null && stars.length()>0) {
                        stars = stars.replace("[", "");
                        stars = stars.replace("]", "");
                        String[] starArr = stars.split(","); stars = "";
                        for (int x=1; x < starArr.length; x=x+2)
                        {
                            if (x==1) stars = starArr[x];
                            else stars = stars + ", " + starArr[x];
                        }
                    } else stars ="";
                    starsView.setText("Stars : "+ stars);
                }
                catch(Exception ex)
                {
                    Log.d("single movie.parsedata", String.valueOf(ex.getStackTrace()));
                    // message.setText("Error in displaying Single Movie");
                }
            }
        },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("single movie.error", error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                final Map<String, String> params = new HashMap<>();
                params.put("id", mID.getText().toString());
                //   params.put("searchTitle", mTitle.getText().toString());
                params.put("recordsPerPage", "20");
                Log.d("single movie params =" , params.toString());
                return params;
            }
        };
        queue.add(singleMovieRequest);
    }
}