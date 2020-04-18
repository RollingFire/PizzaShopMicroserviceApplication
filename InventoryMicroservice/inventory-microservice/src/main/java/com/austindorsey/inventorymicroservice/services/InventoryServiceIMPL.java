package com.austindorsey.inventorymicroservice.services;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.austindorsey.inventorymicroservice.model.InventoryItem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

@Service
@PropertySource("classpath:database.properties")
public class InventoryServiceIMPL implements InventoryService {

    @Value("${mysql.host}")
    private String dbHost;
    @Value("${mysql.port}")
    private String dbPort;
    @Value("${mysql.user}")
    private String dbUserName;
    @Value("${mysql.password}")
    private String dbPassword;
    @Value("${mysql.database}")
    private String dbName;
    @Value("${mysql.tableName.inventory}")
    private String tableName;
    private Connection mysql;

    @Autowired private DriverManagerWrapper driverManagerWrapped;

    /**
     * Calls the MySQL server to get all items in the inventory.
     * @return InventoryItem[] Array of all items in the inventory.
     * @throws SQLException SQL errors
     * @throws ClassNotFroundException Only if com.mysql.cj.jdbc.Driver can not be found.
     */
    @Override
    public InventoryItem[] getInventory() throws SQLException, ClassNotFoundException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName;
            mysql = driverManagerWrapped.getConnection(url, dbUserName, dbPassword);
            Statement statement = mysql.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM " + tableName + ";");
            ArrayList<InventoryItem> list = new ArrayList<>();
            while (result.next()) {
                int id = result.getInt("id");
                String name = result.getString("itemName");
                double units = result.getDouble("units");
                String unitType = result.getString("unitType");
                double restockAt = result.getDouble("restockAt");
                double restockAmount = result.getDouble("restockAmount");
                InventoryItem item = new InventoryItem(id, name, units, unitType, restockAt, restockAmount);
                list.add(item);
            }

