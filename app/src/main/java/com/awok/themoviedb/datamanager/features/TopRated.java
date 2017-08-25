package com.awok.themoviedb.datamanager.features;

import com.awok.themoviedb.datamanager.models.MovieModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Umair on 8/25/2017.
 */

public interface TopRated {
    @GET("movie/top_rated")
    Call<MovieModel> getJson(@Query("api_key") String api, @Query("page") int page);
}
