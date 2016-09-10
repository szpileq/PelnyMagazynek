package com.szpilkowski.android.pelnymagazynek.Warehouses;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.szpilkowski.android.pelnymagazynek.DatabaseModels.Warehouse;
import com.szpilkowski.android.pelnymagazynek.R;

/**
 * Created by szpileq on 2016-07-20.
 */
public class EditWarehouseModalBottomSheet extends BottomSheetDialogFragment {

    private WarehouseManipulator warehouseManipulator;
    private static String TAG = "EditWarehouseMBS";
    View contentView;
    private Warehouse currentWarehouse;

    public void setCurrentWarehouse(Warehouse w){
        this.currentWarehouse = w;
    }
    @Override
    public void onAttach(Context context) {
        if (context instanceof WarehouseManipulator) {
            warehouseManipulator = (WarehouseManipulator) context;
            super.onAttach(context);
        } else throw new RuntimeException("Activity must implement WarehousesAdder");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        warehouseManipulator = null;
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
        contentView = View.inflate(getContext(), R.layout.bottom_sheet_edit_warehouse, null);
        dialog.setContentView(contentView);

        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
        final CoordinatorLayout.Behavior behavior = params.getBehavior();

        if( behavior != null && behavior instanceof BottomSheetBehavior ) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
        }

        final EditText editName = (EditText)contentView.findViewById(R.id.warehouseEditText);
        editName.setText(currentWarehouse.getName());
        ImageView okButton = (ImageView)contentView.findViewById(R.id.acceptEditWarehouseBottomSheet);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Create a API call to create a new warehouse.
                warehouseManipulator.editWarehouseRequest(currentWarehouse,editName.getText().toString(), EditWarehouseModalBottomSheet.this);
            }
        });

        ImageView closeButton = (ImageView)contentView.findViewById(R.id.closeEditWarehouseBottomSheet);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}