package com.szpilkowski.android.pelnymagazynek.Warehouses;

import com.szpilkowski.android.pelnymagazynek.DbModels.Warehouse;

import java.util.List;

/**
 * Created by szpileq on 2016-07-24.
 */
public interface WarehouseManipulator {
    int newWarehouseRequest(String s, NewWarehouseModalBottomSheet mbs);
    int editWarehouseRequest(Warehouse w,String newName, EditWarehouseModalBottomSheet mbs);
    int removeWarehouseRequest(Warehouse w);
    List<Warehouse> getWarehouses(String role);
    int showEditModalBottomSheet(EditWarehouseModalBottomSheet mbs);
    int openWarehouse(Warehouse w);
}
