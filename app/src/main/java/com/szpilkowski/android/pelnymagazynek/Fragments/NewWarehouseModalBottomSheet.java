package com.szpilkowski.android.pelnymagazynek.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.szpilkowski.android.pelnymagazynek.API.ApiConnector;
import com.szpilkowski.android.pelnymagazynek.DbModels.Warehouse;
import com.szpilkowski.android.pelnymagazynek.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by szpileq on 2016-07-20.
 */
public class NewWarehouseModalBottomSheet extends BottomSheetDialogFragment {

    private WarehousesAdder warehousesAdder;

    @Override
    public void onAttach(Context context) {
        if (context instanceof WarehousesAdder) {
            warehousesAdder = (WarehousesAdder) context;
            super.onAttach(context);
        } else throw new RuntimeException("Activity must implement WarehousesAdder");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        warehousesAdder = null;
    }

    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {

        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss();
            }
        }
        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
        }
    };

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        final View contentView = View.inflate(getContext(), R.layout.bottom_sheet_new_warehouse, null);
        dialog.setContentView(contentView);

        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
        final CoordinatorLayout.Behavior behavior = params.getBehavior();

        if( behavior != null && behavior instanceof BottomSheetBehavior ) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
        }

        ImageView okButton = (ImageView)contentView.findViewById(R.id.addNewWarehouseBottomSheet);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText editName = (EditText)contentView.findViewById(R.id.warehouseEditText);

                //Create a API call to create a new warehouse.
                newWarehouseRequest(editName.getText().toString());
                Snackbar snackbar = Snackbar
                        .make(contentView.getRootView(), getString(R.string.nameTaken), Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });

        ImageView closeButton = (ImageView)contentView.findViewById(R.id.closeNewWarehouseBottomSheet);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private int newWarehouseRequest(String name) {
        ApiConnector connector = ApiConnector.getInstance();

        Warehouse w = new Warehouse();
        w.setName(name);

        Call call = connector.apiService.addWarehouse(w);
        call.enqueue(new Callback<Warehouse>() {
            @Override
            public void onResponse(Call<Warehouse> call, Response<Warehouse> response) {
                int statusCode = response.code();
                if (statusCode == 201) {

                    //Success, fill up list of warehouses
                    Warehouse temp = response.body();
                    temp.setRole("admin");
                    warehousesAdder.addWarehouse(temp);
                    dismiss();

                } else if (statusCode == 422) {
                    Log.i("COSTAM", "bad request");
                } else if (statusCode == 401) {
                    Log.i("COSTAM", "auth fail");
                }
                Log.i("COSTAM", "onResponse: API response handled.");
            }

            @Override
            public void onFailure(Call<Warehouse> call, Throwable t) {
                Log.i("COSTAM", "onFailure: API call for logging failed");
                // Log error here since request failed
            }
        });
        return 1;
    }

    // Implemented in WarehousesActivity to provide warehouseList needed by this fragment
    public interface WarehousesAdder {
        int addWarehouse(Warehouse w);
    }
}