package com.austindorsey.menumicroservice.services;

import java.util.Map;

import com.austindorsey.menumicroservice.models.Menu;

public class MenuServiceIMPL implements MenuService {

    @Override
    public Menu[] getMenus() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Menu getCurrentMenuById(int id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Menu getCurrentMenuByName(String name) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Menu[] getMenuHistoryById(int origenalId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Menu[] getMenuHistoryByName(String name) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Menu createNewMenu(Menu menu) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Menu updateMenu(int id, Map<String,String> updatePairs) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Menu updateMenu(String name, Map<String,String> updatePairs) {
        // TODO Auto-generated method stub
        Menu menu = getCurrentMenuByName(name);
        if (menu != null) {
            int id = menu.getId();
            return updateMenu(id, updatePairs);
        } else {
            return null;
        }
    }

}