package com.szpilkowski.android.pelnymagazynek;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.szpilkowski.android.pelnymagazynek.DbModels.Warehouse;

import java.util.List;

/**
 * Created by szpileq on 2016-07-22.
 */
public class WarehousesAdapter extends RecyclerView.Adapter<WarehousesAdapter.ViewHolder>{

    private static String TAG = "WarehouseAdapter";
    protected final List<Warehouse> contentWarehousesList;

    // Populate the arrays with warehouses details
    public WarehousesAdapter(Context context, List<Warehouse> warehousesList) {
        contentWarehousesList = warehousesList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView role;

        public ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.warehouses_list, parent, false));
            name = (TextView) itemView.findViewById(R.id.warehouseName);
            role = (TextView) itemView.findViewById(R.id.warehouseRole);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Warehouse currentElement = contentWarehousesList.get(getAdapterPosition());
                    Log.i(TAG, "Clicked on warehouse " + currentElement.getName());
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Log.i(TAG, "Long click!");
                    return true;
                }
            });


        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
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
}