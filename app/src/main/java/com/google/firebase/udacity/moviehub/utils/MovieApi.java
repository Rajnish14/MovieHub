package com.google.firebase.udacity.moviehub.utils;

import com.google.firebase.udacity.moviehub.models.MovieModel;
import com.google.firebase.udacity.moviehub.response.MovieResponse;
import com.google.firebase.udacity.moviehub.response.MovieSearchResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieApi {
    // Search for movies
    //https://api.themoviedb.org/3/search/movie?api_key={api_key}&query=Jack+Reacher
    @GET("/3/search/movie")
    Call<MovieSearchResponse> searchMovie(
            @Query("api_key") String key,
            @Query("query") String query,
            @Query("page") int page
    );
    // Get Popular Movies
    //https://api.themoviedb.org/3/movie/popular?api_key=4335ced2da680965d916856a64c1497a&page=2

    @GET("/3/movie/popular")
    Call<MovieSearchResponse> getPopularMovies(
            @Query("api_key") String key,
            @Query("page") int page
    );



    // Making Search with Id.
    //https://api.themoviedb.org/3/movie/550?api_key=4335ced2da680965d916856a64c1497a

    @GET("/3/movie/{movie_id}")
    Call<MovieModel> getMovie(
      @Path("movie_id") int movie_id,
      @Query("api_key") String api_key
    );
}
