package com.szpilkowski.android.pelnymagazynek.Users;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
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
import android.widget.TextView;

import com.szpilkowski.android.pelnymagazynek.API.ApiConnector;
import com.szpilkowski.android.pelnymagazynek.DbModels.User;
import com.szpilkowski.android.pelnymagazynek.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by szpileq on 2016-07-27.
 */
public class UsersActivity extends AppCompatActivity implements
        UsersManipulator {

    SharedPreferences pref;
    ApiConnector connector;
    private static final String TAG = "UsersActivity";

    protected Integer warehouseId;
    protected String warehouseName;
    protected String warehouseRole;

    private BottomSheetBehavior newUsersBottomSheetBehavior;
    List<User> usersList;
    List<User> adminUsersList;
    List<User> editorUsersList;
    List<User> watcherUsersList;

    ViewPager viewPager;
    Adapter adapter;
    View coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        coordinatorLayout = findViewById(R.id.coordinatorLayoutUsers); // for snackbar purposes

        //Setup API connector
        SharedPreferences prefs = getSharedPreferences("AppPref", MODE_PRIVATE);
        String authorizationToken = prefs.getString("AccessToken", null);
        warehouseId = prefs.getInt("warehouseId", 0);
        warehouseName = prefs.getString("warehouseName", null);
        warehouseRole = prefs.getString("warehouseRole", null);

        connector = ApiConnector.getInstance();
        connector.setupApiConnector(authorizationToken);

        TextView warehouseNameTitle = (TextView) findViewById(R.id.usersWarehouseName);
        warehouseNameTitle.setText(warehouseName);

        //Use API to get warehouses and put them on a list
        getUsersList();

        // Adding Toolbar to Main screen
        Toolbar toolbar = (Toolbar) findViewById(R.id.users_toolbar);
        setSupportActionBar(toolbar);

        final NewUserModalBottomSheet modalBottomSheet = new NewUserModalBottomSheet();

        View floatingActionButton = findViewById(R.id.users_fab);
        floatingActionButton.setBackgroundTintList(getResources().getColorStateList(R.color.colorLightRed));

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modalBottomSheet.show(getSupportFragmentManager(), "NewUserModalBottomSheet");
            }
        });

    }

    private void getUsersList() {

        Call call = connector.apiService.getUsers(warehouseId);
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                int statusCode = response.code();
                if (statusCode == 200) {

                    //Success, fill up list of warehouses
                    usersList = new ArrayList<User>();
                    usersList.addAll(response.body());
                    Collections.sort(usersList, new UserComparator());
                    splitListByRoles();

                    // Setting ViewPager for each Tabs
                    viewPager = (ViewPager) findViewById(R.id.users_viewpager);
                    setupViewPager(viewPager);

                    // Set Tabs inside Toolbar
                    TabLayout tabs = (TabLayout) findViewById(R.id.users_tabs);
                    tabs.setupWithViewPager(viewPager);

                } else if (statusCode == 401) {
                    Snackbar snackbar = Snackbar
                            .make(coordinatorLayout, getResources().getString(R.string.authorizationFailRelog), Snackbar.LENGTH_LONG);

                    snackbar.show();
                }
                else if(statusCode == 404) {
                    Snackbar snackbar = Snackbar
                            .make(coordinatorLayout, "There is no such warehouse", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
                Log.i(TAG, "onResponse: API response handled.");
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.i(TAG, "onFailure: API call for logging failed");
                // Log error here since request failed
            }
        });
    }

    // Divide usersList into role-based list that would be used by a sorting tab
    private void splitListByRoles() {
        adminUsersList = new ArrayList<User>();
        editorUsersList = new ArrayList<User>();
        watcherUsersList = new ArrayList<User>();
        for (User u : usersList) {
            switch (u.getRole()) {
                case "admin":
                    adminUsersList.add(u);
                    break;
                case "editor":
                    editorUsersList.add(u);
                    break;
                case "watcher":
                    watcherUsersList.add(u);
                    break;
                default:
                    throw new RuntimeException(); // Found an element with unsupported role set
            }
        }
        Collections.sort(adminUsersList, new UserComparator());
        Collections.sort(editorUsersList, new UserComparator());
        Collections.sort(watcherUsersList, new UserComparator());
    }

    @Override
    public int newUserRequest(final String role,final String email, final NewUserModalBottomSheet mbs) {

        UserRequest userRequest = new UserRequest();
        userRequest.setRoleType(role);
        userRequest.setUserEmail(email);
        Call call = connector.apiService.addUser(warehouseId, userRequest);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                int statusCode = response.code();
                if (statusCode == 201) {
                    Log.i(TAG, "onResponse: API response handled. Adding user");
                    //Success, fill up list of warehouses
                    User temp = response.body();
                    addUser(temp);
                    mbs.dismiss();

                } else if (statusCode == 409) {
                    Log.i(TAG, "onResponse: API response handled. This user is already in a warehouse");
                    Snackbar snackbar = Snackbar
                            .make(mbs.contentView.getRootView(), getString(R.string.userExistsInWarehouse), Snackbar.LENGTH_LONG);
                    snackbar.show();
                } else if (statusCode == 404) {
                    Log.i(TAG, "onResponse: API response handled. There is no such usere");
                    Snackbar snackbar = Snackbar
                            .make(mbs.contentView.getRootView(), getString(R.string.userNotFound), Snackbar.LENGTH_LONG);
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
            public void onFailure(Call<User> call, Throwable t) {
                Log.i(TAG, "onFailure: API call for adding warehouse failed");
                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, getString(R.string.apiCallFailed), Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });
        return 1;
    }

    private int addUser(User u) {
        usersList.add(u);
        Collections.sort(usersList, new UserComparator());
        switch (u.getRole()) {
            case "admin":
                adminUsersList.add(u);
                Collections.sort(adminUsersList, new UserComparator());
                break;
            case "editor":
                editorUsersList.add(u);
                Collections.sort(editorUsersList, new UserComparator());
                break;
            case "watcher":
                watcherUsersList.add(u);
                Collections.sort(watcherUsersList, new UserComparator());
                break;
            default:
                throw new RuntimeException(); // Something created fragment with unsupported role
        }
        adapter.notifyDataSetChanged();
        return 0;
    }

    @Override
    public int editUserRequest(final User user, final String newRole, final EditUserModalBottomSheet mbs) {
        if(!warehouseRole.equals("admin")){
            mbs.dismiss();
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, getString(R.string.wrongPrivileges), Snackbar.LENGTH_LONG);
            snackbar.show();
            return 1;
        }

        UserRequest userRequest = new UserRequest();
        userRequest.setRoleType(newRole);

        Call call = connector.apiService.editUser(warehouseId, user.getId(), userRequest);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                int statusCode = response.code();
                if (statusCode == 200) {
                    Log.i(TAG, "onResponse: API response handled. updating user");

                    editUser(user, newRole);
                    mbs.dismiss();

                } else if (statusCode == 422) {
                    Log.i(TAG, "onResponse: API response handled. This name is taken");
                    Snackbar snackbar = Snackbar
                            .make(mbs.contentView.getRootView(), getString(R.string.badRequest), Snackbar.LENGTH_LONG);
                    snackbar.show();

                } else if (statusCode == 404) {
                    Log.i(TAG, "onResponse: API response handled. This name is taken");
                    Snackbar snackbar = Snackbar
                            .make(mbs.contentView.getRootView(), getString(R.string.userNotFound), Snackbar.LENGTH_LONG);
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
            public void onFailure(Call<User> call, Throwable t) {

                Log.i(TAG, "onFailure: API call for adding warehouse failed");
                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, getString(R.string.apiCallFailed), Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });
        return 1;
    }

    private int editUser(User u, String newRole) {
        int index = usersList.indexOf(u);
        usersList.get(index).setRole(newRole);
        adapter.notifyDataSetChanged();
        return 0;
    }


    @Override
    public void showEditModalBottomSheet(EditUserModalBottomSheet mbs) {
        mbs.show(getSupportFragmentManager(), "EditUserModalBottomSheet");
    }

    @Override
    public void showEmailWarning(NewUserModalBottomSheet mbs) {
        Snackbar snackbar = Snackbar
                .make(mbs.contentView.getRootView(), getString(R.string.userEmailWarning), Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    @Override
    public void showRadioButtonWarning(NewUserModalBottomSheet mbs) {
        Snackbar snackbar = Snackbar
                .make(mbs.contentView.getRootView(), getString(R.string.radioButtonWarning), Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    @Override
    public int removeUserRequest(final User u) {
        Call call = connector.apiService.removeUser(warehouseId, u.getId());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                int statusCode = response.code();
                if (statusCode == 200) {
                    Log.i(TAG, "onResponse: API response handled. Removing user");
                    String itemName = u.getFirstName() + u.getLastName();
                    removeUser(u);
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

    private int removeUser(User u) {
        usersList.remove(u);

        switch (u.getRole()) {
            case "admin":
                adminUsersList.remove(u);
                break;
            case "editor":
                editorUsersList.remove(u);
                break;
            case "watcher":
                watcherUsersList.remove(u);
                break;
            default:
                throw new RuntimeException(); // Something created fragment with unsupported role
        }
        adapter.notifyDataSetChanged();
        return 0;
    }


    @Override
    public List<User> getUsers(String role) {
        switch (role) {
            case "admin":
                return adminUsersList;
            case "editor":
                return editorUsersList;
            case "watcher":
                return watcherUsersList;
            case "all":
                return usersList;
            default:
                throw new RuntimeException(); // Something created fragment with unsupported role
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(UsersFragment.createInstance("all"), getString(R.string.warehousesAll));
        adapter.addFragment(UsersFragment.createInstance("admin"), getString(R.string.warehousesAdmin));
        adapter.addFragment(UsersFragment.createInstance("editor"), getString(R.string.warehousesEditor));
        adapter.addFragment(UsersFragment.createInstance("watcher"), getString(R.string.warehousesWatcher));
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