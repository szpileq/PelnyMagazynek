package com.szpilkowski.android.pelnymagazynek.Items;

import com.szpilkowski.android.pelnymagazynek.DbModels.Item;

import java.util.List;

/**
 * Created by szpileq on 2016-07-24.
 */
public interface ItemsManipulator {
    List<Item> getItems(String fragmentQuantityMarker);
    int removeItemRequest(Item i);
    int openItem(Item i);
}
