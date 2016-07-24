package com.szpilkowski.android.pelnymagazynek.Items;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.szpilkowski.android.pelnymagazynek.DbModels.Item;
import com.szpilkowski.android.pelnymagazynek.R;

import java.util.List;

/**
 * Created by szpileq on 2016-07-24.
 */
public class ItemsAdapter extends RecyclerView.Adapter<ItemHolder>{

    Context mContext;
    private static String TAG = "ItemAdapter";
    protected final List<Item> contentItemsList;
    private ItemsManipulator itemsManipulator;

    Item selectedItem;

    // Populate the arrays with items details
    public ItemsAdapter(Context context, List<Item> itemsList) {
        itemsManipulator = (ItemsManipulator) context;
        this.mContext = context;
        contentItemsList = itemsList;
    }

    public void onDetach(){
        itemsManipulator = null;
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_list, parent, false);
        ItemHolder holder = new ItemHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        String itemName = contentItemsList.get(position).getName();
        String itemComments = contentItemsList.get(position).getComment();
        holder.name.setText(itemName);
        holder.comments.setText(itemComments);

        holder.setClickListeners(new ItemClickListeners() {
            @Override
            public void onItemLongClick(int pos) {
                selectedItem = contentItemsList.get(pos);
                Log.i("WarehouseAdapter", "LongClick on " + selectedItem.getName().toString());
            }

            @Override
            public void onItemClick(int pos) {
                selectedItem = contentItemsList.get(pos);
                Log.i("WarehouseAdapter", "Click on " + selectedItem.getName().toString());
                itemsManipulator.openItem(selectedItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(null != contentItemsList) {
            return contentItemsList.size();
        }
        else
            return 0;
    }

    public void getItemSelected(MenuItem item){
        if(mContext.getResources().getString(R.string.itemsEditName) == item.getTitle()){
            /*
            final EditWarehouseModalBottomSheet modalBottomSheet = new EditWarehouseModalBottomSheet();
            modalBottomSheet.setCurrentWarehouse(selectedWarehouse);
            itemsManipulator.showEditModalBottomSheet(modalBottomSheet); */

        } else {
            itemsManipulator.removeItemRequest(selectedItem);
        }
    }
}
