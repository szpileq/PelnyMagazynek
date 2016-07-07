package com.szpilkowski.android.pelnymagazynek.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import com.szpilkowski.android.pelnymagazynek.API.ApiConnector;
import com.szpilkowski.android.pelnymagazynek.DbModels.Warehouse;
import com.szpilkowski.android.pelnymagazynek.Fragments.WarehousesFragment;
import com.szpilkowski.android.pelnymagazynek.Info.LoginInfo;
import com.szpilkowski.android.pelnymagazynek.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WarehousesActivity extends AppCompatActivity {

    ApiConnector connector;
    private static final String TAG = "WarehousesActivity";
    ArrayList<Warehouse> warehousesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warehouses);

        final View view = findViewById(R.id.coordinatorLayout);

        SharedPreferences prefs = getSharedPreferences("AppPref", MODE_PRIVATE);
        String authorizationToken = prefs.getString("AccessToken", null);

        connector = ApiConnector.getInstance();
        connector.setupApiConnector(authorizationToken);

        //Use API to get warehouses and put them on a list
        Call call = connector.apiService.getWarehouses();
        call.enqueue(new Callback<ArrayList>() {
            @Override
            public void onResponse(Call<ArrayList> call, Response<ArrayList> response) {
                int statusCode = response.code();
                if (statusCode == 200) {
                    //Success, fill up list of warehouses
                    warehousesList = response.body();
                } else if (statusCode == 401) {
                    Snackbar snackbar = Snackbar
                            .make(view, "Error with token, log in again.", Snackbar.LENGTH_LONG);

                    snackbar.show();
                }
                Log.i(TAG, "onResponse: API response handled.");
            }

            @Override
            public void onFailure(Call<ArrayList> call, Throwable t) {
                Log.i(TAG, "onFailure: API call for logging failed");
                // Log error here since request failed
            }
        });

        // Adding Toolbar to Main screen
        Toolbar toolbar = (Toolbar) findViewById(R.id.warehouse_toolbar);
        setSupportActionBar(toolbar);
        // Setting ViewPager for each Tabs
        ViewPager viewPager = (ViewPager) findViewById(R.id.warehouse_viewpager);
        setupViewPager(viewPager);
        // Set Tabs inside Toolbar
        TabLayout tabs = (TabLayout) findViewById(R.id.warehouse_tabs);
        tabs.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(new WarehousesFragment(), getString(R.string.warehousesAll));
        adapter.addFragment(new WarehousesFragment(), getString(R.string.warehousesAdmin));
        adapter.addFragment(new WarehousesFragment(), getString(R.string.warehousesEditor));
        adapter.addFragment(new WarehousesFragment(), getString(R.string.warehousesWatcher));
        viewPager.setAdapter(adapter);
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public Adapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}
