package edu.uci.ics.fabflixmobile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
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

import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;
import org.json.JSONArray;
//import org.json.
//import org.json.simple.parser.JSONParser;
//import java.util.List;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;


public class Login extends ActionBarActivity
{

    private EditText username;
    private EditText password;
    private TextView message;
    private Button loginButton;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        message = findViewById(R.id.message);
        loginButton = findViewById(R.id.login);
        url = "https://10.0.2.2:8443/cs122b_spring20_team_3_war_exploded/api/login";
        loginButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                login();
            }
        });
    }

    public void login()
    {
        message.setText("Trying to login hhh");
        final RequestQueue queue = NetworkManager.sharedManager(this).queue;
        final StringRequest loginRequest = new StringRequest(Request.Method.POST, url , new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                //TODO should parse the json response to redirect to appropriate functions.
               // Object obj = new JSONParser().parse(response);
                Gson g = new Gson();
                Message msg = g.fromJson(response, Message.class);

                // getting firstName and lastName
                String status = msg.getStatus();
                String message = msg.getMessage();
                Log.d("login.success", response);
              //  Intent listPage = new Intent(Login.this, ListViewActivity.class);
               // startActivity(listPage);
                if (status.equals("success")) {
                   Intent searchPage = new Intent(Login.this, Search.class);
                   startActivity(searchPage);

                   /* Intent singleMoviePage = new Intent(Login.this, SingleMovieActivity.class);
                    singleMoviePage.putExtra("movieid", "BsK25");

                    startActivity(singleMoviePage);*/
                }
            }
        },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("login.error", error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                final Map<String, String> params = new HashMap<>();
                params.put("username", username.getText().toString());
                params.put("password", password.getText().toString());
                return params;
            }
        };
        queue.add(loginRequest);
    }
}