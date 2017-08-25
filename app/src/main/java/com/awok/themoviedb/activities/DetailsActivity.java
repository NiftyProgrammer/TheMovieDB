package com.awok.themoviedb.activities;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.awok.themoviedb.R;
import com.awok.themoviedb.datamanager.DataManager;
import com.awok.themoviedb.datamanager.models.DetailsModel;
import com.facebook.drawee.view.SimpleDraweeView;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsActivity extends AppCompatActivity implements Callback<DetailsModel> {

    private int id = 0;

    private TextView overview, gener, date, tagline, runtime, budget, revenue, homepage;
    private SimpleDraweeView mainBackground, posterImage;
    private CollapsingToolbarLayout toolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            id = bundle.getInt("id");
        } else {
            finish();
            return;
        }

        init();

        DataManager manager = new DataManager(this);
        manager.getMovieDetails(id);
    }

    @Override
    public void onResponse(Call<DetailsModel> call, Response<DetailsModel> response) {
        if (response.code() == 200) {
            DetailsModel details = response.body();
            settingValues(details);
        }
    }

    @Override
    public void onFailure(Call<DetailsModel> call, Throwable t) {

    }

    private void init() {
        date = (TextView) findViewById(R.id.details_date);
        gener = (TextView) findViewById(R.id.details_gener);
        budget = (TextView) findViewById(R.id.details_budget);
        tagline = (TextView) findViewById(R.id.details_tagline);
        revenue = (TextView) findViewById(R.id.details_revenue);
        runtime = (TextView) findViewById(R.id.details_runtime);
        homepage = (TextView) findViewById(R.id.details_webpage);
        overview = (TextView) findViewById(R.id.details_overview);
        mainBackground = (SimpleDraweeView) findViewById(R.id.details_poster);
        posterImage = (SimpleDraweeView) findViewById(R.id.details_image);
        toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
    }

    private void settingValues(DetailsModel details) {
        Uri uri = Uri.parse( "https://image.tmdb.org/t/p/w500" + details.backdropPath );
        Uri imageUri = Uri.parse( "https://image.tmdb.org/t/p/w500" + details.posterPath );

        mainBackground.setImageURI(uri);
        posterImage.setImageURI(imageUri);
        toolbarLayout.setTitle(details.title + " (" + details.releaseDate.split("-")[0] + ")" );
        overview.setText(details.overview);
        date.setText(details.releaseDate);
        tagline.setText(details.tagline);
        budget.setText("$" + NumberFormat.getNumberInstance(Locale.US).format(details.budget));
        revenue.setText("$" + NumberFormat.getNumberInstance(Locale.US).format(details.revenue));
        if (details.runtime != null)
            runtime.setText((details.runtime/60) + "h " + (details.runtime%60) + "m");
        else
            runtime.setText("0m");
        homepage.setText(details.homepage);

        //setting geners
        List<DetailsModel.Genre> gList = details.genres;
        if (gList != null && gList.size() > 0) {
            String geners = gList.get(0).name;
            for (int i = 1; i < gList.size(); i++) {
                geners += " | " + gList.get(i).name;
            }
            gener.setText(geners);
        }
    }

}
