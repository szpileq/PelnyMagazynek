package com.szpilkowski.android.pelnymagazynek.ItemsList;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.szpilkowski.android.pelnymagazynek.API.ApiConnector;
import com.szpilkowski.android.pelnymagazynek.DbModels.Item;
import com.szpilkowski.android.pelnymagazynek.Item.ItemActivity;
import com.szpilkowski.android.pelnymagazynek.R;
import com.szpilkowski.android.pelnymagazynek.Users.UsersActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by szpileq on 2016-07-24.
 */
public class ItemsActivity extends AppCompatActivity implements ItemsManipulator {

    ApiConnector connector;
    private static final String TAG = "ItemsActivity";
    protected Integer warehouseId;
    protected String warehouseName;
    protected String warehouseRole;

    List<Item> itemsList;
    List<Item> lowQuantityItemsList;
    List<Item> shortageItemsList;

    ViewPager viewPager;
    Adapter adapter;
    View coordinatorLayout;

    FloatingActionMenu fabMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);
        coordinatorLayout = findViewById(R.id.coordinatorLayoutItems); // for snackbar purposes

        //Setup API connector
        SharedPreferences prefs = getSharedPreferences("AppPref", MODE_PRIVATE);
        String authorizationToken = prefs.getString("AccessToken", null);
        warehouseId = prefs.getInt("warehouseId", 0);
        warehouseName = prefs.getString("warehouseName", null);
        warehouseRole = prefs.getString("warehouseRole", null);

        this.setTitle(warehouseName);

        connector = ApiConnector.getInstance();
        connector.setupApiConnector(authorizationToken);

        //Use API to get warehouses and put them on a list
        getItemsList();

        // Adding Toolbar to Main screen
        Toolbar toolbar = (Toolbar) findViewById(R.id.items_toolbar);
        setSupportActionBar(toolbar);

        //Setting up the FAB menu
        fabMenu = (FloatingActionMenu) findViewById(R.id.fabMenu);
        fabMenu.setClosedOnTouchOutside(true);

        FloatingActionButton addItemFabButton = (FloatingActionButton) findViewById(R.id.addItemFabButton);
        FloatingActionButton qrFabButton = (FloatingActionButton) findViewById(R.id.qrFabButton);
        FloatingActionButton barcodeFabButton = (FloatingActionButton) findViewById(R.id.barcodeFabButton);
        FloatingActionButton manageUsersFabButton = (FloatingActionButton) findViewById(R.id.manageUsersFabButton);

        addItemFabButton.setOnClickListener(fabMenuClickListeners);
        qrFabButton.setOnClickListener(fabMenuClickListeners);
        barcodeFabButton.setOnClickListener(fabMenuClickListeners);
        manageUsersFabButton.setOnClickListener(fabMenuClickListeners);

    }


    private void getItemsList() {

        Call call = connector.apiService.getItems(warehouseId);
        call.enqueue(new Callback<List<Item>>() {
            @Override
            public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
                int statusCode = response.code();
                if (statusCode == 200) {

                    //Success, fill up list of warehouses
                    itemsList = new ArrayList<Item>();
                    itemsList.addAll(response.body());
                    Collections.sort(itemsList, new ItemComparator());
                    splitItemsByQuantity();

                    // Setting ViewPager for each Tabs
                    viewPager = (ViewPager) findViewById(R.id.items_viewpager);
                    setupViewPager(viewPager);

                    // Set Tabs inside Toolbar
                    TabLayout tabs = (TabLayout) findViewById(R.id.items_tabs);
                    tabs.setupWithViewPager(viewPager);

                } else if (statusCode == 401) {
                    Snackbar snackbar = Snackbar
                            .make(coordinatorLayout, "Error with token, log in again.", Snackbar.LENGTH_LONG);

                    snackbar.show();
                }
                Log.i(TAG, "onResponse: API response handled.");
            }

            @Override
            public void onFailure(Call<List<Item>> call, Throwable t) {
                Log.i(TAG, "onFailure: API call for logging failed");
                // Log error here since request failed
            }
        });
    }

    // Divide warehousesList into role-based list that would be used by a sorting tab
    private void splitItemsByQuantity() {
        lowQuantityItemsList = new ArrayList<Item>();
        shortageItemsList = new ArrayList<Item>();
        for (Item i : itemsList) {
            Integer currentQuantity = i.getQuantity();
            Integer currentMinQuantity = i.getMinQuantity();
            if (0 == currentQuantity) {
                shortageItemsList.add(i);
            } else if (currentMinQuantity != null && currentQuantity < i.getMinQuantity()) {
                lowQuantityItemsList.add(i);
            }
        }
        Collections.sort(shortageItemsList, new ItemComparator());
        Collections.sort(lowQuantityItemsList, new ItemComparator());
    }

    @Override
    public List<Item> getItems(String fragmentQuantityMarker) {
        switch (fragmentQuantityMarker) {
            case "all":
                return itemsList;
            case "lowquantity":
                return lowQuantityItemsList;
            case "shortage":
                return shortageItemsList;
            default:
                throw new RuntimeException(); // Something created fragment with unsupported role
        }
    }

    @Override
    public int removeItemRequest(final Item i) {
/*
        if(!warehouseRole.equals("admin")){
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, getString(R.string.wrongPrivileges), Snackbar.LENGTH_LONG);
            snackbar.show();
            return 1;
        }*/
        Call call = connector.apiService.removeItem(i.getId());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                int statusCode = response.code();
                if (statusCode == 204) {
                    Log.i(TAG, "onResponse: API response handled. Removing item");
                    String itemName = i.getName();
                    removeItem(i);
                    Snackbar snackbar = Snackbar
                            .make(coordinatorLayout, getString(R.string.succesRemove) + " " + itemName, Snackbar.LENGTH_LONG);
                    snackbar.show();

                } else if (statusCode == 404) {
                    Log.i(TAG, "onResponse: API response handled. There is no such item");
                } else if (statusCode == 401) {
                    Log.i(TAG, "onResponse: API response handled. Wrong authorization token. ");
                    Snackbar snackbar = Snackbar
                            .make(coordinatorLayout, getString(R.string.authorizationFailRelog), Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i(TAG, "onFailure: API call for removing item failed");
                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, getString(R.string.apiCallFailed), Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });
        return 0;
    }

    private int removeItem(Item i) {
        itemsList.remove(i);

        Integer currentQuantity = i.getQuantity();
        Integer currentMinQuantity = i.getMinQuantity();
        if (0 == currentQuantity) {
            shortageItemsList.remove(i);
        } else if (currentMinQuantity != null && currentQuantity < i.getMinQuantity()) {
            lowQuantityItemsList.remove(i);
        }

        adapter.notifyDataSetChanged();
        return 0;
    }

    @Override
    public int openItem(Item i) {
        Log.i(TAG, "openItem: will open " + i.getName());
        Intent newActivity = new Intent(this, ItemActivity.class);
        newActivity.putExtra("itemId", i.getId());
        startActivity(newActivity);
        return 1;
    }

    public int newItemRequest(Item i) {

        if (warehouseRole.equals("watcher")) {
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, getString(R.string.wrongPrivileges), Snackbar.LENGTH_LONG);
            snackbar.show();
            return 1;
        }

        Call call = connector.apiService.addItem(warehouseId, i);
        call.enqueue(new Callback<Item>() {
            @Override
            public void onResponse(Call<Item> call, Response<Item> response) {
                int statusCode = response.code();
                if (statusCode == 201) {
                    Log.i(TAG, "onResponse: API response handled. Adding item");
                    //Success, fill up list of warehouses
                    Item temp = response.body();
                    addItem(temp);

                } else if (statusCode == 409) {
                    Log.i(TAG, "onResponse: API response handled. This user is already in a warehouse");
                    Snackbar snackbar = Snackbar
                            .make(coordinatorLayout, getString(R.string.userExistsInWarehouse), Snackbar.LENGTH_LONG);
                    snackbar.show();
                } else if (statusCode == 404) {
                    Log.i(TAG, "onResponse: API response handled. There is no such usere");
                    Snackbar snackbar = Snackbar
                            .make(coordinatorLayout, getString(R.string.userNotFound), Snackbar.LENGTH_LONG);
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

    private int addItem(Item i) {
        itemsList.add(i);
        Collections.sort(itemsList, new ItemComparator());

        Integer currentQuantity = i.getQuantity();
        Integer currentMinQuantity = i.getMinQuantity();
        if (0 == currentQuantity) {
            shortageItemsList.add(i);
        } else if (currentMinQuantity != null && currentQuantity < i.getMinQuantity()) {
            lowQuantityItemsList.add(i);
        }

        adapter.notifyDataSetChanged();
        return 0;
    }

    //OnClickListener for Floating Action Menu buttons
    private View.OnClickListener fabMenuClickListeners = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.addItemFabButton:
                    //Create newItem activity in which we would utilize addItem
                    break;
                case R.id.qrFabButton:
                    //Use QR scanner to find/add product
                    break;
                case R.id.barcodeFabButton:
                    //Use barcode scanner to find/add product
                    break;
                case R.id.manageUsersFabButton:
                    showUsersActivity();
                    break;
            }
        }
    };

    private void showUsersActivity() {
        Intent ItemsActivity = new Intent(this, UsersActivity.class);
        startActivity(ItemsActivity);
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(ItemsFragment.createInstance("all"), getString(R.string.itemaAll));
        adapter.addFragment(ItemsFragment.createInstance("lowquantity"), getString(R.string.itemsLowQuantityTitle));
        adapter.addFragment(ItemsFragment.createInstance("shortage"), getString(R.string.itemsShortageTabTitle));
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
