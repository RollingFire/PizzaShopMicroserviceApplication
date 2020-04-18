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
import com.austindorsey.ordermicroservice.modal.OrderItemCreateRequestShort;
import com.austindorsey.ordermicroservice.modal.OrderUpdateRequest;
import com.austindorsey.ordermicroservice.modal.OrderWithItems;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

@Service
@PropertySource("classpath:database.properties")
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

    /**
     * Calls the MySQL server to get orders filtered by status and customerId
     * @param customerId Id of the customer who placed the order.
     * @param status Orderstatus to filter by.
     * @return Order[] Array of all the orders that match filter params.
     * @throws SQLException SQL errors
     * @throws ClassNotFroundException Only if com.mysql.cj.jdbc.Driver can not be found.
     */
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

    /**
     * Calls the MySQL server to create a new order
     * @param request OrderCrateRequest that is used to create the order and order items.
     * @return OrderWithItems Returns the newly created order along with the order items created
     * @throws SQLException SQL errors
     * @throws ClassNotFroundException Only if com.mysql.cj.jdbc.Driver can not be found.
     * @throws Exception Throws exception if the status code of the call to the menu api is not 200 when calling OrderItemCreateRequestShort.toOrderItemCreateRequest
     */
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

    /**
     * Calls the MySQL server to get the order by the id.
     * @param id id to retrive.
     * @return Order. Returns null of it can not be found.
     * @throws SQLException SQL errors
     * @throws ClassNotFroundException Only if com.mysql.cj.jdbc.Driver can not be found.
     */
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

    /**
     * Calls the MySQL server to update the order by the id.
     * @param id id to retrive.
     * @param request OrderUpdateRequest Data that the order will be updated to.
     * @return Order Updated order. Returns null of it can not be found.
     * @throws SQLException SQL errors
     * @throws ClassNotFroundException Only if com.mysql.cj.jdbc.Driver can not be found.
     */
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

    /**
     * Calls the MySQL server to update the order by the id.
     * @param id id to retrive.
     * @param status Status to update the order to.
     * @return Order Updated order. Returns null of it can not be found.
     * @throws SQLException SQL errors
     * @throws ClassNotFroundException Only if com.mysql.cj.jdbc.Driver can not be found.
     */
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