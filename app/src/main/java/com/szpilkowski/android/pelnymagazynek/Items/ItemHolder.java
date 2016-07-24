package com.szpilkowski.android.pelnymagazynek.Items;

import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.TextView;

import com.szpilkowski.android.pelnymagazynek.R;


/**
 * Created by szpileq on 2016-07-24.
 */
public class ItemHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnCreateContextMenuListener, View.OnClickListener{
    public TextView name;
    public TextView comments;
    ItemClickListeners clickListeners;

    public ItemHolder(View itemView) {
        super(itemView);
        name = (TextView) itemView.findViewById(R.id.itemName);
        comments = (TextView) itemView.findViewById(R.id.itemComments);

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
        menu.setHeaderTitle(v.getResources().getString(R.string.itemsContextMenuTitle));
        menu.add(0,0,0,v.getResources().getString(R.string.itemEdit));
        menu.add(0,1,0,v.getResources().getString(R.string.itemDelete));
    }
}