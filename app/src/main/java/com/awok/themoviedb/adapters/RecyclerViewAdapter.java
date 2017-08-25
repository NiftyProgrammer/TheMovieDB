package com.awok.themoviedb.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.awok.themoviedb.R;
import com.awok.themoviedb.activities.DetailsActivity;
import com.awok.themoviedb.datamanager.models.MovieModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Umair on 8/24/2017.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolders> implements View.OnClickListener {

    private List<MovieModel.Result> itemList;
    private Context context;

    public RecyclerViewAdapter(Context context) {
        itemList = new ArrayList<>();
        this.context = context;
    }

    public void addItemList(MovieModel list) {
        this.itemList.addAll(list.results);
    }

    public int clear() {
        int size = this.itemList.size();
        this.itemList.clear();
        return size;
    }

    @Override
    public RecyclerViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_content_list_item, null);
        layoutView.setOnClickListener(this);
        RecyclerViewHolders rcv = new RecyclerViewHolders(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolders holder, int position) {
        holder.getTitle().setText(itemList.get(position).title + " (" +
        itemList.get(position).releaseDate.split("-")[0] + ")");

        //setting image
        Uri uri = Uri.parse( "https://image.tmdb.org/t/p/w500" + itemList.get(position).posterPath );
        holder.getPosterImage().setImageURI(uri);
        holder.setID(itemList.get(position).id);
    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }


    @Override
    public void onClick(View view) {
        int id = (Integer) view.getTag();
        Intent intent = new Intent(context, DetailsActivity.class);
        intent.putExtra("id", id);
        context.startActivity(intent);
    }
}
