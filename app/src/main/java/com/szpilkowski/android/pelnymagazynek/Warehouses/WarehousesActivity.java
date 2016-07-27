package com.szpilkowski.android.pelnymagazynek.Warehouses;

import android.content.Intent;
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
import java.util.Collections;
import java.util.List;

import com.szpilkowski.android.pelnymagazynek.API.ApiConnector;
import com.szpilkowski.android.pelnymagazynek.DbModels.Warehouse;
import com.szpilkowski.android.pelnymagazynek.Items.ItemsActivity;
import com.szpilkowski.android.pelnymagazynek.R;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WarehousesActivity extends AppCompatActivity implements
        WarehouseManipulator{

    SharedPreferences pref;
    ApiConnector connector;
    private static final String TAG = "WarehousesActivity";
    private BottomSheetBehavior newWarehouseBottomSheetBehavior;
    List<Warehouse> warehousesList;
    List<Warehouse> adminWarehousesList;
    List<Warehouse> editorWarehousesList;
    List<Warehouse> watcherWarehousesList;

    ViewPager viewPager;
    Adapter adapter;
    View coordinatorLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warehouses);

        coordinatorLayout = findViewById(R.id.coordinatorLayoutWarehouses); // for snackbar purposes

        //Setup API connector
        pref = getSharedPreferences("AppPref", MODE_PRIVATE);
        String authorizationToken = pref.getString("AccessToken", null);
        connector = ApiConnector.getInstance();
        connector.setupApiConnector(authorizationToken);

        //Use API to get warehouses and put them on a list
        getWarehousesList();

        // Adding Toolbar to Main screen
        Toolbar toolbar = (Toolbar) findViewById(R.id.warehouse_toolbar);
        setSupportActionBar(toolbar);

        final NewWarehouseModalBottomSheet modalBottomSheet = new NewWarehouseModalBottomSheet();

        View floatingActionButton = findViewById(R.id.fab_warehouses);
        floatingActionButton.setBackgroundTintList(getResources().getColorStateList(R.color.colorLightRed));

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modalBottomSheet.show(getSupportFragmentManager(), "NewWarehouseModalBottomSheet");
            }
        });

    }

    private void getWarehousesList() {

        Call call = connector.apiService.getWarehouses();
        call.enqueue(new Callback<List<Warehouse>>() {
            @Override
            public void onResponse(Call<List<Warehouse>> call, Response<List<Warehouse>> response) {
                int statusCode = response.code();
                if (statusCode == 200) {

                    //Success, fill up list of warehouses
                    warehousesList = new ArrayList<Warehouse>();
                    warehousesList.addAll(response.body());
                    Collections.sort(warehousesList, new WarehouseComparator());
                    splitListByRoles();

                    // Setting ViewPager for each Tabs
                    viewPager = (ViewPager) findViewById(R.id.warehouse_viewpager);
                    setupViewPager(viewPager);

                    // Set Tabs inside Toolbar
                    TabLayout tabs = (TabLayout) findViewById(R.id.warehouse_tabs);
                    tabs.setupWithViewPager(viewPager);

                } else if (statusCode == 401) {
                    Snackbar snackbar = Snackbar
                            .make(coordinatorLayout, "Error with token, log in again.", Snackbar.LENGTH_LONG);

                    snackbar.show();
                }
                Log.i(TAG, "onResponse: API response handled.");
            }

            @Override
            public void onFailure(Call<List<Warehouse>> call, Throwable t) {
                Log.i(TAG, "onFailure: API call for logging failed");
                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, getString(R.string.apiCallFailed), Snackbar.LENGTH_LONG);
                snackbar.show();
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
        Collections.sort(adminWarehousesList, new WarehouseComparator());
        Collections.sort(editorWarehousesList, new WarehouseComparator());
        Collections.sort(watcherWarehousesList, new WarehouseComparator());
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

    @Override
    public int newWarehouseRequest(String name, final NewWarehouseModalBottomSheet mbs) {
        ApiConnector connector = ApiConnector.getInstance();

        Warehouse w = new Warehouse();
        w.setName(name);

        Call call = connector.apiService.addWarehouse(w);
        call.enqueue(new Callback<Warehouse>() {
            @Override
            public void onResponse(Call<Warehouse> call, Response<Warehouse> response) {
                int statusCode = response.code();
                if (statusCode == 201) {
                    Log.i(TAG, "onResponse: API response handled. Adding warehouse");
                    //Success, fill up list of warehouses
                    Warehouse temp = response.body();
                    temp.setRole("admin");
                    addWarehouse(temp);
                    mbs.dismiss();

                } else if (statusCode == 422) {
                    Log.i(TAG, "onResponse: API response handled. This name is taken");
                    Snackbar snackbar = Snackbar
                            .make(mbs.contentView.getRootView(), getString(R.string.nameTaken), Snackbar.LENGTH_LONG);
                    snackbar.show();
                } else if (statusCode == 401) {
                    Log.i(TAG, "onResponse: API response handled. Wrong authorization token. ");
                    Snackbar snackbar = Snackbar
                            .make(mbs.contentView.getRootView(), getString(R.string.authorizationFailRelog), Snackbar.LENGTH_LONG);
                    snackbar.show();
                    //TODO: Consider switching automatically to MainActivity
                }

            }

            @Override
            public void onFailure(Call<Warehouse> call, Throwable t) {
                Log.i(TAG, "onFailure: API call for adding warehouse failed");
                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, getString(R.string.apiCallFailed), Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });
        return 1;
    }

    private int addWarehouse(Warehouse w) {
        warehousesList.add(w);
        Collections.sort(warehousesList, new WarehouseComparator());
        switch (w.getRole()) {
            case "admin":
                adminWarehousesList.add(w);
                Collections.sort(adminWarehousesList, new WarehouseComparator());
                break;
            case "editor":
                editorWarehousesList.add(w);
                Collections.sort(editorWarehousesList, new WarehouseComparator());
                break;
            case "watcher":
                watcherWarehousesList.add(w);
                Collections.sort(watcherWarehousesList, new WarehouseComparator());
                break;
            default:
                throw new RuntimeException(); // Something created fragment with unsupported role
        }
        adapter.notifyDataSetChanged();
        return 0;
    }

    @Override
    public int removeWarehouseRequest(final Warehouse w) {
        if(!w.getRole().equals("admin")){
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, getString(R.string.wrongPrivileges), Snackbar.LENGTH_LONG);
            snackbar.show();
            return 1;
        }
        Call call = connector.apiService.removeWarehouse(w.getId());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                int statusCode = response.code();
                if (statusCode == 204) {
                    Log.i(TAG, "onResponse: API response handled. Removing warehouse");
                    String warehouseName = w.getName();
                    removeWarehouse(w);

                    Snackbar snackbar = Snackbar
                            .make(coordinatorLayout, getString(R.string.succesRemove) + " " + warehouseName, Snackbar.LENGTH_LONG);
                    snackbar.show();

                } else if (statusCode == 404) {
                    Log.i(TAG, "onResponse: API response handled. This name is taken");
                } else if (statusCode == 401) {
                    Log.i(TAG, "onResponse: API response handled. Wrong authorization token. ");
                    Snackbar snackbar = Snackbar
                            .make(coordinatorLayout, getString(R.string.authorizationFailRelog), Snackbar.LENGTH_LONG);
                    snackbar.show();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i(TAG, "onFailure: API call for removing warehouse failed");
                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, getString(R.string.apiCallFailed), Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });
        return 0;
    }

    private int removeWarehouse(Warehouse w) {
        warehousesList.remove(w);
        switch (w.getRole()) {
            case "admin":
                adminWarehousesList.remove(w);
                break;
            case "editor":
                editorWarehousesList.remove(w);
                break;
            case "watcher":
                watcherWarehousesList.remove(w);
                break;
            default:
                throw new RuntimeException(); // Something created fragment with unsupported role
        }
        adapter.notifyDataSetChanged();
        return 0;
    }

    @Override
    public int showEditModalBottomSheet(EditWarehouseModalBottomSheet mbs) {
        mbs.show(getSupportFragmentManager(), "EditWarehouseModalBottomSheet");
        return 0;
    }

    @Override
    public int editWarehouseRequest(final Warehouse w, final String newName, final EditWarehouseModalBottomSheet mbs) {

        if(!w.getRole().equals("admin")){
            mbs.dismiss();
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, getString(R.string.wrongPrivileges), Snackbar.LENGTH_LONG);
            snackbar.show();
            return 1;
        }

        Call call = connector.apiService.editWarehouse(w.getId(), w);
        call.enqueue(new Callback<Warehouse>() {
            @Override
            public void onResponse(Call<Warehouse> call, Response<Warehouse> response) {
                int statusCode = response.code();
                if (statusCode == 200) {
                    Log.i(TAG, "onResponse: API response handled. Adding warehouse");

                    editWarehouse(w, newName);
                    mbs.dismiss();

                } else if (statusCode == 422) {
                    Log.i(TAG, "onResponse: API response handled. This name is taken");
                    Snackbar snackbar = Snackbar
                            .make(mbs.contentView.getRootView(), getString(R.string.nameTaken), Snackbar.LENGTH_LONG);
                    snackbar.show();
                } else if (statusCode == 401) {
                    Log.i(TAG, "onResponse: API response handled. Wrong authorization token. ");
                    Snackbar snackbar = Snackbar
                            .make(mbs.contentView.getRootView(), getString(R.string.authorizationFailRelog), Snackbar.LENGTH_LONG);
                    snackbar.show();
                    //TODO: Consider switching automatically to MainActivity
                }

            }

            @Override
            public void onFailure(Call<Warehouse> call, Throwable t) {

                Log.i(TAG, "onFailure: API call for adding warehouse failed");
                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, getString(R.string.apiCallFailed), Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });
        return 1;
    }

    private int editWarehouse(Warehouse w, String newName) {
        int index = warehousesList.indexOf(w);
        warehousesList.get(index).setName(newName);
        adapter.notifyDataSetChanged();
        return 0;
    }

    @Override
    public int openWarehouse(Warehouse w) {
        SharedPreferences.Editor edit = pref.edit();
        edit.putInt("warehouseId", w.getId());
        edit.putString("warehouseName", w.getName());
        edit.putString("warehouseRole", w.getRole());
        edit.commit();
        Intent ItemsActivity = new Intent(this, ItemsActivity.class);
        startActivity(ItemsActivity);
        return 1;
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new Adapter(getSupportFragmentManager());
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

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }

}
