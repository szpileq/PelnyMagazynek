package com.szpilkowski.android.pelnymagazynek.Item;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
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
    private static final int GET_LOCATION = 132;
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

        // Adding Toolbar to Main screen
        Toolbar toolbar = (Toolbar) findViewById(R.id.itemNew_toolbar);
        setSupportActionBar(toolbar);

        //Setting up the FAB menu
        fabAccept = (FloatingActionButton) findViewById(R.id.fabAccept);

        setupView();

        fabAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(0 == getNewItem())
                    newItemRequest(currentItem);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (GET_LOCATION == requestCode) {

            if(resultCode == 1) {
                newLatitude = (float) data.getDoubleExtra("currentLatitude", 0);
                newLongitude = (float) data.getDoubleExtra("currentLongitude", 0);

                TextView itemGps = (TextView) coordinatorLayout.findViewById(R.id.gpsValueItemNew);
                itemGps.setText(getResources().getString(R.string.change));
                itemGps.setTextColor(getResources().getColor(android.R.color.primary_text_light));

                setupView();
            } else {
                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, getString(R.string.gpsCancelled), Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        } else {
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (result != null) {
                if (result.getContents() == null) {
                    Snackbar snackbar = Snackbar
                            .make(coordinatorLayout, getString(R.string.scannerCancelled), Snackbar.LENGTH_LONG);
                    snackbar.show();
                } else {
                    String resultString = result.getContents();
                    Integer resultHash = resultString.hashCode();
                    if (result.getFormatName().equals("QR_CODE")) {
                        newQrCode = resultHash.toString();
                        TextView itemQrCode = (TextView) coordinatorLayout.findViewById(R.id.qrCodeValueItemNew);
                        itemQrCode.setText(getResources().getString(R.string.change));
                        itemQrCode.setTextColor(getResources().getColor(android.R.color.primary_text_light));

                    } else {
                        newBarcode = resultHash.toString();
                        TextView itemBarcode = (TextView) coordinatorLayout.findViewById(R.id.barcodeValueItemNew);
                        itemBarcode.setText(getResources().getString(R.string.change));
                        itemBarcode.setTextColor(getResources().getColor(android.R.color.primary_text_light));
                    }
                    setupView();
                }
            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
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

        TextView itemQrCode = (TextView) coordinatorLayout.findViewById(R.id.qrCodeValueItemNew);
        TextView itemBarcode = (TextView) coordinatorLayout.findViewById(R.id.barcodeValueItemNew);
        TextView itemGPS = (TextView) coordinatorLayout.findViewById(R.id.gpsValueItemNew);

        itemQrCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator intent = new IntentIntegrator(ItemNew.this);
                intent.addExtra("SCAN_MODE", "QR_CODE_MODE");
                intent.initiateScan();
            }
        });

        itemBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator intent = new IntentIntegrator(ItemNew.this);
                intent.addExtra("SCAN_MODE", "PRODUCT_MODE");
                intent.initiateScan();
            }
        });
        itemGPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(ItemNew.this, MapsActivity.class), GET_LOCATION);
            }
        });
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