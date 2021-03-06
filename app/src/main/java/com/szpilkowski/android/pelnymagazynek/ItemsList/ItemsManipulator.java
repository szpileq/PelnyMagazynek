package com.szpilkowski.android.pelnymagazynek.ItemsList;

import com.szpilkowski.android.pelnymagazynek.DatabaseModels.Item;

import java.util.List;

/**
 * Created by szpileq on 2016-07-24.
 */
public interface ItemsManipulator {
    List<Item> getItems(String fragmentQuantityMarker);
    int removeItemRequest(Item i);
    int openItem(Item i);
}
