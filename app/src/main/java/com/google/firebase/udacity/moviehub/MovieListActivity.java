package com.google.firebase.udacity.moviehub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.TargetApi;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.udacity.moviehub.adapters.MovieRecyclerView;
import com.google.firebase.udacity.moviehub.adapters.OnMovieListener;
import com.google.firebase.udacity.moviehub.models.MovieModel;

import com.google.firebase.udacity.moviehub.viewmodels.MovieListViewModel;


import java.util.List;



public class MovieListActivity extends AppCompatActivity implements OnMovieListener {
    // Before we run our app we need to add the network security config


    private static final String TAG = "MainActivity";
    // ViewModel
    private MovieListViewModel movieListViewModel;
    private RecyclerView recyclerView;
    private MovieRecyclerView movieRecyclerViewAdapter;
    private Toolbar toolbar;
    private boolean isPopular = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("MovieHub");
        recyclerView = findViewById(R.id.recycler_view_main);
        movieListViewModel = new ViewModelProvider(this).get(MovieListViewModel.class);
        setupSearchView();
        // Calling the observers
        observeAnyChange();
        observePopularMovies();
        ConfigureRecyclerView();
        // Getting the data for popular movies
        movieListViewModel.searchMoviePop(1);
//        searchMovieApi("Fast",1);
        // Testing the searchMovieApi method
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Displaying the reults of only page number 1
//                searchMovieApi("Fast",1);
//            }
//        });
    }

    private void observePopularMovies() {
        movieListViewModel.getPop().observe(this, new Observer<List<MovieModel>>() {
            @Override
            public void onChanged(List<MovieModel> movieModels) {
                if(movieModels != null){
                    for(MovieModel movieModel: movieModels){
                        // Get the data in log
                        Log.d(TAG, "onChanged: "+ movieModel.getTitle());
                        movieRecyclerViewAdapter.setMovies(movieModels);
                    }
                }
            }
        });
    }

    // Observing any data change
    private void observeAnyChange(){
        movieListViewModel.getMovies().observe(this, new Observer<List<MovieModel>>() {
            @Override
            public void onChanged(List<MovieModel> movieModels) {
                if(movieModels != null){
                    for(MovieModel movieModel: movieModels){
                        // Get the data in log
                        Log.d(TAG, "onChanged: "+ movieModel.getTitle());
                        movieRecyclerViewAdapter.setMovies(movieModels);
                    }
                }
            }
        });
    }
    // 4-Calling searchMovieApi method(Jo view model me hai) in Main Activity
//    private void searchMovieApi(String query, int pageNumber){
//        movieListViewModel.searchMovieApi(query,pageNumber);
//    }
    // Initializing recyclerview and adding data to it
    private void ConfigureRecyclerView(){
        movieRecyclerViewAdapter = new MovieRecyclerView(this);
        recyclerView.setAdapter(movieRecyclerViewAdapter);

        // Recyclerview Pagination
        // Loading next page of api response
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if(!recyclerView.canScrollVertically(1)){
                    // Here we need to display the next search result on the next page of api
                    movieListViewModel.searchNextPage();
                    if(isPopular) {
                        movieListViewModel.searchNextPagePop();
                    }
                }
            }
        });
    }



    @Override
    public void onMovieClick(int position) {
//        Toast.makeText(this,"The Position" + position,Toast.LENGTH_SHORT).show();
        // We need movie id to get all its details
        Intent intent = new Intent(this,MovieDetails.class);
        intent.putExtra("movie",movieRecyclerViewAdapter.getSelectedMovie(position));
        Log.d("check", "onMovieClick: "+ movieRecyclerViewAdapter.getSelectedMovie(position).getTitle());
        startActivity(intent);
    }

    @Override
    public void onCategoryClick(String category) {

    }
    private void setupSearchView(){
        final SearchView searchView = findViewById(R.id.search_view);

        searchView.setLayoutParams(new Toolbar.LayoutParams(Gravity.RIGHT));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                movieListViewModel.searchMovieApi(
                        query,
                        1
                );
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPopular = false;
            }
        });
    }

    // Ye pura main thread pe ho raha tha par ise hume background thread pe karna hai isliye comment kar diya
//    private void getRetrofitResponse() {
//        MovieApi movieApi = Services.getMovieApi();
//        Call<MovieSearchResponse> responseCall = movieApi.searchMovie(
//                Credentials.API_KEY,
//                "Fast",
//                1
//        );
//        // In order to enque this response
//        responseCall.enqueue(new Callback<MovieSearchResponse>() {
//            @Override
//            public void onResponse(Call<MovieSearchResponse> call, Response<MovieSearchResponse> response) {
//                if (response.code() == 200) {
//                    // 200 is for OK
//                    Log.d(TAG, "onResponse: " + response.body().toString());
//                    List<MovieModel> movies = new ArrayList<>(response.body().getMovies());
//                    for (MovieModel movie : movies) {
//                        Log.v(TAG, "Movie Name: " + movie.getTitle());
//                    }
//                }
//                else {
//                    try {
//                        Log.v(TAG, "Error: " + response.errorBody().string());
//                    }
//                    catch (IOException e){
//                        e.printStackTrace();
//                    }
//
//                }
//            }
//
//            @Override
//            public void onFailure(Call<MovieSearchResponse> call, Throwable t) {
//
//            }
//        });
//    }
//    private void getRetrofitResponseAccordingToID(){
//        MovieApi movieApi = Services.getMovieApi();
//        Call<MovieModel> responseCall = movieApi.getMovie(
//                550,
//                Credentials.API_KEY
//        );
//        responseCall.enqueue(new Callback<MovieModel>() {
//            @Override
//            public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
//                if(response.code() == 200){
//                    MovieModel movie = response.body();
//                    Log.v(TAG,"Response: " + movie.getTitle());
//                }
//                else{
//                    try{
//                        Log.d(TAG, "Error" + response.errorBody().string());
//                    }catch (IOException e){
//                        e.printStackTrace();
//                    }
//
//                }
//            }
//
//            @Override
//            public void onFailure(Call<MovieModel> call, Throwable t) {
//
//            }
//        });
//    }
}