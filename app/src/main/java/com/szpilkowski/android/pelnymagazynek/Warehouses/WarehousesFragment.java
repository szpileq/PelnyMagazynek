package com.szpilkowski.android.pelnymagazynek.Warehouses;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.szpilkowski.android.pelnymagazynek.DbModels.Warehouse;
import com.szpilkowski.android.pelnymagazynek.R;

import java.util.List;

/**
 * Provides UI for the view with List.
 */
public class WarehousesFragment extends Fragment {

    private String fragmentRole;
    protected List<Warehouse> warehousesList;
    private WarehousesProvider provider;
    private static String TAG = "WarehousesFragment";

    protected WarehousesAdapter adapter;
    RecyclerView recyclerView;

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

        recyclerView = (RecyclerView) // Actual container for warehouses
                inflater.inflate (R.layout.recycler_view, container, false);

        // If there is any warehouse with fragment's role - populate the list,
        // otherwise - show no-elements screen
        if (warehousesList.size() > 0) {
            noElements.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

            adapter = new WarehousesAdapter(getActivity(), warehousesList);

            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

            return recyclerView;
        } else {
            recyclerView.setVisibility(View.GONE);
            noElements.setVisibility(View.VISIBLE);

            return noElements;
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item){

        if (getUserVisibleHint()) {
            adapter.getItemSelected(item);
            return true;
        }
        return false;
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
}