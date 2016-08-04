package com.szpilkowski.android.pelnymagazynek.Item;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.szpilkowski.android.pelnymagazynek.API.ApiConnector;
import com.szpilkowski.android.pelnymagazynek.DbModels.Item;
import com.szpilkowski.android.pelnymagazynek.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by szpileq on 02.08.2016.
 */
public class ItemNew extends AppCompatActivity {
    ApiConnector connector;
    private static final String TAG = "ItemNewActivity";
    Item currentItem;

    View coordinatorLayout;
    FloatingActionButton fabAccept;
    protected Integer warehouseId;

    String newQrCode;
    String newBarcode;
    Float newLongitude;
    Float newLatitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_new);
        coordinatorLayout = findViewById(R.id.coordinatorLayoutItemNew); // for snackbar purposes

        String title = getResources().getString(R.string.itemNewTitle);
        this.setTitle(title);

        //Setup API connector
        SharedPreferences prefs = getSharedPreferences("AppPref", MODE_PRIVATE);
        String authorizationToken = prefs.getString("AccessToken", null);
        warehouseId = prefs.getInt("warehouseId", 0);

        connector = ApiConnector.getInstance();
        connector.setupApiConnector(authorizationToken);

        Intent intent = getIntent();

        // Adding Toolbar to Main screen
        Toolbar toolbar = (Toolbar) findViewById(R.id.itemNew_toolbar);
        setSupportActionBar(toolbar);

        //Setting up the FAB menu
        fabAccept = (FloatingActionButton) findViewById(R.id.fabAccept);

        fabAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(0 == getNewItem())
                    newItemRequest(currentItem);
            }
        });

    }

    private Integer getNewItem() {

        TextView itemName = (TextView) coordinatorLayout.findViewById(R.id.itemNewNameHeader);
        TextView itemQuantity = (TextView) coordinatorLayout.findViewById(R.id.itemNewQuantityHeader);
        TextView itemTargetQuantity = (TextView) coordinatorLayout.findViewById(R.id.targetQuantityValueItemNew);
        TextView itemMinQuantity = (TextView) coordinatorLayout.findViewById(R.id.minQuantityValueItemNew);

        TextView itemComments = (TextView) coordinatorLayout.findViewById(R.id.commentsValueItemNew);

        currentItem = new Item();

        if (!itemName.getText().toString().equals(""))
            currentItem.setName(itemName.getText().toString());
        else {
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, getString(R.string.nameFieldMandatoryNewItem), Snackbar.LENGTH_LONG);
            snackbar.show();
            return 1;
        }

        if (!itemQuantity.getText().toString().equals(""))
            currentItem.setQuantity(Integer.parseInt(itemQuantity.getText().toString()));
        else {
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, getString(R.string.quantityFieldMandatoryNewItem), Snackbar.LENGTH_LONG);
            snackbar.show();
            return 1;
        }

        if (!itemMinQuantity.getText().toString().equals(""))
            currentItem.setMinQuantity(Integer.parseInt(itemMinQuantity.getText().toString()));

        if (!itemTargetQuantity.getText().toString().equals(""))
            currentItem.setTargetQuantity(Integer.parseInt(itemTargetQuantity.getText().toString()));

        if (null != newQrCode)
            currentItem.setQrcode(newQrCode);

        if (null != newBarcode)
            currentItem.setBarcode(newBarcode);

        if (null != newLongitude && null != newLatitude) {
            currentItem.setLongitude(newLongitude);
            currentItem.setLatitude(newLatitude);
        }

        if (!itemComments.getText().toString().equals("")) {
            currentItem.setComment(itemComments.getText().toString());
        }
        return 0;
    }

    private void setupView(){
        Toolbar itemToolbar = (Toolbar) coordinatorLayout.findViewById(R.id.itemNew_toolbar);
        AppBarLayout itemAppBar = (AppBarLayout) coordinatorLayout.findViewById(R.id.AppBarLayoutItemNew);

        TextView itemName = (TextView) coordinatorLayout.findViewById(R.id.itemNewNameHeader);
        TextView itemQuantity = (TextView) coordinatorLayout.findViewById(R.id.itemNewQuantityHeader);
        TextView itemTargetQuantity = (TextView) coordinatorLayout.findViewById(R.id.targetQuantityValueItemNew);
        TextView itemMinQuantity = (TextView) coordinatorLayout.findViewById(R.id.minQuantityValueItemNew);
        TextView itemQrCode = (TextView) coordinatorLayout.findViewById(R.id.qrCodeValueItemNew);
        TextView itemBarcode = (TextView) coordinatorLayout.findViewById(R.id.barcodeValueItemNew);
        TextView itemGPS = (TextView) coordinatorLayout.findViewById(R.id.gpsValueItemNew);
        TextView itemComments = (TextView) coordinatorLayout.findViewById(R.id.commentsValueItemNew);

        itemName.setText(currentItem.getName());

        Integer quantity = currentItem.getQuantity();
        itemQuantity.setText(quantity.toString());

        if(0 == quantity){
            itemToolbar.setBackgroundColor(getResources().getColor(R.color.colorDarkRed));
            itemAppBar.setBackgroundColor(getResources().getColor(R.color.colorDarkRed));
        }

        Integer targetQuantity = currentItem.getTargetQuantity();
        if(null != targetQuantity){
            itemTargetQuantity.setText(targetQuantity.toString());
            itemTargetQuantity.setTextColor(getResources().getColor(android.R.color.primary_text_light));
        }

        Integer minQuantity = currentItem.getMinQuantity();
        if(null != minQuantity){
            itemMinQuantity.setText(minQuantity.toString());
            itemMinQuantity.setTextColor(getResources().getColor(android.R.color.primary_text_light));
            if(quantity < minQuantity) {
                itemToolbar.setBackgroundColor(getResources().getColor(R.color.colorDarkYellow));
                itemAppBar.setBackgroundColor(getResources().getColor(R.color.colorDarkYellow));
            }
        }

        if(null != currentItem.getQrcode()){
            itemQrCode.setText(currentItem.getQrcode().toString());
            itemQrCode.setTextColor(getResources().getColor(android.R.color.primary_text_light));
        }

        if(null != currentItem.getBarcode()){
            itemBarcode.setText(currentItem.getBarcode().toString());
            itemBarcode.setTextColor(getResources().getColor(android.R.color.primary_text_light));
        }

        if(null != currentItem.getLatitude() && null != currentItem.getLongitude()){
            itemGPS.setText(currentItem.getLatitude().toString());
            itemGPS.setTextColor(getResources().getColor(android.R.color.primary_text_light));
        }

        if(null != currentItem.getComment()){
            itemComments.setText(currentItem.getComment().toString());
            itemComments.setTextColor(getResources().getColor(android.R.color.primary_text_light));
        }
    }

    public int newItemRequest(Item i) {

        Call call = connector.apiService.addItem(warehouseId, i);
        call.enqueue(new Callback<Item>() {
            @Override
            public void onResponse(Call<Item> call, Response<Item> response) {
                int statusCode = response.code();
                if (statusCode == 201) {
                    Log.i(TAG, "onResponse: API response handled. Adding item");

                    Intent resultItem = new Intent();
                    setResult(2, resultItem);
                    finish();

                } else if (statusCode == 422) {
                    Log.i(TAG, "onResponse: API response handled. You've got item with such name");
                    Snackbar snackbar = Snackbar
                            .make(coordinatorLayout, getString(R.string.itemExistsInWarehouse), Snackbar.LENGTH_LONG);
                    snackbar.show();
                } else if (statusCode == 401) {
                    Log.i(TAG, "onResponse: API response handled. Wrong authorization token. ");
                    Snackbar snackbar = Snackbar
                            .make(coordinatorLayout, getString(R.string.authorizationFailRelog), Snackbar.LENGTH_LONG);
                    snackbar.show();
                    //TODO: Consider switching automatically to MainActivity
                }

            }

            @Override
            public void onFailure(Call<Item> call, Throwable t) {
                Log.i(TAG, "onFailure: API call for adding warehouse failed");
                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, getString(R.string.apiCallFailed), Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });
        return 1;
    }
}