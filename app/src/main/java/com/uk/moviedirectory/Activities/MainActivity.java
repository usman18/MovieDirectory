package com.uk.moviedirectory.Activities;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
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
    private MovieAdapter adapter;
    private List<Movie> movies;
    private AlertDialog alertDialog;
    private AlertDialog.Builder builder;
    private EditText et_name;
    private Button btn_search;


    //Default would be list type view
    private int view_type = MovieAdapter.LIST_TYPE;
    private int grid_count = 2;//bt default for portrait mode


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        int orientation = getResources().getConfiguration().orientation;

        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            grid_count = 4;
        } else {
            grid_count = 2;
        }

        queue = Volley.newRequestQueue(this);
        movies = new ArrayList<>();

        recyclerView =findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        //Using shared preferences to fetch the previous search and provide the user with the results of the same
        Preference preference = new Preference(MainActivity.this);
        String search = preference.getPreferences();

        fetchMovies(search);

        adapter = new MovieAdapter(this,movies,view_type);
        recyclerView.setAdapter(adapter);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlertDialog();
            }
        });

    }

    private void fetchMovies(final String search) {

        //clear movie list inorder to remove the previous items and add new items
        movies.clear();

        JsonObjectRequest objectRequest = new JsonObjectRequest(JsonObjectRequest.Method.GET,
                Constants.LEFT_URL + search + Constants.RIGHT_URL,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray array = response.getJSONArray("Search");

                            for(int i = 0; i < array.length(); i++){

                                JSONObject object = array.getJSONObject(i);

                                Movie movie = new Movie();
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

                Toast.makeText(getApplicationContext(),"Could not find movies ",Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(objectRequest);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.view_type) {

            if (view_type == MovieAdapter.LIST_TYPE) {

                view_type = MovieAdapter.GRID_TYPE;
                item.setIcon(R.drawable.ic_list);

                adapter = new MovieAdapter(getApplicationContext(),movies,view_type);
                recyclerView.setLayoutManager(new GridLayoutManager(this,grid_count));
                recyclerView.setAdapter(adapter);

            }else if (view_type == MovieAdapter.GRID_TYPE) {

                view_type = MovieAdapter.LIST_TYPE;
                item.setIcon(R.drawable.ic_grid);

                adapter = new MovieAdapter(getApplicationContext(),movies,view_type);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                recyclerView.setAdapter(adapter);

            }

        }


        return false;
    }

    private void showAlertDialog() {

        builder = new AlertDialog.Builder(this);

        View inflated_alert = getLayoutInflater().inflate(R.layout.alert_pop_up,null);
        et_name = inflated_alert.findViewById(R.id.title_movie_alert);
        btn_search = inflated_alert.findViewById(R.id.search_alert);
        builder.setView(inflated_alert);

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(et_name.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"Please fill in something ",Toast.LENGTH_SHORT).show();
                }
                else {
                    String search_name = et_name.getText().toString();
                    Preference preference = new Preference(MainActivity.this);
                    preference.setPreferences(et_name.getText().toString());
                    fetchMovies(search_name);
                    alertDialog.dismiss();
                }
            }
        });

        alertDialog = builder.create();
        alertDialog.show();

    }



}
