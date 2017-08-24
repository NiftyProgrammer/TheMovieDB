package com.awok.themoviedb.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.awok.themoviedb.R;
import com.awok.themoviedb.datamanager.models.PopularModel;

import java.util.List;

/**
 * Created by Umair on 8/24/2017.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolders> {

    private List<PopularModel.Result> itemList;
    private Context context;

    public RecyclerViewAdapter(Context context, PopularModel list) {
        this.context = context;
        this.itemList = list.results;
    }

    @Override
    public RecyclerViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_content_list_item, null);
        RecyclerViewHolders rcv = new RecyclerViewHolders(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolders holder, int position) {
        holder.title.setText(itemList.get(position).title);

        //setting image
        Uri uri = Uri.parse( "https://image.tmdb.org/t/p/w500" + itemList.get(position).posterPath );
        holder.image.setImageURI(uri);
    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }
}
