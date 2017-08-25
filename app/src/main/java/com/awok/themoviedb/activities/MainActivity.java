package com.awok.themoviedb.activities;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.awok.themoviedb.R;
import com.awok.themoviedb.adapters.RecyclerViewAdapter;
import com.awok.themoviedb.datamanager.DataManager;
import com.awok.themoviedb.datamanager.DataType;
import com.awok.themoviedb.datamanager.models.PopularModel;
import com.crashlytics.android.Crashlytics;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.paginate.Paginate;
import com.paginate.recycler.LoadingListItemCreator;
import com.paginate.recycler.LoadingListItemSpanLookup;

import io.fabric.sdk.android.Fabric;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, Callback {

    private int currentPage = 0;
    private int totalPage = Integer.MAX_VALUE;
    private boolean loading = false;

    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private DataManager dataManager;
    private DataType selectedDataType;

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
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

        currentPage = 0;
        totalPage = Integer.MAX_VALUE;
        int size = mAdapter.clear();
        mAdapter.notifyItemRangeRemoved(0, size);
        mAdapter.notifyDataSetChanged();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();

        selectedDataType = DataType.Popular_Movies;

        //pagination
        Paginate.with(mRecyclerView, callbacks)
                .setLoadingTriggerThreshold(4)
                .addLoadingListItem(true)
                .build();
    }

    private void getNextPage() {
        dataManager.getNextPage(++currentPage, selectedDataType);
    }

    @Override
    public void onResponse(Call call, Response response) {
        if ( response.code() == 200 ) {
            PopularModel pMovies = (PopularModel) response.body();
            totalPage = pMovies.totalPages;
            mAdapter.addItemList(pMovies);
            mAdapter.notifyDataSetChanged();
        }

        loading = false;
    }

    @Override
    public void onFailure(Call call, Throwable t) {
        loading = false;
    }
}
