package com.awok.themoviedb.datamanager.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Umair on 8/25/2017.
 */

public abstract class MainResult {

    @SerializedName("id")
    public Integer id;
    @SerializedName("title")
    public String title;
    @SerializedName("poster_path")
    public String posterPath;
}
