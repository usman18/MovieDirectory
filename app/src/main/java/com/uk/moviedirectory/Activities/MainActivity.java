package com.uk.moviedirectory.Activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.uk.moviedirectory.Data.MovieAdapter;
import com.uk.moviedirectory.Model.Movie;
import com.uk.moviedirectory.R;
import com.uk.moviedirectory.Utility.Constants;
import com.uk.moviedirectory.Utility.Preference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RequestQueue queue;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<Movie> movies;
    private AlertDialog alertDialog;
    private AlertDialog.Builder builder;
    private EditText title_name;
    private Button search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        queue= Volley.newRequestQueue(this);
        movies=new ArrayList<>();

        recyclerView=findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Preference preference=new Preference(MainActivity.this);
        String search=preference.getPreferences();
        fetch_Json(search);

        adapter=new MovieAdapter(this,movies);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show_alert_dialog();
            }
        });

    }

    private void fetch_Json(final String search) {

        //clear movie list inorder to remove the previous items fetched
        movies.clear();

        JsonObjectRequest objectRequest=new JsonObjectRequest(JsonObjectRequest.Method.GET,
                Constants.LEFT_URL + search + Constants.RIGHT_URL,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray array=response.getJSONArray("Search");
                            for(int i=0;i<array.length();i++){
                                JSONObject object=array.getJSONObject(i);
                                Movie movie=new Movie();
                                movie.setTitle(object.getString("Title"));
                                movie.setYear("Released Year : "+object.getString("Year"));
                                movie.setImdbId(object.getString("imdbID"));
                                movie.setMovieType("Category : "+object.getString("Type"));
                                movie.setPoster(object.getString("Poster"));

                                movies.add(movie);
                            }
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"OnError",Toast.LENGTH_SHORT).show();
                VolleyLog.d("error ", error.getMessage());
            }
        });

        queue.add(objectRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.search) {
            show_alert_dialog();
            //return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void show_alert_dialog() {
        builder=new AlertDialog.Builder(this);

        View inflated_alert=getLayoutInflater().inflate(R.layout.alert_pop_up,null);
        title_name=inflated_alert.findViewById(R.id.title_movie_alert);
        search=inflated_alert.findViewById(R.id.search_alert);
        builder.setView(inflated_alert);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(title_name.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"Please fill in something ",Toast.LENGTH_SHORT).show();
                }
                else {
                    String search_name=title_name.getText().toString();
                    Preference preference=new Preference(MainActivity.this);
                    preference.setPreferences(title_name.getText().toString());
                    fetch_Json(search_name);
                    alertDialog.dismiss();
               //     adapter.notifyDataSetChanged();
                }
            }
        });

        alertDialog=builder.create();
        alertDialog.show();
    }
}
