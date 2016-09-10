package com.szpilkowski.android.pelnymagazynek.Warehouses;

import com.szpilkowski.android.pelnymagazynek.DatabaseModels.Warehouse;

import java.util.Comparator;

/**
 * Created by szpileq on 2016-07-24.
 */
public class WarehouseComparator implements Comparator<Warehouse> {
    @Override
    public int compare(Warehouse o1, Warehouse o2) {
        return o1.getName().toLowerCase().compareTo(o2.getName().toLowerCase());
    }
}