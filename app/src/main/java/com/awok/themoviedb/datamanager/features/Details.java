package com.awok.themoviedb.datamanager.features;

import com.awok.themoviedb.datamanager.models.DetailsModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Umair on 8/25/2017.
 */

public interface Details {
    @GET("movie/{movieid}")
    Call<DetailsModel> getJson(@Path("movieid") int movie_id, @Query("api_key") String api);
}
