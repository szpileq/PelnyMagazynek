package com.szpilkowski.android.pelnymagazynek.Users;

import com.szpilkowski.android.pelnymagazynek.DbModels.User;

import java.util.List;

/**
 * Created by szpileq on 2016-07-27.
 */
public interface UsersManipulator {
    int newUserRequest(String role, String email, NewUserModalBottomSheet mbs);
    void showEmailWarning(NewUserModalBottomSheet mbs);
    void showRadioButtonWarning(NewUserModalBottomSheet mbs);
    int removeUserRequest(User u);
    List<User> getUsers(String fragmentRoleMarker);
}
