package com.awok.themoviedb.datamanager;

import com.awok.themoviedb.datamanager.features.NowPlaying;
import com.awok.themoviedb.datamanager.features.Popular;
import com.awok.themoviedb.datamanager.features.TopRated;
import com.awok.themoviedb.datamanager.features.Upcoming;
import com.awok.themoviedb.datamanager.models.PopularModel;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Umair on 8/24/2017.
 */

public class DataManager {

    private Retrofit retrofit;
    private Callback callback;

    private String _BASE_URL = "https://api.themoviedb.org/3/movie/";
    private String _API_KEY = "70ee3af0454ea20ca9ffe5aafc9047d8";

    public DataManager(Callback callback) {
        retrofit = new Retrofit.Builder()
                .baseUrl(_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        this.callback = callback;
    }

    public void getNextPage(int pageNo, DataType type) {
        Call c = null;
        switch (type) {
            case Popular_Movies: {
                c = getPopularMovies(pageNo);
                break;
            }
            case TopRated_Movies: {
                c = getTopRatedMovies(pageNo);
                break;
            }
            case UpComing_Movies: {
                c = getUpcomingMovies(pageNo);
                break;
            }
            case NowPlaying_Movies: {
                c = getNowPlayingMovies(pageNo);
                break;
            }
        }

        if (c != null)
            c.enqueue(this.callback);
    }

    private Call<PopularModel> getPopularMovies( int pageNo ) {
        Popular popular = retrofit.create(Popular.class);
        Call<PopularModel> call = popular.getJson( _API_KEY, pageNo );
        return call;
    }

    private Call<PopularModel> getTopRatedMovies(int pageNo) {
        TopRated topRated = retrofit.create(TopRated.class);
        Call<PopularModel> call = topRated.getJson(_API_KEY, pageNo);
        return call;
    }

    private Call<PopularModel> getUpcomingMovies(int pageNo) {
        Upcoming upcoming = retrofit.create(Upcoming.class);
        Call<PopularModel> call = upcoming.getJson(_API_KEY, pageNo);
        return call;
    }

    private Call<PopularModel> getNowPlayingMovies(int pageNo) {
        NowPlaying nowPlaying = retrofit.create(NowPlaying.class);
        Call<PopularModel> call = nowPlaying.getJson(_API_KEY, pageNo);
        return call;
    }
}
