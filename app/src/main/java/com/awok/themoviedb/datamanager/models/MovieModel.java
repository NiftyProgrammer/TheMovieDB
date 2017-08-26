package com.awok.themoviedb.datamanager.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Umair on 8/24/2017.
 */

public class MovieModel {

    @SerializedName("page")
    public Integer page;
    @SerializedName("total_results")
    public Integer totalResults;
    @SerializedName("total_pages")
    public Integer totalPages;
    @SerializedName("results")
    public List<Result> results = null;

    public class Result {

        @SerializedName("vote_count")
        public Integer voteCount;
        @SerializedName("id")
        public Integer id;
        @SerializedName("video")
        public Boolean video;
        @SerializedName("vote_average")
        public Double voteAverage;
        @SerializedName("title")
        public String title;
        @SerializedName("popularity")
        public Double popularity;
        @SerializedName("poster_path")
        public String posterPath;
        @SerializedName("original_language")
        public String originalLanguage;
        @SerializedName("original_title")
        public String originalTitle;
        @SerializedName("genre_ids")
        public List<Integer> genreIds = null;
        @SerializedName("backdrop_path")
        public String backdropPath;
        @SerializedName("adult")
        public Boolean adult;
        @SerializedName("overview")
        public String overview;
        @SerializedName("release_date")
        public String releaseDate;

    }
}
