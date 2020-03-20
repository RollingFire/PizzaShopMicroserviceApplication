package com.austindorsey.menumicroservice.services;

import java.sql.SQLException;
import java.util.Map;

import com.austindorsey.menumicroservice.models.Menu;

public interface MenuService {
    public Menu[] getMenus() throws SQLException, ClassNotFoundException;
    public Menu getCurrentMenuById(int id) throws SQLException, ClassNotFoundException;
    public Menu getCurrentMenuByName(String name) throws SQLException, ClassNotFoundException;
    public Menu[] getMenuHistoryById(int origenalId) throws SQLException, ClassNotFoundException;
    public Menu[] getMenuHistoryByName(String name) throws SQLException, ClassNotFoundException;
    
    public Menu createNewMenu(Menu menu) throws SQLException, ClassNotFoundException;
    public Menu updateMenu(int id, Map<String,Object> updatePairs) throws SQLException, ClassNotFoundException;
    public Menu updateMenu(String name, Map<String,Object> updatePairs) throws SQLException, ClassNotFoundException;
}