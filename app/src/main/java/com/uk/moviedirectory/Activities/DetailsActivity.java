package com.uk.moviedirectory.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;
import com.uk.moviedirectory.Model.Movie;
import com.uk.moviedirectory.R;
import com.uk.moviedirectory.Utility.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DetailsActivity extends AppCompatActivity {
    private RequestQueue requestQueue;
    private Movie movie;
    private TextView title;
    private TextView release_year;
    private TextView category;
    private TextView runtime;
    private TextView release_date;
    private TextView rating;
    private TextView actors;
    private TextView director;
    private TextView Country;
    private TextView Writer;
    private TextView BoxOffice;
    private ImageView poster_image;

    private RatingBar mRatingBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_activity);

        requestQueue= Volley.newRequestQueue(DetailsActivity.this);

        setUI();
        String imdBid = movie.getImdbId();
        fetchMovieDetails(imdBid);

    }

    private void fetchMovieDetails(String imdbID) {

        JsonObjectRequest request = new JsonObjectRequest(JsonObjectRequest.Method.GET,
                Constants.DETAILS_URL + imdbID + Constants.RIGHT_URL,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("Object",response.toString());

                        title.setText(movie.getTitle());
                        release_year.setText(movie.getYear());
                        category.setText(movie.getMovieType());

                        Picasso.with(getApplicationContext()).load(movie.getPoster()).placeholder(android.R.drawable.stat_notify_error).into(
                                poster_image
                        );

                        try {
                            runtime.setText("Runtime : "+response.getString("Runtime"));
                            release_date.setText("Release Date : "+response.getString("Released"));
                            rating.setText("Rated : "+response.getString("Rated"));

                            float rating = getRating(response.getJSONArray("Ratings"));
                            mRatingBar.setRating(rating);

                            actors.setText("Actors : "+response.getString("Actors"));
                            director.setText("Director : "+response.getString("Director"));
                            Writer.setText("Writer : "+response.getString("Writer"));
                            Country.setText("Country : "+response.getString("Country"));
                            BoxOffice.setText("BoxOffice : "+response.getString("BoxOffice"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Error : ",error.getMessage());
            }
        });

        requestQueue.add(request);
    }

    private void setUI() {

        movie = (Movie) getIntent().getSerializableExtra("movie");
        poster_image = findViewById(R.id.image_details);
        title = findViewById(R.id.title_details);
        release_year = findViewById(R.id.release_year_details);
        category = findViewById(R.id.category_details);
        runtime = findViewById(R.id.runtime_details);
        release_date = findViewById(R.id.release_date_details);
        rating = findViewById(R.id.rating_details);
        actors = findViewById(R.id.actors_details);
        director = findViewById(R.id.director_Details);
        Country = findViewById(R.id.country_Details);
        Writer = findViewById(R.id.writer_details);
        BoxOffice = findViewById(R.id.box_office_details);

        mRatingBar = findViewById(R.id.ratingbar);

    }

    private float getRating(JSONArray array) {

        if (array != null) {

            try {
                JSONObject object = array.getJSONObject(0);

                if (object != null) {

                    String rating = object.getString("Value");

                    if (rating != null){

                        int index = rating.indexOf("/");

                        //rating is in the form of String (eg.  8/10 )
                        //so parsing the string to get the required part
                        float ratingReceived = Float.parseFloat(rating.substring(0,index));
                        float maxRating = Float.parseFloat(rating.substring(index+1));

                        float dividing_factor = (float) (maxRating/5.0);
                        ratingReceived /= dividing_factor;

                        Log.d("Rating", "getRating: " + ratingReceived);

                        return ratingReceived;

                    }

                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }
}
