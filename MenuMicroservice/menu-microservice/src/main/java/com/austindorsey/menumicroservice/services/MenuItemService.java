package com.austindorsey.menumicroservice.services;

import java.util.Map;

import com.austindorsey.menumicroservice.models.MenuItem;

public interface MenuItemService {
    public MenuItem[] getMenuItems();
    public MenuItem getMenuItemByID(int id);
    public MenuItem[] getMenuItemHistoryById(int origenalId);
    public MenuItem[] searchMenuItemsByName(String name);

    public MenuItem createNewMenuItem(MenuItem item);
    public MenuItem updateMenuItem(int id, Map<String,String> updatePairs);
}