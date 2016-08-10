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
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.szpilkowski.android.pelnymagazynek.API.ApiConnector;
import com.szpilkowski.android.pelnymagazynek.DbModels.Item;
import com.szpilkowski.android.pelnymagazynek.ItemsList.ItemsManipulator;
import com.szpilkowski.android.pelnymagazynek.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by szpileq on 2016-07-28.
 */
public class ItemEdit extends AppCompatActivity {
    ApiConnector connector;
    private static final String TAG = "ItemEditActivity";
    Item currentItem;
    private static final int GET_LOCATION = 132;

    View coordinatorLayout;
    FloatingActionButton fabAccept;

    TextView itemName;
    TextView itemQuantity;
    TextView itemTargetQuantity;
    TextView itemMinQuantity;
    TextView itemQrCode;
    TextView itemBarcode;
    TextView itemGPS;
    TextView itemComments;

    String newQrCode;
    String newBarcode;
    Float newLongitude;
    Float newLatitude;
    String newGeocode;
    String initialGeocode;

    protected String warehouseRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_edit);
        coordinatorLayout = findViewById(R.id.coordinatorLayoutItemEdit); // for snackbar purposes

        String title = getResources().getString(R.string.itemEditTitle);
        this.setTitle(title);

        //Setup API connector
        SharedPreferences prefs = getSharedPreferences("AppPref", MODE_PRIVATE);
        String authorizationToken = prefs.getString("AccessToken", null);
        warehouseRole = prefs.getString("warehouseRole", null);

        connector = ApiConnector.getInstance();
        connector.setupApiConnector(authorizationToken);

        Intent intent = getIntent();
        currentItem = intent.getParcelableExtra("currentItem");
        initialGeocode = intent.getStringExtra("geocode");
        setupView();

        // Adding Toolbar to Main screen
        Toolbar toolbar = (Toolbar) findViewById(R.id.itemEdit_toolbar);
        setSupportActionBar(toolbar);

        //Setting up the FAB menu
        fabAccept = (FloatingActionButton) findViewById(R.id.fabAccept);

        fabAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEditedItem();
                editItemRequest(currentItem);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (GET_LOCATION == requestCode) {

            if(resultCode == 1) {
                newLatitude = (float) data.getDoubleExtra("currentLatitude", 0);
                newLongitude = (float) data.getDoubleExtra("currentLongitude", 0);
                newGeocode = data.getStringExtra("geocode");

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
                    } else {
                        newBarcode = resultHash.toString();
                    }
                    setupView();
                }
            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    private void getEditedItem(){

        if(itemName.getText().toString().equals(currentItem.getName()))
            currentItem.setName(null);
        else
            currentItem.setName(itemName.getText().toString());

        if(!itemQuantity.getText().toString().equals(currentItem.getQuantity().toString()))
            currentItem.setQuantity(Integer.parseInt(itemQuantity.getText().toString()));

        if(!itemMinQuantity.getText().toString().equals(""))
            currentItem.setMinQuantity(Integer.parseInt(itemMinQuantity.getText().toString()));

        if(!itemTargetQuantity.getText().toString().equals(""))
            currentItem.setTargetQuantity(Integer.parseInt(itemTargetQuantity.getText().toString()));

        if(null != newQrCode)
            currentItem.setQrcode(newQrCode);

        if(null != newBarcode)
            currentItem.setBarcode(newBarcode);

        if(null != newLongitude && null != newLatitude) {
            currentItem.setLongitude(newLongitude);
            currentItem.setLatitude(newLatitude);
        }

        if(null != itemComments.getText().toString()){
            currentItem.setComment(itemComments.getText().toString());
        }

    }

    private void setupView() {

        itemName = (TextView) coordinatorLayout.findViewById(R.id.itemEditNameHeader);
        itemQuantity = (TextView) coordinatorLayout.findViewById(R.id.itemEditQuantityHeader);
        itemTargetQuantity = (TextView) coordinatorLayout.findViewById(R.id.targetQuantityValueItemEdit);
        itemMinQuantity = (TextView) coordinatorLayout.findViewById(R.id.minQuantityValueItemEdit);
        itemQrCode = (TextView) coordinatorLayout.findViewById(R.id.qrCodeValueItemEdit);
        itemBarcode = (TextView) coordinatorLayout.findViewById(R.id.barcodeValueItemEdit);
        itemGPS = (TextView) coordinatorLayout.findViewById(R.id.gpsValueItemEdit);
        itemComments = (TextView) coordinatorLayout.findViewById(R.id.commentsValueItemEdit);

        itemName.setText(currentItem.getName());

        Integer quantity = currentItem.getQuantity();
        itemQuantity.setText(quantity.toString());

        Integer targetQuantity = currentItem.getTargetQuantity();
        if (null != targetQuantity) {
            itemTargetQuantity.setText(targetQuantity.toString());
            itemTargetQuantity.setTextColor(getResources().getColor(android.R.color.primary_text_light));
        }

        Integer minQuantity = currentItem.getMinQuantity();
        if (null != minQuantity) {
            itemMinQuantity.setText(minQuantity.toString());
            itemMinQuantity.setTextColor(getResources().getColor(android.R.color.primary_text_light));
        }

        if (null != newQrCode || null != currentItem.getQrcode()) {
            itemQrCode.setText(getResources().getString(R.string.change));
            itemQrCode.setTextColor(getResources().getColor(android.R.color.primary_text_light));
            itemQrCode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    IntentIntegrator intent = new IntentIntegrator(ItemEdit.this);
                    intent.addExtra("SCAN_MODE","QR_CODE_MODE");
                    intent.initiateScan();
                }
            });
        } else{
            itemQrCode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    IntentIntegrator intent = new IntentIntegrator(ItemEdit.this);
                    intent.addExtra("SCAN_MODE","QR_CODE_MODE");
                    intent.initiateScan();
                }
            });
        }

        if (null != newBarcode || null != currentItem.getBarcode()) {
            itemBarcode.setText(getResources().getString(R.string.change));
            itemBarcode.setTextColor(getResources().getColor(android.R.color.primary_text_light));
            itemBarcode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    IntentIntegrator intent = new IntentIntegrator(ItemEdit.this);
                    intent.addExtra("SCAN_MODE","PRODUCT_MODE");
                    intent.initiateScan();
                }
            });
        }
        else{
            itemBarcode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    IntentIntegrator intent = new IntentIntegrator(ItemEdit.this);
                    intent.addExtra("SCAN_MODE","PRODUCT_MODE");
                    intent.initiateScan();
                }
            });
        }

        if ((null != currentItem.getLatitude() && null != currentItem.getLongitude())
                || (null != newLatitude && null != newLongitude))  {

            if(null != newGeocode)
                itemGPS.setText(newGeocode);
            else
                itemGPS.setText(initialGeocode);

            itemGPS.setTextColor(getResources().getColor(android.R.color.primary_text_light));
            itemGPS.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent editLocation = new Intent(ItemEdit.this, EditMapPosition.class);
                    editLocation.putExtra("lat", newLatitude);
                    editLocation.putExtra("lng", newLongitude);
                    startActivityForResult(editLocation, GET_LOCATION);
                }
            });
        }
        else{
            itemGPS.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivityForResult(new Intent(ItemEdit.this, NewMapPosition.class), GET_LOCATION);
                }
            });
        }

        if (null != currentItem.getComment()) {
            itemComments.setText(currentItem.getComment().toString());
            itemComments.setTextColor(getResources().getColor(android.R.color.primary_text_light));
        }
    }

    public int editItemRequest(Item i) {

        if (warehouseRole.equals("watcher")) {
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, getString(R.string.wrongPrivileges), Snackbar.LENGTH_LONG);
            snackbar.show();
            return 1;
        }
        Call call = connector.apiService.editItem(i.getId(), i);
        call.enqueue(new Callback<Item>() {
            @Override
            public void onResponse(Call<Item> call, Response<Item> response) {
                int statusCode = response.code();
                if (statusCode == 200) {
                    currentItem = response.body();
                    Intent resultItem = new Intent();
                    resultItem.putExtra("currentItem", currentItem);
                    setResult(1, resultItem);
                    finish();

                } else if (statusCode == 205) {
                    Snackbar snackbar = Snackbar
                            .make(coordinatorLayout, getString(R.string.someoneHasUpdated) , Snackbar.LENGTH_LONG);
                    snackbar.show();
                    currentItem = response.body();
                    setupView();
                }
                else if (statusCode == 401) {
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
        return 0;
    }

}