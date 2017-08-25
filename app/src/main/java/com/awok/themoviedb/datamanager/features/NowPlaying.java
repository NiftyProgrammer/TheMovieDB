package com.awok.themoviedb.datamanager.features;

import com.awok.themoviedb.datamanager.models.PopularModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Umair on 8/25/2017.
 */

public interface NowPlaying {
    @GET("now_playing")
    Call<PopularModel> getJson(@Query("api_key") String api, @Query("page") int page);
}
