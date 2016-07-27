package com.szpilkowski.android.pelnymagazynek.Users;

import com.szpilkowski.android.pelnymagazynek.DbModels.User;

import java.util.List;

/**
 * Created by szpileq on 2016-07-27.
 */
public interface UsersManipulator {
    //int editUserRequest(User u, String newRole, EditUserModalBottomSheet mbs);
    int removeUserRequest(User u);
    List<User> getUsers(String fragmentRoleMarker);
}
