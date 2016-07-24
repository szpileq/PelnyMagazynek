package com.szpilkowski.android.pelnymagazynek.Items;

/**
 * Created by szpileq on 2016-07-24.
 */

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

import com.szpilkowski.android.pelnymagazynek.DbModels.Item;
import com.szpilkowski.android.pelnymagazynek.R;

import java.util.List;

/**
 * Provides UI for the view with List.
 */
public class ItemsFragment extends Fragment {

    private String fragmentQuantityMarker;
    protected List<Item> itemsList;
    private ItemsManipulator itemsManipulator;
    private static String TAG = "ItemsFragment";

    protected ItemsAdapter adapter;
    RecyclerView recyclerView;

    @Override
    public void onAttach(Context context) {
        if (context instanceof ItemsManipulator) {
            itemsManipulator = (ItemsManipulator) context;
            super.onAttach(context);
        } else throw new RuntimeException("Activity must implement ItemsManipulator");
    }

    @Override
    public void onDetach() {
        itemsManipulator = null;
        super.onDetach();
        adapter.onDetach();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Get from the Activity the right set of items depending on their quantity
        fragmentQuantityMarker = getArguments().getString("quantityMarker");
        itemsList = itemsManipulator.getItems(fragmentQuantityMarker);

        LinearLayout noElements = (LinearLayout) // No-elements screen
                inflater.inflate(R.layout.no_elements, container, false);

        recyclerView = (RecyclerView) // Actual container for items
                inflater.inflate (R.layout.recycler_view, container, false);

        // If there are any items - populate the list,
        // otherwise - show no-elements screen
        if (itemsList.size() > 0) {
            noElements.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

            adapter = new ItemsAdapter(getActivity(), itemsList);

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
    public static ItemsFragment createInstance(String role) {
        ItemsFragment fragment = new ItemsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("quantityMarker", role);
        fragment.setArguments(bundle);
        return fragment;
    }
}
