package com.awok.themoviedb.datamanager;

import com.awok.themoviedb.datamanager.features.Details;
import com.awok.themoviedb.datamanager.features.NowPlaying;
import com.awok.themoviedb.datamanager.features.Popular;
import com.awok.themoviedb.datamanager.features.Search;
import com.awok.themoviedb.datamanager.features.TopRated;
import com.awok.themoviedb.datamanager.features.Upcoming;
import com.awok.themoviedb.datamanager.models.DetailsModel;
import com.awok.themoviedb.datamanager.models.MovieModel;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Umair on 8/24/2017.
 */

public class DataManager {

    private Retrofit retrofit;
    private Callback callback;
    private String searchedQuery;

    private String _BASE_URL = "https://api.themoviedb.org/3/";
    private String _API_KEY = "70ee3af0454ea20ca9ffe5aafc9047d8";

    public DataManager(Callback callback) {
        retrofit = new Retrofit.Builder()
                .baseUrl(_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        this.callback = callback;
    }

    public void setSearchedQuery(String query) {
        this.searchedQuery = query;
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
            case Search_Movies: {
                c = getSearchedMovies(pageNo, searchedQuery);
                break;
            }
        }

        if (c != null)
            c.enqueue(this.callback);
    }

    public void getMovieDetails(int id) {
        Details details = retrofit.create(Details.class);
        Call<DetailsModel> call = details.getJson(id, _API_KEY);
        call.enqueue(callback);
    }

    private Call<MovieModel> getPopularMovies(int pageNo ) {
        Popular popular = retrofit.create(Popular.class);
        Call<MovieModel> call = popular.getJson( _API_KEY, pageNo );
        return call;
    }

    private Call<MovieModel> getTopRatedMovies(int pageNo) {
        TopRated topRated = retrofit.create(TopRated.class);
        Call<MovieModel> call = topRated.getJson(_API_KEY, pageNo);
        return call;
    }

    private Call<MovieModel> getUpcomingMovies(int pageNo) {
        Upcoming upcoming = retrofit.create(Upcoming.class);
        Call<MovieModel> call = upcoming.getJson(_API_KEY, pageNo);
        return call;
    }

    private Call<MovieModel> getNowPlayingMovies(int pageNo) {
        NowPlaying nowPlaying = retrofit.create(NowPlaying.class);
        Call<MovieModel> call = nowPlaying.getJson(_API_KEY, pageNo);
        return call;
    }

    private Call<MovieModel> getSearchedMovies(int pageNo, String query) {
        Search search = retrofit.create(Search.class);
        Call<MovieModel> call = null;
        try {
            call = search.getJson(_API_KEY,
                    URLEncoder.encode(query, "UTF-8"),
                    pageNo);
        } catch (UnsupportedEncodingException e) {
            call = search.getJson(_API_KEY, query, pageNo);
        }
        return call;
    }
}
