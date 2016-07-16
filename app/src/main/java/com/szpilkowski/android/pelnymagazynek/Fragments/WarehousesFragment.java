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

import com.szpilkowski.android.pelnymagazynek.Activities.WarehousesActivity;
import com.szpilkowski.android.pelnymagazynek.DbModels.Warehouse;
import com.szpilkowski.android.pelnymagazynek.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides UI for the view with List.
 */
public class WarehousesFragment extends Fragment {

    String fragmentRole;
    protected List<Warehouse> warehousesList;
    private WarehousesProvider provider;

    @Override
    public void onAttach(Context context){
        if(context instanceof WarehousesProvider){
            provider = (WarehousesProvider) context; // Activity dziedziczy z kontekstu
            super.onAttach(context);
        } else throw new RuntimeException("Activity must implement WarehousesProvider"); //wyjeb apke jak nie bedzie implementowal interfejsu
    }

    @Override
    public void onDetach(){
        super.onDetach();
        provider = null; //usun odniesienia do activity zeby nie bylo wyciekow pamieci
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        fragmentRole = getArguments().getString("role");
        warehousesList = new ArrayList<>();
        warehousesList = provider.getWarehouses(fragmentRole);


        RecyclerView recyclerView = (RecyclerView) inflater.inflate(
                R.layout.recycler_view, container, false);
        ContentAdapter adapter = new ContentAdapter(recyclerView.getContext(), warehousesList);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return recyclerView;
    }

    //Thanks to role string Fragment knows which part of warehouseList it needs
    public static WarehousesFragment createInstance(String role){
        WarehousesFragment fragment = new WarehousesFragment();
        Bundle bundle = new Bundle();
        bundle.putString("role", role);
        fragment.setArguments(bundle);
        return fragment;
    }

    // Implemented in WarehousesActivity to provide warehouseList needed by this fragment
    public interface WarehousesProvider{
        List<Warehouse> getWarehouses(String role);
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
        private static int LENGTH = 0;
        private final List<String> mWarehousesName;
        private final List<String> mWarehousesRole;
        public ContentAdapter(Context context, List<Warehouse> warehousesList) {
            mWarehousesName = new ArrayList<>();
            mWarehousesRole = new ArrayList<>();
            if(warehousesList != null) {
                for (Warehouse w : warehousesList) {
                    mWarehousesName.add(w.getName());
                    mWarehousesRole.add(w.getRole());
                }
                LENGTH = mWarehousesName.size();
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            if(mWarehousesName.size() > 0)
                holder.name.setText(mWarehousesName.get(position % mWarehousesName.size()));
            if(mWarehousesRole.size() > 0)
                holder.role.setText(mWarehousesRole.get(position % mWarehousesRole.size()));
        }

        @Override
        public int getItemCount() {
            return LENGTH;
        }
    }
}