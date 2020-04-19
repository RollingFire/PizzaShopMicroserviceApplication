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

    /**
     * Calls the MySQL server to get all order items that belong to the orderId.
     * @param orderId Gets order items that belong to orderId.
     * @return OrderItem[]
     * @throws SQLException SQL errors
     * @throws ClassNotFroundException Only if com.mysql.cj.jdbc.Driver can not be found.
     */
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

    /**
     * Calls the MySQL server to add and order item
     * @param orderId orderId that the order itme will belong to.
     * @param request OrederItemCreateRequest that will be used to create the order item.
     * @return OrderItem Newly created order item.
     * @throws SQLException SQL errors
     * @throws ClassNotFroundException Only if com.mysql.cj.jdbc.Driver can not be found.
     */
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

    /**
     * Calls the MySQL server to get the order item by id
     * @param id id of the order item.
     * @return OrderItem Retrived order item.
     * @throws SQLException SQL errors
     * @throws ClassNotFroundException Only if com.mysql.cj.jdbc.Driver can not be found.
     */
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

    /**
     * Calls the MySQL server to update the order item by id
     * @param id id of the order item to update
     * @param request OrderItemUpdateRequest Is used to update the order item
     * @return OrderItem Updated order item.
     * @throws SQLException SQL errors
     * @throws ClassNotFroundException Only if com.mysql.cj.jdbc.Driver can not be found.
     */
    @Override
    public OrderItem updateOrderItemById(int id, OrderItemUpdateRequest request)
            throws SQLException, ClassNotFoundException {
        String sql = request.getSQLUpdateStatement(id, tableNameOrderItem, getOrderItemById(id).getMenuItemId());
        
        //To build the update sql, the request calls the menuAPI. If that fails, the sql is null.
        if (sql == null) { 
            return null;
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName;
            mysql = driverManagerWrapped.getConnection(url, dbUserName, dbPassword);
            Statement statement = mysql.createStatement();
            statement.executeUpdate(sql);
            return getOrderItemById(id);
        } finally {
            if (mysql != null) {
                mysql.close();
            }
        }
    }

    /**
     * Calls the MySQL server to update the status of the order item by id
     * @param id id of the order item to update
     * @param status Status to update order item to.
     * @return OrderItem Updated order item.
     * @throws SQLException SQL errors
     * @throws ClassNotFroundException Only if com.mysql.cj.jdbc.Driver can not be found.
     */
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

    /**
     * Calls the MySQL server to get all order items with the matching status.
     * @param status Status to search for
     * @return OrderItem[] OrderItems that match status
     * @throws SQLException SQL errors
     * @throws ClassNotFroundException Only if com.mysql.cj.jdbc.Driver can not be found.
     */
    @Override
    public OrderItem[] getOrderItemsByStatus(String status) throws SQLException, ClassNotFoundException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName;
            mysql = driverManagerWrapped.getConnection(url, dbUserName, dbPassword);
            Statement statement = mysql.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM " + tableNameOrderItem + " WHERE orderItemStatus LIKE '" + status +  "';");
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