package com.szpilkowski.android.pelnymagazynek.Users;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.szpilkowski.android.pelnymagazynek.R;
import com.szpilkowski.android.pelnymagazynek.Warehouses.WarehouseManipulator;

/**
 * Created by szpileq on 2016-07-28.
 */
public class NewUserModalBottomSheet extends BottomSheetDialogFragment {

    private UsersManipulator usersManipulator;
    private static String TAG = "NewUserMBS";
    View contentView;

    @Override
    public void onAttach(Context context) {
        if (context instanceof UsersManipulator) {
            usersManipulator = (UsersManipulator) context;
            super.onAttach(context);
        } else throw new RuntimeException("Activity must implement WarehousesAdder");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        usersManipulator = null;
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
        contentView = View.inflate(getContext(), R.layout.bottom_sheet_new_user, null);
        dialog.setContentView(contentView);

        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
        final CoordinatorLayout.Behavior behavior = params.getBehavior();

        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
        }

        ImageView okButton = (ImageView) contentView.findViewById(R.id.addNewUserBottomSheet);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RadioGroup roleRadioGroup = (RadioGroup) contentView.findViewById(R.id.radioGroup);
                RadioButton roleRadioButton = (RadioButton) contentView.findViewById(roleRadioGroup.getCheckedRadioButtonId());
                EditText emailEditText = (EditText) contentView.findViewById(R.id.userEmail);
                String email = emailEditText.getText().toString();

                if(0 == email.length() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    usersManipulator.showEmailWarning(NewUserModalBottomSheet.this);
                    return;
                } else

                if(null == roleRadioButton){
                    usersManipulator.showRadioButtonWarning(NewUserModalBottomSheet.this);
                    return;
                } else {
                    String role = roleRadioButton.getText().toString();
                    if (role.equals(getResources().getString(R.string.warehousesEditor)))
                        role = "editor";
                    else if (role.equals(getResources().getString(R.string.warehousesWatcher)))
                        role = "watcher";

                    usersManipulator.newUserRequest(role, email, NewUserModalBottomSheet.this);
                    return;
                }



                //Create a API call to create a new user.
                //usersManipulator.newUserRequest(newRole, NewUserModalBottomSheet.this);
            }
        });

        ImageView closeButton = (ImageView) contentView.findViewById(R.id.closeNewUserBottomSheet);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}