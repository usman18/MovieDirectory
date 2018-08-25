package com.uk.moviedirectory.Data;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Intent;


import com.squareup.picasso.Picasso;
import com.uk.moviedirectory.Activities.DetailsActivity;
import com.uk.moviedirectory.Model.Movie;
import com.uk.moviedirectory.R;

import java.util.List;

/**
 * Created by usman on 29-12-2017.
 */

public class MovieAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<Movie> movies;
    private int view_type;

    public static final int LIST_TYPE = 1;
    public static final int GRID_TYPE = 2;


    public MovieAdapter(Context context, List<Movie> movies, int view_type) {
        this.context = context;
        this.movies = movies;
        this.view_type = view_type;
    }


    @Override
    public int getItemViewType(int position) {
        return view_type;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == GRID_TYPE){
            return new GridViewHolder(LayoutInflater.from(parent.getContext())
            .inflate(R.layout.grid_view,parent,false));
        }else if (viewType == LIST_TYPE){
            return new ListViewHolder(LayoutInflater.from(parent.getContext())
            .inflate(R.layout.list_view,parent,false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder (RecyclerView.ViewHolder holder,int position){

        final Movie movie = movies.get(position);

        if (view_type == LIST_TYPE) {

            String image_link = movie.getPoster();

            Picasso.with(context).load(image_link).placeholder(android.R.drawable.stat_notify_error).into(((ListViewHolder)holder).movie_poster);


            ((ListViewHolder)holder).movie_title
                    .setText(movie.getTitle());

            ((ListViewHolder) holder).category.setText(movie.getMovieType());
            ((ListViewHolder) holder).release_year.setText(movie.getYear());

            ((ListViewHolder) holder).mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, DetailsActivity.class);
                    intent.putExtra("movie",movie);
                    context.startActivity(intent);

                }
            });


        }else if (view_type == GRID_TYPE) {

            String image_link = movie.getPoster();

            Picasso.with(context).load(image_link).placeholder(android.R.drawable.stat_notify_error).into(((GridViewHolder)holder).movie_poster);


            ((GridViewHolder) holder).movie_title
                    .setText(movie.getTitle());

            ((GridViewHolder) holder).category.setText(movie.getMovieType());
            ((GridViewHolder) holder).release_year.setText(movie.getYear());

            ((GridViewHolder) holder).mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context,DetailsActivity.class);
                    intent.putExtra("movie",movie);
                    context.startActivity(intent);
                }
            });


        }

    }

    @Override
    public int getItemCount () {
        return movies.size();
    }


//    public MovieAdapter(Context context, List<Movie> movies) {
//        this.context = context;
//        this.movies = movies;
//    }
//
//    @Override
//    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View inflated_view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view,
//                parent,false);
//
//        return new ViewHolder(inflated_view);
//    }
//
//    @Override
//    public void onBindViewHolder(ViewHolder holder, int position) {
//        Movie movie = movies.get(position);
//        String image_link = movie.getPoster();
//
//        Picasso.with(context).load(image_link).placeholder(android.R.drawable.stat_notify_error).into(holder.movie_poster);
//
//        holder.movie_title.setText(movie.getTitle());
//        holder.release_year.setText(movie.getYear());
//        holder.category.setText(movie.getMovieType());
//
//    }

    public static class ListViewHolder extends RecyclerView.ViewHolder {
        ImageView movie_poster;
        TextView movie_title;
        TextView release_year;
        TextView category;
        View mView;


        public ListViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
            movie_poster = itemView.findViewById(R.id.movie_image_view);
            movie_title = itemView.findViewById(R.id.movie_title_view);
            release_year = itemView.findViewById(R.id.release_id_view);
            category = itemView.findViewById(R.id.category_view);


        }
    }



    public static class GridViewHolder extends RecyclerView.ViewHolder{
        ImageView movie_poster;
        TextView movie_title;
        TextView release_year;
        TextView category;
        View mView;

        public GridViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
            movie_poster = itemView.findViewById(R.id.grid_movie_img);
            movie_title = itemView.findViewById(R.id.grid_movie_name);
            release_year = itemView.findViewById(R.id.grid_movie_released);
            category = itemView.findViewById(R.id.grid_movie_category);


        }
    }

}
