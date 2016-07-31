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
 * Created by szpileq on 2016-07-28.
 */
public class ItemActivity extends AppCompatActivity {
    ApiConnector connector;
    private static final String TAG = "ItemsActivity";
    Item currentItem;

    View coordinatorLayout;
    FloatingActionButton fabEditItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        coordinatorLayout = findViewById(R.id.coordinatorLayoutItem); // for snackbar purposes

        String title = getResources().getString(R.string.itemActivityTitle);
        this.setTitle(title);

        //Setup API connector
        SharedPreferences prefs = getSharedPreferences("AppPref", MODE_PRIVATE);
        String authorizationToken = prefs.getString("AccessToken", null);

        connector = ApiConnector.getInstance();
        connector.setupApiConnector(authorizationToken);

        Intent intent = getIntent();
        Integer currentItemId = intent.getIntExtra("itemId",0);

        getItem(currentItemId);

        // Adding Toolbar to Main screen
        Toolbar toolbar = (Toolbar) findViewById(R.id.item_toolbar);
        setSupportActionBar(toolbar);

        //Setting up the FAB menu
        fabEditItem = (FloatingActionButton) findViewById(R.id.fabEditItem);

        fabEditItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: open EditItemActivity
            }
        });

    }

    private void getItem(int itemId) {

        Call call = connector.apiService.getItem(itemId);
        call.enqueue(new Callback<Item>() {
            @Override
            public void onResponse(Call<Item> call, Response<Item> response) {
                int statusCode = response.code();
                if (statusCode == 200) {

                    //Success, fill up list of warehouses
                    currentItem = response.body();
                    setupView();

                } else if (statusCode == 401) {
                    Snackbar snackbar = Snackbar
                            .make(coordinatorLayout, "Error with token, log in again.", Snackbar.LENGTH_LONG);

                    snackbar.show();
                }
                Log.i(TAG, "onResponse: API response handled.");
            }

            @Override
            public void onFailure(Call<Item> call, Throwable t) {
                Log.i(TAG, "onFailure: API call for logging failed");
                // Log error here since request failed
            }
        });
    }

    private void setupView(){
        Toolbar itemToolbar = (Toolbar) coordinatorLayout.findViewById(R.id.item_toolbar);
        AppBarLayout itemAppBar = (AppBarLayout) coordinatorLayout.findViewById(R.id.AppBarLayoutItem);

        TextView itemName = (TextView) coordinatorLayout.findViewById(R.id.itemNameHeader);
        TextView itemQuantity = (TextView) coordinatorLayout.findViewById(R.id.itemQuantityHeader);
        TextView itemTargetQuantity = (TextView) coordinatorLayout.findViewById(R.id.targetQuantityValueItem);
        TextView itemMinQuantity = (TextView) coordinatorLayout.findViewById(R.id.minQuantityValueItem);
        TextView itemQrCode = (TextView) coordinatorLayout.findViewById(R.id.qrCodeValueItem);
        TextView itemBarcode = (TextView) coordinatorLayout.findViewById(R.id.barcodeValueItem);
        TextView itemGPS = (TextView) coordinatorLayout.findViewById(R.id.gpsValueItem);
        TextView itemComments = (TextView) coordinatorLayout.findViewById(R.id.commentsValueItem);

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
}