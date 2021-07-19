package com.google.firebase.udacity.moviehub.request;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.udacity.moviehub.AppExecutors;
import com.google.firebase.udacity.moviehub.models.MovieModel;
import com.google.firebase.udacity.moviehub.response.MovieSearchResponse;
import com.google.firebase.udacity.moviehub.utils.Credentials;
import com.google.firebase.udacity.moviehub.utils.MovieApi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Response;

public class MovieApiClient {
    // Ye background thread pe ho rha
    // This client will work as a  bridge between retrofit data and live data.
    //LiveData for search
    private static MutableLiveData<List<MovieModel>> mMovies;
    private static MovieApiClient instance;

    // Live data for popular
    private static MutableLiveData<List<MovieModel>> mMoviesPopular;
    // Making popular movies runnable request
    private RetrieveMoviesRunnablePop retrieveMoviesRunnablePop;
    // Making global runnable request
    private RetrieveMoviesRunnable retrieveMoviesRunnable;
    public static MovieApiClient getInstance(){
        if(instance == null){
            instance = new MovieApiClient();
        }
        return instance;
    }
    private MovieApiClient(){
        mMovies = new MutableLiveData<>();
        mMoviesPopular = new MutableLiveData<>();
    }
    public static LiveData<List<MovieModel>> getMovies(){
        return mMovies;
    }
    public static LiveData<List<MovieModel>> getMoviesPop(){
        return mMoviesPopular;
    }
    public void searchMoviesPop(int pageNumber){
        if(retrieveMoviesRunnablePop!=null){
            retrieveMoviesRunnablePop = null;
        }
        retrieveMoviesRunnablePop = new RetrieveMoviesRunnablePop(pageNumber);
        // We will make three runnable threads
        //1.
        final Future myHandler2 = AppExecutors.getInstance().getNetworkIO().submit(retrieveMoviesRunnablePop);
        // We need to set the timeout for this execution
        //2.
        AppExecutors.getInstance().getNetworkIO().schedule(new Runnable() {
            @Override
            public void run() {
                // Cancelling the retrofit call
                myHandler2.cancel(true);
            }
        },1000, TimeUnit.MILLISECONDS);


    }
    public void searchMoviesApi(String query, int pageNumber){
        if(retrieveMoviesRunnable!=null){
            retrieveMoviesRunnable = null;
        }
        retrieveMoviesRunnable = new RetrieveMoviesRunnable(query,pageNumber);
        // We will make three runnable threads
        //1.
        final Future myHandler = AppExecutors.getInstance().getNetworkIO().submit(retrieveMoviesRunnable);
        // We need to set the timeout for this execution
        //2.
        AppExecutors.getInstance().getNetworkIO().schedule(new Runnable() {
            @Override
            public void run() {
                // Cancelling the retrofit call
                myHandler.cancel(true);
            }
        },3000, TimeUnit.MILLISECONDS);


    }
    // Retrieving data from RestApi by runnable class
    // We have 2 types of query: Id & search query
    private class RetrieveMoviesRunnable implements Runnable{

        private String query;
        private int pageNumber;
        boolean cancelRequest;

        public RetrieveMoviesRunnable(String query, int pageNumber) {
            this.query = query;
            this.pageNumber = pageNumber;
            cancelRequest = false;
        }

        @Override
        public void run() {
            // Getting the response object
            try{
                Response response = getMovies(query,pageNumber).execute();
                if(cancelRequest){
                    return;
                }
                if(response.code() == 200){
                    List<MovieModel> list = new ArrayList<>(((MovieSearchResponse)response.body()).getMovies());
                    if(pageNumber == 1){
                        // Sending data to live data
                        // postValue is used for background thread
                        // setValue is not used for background thread
                        mMovies.postValue(list);
                    }
                    else {
                        List<MovieModel> currentMovie = mMovies.getValue();
                        currentMovie.addAll(list);
                        mMovies.postValue(currentMovie); // adding to live data
                    }
                }else{
                    String error = response.errorBody().string();
                    Log.v("tag","Error: "+ error);
                    mMovies.postValue(null);
                }
            }catch (IOException e){
                e.printStackTrace();
                mMovies.postValue(null);
            }

        }
        // search method/query
        private Call<MovieSearchResponse> getMovies(String query, int pageNumber){
            return Services.getMovieApi().searchMovie(
                    Credentials.API_KEY,
                    query,
                    pageNumber
            );
        }
        private void cancelRequest(){
            Log.v("TAG", "cancelling Search Request ");
            cancelRequest = true;
        }

    }
    private class RetrieveMoviesRunnablePop implements Runnable{

        private int pageNumber;
        boolean cancelRequest;

        public RetrieveMoviesRunnablePop(int pageNumber) {
            this.pageNumber = pageNumber;
            cancelRequest = false;
        }

        @Override
        public void run() {
            // Getting the response object
            try{
                Response response2 = getPop(pageNumber).execute();
                if(cancelRequest){
                    return;
                }
                if(response2.code() == 200){
                    List<MovieModel> list = new ArrayList<>(((MovieSearchResponse)response2.body()).getMovies());
                    if(pageNumber == 1){
                        // Sending data to live data
                        // postValue is used for background thread
                        // setValue is not used for background thread
                        mMoviesPopular.postValue(list);
                    }
                    else {
                        List<MovieModel> currentMovie = mMoviesPopular.getValue();
                        currentMovie.addAll(list);
                        mMoviesPopular.postValue(currentMovie); // adding to live data
                    }
                }else{
                    String error = response2.errorBody().string();
                    Log.v("tag","Error: "+ error);
                    mMoviesPopular.postValue(null);
                }
            }catch (IOException e){
                e.printStackTrace();
                mMoviesPopular.postValue(null);
            }

        }
        // search method/query
        private Call<MovieSearchResponse> getPop(int pageNumber){
            return Services.getMovieApi().getPopularMovies(
                    Credentials.API_KEY,
                    pageNumber
            );
        }
        private void cancelRequest(){
            Log.v("TAG", "cancelling Search Request ");
            cancelRequest = true;
        }

    }

}
