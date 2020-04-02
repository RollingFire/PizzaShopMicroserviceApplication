package com.austindorsey.ordermicroservice.services;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;

import com.austindorsey.ordermicroservice.modal.Order;
import com.austindorsey.ordermicroservice.modal.OrderCreateRequest;
import com.austindorsey.ordermicroservice.modal.OrderItem;
import com.austindorsey.ordermicroservice.modal.OrderItemCreateRequest;
import com.austindorsey.ordermicroservice.modal.OrderUpdateRequest;
import com.austindorsey.ordermicroservice.modal.OrderWithItems;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class OrderServiceIMPLTest {
    @InjectMocks private OrderService service = Mockito.spy(OrderServiceIMPL.class);
    @InjectMocks private DriverManagerWrapper driverManagerWrapper = Mockito.spy(DriverManagerWrapper.class);
    @Mock private Connection mockConnection;
    @Mock private Statement mockStatement;
    @Mock private ResultSet mockResult;
    @Captor ArgumentCaptor<String> captor;

    @Test
    public void getOrders_3Found() throws Exception {
        Order order1 = new Order(1, 2, "PLACED", Date.valueOf("2012-3-31"));
        Order order2 = new Order(2, 3, "CANCELLED", Date.valueOf("2012-3-20"));
        Order order3 = new Order(3, 4, "FULFILLED", Date.valueOf("2012-2-12"));
        Order[] expected = {order1, order2, order3};
        String expectedSQL = "SELECT * FROM null";

        String filterStatus = null;
        Integer filterCustomerId = null;
        Order[] returnedItems = service.getOrders(filterStatus, filterCustomerId);

        Mockito.verify(mockStatement).executeQuery(captor.capture());
        String capturedSQL = captor.getValue();

        assertArrayEquals(expected, returnedItems);
        assertEquals(expectedSQL, capturedSQL);
    }
    @Test
    public void getOrders_1Found() throws Exception {
        Order order1 = new Order(1, 2, "PLACED", Date.valueOf("2012-3-31"));
        Order[] expected = {order1};
        String expectedSQL = "SELECT * FROM null";

        String filterStatus = null;
        Integer filterCustomerId = null;
        Order[] returnedItems = service.getOrders(filterStatus, filterCustomerId);

        Mockito.verify(mockStatement).executeQuery(captor.capture());
        String capturedSQL = captor.getValue();

        assertArrayEquals(expected, returnedItems);
        assertEquals(expectedSQL, capturedSQL);
    }
    @Test
    public void getOrders_0Found() throws Exception {
        Order[] expected = {};
        String expectedSQL = "SELECT * FROM null";

        String filterStatus = null;
        Integer filterCustomerId = null;
        Order[] returnedItems = service.getOrders(filterStatus, filterCustomerId);

        Mockito.verify(mockStatement).executeQuery(captor.capture());
        String capturedSQL = captor.getValue();

        assertArrayEquals(expected, returnedItems);
        assertEquals(expectedSQL, capturedSQL);
    }
    @Test
    public void getOrders_filterStatus() throws Exception {
        Order[] expected = {};
        String filterStatus = "TestStatusFilter";
        String expectedSQL = "SELECT * FROM null WHERE status LIKE '" + filterStatus + "';";

        Integer filterCustomerId = null;
        Order[] returnedItems = service.getOrders(filterStatus, filterCustomerId);

        Mockito.verify(mockStatement).executeQuery(captor.capture());
        String capturedSQL = captor.getValue();

        assertArrayEquals(expected, returnedItems);
        assertEquals(expectedSQL, capturedSQL);
    }
    @Test
    public void getOrders_filterCustomerId() throws Exception {
        Order[] expected = {};
        int filterCustomerId = 12;
        String expectedSQL = "SELECT * FROM null WHERE customerId=" + filterCustomerId + "';";

        String filterStatus = null;
        Order[] returnedItems = service.getOrders(filterStatus, filterCustomerId);

        Mockito.verify(mockStatement).executeQuery(captor.capture());
        String capturedSQL = captor.getValue();

        assertArrayEquals(expected, returnedItems);
        assertEquals(expectedSQL, capturedSQL);
    }
    @Test
    public void getOrders_filterStatusAndCustomerId() throws Exception {
        Order[] expected = {};
        String expectedSQL = "SELECT * FROM null";

        String filterStatus = null;
        Integer filterCustomerId = null;
        Order[] returnedItems = service.getOrders(filterStatus, filterCustomerId);

        Mockito.verify(mockStatement).executeQuery(captor.capture());
        String capturedSQL = captor.getValue();

        assertArrayEquals(expected, returnedItems);
        assertEquals(expectedSQL, capturedSQL);
    }

    @Test
    public void postOrder_0Items() throws Exception {
        Order expectedOrder = new Order(1, 2, "PLACED", Date.valueOf("2012-3-31"));
        OrderItemCreateRequest[] itemRequests = {};
        OrderItem[] items = {};
        OrderCreateRequest request = new OrderCreateRequest(2, "PLACED", itemRequests);
        String expectedSQL = "INSERT INTO null (customerId, orderItemStatus) VALUES (" +
                            expectedOrder.getCustomerId() + ", '" +
                            expectedOrder.getOrderStatus() + "');";
        OrderWithItems expected = new OrderWithItems(expectedOrder, items);

        OrderWithItems returned = service.postOrder(request);

        Mockito.verify(mockStatement).executeUpdate(captor.capture());
        String capturedSQL = captor.getValue();

        assertEquals(expected, returned);
        assertEquals(expectedSQL, capturedSQL);
    }
    @Test
    public void postOrder_2Items() throws Exception {
        Order expectedOrder = new Order(1, 2, "PLACED", Date.valueOf("2012-3-31"));
        OrderItem item1 = new OrderItem(1, 2, 3, 4, "PLACED", 20.14, Date.valueOf("2012-3-31"));
        OrderItem item2 = new OrderItem(2, 2, 4, 5, "PROCESSING", 10.80, Date.valueOf("2012-3-20"));
        OrderItem[] items = {item1, item2};
        OrderItemCreateRequest item1Request = new OrderItemCreateRequest(2, 3, 4, "PLACED");
        OrderItemCreateRequest item2Request = new OrderItemCreateRequest(2, 4, 5, "PROCESSING");
        OrderItemCreateRequest[] itemRequests = {item1Request, item2Request};
        OrderCreateRequest request = new OrderCreateRequest(2, "PLACED", itemRequests);
        String expectedSQL = "INSERT INTO null (customerId, orderItemStatus) VALUES (" +
                            expectedOrder.getCustomerId() + ", '" +
                            expectedOrder.getOrderStatus() + "');";
        OrderWithItems expected = new OrderWithItems(expectedOrder, items);

        OrderWithItems returned = service.postOrder(request);

        Mockito.verify(mockStatement).executeUpdate(captor.capture());
        String capturedSQL = captor.getValue();

        assertEquals(expected, returned);
        assertEquals(expectedSQL, capturedSQL);
    }
    
    @Test
    public void getOrderById_found() throws Exception {
        Order expected = new Order(1, 2, "PLACED", Date.valueOf("2012-3-31"));
        String expectedSQL = "SELECT * FROM null WHERE id=" + expected.getId() + ";";

        Order returned = service.getOrderById(expected.getId());

        Mockito.verify(mockStatement).executeQuery(captor.capture());
        String capturedSQL = captor.getValue();

        assertEquals(expected, returned);
        assertEquals(expectedSQL, capturedSQL);
    }
    @Test
    public void getOrderById_notFound() throws Exception {
        String expectedSQL = "SELECT * FROM null WHERE id=0;";

        Order returned = service.getOrderById(0);

        Mockito.verify(mockStatement).executeQuery(captor.capture());
        String capturedSQL = captor.getValue();

        assertEquals(null, returned);
        assertEquals(expectedSQL, capturedSQL);
    }

    @Test
    public void updateOrderById() throws Exception {
        Order oldOrder = new Order(1, 2, "PLACED", Date.valueOf("2012-3-31"));
        Order newOrder = new Order(1, 2, "FULLFILLED", Date.valueOf("2012-4-1"));
        OrderUpdateRequest request = new OrderUpdateRequest(newOrder.getOrderStatus());
        String expectedSQL = "UPDATE null SET orderStatus='" + newOrder.getOrderStatus() + "';";

        Order returned = service.updateOrderById(oldOrder.getId(), request);

        Mockito.verify(mockStatement).executeUpdate(captor.capture());
        String capturedSQL = captor.getValue();

        assertEquals(newOrder, returned);
        assertEquals(expectedSQL, capturedSQL);
    }

    @Test
    public void updateOrderStatusById() throws Exception {
        Order oldOrder = new Order(1, 2, "PLACED", Date.valueOf("2012-3-31"));
        Order newOrder = new Order(1, 2, "FULLFILLED", Date.valueOf("2012-4-1"));
        String expectedSQL = "UPDATE null SET orderStatus='" + newOrder.getOrderStatus() + "';";

        Order returned = service.updateOrderStatusById(oldOrder.getId(), newOrder.getOrderStatus());

        Mockito.verify(mockStatement).executeUpdate(captor.capture());
        String capturedSQL = captor.getValue();

        assertEquals(newOrder, returned);
        assertEquals(expectedSQL, capturedSQL);
    }
}
