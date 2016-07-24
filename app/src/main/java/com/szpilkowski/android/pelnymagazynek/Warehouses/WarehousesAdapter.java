package com.szpilkowski.android.pelnymagazynek.Warehouses;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.szpilkowski.android.pelnymagazynek.API.ApiConnector;
import com.szpilkowski.android.pelnymagazynek.DbModels.Warehouse;
import com.szpilkowski.android.pelnymagazynek.R;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by szpileq on 2016-07-22.
 */
public class WarehousesAdapter extends RecyclerView.Adapter<WarehouseHolder>{

    Context mContext;
    private static String TAG = "WarehouseAdapter";
    protected final List<Warehouse> contentWarehousesList;
    private WarehousesRemover warehousesRemover;

    Warehouse selectedWarehouse;

    // Populate the arrays with warehouses details
    public WarehousesAdapter(Context context, List<Warehouse> warehousesList) {
        warehousesRemover = (WarehousesRemover) context;
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
                selectedWarehouse = contentWarehousesList.get(pos);
                Log.i("WarehouseAdapter", "LongClick on " + selectedWarehouse.getName().toString());
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
            //edit name
            return;
        } else {
            removeWarehouseRequest(selectedWarehouse);
        }


    }

    private int removeWarehouseRequest(final Warehouse w) {
        ApiConnector connector = ApiConnector.getInstance();

        Call call = connector.apiService.removeWarehouse(w.getId());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                int statusCode = response.code();
                if (statusCode == 204) {
                    Log.i(TAG, "onResponse: API response handled. Removing warehouse");
                    warehousesRemover.removeWarehouse(w);

                } else if (statusCode == 422) {
                    Log.i(TAG, "onResponse: API response handled. This name is taken");
                } else if (statusCode == 401) {
                    Log.i(TAG, "onResponse: API response handled. Wrong authorization token. ");
                    /*Snackbar snackbar = Snackbar
                            .make(mContext, getString(R.string.authorizationFailRelog), Snackbar.LENGTH_LONG);
                    snackbar.show();*/
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
         /*       Snackbar snackbar = Snackbar
                        .make(contentView.getRootView(), getString(R.string.authorizationFailRelog), Snackbar.LENGTH_LONG);
                snackbar.show();*/
                Log.i(TAG, "onFailure: API call for adding warehouse failed");
                // Log error here since request failed
            }
        });
        return 1;
    }

    // Implemented in WarehousesActivity for removing warehouses
    public interface WarehousesRemover {
        int removeWarehouse(Warehouse w);
    }

}