package edu.uci.ics.fabflixmobile;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Search extends ActionBarActivity
{

    private EditText mTitle;
    private TextView message;
    private Button searchButton;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
        mTitle = findViewById(R.id.mTitle);
        message = findViewById(R.id.message);
        searchButton = findViewById(R.id.search);
        url = "http://10.0.2.2:8080/cs122b_spring20_team_3_war_exploded/api/movies";
       // url = "https://192.168.1.129:8443/cs122b_spring20_team_3_war_exploded/api/movies";

       // url="https://10.0.2.2:8443/cs122b_spring20_project2_login_cart_example_war_exploded/api/movies"; //good

        searchButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                search();
            }
        });
    }

    public void search()
    {
        message.setText("Searching Movie");
        final RequestQueue queue = NetworkManager.sharedManager(this).queue;
        final StringRequest loginRequest = new StringRequest(Request.Method.POST, url , new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                //TODO should parse the json response to redirect to appropriate functions.
                Log.d("search.success", response);
                //  Intent listPage = new Intent(Login.this, ListViewActivity.class);
                // startActivity(listPage);
                Intent listPage = new Intent(Search.this, ListViewActivity.class);
                //Gson g = new Gson();
                try
                {
                    JSONObject jsonObj = new JSONObject(response);
                    System.out.println("JSON OBJECT " + jsonObj.get("movieArray"));
                    Log.d("parse movie data", jsonObj.get("movieArray").toString());
                    listPage.putExtra("movielist", jsonObj.get("movieArray").toString());
                }
                catch(Exception ex)
                {
                    Log.d("search.parsedata", String.valueOf(ex.getStackTrace()));
                }


                startActivity(listPage);
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
                params.put("ftMovieTitle", mTitle.getText().toString());
             //   params.put("searchTitle", mTitle.getText().toString());
                params.put("recordsPerPage", "20");
                Log.d("movie params =" , params.toString());
                return params;
            }
        };
        queue.add(loginRequest);
    }
}