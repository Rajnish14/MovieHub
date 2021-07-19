package com.google.firebase.udacity.moviehub;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.udacity.moviehub.models.MovieModel;

public class MovieDetails extends AppCompatActivity {
    private ImageView imageViewDetails;
    private TextView titleDetails, descDetails;
    private RatingBar ratingBarDetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        imageViewDetails = findViewById(R.id.imageView_detail);
        titleDetails = findViewById(R.id.textView_title_detail);
        descDetails = findViewById(R.id.textView_desc_detail);
        ratingBarDetails = findViewById(R.id.ratingBar_details);

        GetDataFromIntent();
    }

    private void GetDataFromIntent() {
        if(getIntent().hasExtra("movie")){
            MovieModel movie = getIntent().getParcelableExtra("movie");
            titleDetails.setText(movie.getTitle());
            descDetails.setText(movie.getMovie_overview());
            Log.d("desc", "GetDataFromIntent: "+ movie.getMovie_overview());
            ratingBarDetails.setRating((movie.getVote_average())/2);

            Glide.with(this)
                    .load("https://image.tmdb.org/t/p/w500/"+movie.getBackdrop_path())
                    .into(imageViewDetails);

        }
    }
}