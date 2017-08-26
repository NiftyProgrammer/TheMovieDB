package com.awok.themoviedb.activities;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.awok.themoviedb.R;
import com.awok.themoviedb.adapters.RecyclerViewAdapter;
import com.awok.themoviedb.database.DatabaseManager;
import com.awok.themoviedb.datamanager.DataManager;
import com.awok.themoviedb.datamanager.DataType;
import com.awok.themoviedb.datamanager.models.MovieModel;
import com.crashlytics.android.Crashlytics;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.paginate.Paginate;

import java.util.Set;
import java.util.TreeSet;

import io.fabric.sdk.android.Fabric;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, Callback {

    private int currentPage = 0;
    private int totalPage = Integer.MAX_VALUE;
    private boolean loading = false, searchComplete = false;

    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private MaterialSearchView searchView;
    private Set<String> searchAdapter;
    private DataManager dataManager;
    private DataType selectedDataType;
    private DatabaseManager dbManager;


    private Paginate.Callbacks callbacks = new Paginate.Callbacks() {
        @Override
        public void onLoadMore() {
            loading = true;
            getNextPage();
        }

        @Override
        public boolean isLoading() {
            return loading;
        }

        @Override
        public boolean hasLoadedAllItems() {
            return currentPage == totalPage;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Fabric.with(this, new Crashlytics());
        Fresco.initialize(this);
        dbManager = new DatabaseManager(this).open();

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Popular");
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.popular_movies);

        //Recycle View
        mRecyclerView = (RecyclerView) findViewById(R.id.main_recycler_view);
        mLayoutManager = new GridLayoutManager(this, 2);
        mAdapter = new RecyclerViewAdapter(this);
        dataManager = new DataManager(this);

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);

        selectedDataType = DataType.Popular_Movies;

        //pagination
        Paginate.with(mRecyclerView, callbacks)
                .setLoadingTriggerThreshold(4)
                .addLoadingListItem(true)
                .build();


        //search view
        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        searchAdapter = new TreeSet<>();
        searchAdapter.addAll(dbManager.fetchAllTitles());
        searchView.setSuggestions(searchAdapter.toArray(new String[]{}));
        searchView.setSubmitOnClick(true);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchComplete = true;
                selectedDataType = DataType.Search_Movies;
                getSupportActionBar().setTitle(query);

                dataManager.setSearchedQuery(query);
                updateView();

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!newText.isEmpty()) {
                    searchComplete = false;
                    dataManager.setSearchedQuery(newText);
                    dataManager.getNextPage(1, DataType.Search_Movies);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        if (dbManager != null) {
            dbManager.close();
        }

        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        //inserting the search action item
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.popular_movies) {
            getSupportActionBar().setTitle("Popular");
            selectedDataType = DataType.Popular_Movies;
        } else if (id == R.id.top_rated_movies) {
            getSupportActionBar().setTitle("Top Rated");
            selectedDataType = DataType.TopRated_Movies;
        } else if (id == R.id.upcoming_movies) {
            getSupportActionBar().setTitle("Upcoming");
            selectedDataType = DataType.UpComing_Movies;
        } else if (id == R.id.now_playing_movies) {
            getSupportActionBar().setTitle("Now Playing");
            selectedDataType = DataType.NowPlaying_Movies;
        }

        updateView();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void updateView() {
        currentPage = 0;
        totalPage = Integer.MAX_VALUE;
        int size = mAdapter.clear();
        mAdapter.notifyItemRangeRemoved(0, size);
        mAdapter.notifyDataSetChanged();
    }

    private void getNextPage() {
        if (isNetworkConnected()) {
            dataManager.getNextPage(++currentPage, selectedDataType);
        } else {
            Snackbar.make(findViewById(android.R.id.content), "Internet not accessable", Snackbar.LENGTH_LONG)
                    .setAction("Ok", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {}
                    })
                    .setActionTextColor(Color.GREEN)
                    .show();
        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @Override
    public void onResponse(Call call, Response response) {
        if ( response.code() == 200 ) {
            MovieModel model = (MovieModel) response.body();
            if ( call.request().url().toString().contains("search") && !searchComplete ) {
                //setting suggestions for search result
                searchAdapter.addAll(dbManager.insertAllMovie(model));
                searchView.setSuggestions(searchAdapter.toArray(new String[]{}));
            } else {
                totalPage = model.totalPages;
                mAdapter.addItemList(model);
                mAdapter.notifyDataSetChanged();
                dbManager.insertAllMovie(model);
            }
        }
        loading = false;
    }

    @Override
    public void onFailure(Call call, Throwable t) {
        loading = false;
    }
}
