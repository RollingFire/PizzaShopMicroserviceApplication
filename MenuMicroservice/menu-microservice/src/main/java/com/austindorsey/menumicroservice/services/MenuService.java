package com.austindorsey.menumicroservice.services;

import java.util.Map;

import com.austindorsey.menumicroservice.models.Menu;

public interface MenuService {
    public Menu[] getMenus();
    public Menu getCurrentMenuById(int id);
    public Menu getCurrentMenuByName(String name);
    public Menu[] getMenuHistoryById(int origenalId);
    public Menu[] getMenuHistoryByName(String name);
    
    public Menu createNewMenu(Menu menu);
    public Menu updateMenu(int id, Map<String,String> updatePairs);
    public Menu updateMenu(String name, Map<String,String> updatePairs);
}