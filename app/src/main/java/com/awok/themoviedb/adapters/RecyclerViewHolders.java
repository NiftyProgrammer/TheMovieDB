package com.awok.themoviedb.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.awok.themoviedb.R;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by Umair on 8/24/2017.
 */

public class RecyclerViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener {

    public SimpleDraweeView image;
    public TextView title;

    public RecyclerViewHolders(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        title = (TextView)itemView.findViewById(R.id.item_title);
        image = (SimpleDraweeView)itemView.findViewById(R.id.item_image);
    }

    @Override
    public void onClick(View view) {

    }
}
