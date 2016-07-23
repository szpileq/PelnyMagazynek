package com.szpilkowski.android.pelnymagazynek;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.szpilkowski.android.pelnymagazynek.DbModels.Warehouse;
import com.szpilkowski.android.pelnymagazynek.Fragments.NewWarehouseModalBottomSheet;

import java.util.List;

/**
 * Created by szpileq on 2016-07-22.
 */
public class WarehousesAdapter extends RecyclerView.Adapter<WarehouseHolder>{

    Context mContext;
    private static String TAG = "WarehouseAdapter";
    protected final List<Warehouse> contentWarehousesList;

    private String currentName;

    // Populate the arrays with warehouses details
    public WarehousesAdapter(Context context, List<Warehouse> warehousesList) {
        this.mContext = context;
        contentWarehousesList = warehousesList;
    }

    @Override
    public WarehouseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.warehouses_list, parent, false);
        WarehouseHolder holder = new WarehouseHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(WarehouseHolder holder, int position) {
        holder.name.setText(contentWarehousesList.get(position).getName());
        holder.role.setText(contentWarehousesList.get(position).getRole());
        holder.setLongClickListener(new WarehouseLongClickListener() {
            @Override
            public void onItemLongClick(int pos) {
                Log.i("kurwa blagam", "serio");
                currentName = contentWarehousesList.get(pos).getName().toString();
                Toast.makeText(mContext, currentName,Toast.LENGTH_SHORT).show();
            }
        });

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
        Log.i("getItemSelected", "serioplis");
        Toast.makeText(mContext, currentName + item.getTitle(),Toast.LENGTH_SHORT).show();
    }

    /*
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
                    final NewWarehouseModalBottomSheet modalBottomSheet = new NewWarehouseModalBottomSheet();
                    Log.i(TAG, "Long click!");


                    return true;
                }
            });
        }
    }
*/
}