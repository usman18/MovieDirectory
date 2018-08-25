package com.uk.moviedirectory.Data;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.uk.moviedirectory.Activities.DetailsActivity;
import com.uk.moviedirectory.Model.Movie;
import com.uk.moviedirectory.R;

import java.util.List;

/**
 * Created by usman on 29-12-2017.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    private Context context;
    private List<Movie> movies;

    public MovieAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflated_view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view,
                parent,false);

        return new ViewHolder(inflated_view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Movie movie = movies.get(position);
        String image_link = movie.getPoster();

        Picasso.with(context).load(image_link).placeholder(android.R.drawable.stat_notify_error).into(holder.movie_poster);

        holder.movie_title.setText(movie.getTitle());
        holder.release_year.setText(movie.getYear());
        holder.category.setText(movie.getMovieType());

    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView movie_poster;
        public TextView movie_title;
        public TextView release_year;
        public TextView category;


        public ViewHolder(View itemView) {
            super(itemView);

            movie_poster = itemView.findViewById(R.id.movie_image_view);
            movie_title = itemView.findViewById(R.id.movie_title_view);
            release_year = itemView.findViewById(R.id.release_id_view);
            category = itemView.findViewById(R.id.category_view);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Movie movie=movies.get(getAdapterPosition());
                    Intent intent = new Intent(context,DetailsActivity.class);

                    intent.putExtra("movie",movie);
                    context.startActivity(intent);
                }
            });
        }
    }
}
