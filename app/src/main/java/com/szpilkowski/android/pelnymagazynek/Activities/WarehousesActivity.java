package com.szpilkowski.android.pelnymagazynek.Activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
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

import java.util.ArrayList;
import java.util.List;

import com.szpilkowski.android.pelnymagazynek.API.ApiConnector;
import com.szpilkowski.android.pelnymagazynek.DbModels.Warehouse;
import com.szpilkowski.android.pelnymagazynek.Fragments.WarehousesFragment;
import com.szpilkowski.android.pelnymagazynek.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WarehousesActivity extends AppCompatActivity implements WarehousesFragment.WarehousesProvider {

    ApiConnector connector;
    private static final String TAG = "WarehousesActivity";
    private BottomSheetBehavior newWarehouseBottomSheetBehavior;
    List<Warehouse> warehousesList;
    List<Warehouse> adminWarehousesList;
    List<Warehouse> editorWarehousesList;
    List<Warehouse> watcherWarehousesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warehouses);

        final View view = findViewById(R.id.coordinatorLayout); // for snackbar purposes

        //Setup API connector
        SharedPreferences prefs = getSharedPreferences("AppPref", MODE_PRIVATE);
        String authorizationToken = prefs.getString("AccessToken", null);
        connector = ApiConnector.getInstance();
        connector.setupApiConnector(authorizationToken);

        //Use API to get warehouses and put them on a list
        getWarehousesList();

        // Adding Toolbar to Main screen
        Toolbar toolbar = (Toolbar) findViewById(R.id.warehouse_toolbar);
        setSupportActionBar(toolbar);

        View bottomSheet = findViewById( R.id.bottom_sheet );
        newWarehouseBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);

        View floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setBackgroundTintList(getResources().getColorStateList(R.color.colorLightRed));

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newWarehouseBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
    }

    private void getWarehousesList() {
        final View view = findViewById(R.id.coordinatorLayout);

        Call call = connector.apiService.getWarehouses();
        call.enqueue(new Callback<List<Warehouse>>() {
            @Override
            public void onResponse(Call<List<Warehouse>> call, Response<List<Warehouse>> response) {
                int statusCode = response.code();
                if (statusCode == 200) {

                    //Success, fill up list of warehouses
                    warehousesList = new ArrayList<Warehouse>();
                    warehousesList.addAll(response.body());
                    splitListByRoles();

                    // Setting ViewPager for each Tabs
                    ViewPager viewPager = (ViewPager) findViewById(R.id.warehouse_viewpager);
                    setupViewPager(viewPager);

                    // Set Tabs inside Toolbar
                    TabLayout tabs = (TabLayout) findViewById(R.id.warehouse_tabs);
                    tabs.setupWithViewPager(viewPager);

                } else if (statusCode == 401) {
                    Snackbar snackbar = Snackbar
                            .make(view, "Error with token, log in again.", Snackbar.LENGTH_LONG);

                    snackbar.show();
                }
                Log.i(TAG, "onResponse: API response handled.");
            }

            @Override
            public void onFailure(Call<List<Warehouse>> call, Throwable t) {
                Log.i(TAG, "onFailure: API call for logging failed");
                // Log error here since request failed
            }
        });
    }

    // Divide warehousesList into role-based list that would be used by a sorting tab
    private void splitListByRoles() {
        adminWarehousesList = new ArrayList<Warehouse>();
        editorWarehousesList = new ArrayList<Warehouse>();
        watcherWarehousesList = new ArrayList<Warehouse>();
        for (Warehouse w : warehousesList) {
            switch (w.getRole()) {
                case "admin":
                    adminWarehousesList.add(w);
                    break;
                case "editor":
                    editorWarehousesList.add(w);
                    break;
                case "watcher":
                    watcherWarehousesList.add(w);
                    break;
                default:
                    throw new RuntimeException(); // Found an element with unsupported role set
            }
        }
    }

    @Override
    public List<Warehouse> getWarehouses(String role) {
        switch (role) {
            case "admin":
                return adminWarehousesList;
            case "editor":
                return editorWarehousesList;
            case "watcher":
                return watcherWarehousesList;
            case "all":
                return warehousesList;
            default:
                throw new RuntimeException(); // Something created fragment with unsupported role
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(WarehousesFragment.createInstance("all"), getString(R.string.warehousesAll));
        adapter.addFragment(WarehousesFragment.createInstance("admin"), getString(R.string.warehousesAdmin));
        adapter.addFragment(WarehousesFragment.createInstance("editor"), getString(R.string.warehousesEditor));
        adapter.addFragment(WarehousesFragment.createInstance("watcher"), getString(R.string.warehousesWatcher));
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
