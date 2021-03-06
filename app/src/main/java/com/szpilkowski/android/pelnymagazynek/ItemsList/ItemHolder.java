package com.szpilkowski.android.pelnymagazynek.ItemsList;

import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.CircleProgress;
import com.szpilkowski.android.pelnymagazynek.R;


/**
 * Created by szpileq on 2016-07-24.
 */
public class ItemHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnCreateContextMenuListener, View.OnClickListener{
    public TextView name;
    public TextView comments;
    public CircleProgress circleProgress;
    ItemClickListeners clickListeners;

    public ItemHolder(View itemView) {
        super(itemView);

        name = (TextView) itemView.findViewById(R.id.itemName);
        comments = (TextView) itemView.findViewById(R.id.itemComments);
        circleProgress = (CircleProgress) itemView.findViewById(R.id.circleItemStoragePercentage);

        itemView.setOnLongClickListener(this);
        itemView.setOnClickListener(this);
        itemView.setOnCreateContextMenuListener(this);

    }

    public void setClickListeners(ItemClickListeners lc){
        this.clickListeners = lc;
    }

    @Override
    public boolean onLongClick(View v) {
        this.clickListeners.onItemLongClick(getAdapterPosition());
        return false;
    }

    @Override
    public void onClick(View v){
        this.clickListeners.onItemClick(getAdapterPosition());
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle(v.getResources().getString(R.string.ContextMenuTitle));
        menu.add(0,0,0,v.getResources().getString(R.string.optionOpen));
        menu.add(0,1,0,v.getResources().getString(R.string.optionDelete));
    }
}