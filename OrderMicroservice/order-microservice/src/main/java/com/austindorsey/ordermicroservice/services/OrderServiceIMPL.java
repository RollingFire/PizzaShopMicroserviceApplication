package com.austindorsey.ordermicroservice.services;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.austindorsey.ordermicroservice.modal.Order;
import com.austindorsey.ordermicroservice.modal.OrderCreateRequest;
import com.austindorsey.ordermicroservice.modal.OrderUpdateRequest;
import com.austindorsey.ordermicroservice.modal.OrderWithItems;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

@Service
@PropertySource("classpath:database.properties")
@PropertySource("classpath:api.properties")
public class OrderServiceIMPL implements OrderService {
    
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

    @Override
    public Order[] getOrders() throws SQLException, ClassNotFoundException {
        return getOrders(null, null);
    }

    @Override
    public Order[] getOrders(Integer customerId) throws SQLException, ClassNotFoundException {
        return getOrders(null, customerId);
    }

    @Override
    public Order[] getOrders(String status) throws SQLException, ClassNotFoundException {
        return getOrders(status, null);
    }

    @Override
    public Order[] getOrders(String status, Integer customerId) throws SQLException, ClassNotFoundException {
        String filterQuary = "";
        if (status != null) {
            filterQuary = " WHERE status LIKE " + status;
        }
        if (customerId != null) {
            if (filterQuary == "") {
                filterQuary = " WHERE customerId=" + customerId;
            } else {
                filterQuary += " AND customerId=" + customerId;
            }
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName;
            mysql = driverManagerWrapped.getConnection(url, dbUserName, dbPassword);
            Statement statement = mysql.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM " + tableName + filterQuary + ";");
            List<Order> list = new ArrayList<>();
            while (result.next()) {
                int id = result.getInt("id");
                int OrderCustomerId = result.getInt("customerId");
                String orderStatus = result.getString("orderStatus");
                Date datePlaced = result.getDate("datePlaced");
                list.add(new Order(id, OrderCustomerId, orderStatus, datePlaced));
            }
            return list.toArray(new Order[list.size()]);
        } finally {
            if (mysql != null) {
                mysql.close();
            }
        }
    }

    @Override
    public OrderWithItems postOrder(OrderCreateRequest request) throws SQLException, ClassNotFoundException {
        // try {
        //     Class.forName("com.mysql.cj.jdbc.Driver");
        //     String url = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName;
        //     mysql = driverManagerWrapped.getConnection(url, dbUserName, dbPassword);
        //     Statement statement = mysql.createStatement();
        //     statement.executeUpdate("INSERT INTO " + tableName);


            
        //     List<Order> list = new ArrayList<>();
        //     if (result.next()) {
        //         int id = result.getInt("id");
        //         int OrderCustomerId = result.getInt("customerId");
        //         String orderStatus = result.getString("orderStatus");
        //         Date datePlaced = result.getDate("datePlaced");
        //         list.add(new Order(id, OrderCustomerId, orderStatus, datePlaced));
        //     }
        //     return list.toArray(new Order[list.size()]);
        // } finally {
        //     if (mysql != null) {
        //         mysql.close();
        //     }
        // }
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Order getOrderById(int id) throws SQLException, ClassNotFoundException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Order updateOrderById(int id, OrderUpdateRequest request) throws SQLException, ClassNotFoundException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Order updateOrderStatusById(int id, String status) throws SQLException, ClassNotFoundException {
        // TODO Auto-generated method stub
        return null;
    }
    
}