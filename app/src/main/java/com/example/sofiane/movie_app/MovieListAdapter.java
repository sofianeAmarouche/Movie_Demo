package com.example.sofiane.movie_app;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MovieViewHolder> {

        List<Movies> moviesList;
        private ArrayList<Movies> arrayList;

    public MovieListAdapter(List<Movies> moviesList) {
            this.moviesList = moviesList;
            this.arrayList = new ArrayList<Movies>();
            this.arrayList.addAll(moviesList);

        }

        @NonNull
        @Override
        public MovieListAdapter.MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.items, parent, false);
            MovieViewHolder movieViewHolder = new MovieViewHolder(v);
            return movieViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull MovieListAdapter.MovieViewHolder holder, int position) {
            final Movies movie = moviesList.get(position);

            String moviePoster = movie.getPoster();
            Picasso.get().load(moviePoster).fit().into((ImageView) holder.poster.findViewById(R.id.movie_poster));
            holder.title.setText(movie.getTitle());
            holder.year.setText(movie.getYear());
        }

        public void filter(String charText) {
            charText = charText.toLowerCase(Locale.getDefault());
            moviesList.clear();

            if (charText.length() == 0) {
                moviesList.addAll(arrayList);
            }
            else {
                for (Movies movie : arrayList) {

                    String movieGenre  = movie.getGenre().toLowerCase();
                    String movieTitle  = movie.getTitle().toLowerCase();

                    if(movieTitle.contains(charText) || movieGenre.contains(charText)){
                        moviesList.add(movie);
                    }

                }
            }

            notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {

            return moviesList.size();
        }


        class MovieViewHolder extends RecyclerView.ViewHolder{
            View layout;
            ImageView poster;
            TextView title, year;

            public MovieViewHolder(@NonNull View itemView) {
                super(itemView);
                layout = itemView.findViewById(R.id.list_item);
                poster = itemView.findViewById(R.id.movie_poster);
                title = itemView.findViewById(R.id.movie_title);
                year = itemView.findViewById(R.id.release_year);
            }
        }
}
