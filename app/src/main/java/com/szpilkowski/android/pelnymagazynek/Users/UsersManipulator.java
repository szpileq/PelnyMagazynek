package com.szpilkowski.android.pelnymagazynek.Users;

import com.szpilkowski.android.pelnymagazynek.DatabaseModels.User;

import java.util.List;

/**
 * Created by szpileq on 2016-07-27.
 */
public interface UsersManipulator {
    int newUserRequest(String role, String email, NewUserModalBottomSheet mbs);
    int editUserRequest(User user, String newRole, EditUserModalBottomSheet mbs);
    void showEditModalBottomSheet(EditUserModalBottomSheet mbs);
    void showEmailWarning(NewUserModalBottomSheet mbs);
    void showRadioButtonWarning(NewUserModalBottomSheet mbs);
    int removeUserRequest(User u);
    List<User> getUsers(String fragmentRoleMarker);
}
