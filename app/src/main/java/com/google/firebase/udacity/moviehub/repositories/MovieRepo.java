package com.google.firebase.udacity.moviehub.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.udacity.moviehub.models.MovieModel;
import com.google.firebase.udacity.moviehub.request.MovieApiClient;

import java.util.List;

public class MovieRepo {
    // This class is acting as repositories
    public static MovieRepo instance;
    private MovieApiClient movieApiClient;
    private String mQuery;
    private int mPage;

    public static MovieRepo getInstance(){
        if(instance == null){
            instance = new MovieRepo();
        }
        return instance;
    }
    private MovieRepo(){

        movieApiClient = MovieApiClient.getInstance();
    }
    public LiveData<List<MovieModel>> getMovies(){
        return MovieApiClient.getMovies();
    }
    public LiveData<List<MovieModel>> getPop(){
        return MovieApiClient.getMoviesPop();
    }
    // 2-Calling the searchMoviesApi method(Jo ApiClient me hai) in repo
    public void searchMovieApi(String query, int pageNumber){
        mQuery = query;
        mPage = pageNumber;

        movieApiClient.searchMoviesApi(query,pageNumber);

    }
    public void searchMoviePop(int pageNumber){
        mPage = pageNumber;
        movieApiClient.searchMoviesPop(pageNumber);

    }
    public void searchNextPage(){
        searchMovieApi(mQuery,mPage+1);
    }
    public void searchNextPagePop(){
        searchMoviePop(mPage+1);
    }
}
