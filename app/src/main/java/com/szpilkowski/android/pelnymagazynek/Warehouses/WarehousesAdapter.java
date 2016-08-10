package com.szpilkowski.android.pelnymagazynek.Warehouses;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

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
    private WarehouseManipulator warehouseManipulator;

    Warehouse selectedWarehouse;

    // Populate the arrays with warehouses details
    public WarehousesAdapter(Context context, List<Warehouse> warehousesList) {
        warehouseManipulator = (WarehouseManipulator) context;
        this.mContext = context;
        contentWarehousesList = warehousesList;
    }

    public void onDetach(){
        warehouseManipulator = null;
    }

    @Override
    public WarehouseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.warehouses_list, parent, false);
        WarehouseHolder holder = new WarehouseHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(WarehouseHolder holder, int position) {
        String itemName = contentWarehousesList.get(position).getName();
        holder.name.setText(itemName);

        String itemRole = contentWarehousesList.get(position).getRole();
        String translatedRole;

        if(itemRole.equals("admin"))
            translatedRole = mContext.getResources().getString(R.string.warehousesAdmin);
        else if (itemRole.equals("editor"))
            translatedRole = mContext.getResources().getString(R.string.warehousesEditor);
        else if (itemRole.equals("watcher"))
            translatedRole = mContext.getResources().getString(R.string.warehousesWatcher);
        else
            translatedRole = "Error!";
        holder.role.setText(translatedRole);

        holder.setClickListeners(new WarehouseClickListeners() {
            @Override
            public void onItemLongClick(int pos) {
                selectedWarehouse = contentWarehousesList.get(pos);
                Log.i("WarehouseAdapter", "LongClick on " + selectedWarehouse.getName().toString());
            }

            @Override
            public void onItemClick(int pos) {
                selectedWarehouse = contentWarehousesList.get(pos);
                Log.i("WarehouseAdapter", "Click on " + selectedWarehouse.getName().toString());
                warehouseManipulator.openWarehouse(selectedWarehouse);
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
        if(mContext.getResources().getString(R.string.warehousesEditName) == item.getTitle()){
            final EditWarehouseModalBottomSheet modalBottomSheet = new EditWarehouseModalBottomSheet();
            modalBottomSheet.setCurrentWarehouse(selectedWarehouse);
            warehouseManipulator.showEditModalBottomSheet(modalBottomSheet);

        } else {

            new AlertDialog.Builder(mContext)
                    .setTitle(mContext.getResources().getString(R.string.deleteAlertTitle))
                    .setMessage(mContext.getResources().getString(R.string.deleteAlertMessage))
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            warehouseManipulator.removeWarehouseRequest(selectedWarehouse);
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();

        }
    }
}