package com.awok.themoviedb.datamanager.features;

import com.awok.themoviedb.datamanager.models.MovieModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Umair on 8/25/2017.
 */

public interface Search {
    @GET("search/movie")
    Call<MovieModel> getJson(@Query("api_key") String api, @Query("query") String query,
                             @Query("page") int page);
}
