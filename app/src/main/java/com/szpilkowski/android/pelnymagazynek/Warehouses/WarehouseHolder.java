package com.szpilkowski.android.pelnymagazynek.Warehouses;

import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.TextView;

import com.szpilkowski.android.pelnymagazynek.R;

/**
 * Created by szpileq on 2016-07-23.
 */
public class WarehouseHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnCreateContextMenuListener, View.OnClickListener{
    public TextView name;
    public TextView role;
    WarehouseClickListeners clickListeners;

    public WarehouseHolder(View itemView) {
        super(itemView);
        name = (TextView) itemView.findViewById(R.id.warehouseName);
        role = (TextView) itemView.findViewById(R.id.warehouseRole);

        itemView.setOnLongClickListener(this);
        itemView.setOnClickListener(this);
        itemView.setOnCreateContextMenuListener(this);

    }

    public void setClickListeners(WarehouseClickListeners lc){
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
        menu.setHeaderTitle(v.getResources().getString(R.string.warehousesContextMenuTitle));
        menu.add(0,0,0,v.getResources().getString(R.string.warehousesEditName));
        menu.add(0,1,0,v.getResources().getString(R.string.warehouseDelete));
    }
}