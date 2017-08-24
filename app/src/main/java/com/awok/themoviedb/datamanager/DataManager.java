package com.awok.themoviedb.datamanager;

import com.awok.themoviedb.datamanager.features.Popular;
import com.awok.themoviedb.datamanager.models.PopularModel;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Umair on 8/24/2017.
 */

public class DataManager {

    private Retrofit retrofit;

    private String _BASE_URL = "https://api.themoviedb.org/3/movie/";
    private String _API_KEY = "70ee3af0454ea20ca9ffe5aafc9047d8";

    public DataManager() {
        retrofit = new Retrofit.Builder()
                .baseUrl(_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public Call<PopularModel> getPopularMovies( int pageNo ) {
        Popular popular = retrofit.create(Popular.class);
        Call<PopularModel> call = popular.getJson( _API_KEY, pageNo );
        return call;
    }
}
