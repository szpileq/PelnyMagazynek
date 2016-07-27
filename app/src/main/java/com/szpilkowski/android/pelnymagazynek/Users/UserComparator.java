package com.szpilkowski.android.pelnymagazynek.Users;

import com.szpilkowski.android.pelnymagazynek.DbModels.User;

import java.util.Comparator;

/**
 * Created by szpileq on 2016-07-27.
 */
public class UserComparator implements Comparator<User> {
    @Override
    public int compare(User u1, User u2) {
        return (u1.getFirstName()+u1.getLastName()).toLowerCase().compareTo((u2.getFirstName()+u2.getLastName()).toLowerCase());
    }
}