package com.szpilkowski.android.pelnymagazynek.Users;

import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.TextView;

import com.szpilkowski.android.pelnymagazynek.R;

/**
 * Created by szpileq on 2016-07-27.
 */
public class UserHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnCreateContextMenuListener, View.OnClickListener{
    public TextView icon;
    public TextView name;
    public TextView role;
    UserClickListeners clickListeners;

    public UserHolder(View itemView) {
        super(itemView);

        icon = (TextView) itemView.findViewById(R.id.userIcon);
        name = (TextView) itemView.findViewById(R.id.userName);
        role = (TextView) itemView.findViewById(R.id.userRole);

        itemView.setOnLongClickListener(this);
        itemView.setOnClickListener(this);
        itemView.setOnCreateContextMenuListener(this);

    }

    public void setClickListeners(UserClickListeners lc){
        this.clickListeners = lc;
    }

    @Override
    public boolean onLongClick(View v) {
        this.clickListeners.onItemLongClick(getAdapterPosition());
        return false;
    }

    @Override
    public void onClick(View v){
        this.clickListeners.onItemClick(getAdapterPosition());
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle(v.getResources().getString(R.string.ContextMenuTitle));
        menu.add(0,0,0,v.getResources().getString(R.string.userEdit));
        menu.add(0,1,0,v.getResources().getString(R.string.optionDelete));
    }
}