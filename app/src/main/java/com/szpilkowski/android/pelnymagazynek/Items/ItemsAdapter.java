package com.szpilkowski.android.pelnymagazynek.Items;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.szpilkowski.android.pelnymagazynek.DbModels.Item;
import com.szpilkowski.android.pelnymagazynek.R;

import java.util.List;

import retrofit2.http.PUT;

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
        Integer itemQuantity = contentItemsList.get(position).getQuantity();
        Integer itemTargetQuantity = contentItemsList.get(position).getTargetQuantity();
        Integer itemMinQuantity = contentItemsList.get(position).getMinQuantity();

        holder.name.setText(itemName);
        holder.comments.setText(itemComments);

        holder.circleProgress.setSuffixText("");
        holder.circleProgress.setProgress(itemQuantity);
        holder.circleProgress.setMax(itemQuantity);

        holder.circleProgress.setFinishedColor(mContext.getResources().getColor(R.color.colorPrimary));
        holder.circleProgress.setUnfinishedColor(mContext.getResources().getColor(R.color.colorPrimaryDark));

        if(null != itemTargetQuantity) {
            if(itemTargetQuantity < itemQuantity)
                holder.circleProgress.setMax(itemQuantity);
            else
                holder.circleProgress.setMax(itemTargetQuantity);
        }

        if(null != itemMinQuantity && itemQuantity < itemMinQuantity){
            holder.circleProgress.setFinishedColor(mContext.getResources().getColor(R.color.colorLightYellow));
            holder.circleProgress.setUnfinishedColor(mContext.getResources().getColor(R.color.colorDarkYellow));
        }

        if(0 == itemQuantity){
            holder.circleProgress.setUnfinishedColor(mContext.getResources().getColor(R.color.colorDarkRed));
        }


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
            itemsManipulator.openItem(selectedItem);

        } else {
            new AlertDialog.Builder(mContext)
                    .setTitle(mContext.getResources().getString(R.string.deleteAlertTitle))
                    .setMessage(mContext.getResources().getString(R.string.deleteAlertMessage))
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            itemsManipulator.removeItemRequest(selectedItem);
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
