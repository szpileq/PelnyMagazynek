package com.szpilkowski.android.pelnymagazynek.Users;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.szpilkowski.android.pelnymagazynek.DatabaseModels.User;
import com.szpilkowski.android.pelnymagazynek.R;

/**
 * Created by szpileq on 2016-07-28.
 */
public class EditUserModalBottomSheet extends BottomSheetDialogFragment {

    private UsersManipulator usersManipulator;
    private static String TAG = "EditUserMBS";
    View contentView;
    private User currentUser;

    public void setCurrentUser(User u){
        this.currentUser = u;
    }

    @Override
    public void onAttach(Context context) {
        if (context instanceof UsersManipulator) {
            usersManipulator = (UsersManipulator) context;
            super.onAttach(context);
        } else throw new RuntimeException("Activity must implement UsersManipulator");
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
        contentView = View.inflate(getContext(), R.layout.bottom_sheet_edit_user, null);
        dialog.setContentView(contentView);

        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
        final CoordinatorLayout.Behavior behavior = params.getBehavior();

        if( behavior != null && behavior instanceof BottomSheetBehavior ) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
        }

        TextView currentUserMail = (TextView) contentView.findViewById(R.id.userEmailEditUserBottomSheet);
        currentUserMail.setText(currentUser.getEmail());

        final RadioGroup roleRadioGroup = (RadioGroup) contentView.findViewById(R.id.radioGroupEditUserBottomSheet);
        if(currentUser.getRole().equals("editor")){
            RadioButton editorRadioButton = (RadioButton) contentView.findViewById(R.id.radioEditorEditUserBottomSheet);
            editorRadioButton.setChecked(true);
        } else if(currentUser.getRole().equals("watcher")){
            RadioButton watcherRadioButton = (RadioButton) contentView.findViewById(R.id.radioWatcherEditUserBottomSheet);
            watcherRadioButton.setChecked(true);
        }


        ImageView okButton = (ImageView)contentView.findViewById(R.id.acceptEditUserBottomSheet);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Create a API call to create a new warehouse.

                RadioButton roleRadioButton = (RadioButton) contentView.findViewById(roleRadioGroup.getCheckedRadioButtonId());

                String role = roleRadioButton.getText().toString();
                if (role.equals(getResources().getString(R.string.warehousesEditor)))
                    role = "editor";
                else if (role.equals(getResources().getString(R.string.warehousesWatcher)))
                    role = "watcher";
                usersManipulator.editUserRequest(currentUser,role, EditUserModalBottomSheet.this);
            }
        });

        ImageView closeButton = (ImageView)contentView.findViewById(R.id.closeEditUserBottomSheet);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
