package com.szpilkowski.android.pelnymagazynek.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.szpilkowski.android.pelnymagazynek.DbModels.Warehouse;
import com.szpilkowski.android.pelnymagazynek.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides UI for the view with List.
 */
public class WarehousesFragment extends Fragment {

    private static String fragmentRole;
    protected List<Warehouse> warehousesList;
    private WarehousesProvider provider;
    private static String TAG = "WarehousesFragment";

    @Override
    public void onAttach(Context context) {
        if (context instanceof WarehousesProvider) {
            provider = (WarehousesProvider) context;
            super.onAttach(context);
        } else throw new RuntimeException("Activity must implement WarehousesProvider");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        provider = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Get from the Activity the right set of warehouses depending on fragment's role
        fragmentRole = getArguments().getString("role");
        warehousesList = provider.getWarehouses(fragmentRole);

        LinearLayout noElements = (LinearLayout) // No-elements screen
                inflater.inflate(R.layout.no_elements, container, false);

        RecyclerView recyclerView = (RecyclerView) // Actual container for warehouses
                inflater.inflate (R.layout.recycler_view, container, false);

        // If there is any warehouse with fragment's role - populate the list,
        // otherwise - show no-elements screen
        if (warehousesList.size() > 0) {
            noElements.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            ContentAdapter adapter = new ContentAdapter(recyclerView.getContext(), warehousesList);
            recyclerView.setAdapter(adapter);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            return recyclerView;
        } else {
            recyclerView.setVisibility(View.GONE);
            noElements.setVisibility(View.VISIBLE);
            return noElements;
        }
    }

    //Thanks to role string Fragment knows which part of warehouseList it needs
    public static WarehousesFragment createInstance(String role) {
        WarehousesFragment fragment = new WarehousesFragment();
        Bundle bundle = new Bundle();
        bundle.putString("role", role);
        fragment.setArguments(bundle);
        return fragment;
    }

    // Implemented in WarehousesActivity to provide warehouseList needed by this fragment
    public interface WarehousesProvider {
        List<Warehouse> getWarehouses(String role);
    }

    public static class ContentAdapter extends RecyclerView.Adapter<ContentAdapter.ViewHolder> {
        protected final List<Warehouse> contentWarehousesList;

        // Populate the arrays with warehouses details
        public ContentAdapter(Context context, List<Warehouse> warehousesList) {
            contentWarehousesList = warehousesList;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public ImageView warehouseIcon;
            public TextView name;
            public TextView role;

            public ViewHolder(LayoutInflater inflater, ViewGroup parent) {
                super(inflater.inflate(R.layout.warehouses_list, parent, false));
                warehouseIcon = (ImageView) itemView.findViewById(R.id.warehouseIcon);
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
                        Log.i(TAG, "Long click!");
                        return true;
                    }
                });


            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.name.setText(contentWarehousesList.get(position).getName());
            holder.role.setText(contentWarehousesList.get(position).getRole());
        }

        @Override
        public int getItemCount() {
            if(null != contentWarehousesList) {
                return contentWarehousesList.size();
            }
            else
                return 0;
        }
    }
}