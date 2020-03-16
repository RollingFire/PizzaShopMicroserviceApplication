package com.austindorsey.menumicroservice.services;

import com.austindorsey.menumicroservice.models.Menu;
import com.austindorsey.menumicroservice.models.MenuItem;

public interface MenuService {
    public Menu[] getMenus();
    public Menu getMenuById(int id);
    public Menu getCurrentMenuByType(String name);
    public Menu[] getMenuHistoryById(int origenalId);
    public Menu[] getMenuHistoryByType(String name);
    
    public Menu createNewMenu(Menu menu);
    public Menu updateMenu(int id, MenuItem[] items);
    public Menu updateMenu(String name, MenuItem[] items);
}