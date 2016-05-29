package com.szpilkowski.android.pelnymagazynek.Fragments;



import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.szpilkowski.android.pelnymagazynek.R;

/**
 * Provides UI for the view with List.
 */
public class WarehousesFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RecyclerView recyclerView = (RecyclerView) inflater.inflate(
                R.layout.recycler_view, container, false);
        ContentAdapter adapter = new ContentAdapter(recyclerView.getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return recyclerView;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView warehouseIcon;
        public TextView name;
        public TextView role;
        public ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.warehouses_list, parent, false));
            warehouseIcon = (ImageView) itemView.findViewById(R.id.warehouseIcon);
            name = (TextView) itemView.findViewById(R.id.warehouseName);
            role = (TextView) itemView.findViewById(R.id.warehouseRole);
        }
    }

    public static class ContentAdapter extends RecyclerView.Adapter<ViewHolder> {
        // Set numbers of List in RecyclerView.
        private static final int LENGTH = 6;
        private final String[] mWarehousesName;
        private final String[] mWarehousesRole;
        public ContentAdapter(Context context) {
            Resources resources = context.getResources();
            mWarehousesName = resources.getStringArray(R.array.places);
            mWarehousesRole = resources.getStringArray(R.array.place_desc);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.name.setText(mWarehousesName[position % mWarehousesName.length]);
            holder.role.setText(mWarehousesRole[position % mWarehousesRole.length]);
        }

        @Override
        public int getItemCount() {
            return LENGTH;
        }
    }
}