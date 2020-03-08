package com.austindorsey.inventorymicroservice.services;

import java.sql.SQLException;

import com.austindorsey.inventorymicroservice.model.InventoryItem;

public interface InventoryService {
    public InventoryItem[] getInventory() throws SQLException, ClassNotFoundException;
    public InventoryItem getItemByName(String name) throws SQLException, ClassNotFoundException;
    public InventoryItem getItemByID(int id) throws SQLException, ClassNotFoundException;
    public double restockItemByName(String name, double unitsAdded) throws SQLException, ClassNotFoundException;
    public double restockItemByID(int id, double unitsAdded) throws SQLException, ClassNotFoundException;
    public InventoryItem addItemToInventory(InventoryItem item) throws SQLException, ClassNotFoundException;
    public InventoryItem addItemToInventory(String name) throws SQLException, ClassNotFoundException;
    public int removeItemFromInventoryByName(String name) throws SQLException, ClassNotFoundException;
    public int removeItemFromInventoryByID(int id) throws SQLException, ClassNotFoundException;
    public InventoryItem updateItemInInventoryByName(String name, InventoryItem item) throws SQLException, ClassNotFoundException;
    public InventoryItem updateItemInInventoryByID(int id, InventoryItem item) throws SQLException, ClassNotFoundException;
}