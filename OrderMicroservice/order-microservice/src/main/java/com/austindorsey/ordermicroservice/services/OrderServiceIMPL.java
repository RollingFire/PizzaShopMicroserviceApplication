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
import com.austindorsey.ordermicroservice.modal.OrderItem;
import com.austindorsey.ordermicroservice.modal.OrderItemCreateRequest;
import com.austindorsey.ordermicroservice.modal.OrderItemCreateRequestShort;
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
    @Value("${mysql.tableName.order}")
    private String tableNameOrder;
    @Value("${mysql.tableName.orderItem}")
    private String tableNameOrderItem;
    private Connection mysql;

    @Autowired private DriverManagerWrapper driverManagerWrapped;
    @Autowired private OrderItemService orderItemService;

    @Override
    public Order[] getOrders(String status, Integer customerId) throws SQLException, ClassNotFoundException {
        String filterQuary = "";
        if (status != null) {
            filterQuary = " WHERE orderStatus LIKE '" + status + "'";
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
            ResultSet result = statement.executeQuery("SELECT * FROM " + tableNameOrder + filterQuary + ";");
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
    public OrderWithItems postOrder(OrderCreateRequest request) throws Exception {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName;
            mysql = driverManagerWrapped.getConnection(url, dbUserName, dbPassword);
            Statement statement = mysql.createStatement();
            int rowsCreated = statement.executeUpdate(request.getOrderSQLInsertStatement(tableNameOrder));
            if (rowsCreated > 0) {
                ResultSet result = statement.executeQuery(request.getSQLSelectStatement(tableNameOrder));
                if (result.next()) {
                    int id = result.getInt("id");
                    int OrderCustomerId = result.getInt("customerId");
                    String orderStatus = result.getString("orderStatus");
                    Date datePlaced = result.getDate("datePlaced");
                    Order order = new Order(id, OrderCustomerId, orderStatus, datePlaced);


                    List<OrderItem> items = new ArrayList<>();
                    for (OrderItemCreateRequestShort item : request.getOrderItems()) {
                        items.add(orderItemService.addOrderItemToOrderId(order.getId(), item.toOrderItemCreateRequest(order.getId())));
                    }
                    return new OrderWithItems(order, items.toArray(new OrderItem[items.size()]));
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
    public Order getOrderById(int id) throws SQLException, ClassNotFoundException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName;
            mysql = driverManagerWrapped.getConnection(url, dbUserName, dbPassword);
            Statement statement = mysql.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM " + tableNameOrder + " WHERE id=" + id + ";");
            if (result.next()) {
                int OrderCustomerId = result.getInt("customerId");
                String orderStatus = result.getString("orderStatus");
                Date datePlaced = result.getDate("datePlaced");
                return new Order(id, OrderCustomerId, orderStatus, datePlaced);
            }
            return null;
        } finally {
            if (mysql != null) {
                mysql.close();
            }
        }
    }

    @Override
    public Order updateOrderById(int id, OrderUpdateRequest request) throws SQLException, ClassNotFoundException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName;
            mysql = driverManagerWrapped.getConnection(url, dbUserName, dbPassword);
            Statement statement = mysql.createStatement();
            statement.executeUpdate(request.getSQLUpdateStatement(tableNameOrder, id));
            return getOrderById(id);
        } finally {
            if (mysql != null) {
                mysql.close();
            }
        }
    }

    @Override
    public Order updateOrderStatusById(int id, String status) throws SQLException, ClassNotFoundException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName;
            mysql = driverManagerWrapped.getConnection(url, dbUserName, dbPassword);
            Statement statement = mysql.createStatement();
            statement.executeUpdate("UPDATE " + tableNameOrder + " SET orderStatus='" + status + "' WHERE id=" + id + ";");
            return getOrderById(id);
        } finally {
            if (mysql != null) {
                mysql.close();
            }
        }
    }
}