package com.szpilkowski.android.pelnymagazynek.Items;

import com.szpilkowski.android.pelnymagazynek.DbModels.Item;

import java.util.Comparator;

/**
 * Created by szpileq on 2016-07-24.
 */
public class ItemComparator implements Comparator<Item> {
    @Override
    public int compare(Item o1, Item o2) {
        return o1.getName().toLowerCase().compareTo(o2.getName().toLowerCase());
    }
}