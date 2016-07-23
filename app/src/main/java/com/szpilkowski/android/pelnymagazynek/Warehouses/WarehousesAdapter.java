package com.szpilkowski.android.pelnymagazynek.Warehouses;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.szpilkowski.android.pelnymagazynek.DbModels.Warehouse;
import com.szpilkowski.android.pelnymagazynek.R;

import java.util.List;

/**
 * Created by szpileq on 2016-07-22.
 */
public class WarehousesAdapter extends RecyclerView.Adapter<WarehouseHolder>{

    Context mContext;
    private static String TAG = "WarehouseAdapter";
    protected final List<Warehouse> contentWarehousesList;

    private Warehouse selectedWarehouse;

    // Populate the arrays with warehouses details
    public WarehousesAdapter(Context context, List<Warehouse> warehousesList) {
        this.mContext = context;
        contentWarehousesList = warehousesList;
    }

    @Override
    public WarehouseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.warehouses_list, parent, false);
        WarehouseHolder holder = new WarehouseHolder(v);
        holder.setLongClickListener(new WarehouseLongClickListener() {
            @Override
            public void onItemLongClick(int pos) {
                selectedWarehouse = contentWarehousesList.get(pos);
                Log.i("WarehouseAdapter", "LongClick on " + selectedWarehouse.getName().toString());
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(WarehouseHolder holder, int position) {
        holder.name.setText(contentWarehousesList.get(position).getName());
        holder.role.setText(contentWarehousesList.get(position).getRole());

    }

    @Override
    public int getItemCount() {
        if(null != contentWarehousesList) {
            return contentWarehousesList.size();
        }
        else
            return 0;
    }

    public void getItemSelected(MenuItem item){
        Log.i("getItemSelected", "Selected " + item.getTitle() + " on " + selectedWarehouse.getName().toString());

    }

}