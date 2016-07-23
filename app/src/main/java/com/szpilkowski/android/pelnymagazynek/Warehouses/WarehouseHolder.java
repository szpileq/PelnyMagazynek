package com.szpilkowski.android.pelnymagazynek.Warehouses;

import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.TextView;

import com.szpilkowski.android.pelnymagazynek.R;

/**
 * Created by szpileq on 2016-07-23.
 */
public class WarehouseHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnCreateContextMenuListener{
    public TextView name;
    public TextView role;
    WarehouseLongClickListener longClickListener;

    public WarehouseHolder(View itemView) {
        super(itemView);
        name = (TextView) itemView.findViewById(R.id.warehouseName);
        role = (TextView) itemView.findViewById(R.id.warehouseRole);

        itemView.setOnLongClickListener(this);
        itemView.setOnCreateContextMenuListener(this);

    }

    public void setLongClickListener(WarehouseLongClickListener lc){
        this.longClickListener = lc;
    }

    @Override
    public boolean onLongClick(View v) {
        this.longClickListener.onItemLongClick(getAdapterPosition()); // OR ADAPTER POSITION, TO CHECK
        return false;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle(v.getResources().getString(R.string.warehousesContextMenuTitle));
        menu.add(0,0,0,v.getResources().getString(R.string.warehousesEditName));
        menu.add(0,1,0,v.getResources().getString(R.string.warehouseDelete));
    }
}