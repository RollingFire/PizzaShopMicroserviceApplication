package com.austindorsey.menumicroservice.services;

import java.sql.SQLException;
import java.util.Map;

import com.austindorsey.menumicroservice.models.MenuItem;

public interface MenuItemService {
    public MenuItem[] getMenuItems() throws SQLException, ClassNotFoundException;
    public MenuItem getMenuItemByID(int id) throws SQLException, ClassNotFoundException;
    public MenuItem[] getMenuItemHistoryById(int origenalId) throws SQLException, ClassNotFoundException;
    public MenuItem[] searchMenuItemsByName(String search) throws SQLException, ClassNotFoundException;

    public MenuItem createNewMenuItem(MenuItem item) throws SQLException, ClassNotFoundException;
    public MenuItem updateMenuItem(int id, Map<String,Object> updatePairs) throws SQLException, ClassNotFoundException;
}