            return list.toArray(new InventoryItem[list.size()]);
        } finally {
            if (mysql != null) {
                mysql.close();
            }
        }
    }

    /**
     * Calls the MySQL server to gets the inventory matching the given name.
     * @param name Name of the inventory item.
     * @return InventoryItem Inventory item that matches the name. Returns null if inventory item is not found.
     * @throws SQLException SQL errors
     * @throws ClassNotFroundException Only if com.mysql.cj.jdbc.Driver can not be found.
     */
    @Override
    public InventoryItem getItemByName(String name) throws SQLException, ClassNotFoundException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName;
            mysql = driverManagerWrapped.getConnection(url, dbUserName, dbPassword);
            InventoryItem item = null;
            Statement statement = mysql.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM " + tableName + " WHERE itemName='" + name + "';");
            if (result.next()) {
                int id = result.getInt("id");
                String confirmName = result.getString("itemName");
                double units = result.getDouble("units");
                String unitType = result.getString("unitType");
                double restockAt = result.getDouble("restockAt");
                double restockAmount = result.getDouble("restockAmount");

                item = new InventoryItem(id, confirmName, units, unitType, restockAt, restockAmount);
            }
            return item;
        } finally {
            mysql.close();
        }
    }

    /**
     * Calls the MySQL server to gets the inventory matching the given id.
     * @param id id of the inventory item.
     * @return InventoryItem Inventory item that matches the name. Returns null if inventory item is not found.
     * @throws SQLException SQL errors
     * @throws ClassNotFroundException Only if com.mysql.cj.jdbc.Driver can not be found.
     */
    @Override
    public InventoryItem getItemByID(int id) throws SQLException, ClassNotFoundException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName;
            mysql = driverManagerWrapped.getConnection(url, dbUserName, dbPassword);
            InventoryItem item = null;
            Statement statement = mysql.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM " + tableName + " WHERE id='" + id + "';");
            if (result.next()) {
                int confirmID = result.getInt("id");
                String name = result.getString("itemName");
                double units = result.getDouble("units");
                String unitType = result.getString("unitType");
                double restockAt = result.getDouble("restockAt");
                double restockAmount = result.getDouble("restockAmount");

                item = new InventoryItem(confirmID, name, units, unitType, restockAt, restockAmount);
            }
            return item;
        } finally {
            mysql.close();
        }
    }

    /**
     * Calls the MySQL server to add stock to the inventory item matching the given name.
     * @param name Name of the inventory item.
     * @param unitsAdded The number of units that you want to add to the inventory stick.
     * @return double New number of units in the inventory. Returns -1 if inventory item not found.
     * @throws SQLException SQL errors
     * @throws ClassNotFroundException Only if com.mysql.cj.jdbc.Driver can not be found.
     */
    @Override
    public double restockItemByName(String name, double unitsAdded) throws SQLException, ClassNotFoundException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName;
            mysql = driverManagerWrapped.getConnection(url, dbUserName, dbPassword);
            Statement statement = mysql.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM " + tableName + " WHERE itemName='" + name + "';");
            if (result.next()) {
                double units = result.getDouble("units");
                double newAmount = units + unitsAdded;
                if (newAmount < 0) {
                    newAmount = 0;
                }
                statement.executeUpdate("UPDATE " + tableName + " SET units='" + newAmount + "' WHERE itemName='" + name + "';");
                return newAmount;
            }
            else {
                return -1;
            }
        } finally {
            mysql.close();
        }
    }

    /**
     * Calls the MySQL server to add stock to the inventory item matching the given id.
     * @param id id of the inventory item.
     * @param unitsAdded The number of units that you want to add to the inventory stick.
     * @return double New number of units in the inventory. Returns -1 if inventory item not found.
     * @throws SQLException SQL errors
     * @throws ClassNotFroundException Only if com.mysql.cj.jdbc.Driver can not be found.
     */
    @Override
    public double restockItemByID(int id, double unitsAdded) throws SQLException, ClassNotFoundException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName;
            mysql = driverManagerWrapped.getConnection(url, dbUserName, dbPassword);
            Statement statement = mysql.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM " + tableName + " WHERE id='" + id + "';");
            if (result.next()) {
                double units = result.getDouble("units");
                double newAmount = units + unitsAdded;
                if (newAmount < 0) {
                    newAmount = 0;
                }
                statement.executeUpdate("UPDATE " + tableName + " SET units='" + newAmount + "' WHERE id='" + id + "';");
                return newAmount;
            }
            else {
                return -1;
            }
        } finally {
            mysql.close();
        }
    }

    /**
     * Calls the MySQL server to add a new item to the inventory.
     * @param item InventoryItem New item to add to the inventory.
     * @return InventoryItem Returns the inventory item that is created.
     * @throws SQLException SQL errors
     * @throws ClassNotFroundException Only if com.mysql.cj.jdbc.Driver can not be found.
     */
    @Override
    public InventoryItem addItemToInventory(InventoryItem item) throws SQLException, ClassNotFoundException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName;
            mysql = driverManagerWrapped.getConnection(url, dbUserName, dbPassword);
            Statement statement = mysql.createStatement();
            statement.executeUpdate("INSERT INTO " + tableName + 
                " (itemName, units, unitType, restockAt, restockAmount) VALUES ('" + 
                item.getName() + "', " + item.getUnits()  + ", '" + item.getUnitType() + "', "  + item.getRestockAt() + "," + item.getRestockAmount() + ");");
            return getItemByName(item.getName());
        } finally {
            mysql.close();
        }
    }

    /**
     * Calls the MySQL server to add a new item to the inventory.
     * @param name Name of the new inventory item.
     * @return InventoryItem Returns the inventory item that is created.
     * @throws SQLException SQL errors
     * @throws ClassNotFroundException Only if com.mysql.cj.jdbc.Driver can not be found.
     */
    @Override
    public InventoryItem addItemToInventory(String name) throws SQLException, ClassNotFoundException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName;
            mysql = driverManagerWrapped.getConnection(url, dbUserName, dbPassword);
            Statement statement = mysql.createStatement();
            statement.executeUpdate("INSERT INTO " + tableName + " (itemName) VALUES ('" + name + "');");
            return getItemByName(name);
        } finally {
            mysql.close();
        }
    }

    /**
     * Calls the MySQL server to remove an item from the inventory.
     * @param name Name of the inventory item to remove.
     * @return int Number of rows deleted.
     * @throws SQLException SQL errors
     * @throws ClassNotFroundException Only if com.mysql.cj.jdbc.Driver can not be found.
     */
    @Override
    public int removeItemFromInventoryByName(String name) throws SQLException, ClassNotFoundException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName;
            mysql = driverManagerWrapped.getConnection(url, dbUserName, dbPassword);
            Statement statement = mysql.createStatement();
            int rowsChanged = statement.executeUpdate("DELETE FROM " + tableName + " WHERE itemName='" + name + "';");
            return rowsChanged;
        } finally {
            mysql.close();
        }
    }

    /**
     * Calls the MySQL server to remove an item from the inventory.
     * @param id Id of the inventory item to remove.
     * @return int Number of rows deleted.
     * @throws SQLException SQL errors
     * @throws ClassNotFroundException Only if com.mysql.cj.jdbc.Driver can not be found.
     */
    @Override
    public int removeItemFromInventoryByID(int id) throws SQLException, ClassNotFoundException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName;
            mysql = driverManagerWrapped.getConnection(url, dbUserName, dbPassword);
            Statement statement = mysql.createStatement();
            int rowsChanged = statement.executeUpdate("DELETE FROM " + tableName + " WHERE id='" + id + "';");
            return rowsChanged;
        } finally {
            mysql.close();
        }
    }

    /**
     * Calls the MySQL server to remove an item from the inventory.
     * @param name Name of the inventory item to update.
     * @param item InventoryItem Contains the data to update the inventory item with. 
     * @return InventoryItem Returns the updated inventory item
     * @throws SQLException SQL errors
     * @throws ClassNotFroundException Only if com.mysql.cj.jdbc.Driver can not be found.
     */
    @Override
    public InventoryItem updateItemInInventoryByName(String name, InventoryItem item) throws SQLException, ClassNotFoundException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName;
            mysql = driverManagerWrapped.getConnection(url, dbUserName, dbPassword);
            Statement statement = mysql.createStatement();
            statement.executeUpdate("UPDATE " + tableName + " SET itemName='" + item.getName() +
                                                              "', units='" + item.getUnits() +
                                                              "', unitType='" + item.getUnitType() +
                                                              "', restockAt='" + item.getRestockAt() +
                                                              "', restockAmount='" + item.getRestockAmount() +
                                                              "' WHERE itemName='" + name + "';");
            return getItemByName(item.getName());
        } finally {
            mysql.close();
        }
    }

    /**
     * Calls the MySQL server to remove an item from the inventory.
     * @param id Id of the inventory item to update.
     * @param item InventoryItem Contains the data to update the inventory item with. 
     * @return InventoryItem Returns the updated inventory item
     * @throws SQLException SQL errors
     * @throws ClassNotFroundException Only if com.mysql.cj.jdbc.Driver can not be found.
     */
    @Override
    public InventoryItem updateItemInInventoryByID(int id, InventoryItem item) throws SQLException, ClassNotFoundException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName;
            mysql = driverManagerWrapped.getConnection(url, dbUserName, dbPassword);
            Statement statement = mysql.createStatement();
            statement.executeUpdate("UPDATE " + tableName + " SET itemName='" + item.getName() +
                                                              "', units='" + item.getUnits() +
                                                              "', unitType='" + item.getUnitType() +
                                                              "', restockAt='" + item.getRestockAt() +
                                                              "', restockAmount='" + item.getRestockAmount() +
                                                              "' WHERE id='" + id + "';");
            return getItemByID(id);
        } finally {
            mysql.close();
        }
    }
}