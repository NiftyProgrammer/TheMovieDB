package com.awok.themoviedb.datamanager.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Umair on 8/25/2017.
 */

public class MainModel {
    @SerializedName("page")
    public Integer page;
    @SerializedName("total_results")
    public Integer totalResults;
    @SerializedName("total_pages")
    public Integer totalPages;
}
