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
public class ItemEdit extends AppCompatActivity {
    ApiConnector connector;
    private static final String TAG = "ItemEditActivity";
    Item currentItem;

    View coordinatorLayout;
    FloatingActionButton fabAccept;
    Toolbar itemToolbar = (Toolbar) coordinatorLayout.findViewById(R.id.itemEdit_toolbar);
    AppBarLayout itemAppBar = (AppBarLayout) coordinatorLayout.findViewById(R.id.AppBarLayoutItemEdit);

    TextView itemName;
    TextView itemQuantity;
    TextView itemTargetQuantity;
    TextView itemMinQuantity;
    TextView itemQrCode;
    TextView itemBarcode;
    TextView itemGPS;
    TextView itemComments;




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

        connector = ApiConnector.getInstance();
        connector.setupApiConnector(authorizationToken);

        Intent intent = getIntent();
        currentItem = intent.getParcelableExtra("currentItem");
        setupView();

        // Adding Toolbar to Main screen
        Toolbar toolbar = (Toolbar) findViewById(R.id.itemEdit_toolbar);
        setSupportActionBar(toolbar);

        //Setting up the FAB menu
        fabAccept = (FloatingActionButton) findViewById(R.id.fabAccept);

        fabAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Item edited = getEditedItem();
                //TODO: editItem(currentItem)
            }
        });

    }

    private Item getEditedItem(){
        Item edited = new Item();
        edited.setId(currentItem.getId());
        edited.setName(itemName.getText().toString());
        edited.setQuantity(Integer.parseInt(itemQuantity.getText().toString()));

        if(null != itemMinQuantity.getText().toString())
            edited.setMinQuantity(Integer.parseInt(itemMinQuantity.getText().toString()));

        if(null != itemTargetQuantity.getText().toString())
            edited.setTargetQuantity(Integer.parseInt(itemTargetQuantity.getText().toString()));

        if(null != itemQrCode.getText().toString())
            edited.set
        return edited;
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
        itemQuantity.setHint(quantity.toString());

        Integer targetQuantity = currentItem.getTargetQuantity();
        if (null != targetQuantity) {
            itemTargetQuantity.setHint(targetQuantity.toString());
            itemTargetQuantity.setTextColor(getResources().getColor(android.R.color.primary_text_light));
        }

        Integer minQuantity = currentItem.getMinQuantity();
        if (null != minQuantity) {
            itemMinQuantity.setHint(minQuantity.toString());
            itemMinQuantity.setTextColor(getResources().getColor(android.R.color.primary_text_light));
        }

        if (null != currentItem.getQrcode()) {
            itemQrCode.setText(getResources().getString(R.string.change));
            itemQrCode.setTextColor(getResources().getColor(android.R.color.primary_text_light));
            itemQrCode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO: open dialog with QR image and options: change / close
                }
            });
        }

        if (null != currentItem.getBarcode()) {
            itemBarcode.setText(getResources().getString(R.string.change));
            itemBarcode.setTextColor(getResources().getColor(android.R.color.primary_text_light));
            itemBarcode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO: open dialog with barcode and options: change / close
                }
            });
        }

        if (null != currentItem.getLatitude() && null != currentItem.getLongitude()) {
            itemGPS.setText(getResources().getString(R.string.change));
            itemGPS.setTextColor(getResources().getColor(android.R.color.primary_text_light));
            itemGPS.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO: show a dialog with a map with given latitude/longitude
                }
            });
        }

        if (null != currentItem.getComment()) {
            itemComments.setText(currentItem.getComment().toString());
            itemComments.setTextColor(getResources().getColor(android.R.color.primary_text_light));
        }
    }
}