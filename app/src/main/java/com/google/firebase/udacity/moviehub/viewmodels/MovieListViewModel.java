package com.google.firebase.udacity.moviehub.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.udacity.moviehub.models.MovieModel;
import com.google.firebase.udacity.moviehub.repositories.MovieRepo;

import java.util.List;

public class MovieListViewModel extends ViewModel {

    private MovieRepo movieRepo;


    public MovieListViewModel() {
        movieRepo = MovieRepo.getInstance();
    }
    public LiveData<List<MovieModel>> getMovies(){
        return movieRepo.getMovies();
    }
    public LiveData<List<MovieModel>> getPop(){
        return movieRepo.getPop();
    }
    // 3-Calling searchMovieApi method(Jo repo me hai) in view-model
    public void searchMovieApi(String query, int pageNumber){
        movieRepo.searchMovieApi(query,pageNumber);
    }
    public void searchMoviePop(int pageNumber){
        movieRepo.searchMoviePop(pageNumber);
    }
    public void searchNextPage(){
        movieRepo.searchNextPage();
    }
    public void searchNextPagePop(){
        movieRepo.searchNextPagePop();
    }

}
