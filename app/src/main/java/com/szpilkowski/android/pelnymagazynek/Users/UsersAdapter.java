package com.szpilkowski.android.pelnymagazynek.Users;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.szpilkowski.android.pelnymagazynek.DatabaseModels.User;
import com.szpilkowski.android.pelnymagazynek.R;

import java.util.List;

/**
 * Created by szpileq on 2016-07-27.
 */
public class UsersAdapter extends RecyclerView.Adapter<UserHolder>{

    Context mContext;
    private static String TAG = "UsersAdapter";
    protected final List<User> contentUsersList;
    private UsersManipulator usersManipulator;

    User selectedUser;

    // Populate the arrays with users details
    public UsersAdapter(Context context, List<User> usersList) {
        usersManipulator = (UsersManipulator) context;
        this.mContext = context;
        contentUsersList = usersList;
    }

    public void onDetach(){
        usersManipulator = null;
    }

    @Override
    public UserHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.users_list, parent, false);
        UserHolder holder = new UserHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(UserHolder holder, int position) {
        String userFirstName = contentUsersList.get(position).getFirstName();
        String userLastName = contentUsersList.get(position).getLastName();
        String userRole = contentUsersList.get(position).getRole();

        if(userRole.equals("editor"))
            holder.icon.setBackground(mContext.getDrawable(R.drawable.blue_circle));
        if(userRole.equals("watcher"))
            holder.icon.setBackground(mContext.getDrawable(R.drawable.yellow_circle));

        holder.icon.setText(userFirstName.substring(0,1).toUpperCase() + userLastName.substring(0,1).toUpperCase());

        holder.name.setText(userFirstName + " " + userLastName);

        String translatedRole;
        if(userRole.equals("admin"))
            translatedRole = mContext.getResources().getString(R.string.warehousesAdmin);
        else if (userRole.equals("editor"))
            translatedRole = mContext.getResources().getString(R.string.warehousesEditor);
        else if (userRole.equals("watcher"))
            translatedRole = mContext.getResources().getString(R.string.warehousesWatcher);
        else
            translatedRole = "Error!";
        holder.role.setText(translatedRole);

        holder.setClickListeners(new UserClickListeners() {
            @Override
            public void onItemLongClick(int pos) {
                selectedUser = contentUsersList.get(pos);
                Log.i("WarehouseAdapter", "LongClick on " + selectedUser.getFirstName().toString());
            }

            @Override
            public void onItemClick(int pos) {
                /*
                selectedWarehouse = contentWarehousesList.get(pos);
                Log.i("WarehouseAdapter", "Click on " + selectedWarehouse.getName().toString());
                warehouseManipulator.openWarehouse(selectedWarehouse); */
            }
        });
    }

    @Override
    public int getItemCount() {
        if(null != contentUsersList) {
            return contentUsersList.size();
        }
        else
            return 0;
    }

    public void getItemSelected(MenuItem item){
        if(mContext.getResources().getString(R.string.userEdit) == item.getTitle()){

            final EditUserModalBottomSheet modalBottomSheet = new EditUserModalBottomSheet();
            modalBottomSheet.setCurrentUser(selectedUser);
            usersManipulator.showEditModalBottomSheet(modalBottomSheet);


        } else {

            new AlertDialog.Builder(mContext)
                    .setTitle(mContext.getResources().getString(R.string.deleteAlertTitle))
                    .setMessage(mContext.getResources().getString(R.string.deleteAlertMessage))
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            usersManipulator.removeUserRequest(selectedUser);
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
