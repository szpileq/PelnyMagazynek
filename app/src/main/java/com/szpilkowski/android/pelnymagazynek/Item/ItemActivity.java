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
import android.view.Window;
import android.view.WindowManager;
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
        coordinatorLayout = findViewById(R.id.coordinatorLayoutItemActivity); // for snackbar purposes

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
        Toolbar toolbar = (Toolbar) findViewById(R.id.itemActivity_toolbar);
        setSupportActionBar(toolbar);

        //Setting up the FAB menu
        fabEditItem = (FloatingActionButton) findViewById(R.id.fabEditItem);

        fabEditItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newActivity = new Intent(ItemActivity.this, ItemEdit.class);
                newActivity.putExtra("currentItem", currentItem);
                startActivityForResult (newActivity, 1);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(1 == resultCode){
            currentItem = data.getParcelableExtra("currentItem");
            setupView();
        }
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
        Toolbar itemToolbar = (Toolbar) coordinatorLayout.findViewById(R.id.itemActivity_toolbar);
        AppBarLayout itemAppBar = (AppBarLayout) coordinatorLayout.findViewById(R.id.AppBarLayoutItemActivity);

        TextView itemName = (TextView) coordinatorLayout.findViewById(R.id.itemActivityNameHeader);
        TextView itemQuantity = (TextView) coordinatorLayout.findViewById(R.id.itemActivityQuantityHeader);
        TextView itemTargetQuantity = (TextView) coordinatorLayout.findViewById(R.id.targetQuantityValueItemActivity);
        TextView itemMinQuantity = (TextView) coordinatorLayout.findViewById(R.id.minQuantityValueItemActivity);
        TextView itemQrCode = (TextView) coordinatorLayout.findViewById(R.id.qrCodeValueItemActivity);
        TextView itemBarcode = (TextView) coordinatorLayout.findViewById(R.id.barcodeValueItemActivity);
        TextView itemGPS = (TextView) coordinatorLayout.findViewById(R.id.gpsValueItemActivity);
        TextView itemComments = (TextView) coordinatorLayout.findViewById(R.id.commentsValueItemActivity);

        itemName.setText(currentItem.getName());

        Integer quantity = currentItem.getQuantity();
        itemQuantity.setText(quantity.toString());

        Integer targetQuantity = currentItem.getTargetQuantity();
        if(null != targetQuantity){
            itemTargetQuantity.setText(targetQuantity.toString());
            itemTargetQuantity.setTextColor(getResources().getColor(android.R.color.primary_text_light));
        }

        Integer minQuantity = currentItem.getMinQuantity();

        if(0 == quantity){
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            window.setStatusBarColor(getResources().getColor(R.color.colorDarkRed));
            itemToolbar.setBackgroundColor(getResources().getColor(R.color.colorLightRed));
            itemAppBar.setBackgroundColor(getResources().getColor(R.color.colorLightRed));
        }
        else if(null != minQuantity){
            itemMinQuantity.setText(minQuantity.toString());
            itemMinQuantity.setTextColor(getResources().getColor(android.R.color.primary_text_light));
            if(quantity < minQuantity) {
                Window window = getWindow();
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

                window.setStatusBarColor(getResources().getColor(R.color.colorDarkYellow));
                itemToolbar.setBackgroundColor(getResources().getColor(R.color.colorLightYellow));
                itemAppBar.setBackgroundColor(getResources().getColor(R.color.colorLightYellow));
            }
        }

        if(null != currentItem.getQrcode()){
            itemQrCode.setText(getResources().getString(R.string.show));
            itemQrCode.setTextColor(getResources().getColor(android.R.color.primary_text_light));
            itemQrCode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO: open dialog with QR image and options: change / close
                }
            });
        }

        if(null != currentItem.getBarcode()){
            itemBarcode.setText(getResources().getString(R.string.show));
            itemBarcode.setTextColor(getResources().getColor(android.R.color.primary_text_light));
            itemBarcode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO: open dialog with barcode and options: change / close
                }
            });
        }

        if(null != currentItem.getLatitude() && null != currentItem.getLongitude()){
            itemGPS.setText(getResources().getString(R.string.show));
            itemGPS.setTextColor(getResources().getColor(android.R.color.primary_text_light));
            itemGPS.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO: show a dialog with a map with given latitude/longitude
                }
            });
        }

        if(null != currentItem.getComment()){
            itemComments.setText(currentItem.getComment().toString());
            itemComments.setTextColor(getResources().getColor(android.R.color.primary_text_light));
        }
    }
}