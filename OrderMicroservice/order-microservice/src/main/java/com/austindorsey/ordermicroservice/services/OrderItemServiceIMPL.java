package com.austindorsey.ordermicroservice.services;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.austindorsey.ordermicroservice.modal.OrderItem;
import com.austindorsey.ordermicroservice.modal.OrderItemCreateRequest;
import com.austindorsey.ordermicroservice.modal.OrderItemUpdateRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

@Service
@PropertySource("classpath:database.properties")
@PropertySource("classpath:api.properties")
public class OrderItemServiceIMPL implements OrderItemService {

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
    @Value("${mysql.tableName.orderItem}")
    private String tableNameOrderItem;
    private Connection mysql;
    
    @Autowired private DriverManagerWrapper driverManagerWrapped;
    
    @Override
    public OrderItem[] getOrderItemsByOrderId(int orderId) throws SQLException, ClassNotFoundException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName;
            mysql = driverManagerWrapped.getConnection(url, dbUserName, dbPassword);
            Statement statement = mysql.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM " + tableNameOrderItem + " WHERE orderId=" + orderId +  ";");
            List<OrderItem> list = new ArrayList<>();
            while (result.next()) {
                int id = result.getInt("id");
                int menuItemId = result.getInt("menuItemId");
                int quantity = result.getInt("quantity");
                String orderItemStatus = result.getString("orderItemStatus");
                Number cost = result.getDouble("cost");
                Date lastRevisionDate = result.getDate("lastRevisionDate");
                list.add(new OrderItem(id, orderId, menuItemId, quantity, orderItemStatus, cost, lastRevisionDate));
            }
            return list.toArray(new OrderItem[list.size()]);
        } finally {
            if (mysql != null) {
                mysql.close();
            }
        }
    }

    @Override
    public OrderItem addOrderItemToOrderId(int orderId, OrderItemCreateRequest request)
            throws SQLException, ClassNotFoundException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName;
            mysql = driverManagerWrapped.getConnection(url, dbUserName, dbPassword);
            Statement statement = mysql.createStatement();
            int rowsCreated = statement.executeUpdate(request.getSQLInsertStatement(tableNameOrderItem));
            if (rowsCreated > 0) {
                ResultSet result = statement.executeQuery(request.getSQLSelectStatement(tableNameOrderItem));
                if (result.next()) {
                    int id = result.getInt("id");
                    int menuItemId = result.getInt("menuItemId");
                    int quantity = result.getInt("quantity");
                    String orderItemStatus = result.getString("orderItemStatus");
                    Number cost = result.getDouble("cost");
                    Date lastRevisionDate = result.getDate("lastRevisionDate");
                    return new OrderItem(id, orderId, menuItemId, quantity, orderItemStatus, cost, lastRevisionDate);
                }
            }
            return null;
        } finally {
            if (mysql != null) {
                mysql.close();
            }
        }
    }

    @Override
    public OrderItem getOrderItemById(int id) throws SQLException, ClassNotFoundException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName;
            mysql = driverManagerWrapped.getConnection(url, dbUserName, dbPassword);
            Statement statement = mysql.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM " + tableNameOrderItem + " WHERE id=" + id +  ";");
            if (result.next()) {
                int orderId = result.getInt("orderId");
                int menuItemId = result.getInt("menuItemId");
                int quantity = result.getInt("quantity");
                String orderItemStatus = result.getString("orderItemStatus");
                Number cost = result.getDouble("cost");
                Date lastRevisionDate = result.getDate("lastRevisionDate");
                return new OrderItem(id, orderId, menuItemId, quantity, orderItemStatus, cost, lastRevisionDate);
            }
            return null;
        } finally {
            if (mysql != null) {
                mysql.close();
            }
        }
    }

    @Override
    public OrderItem updateOrderItemById(int id, OrderItemUpdateRequest request)
            throws SQLException, ClassNotFoundException {

        
        String sql = request.getSQLUpdateStatement(tableNameOrderItem, getOrderItemById(id).getMenuItemId());
        //To build the update sql, the request calls the menuAPI. If that fails, the sql is null.
        if (sql == null) { 
            return null;
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName;
            mysql = driverManagerWrapped.getConnection(url, dbUserName, dbPassword);
            Statement statement = mysql.createStatement();
            getOrderItemById(id).getMenuItemId();
            statement.executeUpdate(request.getSQLUpdateStatement(tableNameOrderItem, getOrderItemById(id).getMenuItemId()));
            return getOrderItemById(id);
        } finally {
            if (mysql != null) {
                mysql.close();
            }
        }
    }

    @Override
    public OrderItem updateOrderItemStatusById(int id, String status) throws SQLException, ClassNotFoundException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName;
            mysql = driverManagerWrapped.getConnection(url, dbUserName, dbPassword);
            Statement statement = mysql.createStatement();
            statement.executeUpdate("UPDATE " + tableNameOrderItem + " SET orderItemStatus='" + status + "' WHERE id=" + id + ";");
            return getOrderItemById(id);
        } finally {
            if (mysql != null) {
                mysql.close();
            }
        }
    }

    @Override
    public OrderItem[] getOrderItemsByStatus(String status) throws SQLException, ClassNotFoundException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName;
            mysql = driverManagerWrapped.getConnection(url, dbUserName, dbPassword);
            Statement statement = mysql.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM " + tableNameOrderItem + " WHERE orderItemStatus LIKE " + status +  ";");
            List<OrderItem> list = new ArrayList<>();
            while (result.next()) {
                int id = result.getInt("id");
                int orderId = result.getInt("orderId");
                int menuItemId = result.getInt("menuItemId");
                int quantity = result.getInt("quantity");
                String orderItemStatus = result.getString("orderItemStatus");
                Number cost = result.getDouble("cost");
                Date lastRevisionDate = result.getDate("lastRevisionDate");
                list.add(new OrderItem(id, orderId, menuItemId, quantity, orderItemStatus, cost, lastRevisionDate));
            }
            return list.toArray(new OrderItem[list.size()]);
        } finally {
            if (mysql != null) {
                mysql.close();
            }
        }
    }
    
}