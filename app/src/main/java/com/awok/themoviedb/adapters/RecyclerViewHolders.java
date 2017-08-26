package com.awok.themoviedb.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.awok.themoviedb.R;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by Umair on 8/24/2017.
 */

public class RecyclerViewHolders extends RecyclerView.ViewHolder {

    private SimpleDraweeView image;
    private TextView title;
    private View itemView;

    public RecyclerViewHolders(View itemView) {
        super(itemView);
        this.itemView = itemView;
        title = (TextView)itemView.findViewById(R.id.item_title);
        image = (SimpleDraweeView)itemView.findViewById(R.id.item_image);
        image.setDrawingCacheEnabled(true);
    }


    public SimpleDraweeView getPosterImage() {
        return image;
    }

    public TextView getTitle() {
        return title;
    }

    public void setID(int id) {
        itemView.setTag(new Integer(id));
    }
}
