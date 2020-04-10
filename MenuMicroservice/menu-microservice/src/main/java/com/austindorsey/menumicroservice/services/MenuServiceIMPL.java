package com.austindorsey.menumicroservice.services;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import com.austindorsey.menumicroservice.models.CreateMenuRequest;
import com.austindorsey.menumicroservice.models.Menu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

@Service
@PropertySource("classpath:database.properties")
public class MenuServiceIMPL implements MenuService {

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
    @Value("${mysql.tableName.menu}")
    private String tableName;
    @Value("${mysql.tableName.menuHistory}")
    private String historyTableName;
    private Connection mysql;

    @Autowired private DriverManagerWrapper driverManagerWrapped;
    

    @Override
    public Menu[] getMenus() throws SQLException, ClassNotFoundException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName;
            mysql = driverManagerWrapped.getConnection(url, dbUserName, dbPassword);
            Statement statement = mysql.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM " + tableName + ";");
            ArrayList<Menu> list = new ArrayList<>();
            while (result.next()) {
                int id = result.getInt("id");
                String menuName = result.getString("menuName");
                int[] items = sqlStringToIntArray(result.getString("items"));
                Date revisionDate = result.getDate("revisionDate");
                Menu item = new Menu(id, menuName, items, revisionDate);
                list.add(item);
            }
            return list.toArray(new Menu[list.size()]);
        } finally {
            if (mysql != null) {
                mysql.close();
            }
        }
    }

    @Override
    public Menu getCurrentMenuById(int id) throws SQLException, ClassNotFoundException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName;
            mysql = driverManagerWrapped.getConnection(url, dbUserName, dbPassword);
            Menu item = null;
            Statement statement = mysql.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM " + tableName + " WHERE id='" + id + "';");
            if (result.next()) {
                String menuName = result.getString("menuName");
                int[] items = sqlStringToIntArray(result.getString("items"));
                Date revisionDate = result.getDate("revisionDate");
                item = new Menu(id, menuName, items, revisionDate);
            }
            return item;
        } finally {
            mysql.close();
        }
    }

    @Override
    public Menu getCurrentMenuByName(String menuName) throws SQLException, ClassNotFoundException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName;
            mysql = driverManagerWrapped.getConnection(url, dbUserName, dbPassword);
            Menu item = null;
            Statement statement = mysql.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM " + tableName + " WHERE menuName LIKE '" + menuName + "';");
            if (result.next()) {
                int id = result.getInt("id");
                String actualMenuName = result.getString("menuName");
                int[] items = sqlStringToIntArray(result.getString("items"));
                Date revisionDate = result.getDate("revisionDate");
                item = new Menu(id, actualMenuName, items, revisionDate);
            }
            return item;
        } finally {
            mysql.close();
        }
    }

    @Override
    public Menu[] getMenuHistoryById(int origenalId) throws SQLException, ClassNotFoundException {
        ArrayList<Menu> history = new ArrayList<>();
        Menu curentItem = getCurrentMenuById(origenalId);
        if (curentItem == null) {
            return null;
        } else {
            history.add(getCurrentMenuById(origenalId));
        }
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName;
            mysql = driverManagerWrapped.getConnection(url, dbUserName, dbPassword);
            Statement statement = mysql.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM " + historyTableName + " WHERE origenalId='" + origenalId + "' ORDER BY entryId DESC;");
            while (result.next()) {
                String menuName = result.getString("menuName");
                int[] items = sqlStringToIntArray(result.getString("items"));
                Date revisionDate = result.getDate("revisionDate");
                Menu item = new Menu(origenalId, menuName, items, revisionDate);
                history.add(item);
            }
            return history.toArray(new Menu[history.size()]);
        } finally {
            mysql.close();
        }
    }

    @Override
    public Menu[] getMenuHistoryByName(String menuName) throws SQLException, ClassNotFoundException {
        ArrayList<Menu> history = new ArrayList<>();
        Menu curentItem = getCurrentMenuByName(menuName);
        if (curentItem == null) {
            return null;
        } else {
            history.add(curentItem);
        }
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName;
            mysql = driverManagerWrapped.getConnection(url, dbUserName, dbPassword);
            Statement statement = mysql.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM " + historyTableName + " WHERE menuName LIKE '" + menuName + "' ORDER BY entryId DESC;");
            while (result.next()) {
                int origenalId = result.getInt("origenalId");
                String actualMenuName = result.getString("menuName");
                int[] items = sqlStringToIntArray(result.getString("items"));
                Date revisionDate = result.getDate("revisionDate");
                Menu item = new Menu(origenalId, actualMenuName, items, revisionDate);
                history.add(item);
            }
            return history.toArray(new Menu[history.size()]);
        } finally {
            mysql.close();
        }
    }

    @Override
    public Menu createNewMenu(CreateMenuRequest menuRequest) throws SQLException, ClassNotFoundException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName;
            mysql = driverManagerWrapped.getConnection(url, dbUserName, dbPassword);
            Statement statement = mysql.createStatement();
            statement.executeUpdate("INSERT INTO " + tableName + " (menuName, items) VALUES ('" + 
                                menuRequest.getName() + "', '" + Arrays.toString(menuRequest.getItems()) + "');");
            ResultSet result = statement.executeQuery("SELECT * FROM " + tableName + " WHERE menuName='" + menuRequest.getName() +
                                                                                      "' AND items='" + Arrays.toString(menuRequest.getItems()) +
                                                                                      " ORDER BY id DESC LIMIT 1;");
            Menu newItem = null;
            if (result.next()) {
                int id = result.getInt("id");
                String menuName = result.getString("menuName");
                int[] items = sqlStringToIntArray(result.getString("items"));
                Date revisionDate = result.getDate("revisionDate");
                newItem = new Menu(id, menuName, items, revisionDate);
            }
            return newItem;
        } finally {
            mysql.close();
        }
    }

    @Override
    public Menu updateMenu(int id, Map<String,Object> updatePairs) throws SQLException, ClassNotFoundException {
        try {
            if (updatePairs.size() > 0) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                String url = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName;
                mysql = driverManagerWrapped.getConnection(url, dbUserName, dbPassword);
                Statement statement = mysql.createStatement();

                String sql = "UPDATE " + tableName + " SET ";
                for (String key : updatePairs.keySet()) {
                    sql = sql.replace("$", ", ");
                    if (updatePairs.get(key) instanceof String) {
                        sql += key + "='" + updatePairs.get(key) + "'$";
                    } else if (updatePairs.get(key) instanceof int[]) {
                        sql += key + "='" + Arrays.toString((int[]) updatePairs.get(key)) + "'$";
                    } else {
                        sql += key + "=" + updatePairs.get(key) + "$";
                    }
                }
                sql = sql.replace("$", (" WHERE id=" + id + ";" ));
                statement.executeUpdate(sql);
            }
            return getCurrentMenuById(id);
        } finally {
            mysql.close();
        }
    }

    @Override
    public Menu updateMenu(String menuName, Map<String,Object> updatePairs) throws SQLException, ClassNotFoundException {
        Menu menu = getCurrentMenuByName(menuName);
        if (menu != null) {
            int id = menu.getId();
            return updateMenu(id, updatePairs);
        } else {
            return null;
        }
    }

    public int[] sqlStringToIntArray(String sqlValue) {
        sqlValue = sqlValue.replaceAll("[\\[$|\\]$|\\s]", "");
        if (sqlValue.length() == 0) {
            return new int[]{};
        }
        String[] strSplit = sqlValue.split(",");
        int[] array = new int[strSplit.length];
        for (int i = 0; i < strSplit.length; i++) {
            array[i] = Integer.valueOf(strSplit[i]);
        }
        return array;
    }
}