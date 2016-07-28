package com.szpilkowski.android.pelnymagazynek.Users;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.szpilkowski.android.pelnymagazynek.DbModels.User;
import com.szpilkowski.android.pelnymagazynek.DividerItemDecoration;
import com.szpilkowski.android.pelnymagazynek.R;

import java.util.List;

/**
 * Created by szpileq on 2016-07-27.
 */
public class UsersFragment extends Fragment {

    private String fragmentRoleMarker;
    protected List<User> usersList;
    private UsersManipulator usersManipulator;
    private static String TAG = "UsersFragment";

    protected UsersAdapter adapter;
    RecyclerView recyclerView;

    @Override
    public void onAttach(Context context) {
        if (context instanceof UsersManipulator) {
            usersManipulator = (UsersManipulator) context;
            super.onAttach(context);
        } else throw new RuntimeException("Activity must implement UsersManipulator");
    }

    @Override
    public void onDetach() {
        usersManipulator = null;
        super.onDetach();
        adapter.onDetach();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Get from the Activity the right set of users depending on their role
        fragmentRoleMarker = getArguments().getString("roleMarker");
        usersList = usersManipulator.getUsers(fragmentRoleMarker);

        LinearLayout noElements = (LinearLayout) // No-elements screen
                inflater.inflate(R.layout.no_elements, container, false);

        recyclerView = (RecyclerView) // Actual container for users
                inflater.inflate(R.layout.recycler_view, container, false);

        // If there are any users - populate the list,
        // otherwise - show no-elements screen
        if (usersList.size() > 0) {
            noElements.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

            adapter = new UsersAdapter(getActivity(), usersList);

            recyclerView.addItemDecoration(
                    new DividerItemDecoration(getActivity(), null));
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

            return recyclerView;
        } else {
            recyclerView.setVisibility(View.GONE);
            noElements.setVisibility(View.VISIBLE);

            return noElements;
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        if (getUserVisibleHint()) {
            adapter.getItemSelected(item);
            return true;
        }
        return false;
    }

    //Thanks to role string Fragment knows which part of warehouseList it needs
    public static UsersFragment createInstance(String role) {
        UsersFragment fragment = new UsersFragment();
        Bundle bundle = new Bundle();
        bundle.putString("roleMarker", role);
        fragment.setArguments(bundle);
        return fragment;
    }
}
