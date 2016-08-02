package com.szpilkowski.android.pelnymagazynek.Item;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.szpilkowski.android.pelnymagazynek.API.ApiConnector;
import com.szpilkowski.android.pelnymagazynek.DbModels.Item;
import com.szpilkowski.android.pelnymagazynek.R;

/**
 * Created by szpileq on 02.08.2016.
 */
public class ItemNew extends AppCompatActivity {
    ApiConnector connector;
    private static final String TAG = "ItemNewActivity";
    Item currentItem;

    View coordinatorLayout;
    FloatingActionButton fabAccept;
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
                //TODO: NewItem(currentItem)
            }
        });

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
}