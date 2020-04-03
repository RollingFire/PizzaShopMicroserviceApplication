package com.austindorsey.ordermicroservice.services;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;

import com.austindorsey.ordermicroservice.modal.Order;
import com.austindorsey.ordermicroservice.modal.OrderCreateRequest;
import com.austindorsey.ordermicroservice.modal.OrderItem;
import com.austindorsey.ordermicroservice.modal.OrderItemCreateRequest;
import com.austindorsey.ordermicroservice.modal.OrderItemCreateRequestShort;
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
    @InjectMocks private OrderItemService orderItemService = Mockito.spy(OrderItemServiceIMPL.class);
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
        String expectedSQL = "SELECT * FROM null;";

        Mockito.mock(DriverManagerWrapper.class);
        doReturn(mockConnection).when(driverManagerWrapper).getConnection(any(), any(), any());
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResult);
        when(mockResult.next())
            .thenReturn(true)
            .thenReturn(true)
            .thenReturn(true)
            .thenReturn(false);
        when(mockResult.getInt("id"))
            .thenReturn(order1.getId())
            .thenReturn(order2.getId())
            .thenReturn(order3.getId());
        when(mockResult.getInt("customerId"))
            .thenReturn(order1.getCustomerId())
            .thenReturn(order2.getCustomerId())
            .thenReturn(order3.getCustomerId());
        when(mockResult.getString("orderStatus"))
            .thenReturn(order1.getOrderStatus())
            .thenReturn(order2.getOrderStatus())
            .thenReturn(order3.getOrderStatus());
        when(mockResult.getDate("datePlaced"))
            .thenReturn(order1.getDatePlaced())
            .thenReturn(order2.getDatePlaced())
            .thenReturn(order3.getDatePlaced());

        String filterStatus = null;
        Integer filterCustomerId = null;
        Order[] returnedItems = service.getOrders(filterStatus, filterCustomerId);

        Mockito.verify(mockStatement).executeQuery(captor.capture());
        String capturedSQL = captor.getValue();

        assertArrayEquals(expected, returnedItems);
        assertEquals(expectedSQL, capturedSQL);
        verify(driverManagerWrapper, times(1)).getConnection(any(), any(), any());
        verify(mockConnection, times(1)).createStatement();
        verify(mockStatement, times(1)).executeQuery(anyString());
    }
    @Test
    public void getOrders_1Found() throws Exception {
        Order order1 = new Order(1, 2, "PLACED", Date.valueOf("2012-3-31"));
        Order[] expected = {order1};
        String expectedSQL = "SELECT * FROM null;";

        Mockito.mock(DriverManagerWrapper.class);
        doReturn(mockConnection).when(driverManagerWrapper).getConnection(any(), any(), any());
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResult);
        when(mockResult.next())
            .thenReturn(true)
            .thenReturn(false);
        when(mockResult.getInt("id"))
            .thenReturn(order1.getId());
        when(mockResult.getInt("customerId"))
            .thenReturn(order1.getCustomerId());
        when(mockResult.getString("orderStatus"))
            .thenReturn(order1.getOrderStatus());
        when(mockResult.getDate("datePlaced"))
            .thenReturn(order1.getDatePlaced());

        String filterStatus = null;
        Integer filterCustomerId = null;
        Order[] returnedItems = service.getOrders(filterStatus, filterCustomerId);

        Mockito.verify(mockStatement).executeQuery(captor.capture());
        String capturedSQL = captor.getValue();

        assertArrayEquals(expected, returnedItems);
        assertEquals(expectedSQL, capturedSQL);
        verify(driverManagerWrapper, times(1)).getConnection(any(), any(), any());
        verify(mockConnection, times(1)).createStatement();
        verify(mockStatement, times(1)).executeQuery(anyString());
    }
    @Test
    public void getOrders_0Found() throws Exception {
        Order[] expected = {};
        String expectedSQL = "SELECT * FROM null;";

        Mockito.mock(DriverManagerWrapper.class);
        doReturn(mockConnection).when(driverManagerWrapper).getConnection(any(), any(), any());
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResult);
        when(mockResult.next())
            .thenReturn(false);

        String filterStatus = null;
        Integer filterCustomerId = null;
        Order[] returnedItems = service.getOrders(filterStatus, filterCustomerId);

        Mockito.verify(mockStatement).executeQuery(captor.capture());
        String capturedSQL = captor.getValue();

        assertArrayEquals(expected, returnedItems);
        assertEquals(expectedSQL, capturedSQL);
        verify(driverManagerWrapper, times(1)).getConnection(any(), any(), any());
        verify(mockConnection, times(1)).createStatement();
        verify(mockStatement, times(1)).executeQuery(anyString());
    }
    @Test
    public void getOrders_filterStatus() throws Exception {
        Order[] expected = {};
        String filterStatus = "TestStatusFilter";
        String expectedSQL = "SELECT * FROM null WHERE orderStatus LIKE '" + filterStatus + "';";

        Mockito.mock(DriverManagerWrapper.class);
        doReturn(mockConnection).when(driverManagerWrapper).getConnection(any(), any(), any());
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResult);
        when(mockResult.next())
            .thenReturn(false);

        Integer filterCustomerId = null;
        Order[] returnedItems = service.getOrders(filterStatus, filterCustomerId);

        Mockito.verify(mockStatement).executeQuery(captor.capture());
        String capturedSQL = captor.getValue();

        assertArrayEquals(expected, returnedItems);
        assertEquals(expectedSQL, capturedSQL);
        verify(driverManagerWrapper, times(1)).getConnection(any(), any(), any());
        verify(mockConnection, times(1)).createStatement();
        verify(mockStatement, times(1)).executeQuery(anyString());
    }
    @Test
    public void getOrders_filterCustomerId() throws Exception {
        Order[] expected = {};
        int filterCustomerId = 12;
        String expectedSQL = "SELECT * FROM null WHERE customerId=" + filterCustomerId + ";";

        Mockito.mock(DriverManagerWrapper.class);
        doReturn(mockConnection).when(driverManagerWrapper).getConnection(any(), any(), any());
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResult);
        when(mockResult.next())
            .thenReturn(false);

        String filterStatus = null;
        Order[] returnedItems = service.getOrders(filterStatus, filterCustomerId);

        Mockito.verify(mockStatement).executeQuery(captor.capture());
        String capturedSQL = captor.getValue();

        assertArrayEquals(expected, returnedItems);
        assertEquals(expectedSQL, capturedSQL);
        verify(driverManagerWrapper, times(1)).getConnection(any(), any(), any());
        verify(mockConnection, times(1)).createStatement();
        verify(mockStatement, times(1)).executeQuery(anyString());
    }
    @Test
    public void getOrders_filterStatusAndCustomerId() throws Exception {
        Order[] expected = {};
        String filterStatus = "testFilterStatus";
        Integer filterCustomerId = 123456;
        String expectedSQL = "SELECT * FROM null WHERE orderStatus LIKE '" + filterStatus + "' AND customerId=" + filterCustomerId + ";";

        Mockito.mock(DriverManagerWrapper.class);
        doReturn(mockConnection).when(driverManagerWrapper).getConnection(any(), any(), any());
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResult);
        when(mockResult.next())
            .thenReturn(false);

        Order[] returnedItems = service.getOrders(filterStatus, filterCustomerId);

        Mockito.verify(mockStatement).executeQuery(captor.capture());
        String capturedSQL = captor.getValue();

        assertArrayEquals(expected, returnedItems);
        assertEquals(expectedSQL, capturedSQL);
        verify(driverManagerWrapper, times(1)).getConnection(any(), any(), any());
        verify(mockConnection, times(1)).createStatement();
        verify(mockStatement, times(1)).executeQuery(anyString());
    }

    @Test
    public void postOrder_0Items() throws Exception {
        Order expectedOrder = new Order(1, 2, "PLACED", Date.valueOf("2012-3-31"));
        OrderItemCreateRequestShort[] itemRequests = {};
        OrderItem[] items = {};
        OrderCreateRequest request = new OrderCreateRequest(2, "PLACED", itemRequests);
        String expectedSQL = "INSERT INTO null (customerId, orderStatus) VALUES (" +
                            expectedOrder.getCustomerId() + ", '" +
                            expectedOrder.getOrderStatus() + "');";
        OrderWithItems expected = new OrderWithItems(expectedOrder, items);

        Mockito.mock(DriverManagerWrapper.class);
        doReturn(mockConnection).when(driverManagerWrapper).getConnection(any(), any(), any());
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeUpdate(anyString())).thenReturn(1);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResult);
        when(mockResult.next())
            .thenReturn(true)
            .thenReturn(false);
        when(mockResult.getInt("id"))
            .thenReturn(expectedOrder.getId());
        when(mockResult.getInt("customerId"))
            .thenReturn(expectedOrder.getCustomerId());
        when(mockResult.getString("orderStatus"))
            .thenReturn(expectedOrder.getOrderStatus());
        when(mockResult.getDate("datePlaced"))
            .thenReturn(expectedOrder.getDatePlaced());

        OrderWithItems returned = service.postOrder(request);

        Mockito.verify(mockStatement).executeUpdate(captor.capture());
        String capturedSQL = captor.getValue();

        assertEquals(expected, returned);
        assertEquals(expectedSQL, capturedSQL);
        verify(driverManagerWrapper, times(1)).getConnection(any(), any(), any());
        verify(mockConnection, times(1)).createStatement();
        verify(mockStatement, times(1)).executeQuery(anyString());
    }

    // TODO Fix this test
    // @Test
    // public void postOrder_2Items() throws Exception {
    //     Order expectedOrder = new Order(1, 2, "PLACED", Date.valueOf("2012-3-31"));
    //     OrderItem item1 = new OrderItem(1, 2, 3, 4, "PLACED", 20.14, Date.valueOf("2012-3-31"));
    //     OrderItem item2 = new OrderItem(2, 2, 4, 5, "PROCESSING", 10.80, Date.valueOf("2012-3-20"));
    //     OrderItem[] items = {item1, item2};
    //     OrderItemCreateRequestShort item1Request = Mockito.mock(OrderItemCreateRequestShort.class);
    //     OrderItemCreateRequestShort item2Request = Mockito.mock(OrderItemCreateRequestShort.class);
    //     OrderItemCreateRequestShort[] itemRequests = {item1Request, item2Request};
    //     OrderCreateRequest request = new OrderCreateRequest(2, "PLACED", itemRequests);
    //     String expectedSQL = "INSERT INTO null (customerId, orderStatus) VALUES (" +
    //                         expectedOrder.getCustomerId() + ", '" +
    //                         expectedOrder.getOrderStatus() + "');";
    //     OrderWithItems expected = new OrderWithItems(expectedOrder, items);

    //     doReturn(item1).when(orderItemService).addOrderItemToOrderId(expectedOrder.getId(), item1Request);
    //     doReturn(item2).when(orderItemService).addOrderItemToOrderId(expectedOrder.getId(), item2Request);
    //     Mockito.mock(DriverManagerWrapper.class);
    //     doReturn(mockConnection).when(driverManagerWrapper).getConnection(any(), any(), any());
    //     when(mockConnection.createStatement()).thenReturn(mockStatement);
    //     when(mockStatement.executeUpdate(anyString())).thenReturn(1);
    //     when(mockStatement.executeQuery(anyString())).thenReturn(mockResult);
    //     when(mockResult.next())
    //         .thenReturn(true)
    //         .thenReturn(false);
    //     when(mockResult.getInt("id"))
    //         .thenReturn(expectedOrder.getId());
    //     when(mockResult.getInt("customerId"))
    //         .thenReturn(expectedOrder.getCustomerId());
    //     when(mockResult.getString("orderStatus"))
    //         .thenReturn(expectedOrder.getOrderStatus());
    //     when(mockResult.getDate("datePlaced"))
    //         .thenReturn(expectedOrder.getDatePlaced());

    //     OrderWithItems returned = service.postOrder(request);

    //     Mockito.verify(mockStatement).executeUpdate(captor.capture());
    //     String capturedSQL = captor.getValue();

    //     assertEquals(expected, returned);
    //     assertEquals(expectedSQL, capturedSQL);
    //     verify(driverManagerWrapper, times(1)).getConnection(any(), any(), any());
    //     verify(mockConnection, times(1)).createStatement();
    //     verify(mockStatement, times(1)).executeUpdate(anyString());
    //     verify(mockStatement, times(1)).executeQuery(anyString());
    // }
    
    @Test
    public void getOrderById_found() throws Exception {
        Order expected = new Order(1, 2, "PLACED", Date.valueOf("2012-3-31"));
        String expectedSQL = "SELECT * FROM null WHERE id=" + expected.getId() + ";";

        Mockito.mock(DriverManagerWrapper.class);
        doReturn(mockConnection).when(driverManagerWrapper).getConnection(any(), any(), any());
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResult);
        when(mockResult.next())
            .thenReturn(true)
            .thenReturn(false);
        when(mockResult.getInt("customerId"))
            .thenReturn(expected.getCustomerId());
        when(mockResult.getString("orderStatus"))
            .thenReturn(expected.getOrderStatus());
        when(mockResult.getDate("datePlaced"))
            .thenReturn(expected.getDatePlaced());

        Order returned = service.getOrderById(expected.getId());

        Mockito.verify(mockStatement).executeQuery(captor.capture());
        String capturedSQL = captor.getValue();

        assertEquals(expected, returned);
        assertEquals(expectedSQL, capturedSQL);
        verify(driverManagerWrapper, times(1)).getConnection(any(), any(), any());
        verify(mockConnection, times(1)).createStatement();
        verify(mockStatement, times(1)).executeQuery(anyString());
    }
    @Test
    public void getOrderById_notFound() throws Exception {
        String expectedSQL = "SELECT * FROM null WHERE id=0;";

        Mockito.mock(DriverManagerWrapper.class);
        doReturn(mockConnection).when(driverManagerWrapper).getConnection(any(), any(), any());
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResult);
        when(mockResult.next())
            .thenReturn(false);

        Order returned = service.getOrderById(0);

        Mockito.verify(mockStatement).executeQuery(captor.capture());
        String capturedSQL = captor.getValue();

        assertEquals(null, returned);
        assertEquals(expectedSQL, capturedSQL);
        verify(driverManagerWrapper, times(1)).getConnection(any(), any(), any());
        verify(mockConnection, times(1)).createStatement();
        verify(mockStatement, times(1)).executeQuery(anyString());
    }

    @Test
    public void updateOrderById() throws Exception {
        Order oldOrder = new Order(1, 2, "PLACED", Date.valueOf("2012-3-31"));
        Order newOrder = new Order(1, 2, "FULLFILLED", Date.valueOf("2012-4-1"));
        OrderUpdateRequest request = new OrderUpdateRequest(newOrder.getOrderStatus());
        String expectedSQL = "UPDATE null SET orderStatus='" + newOrder.getOrderStatus() + "' WHERE id=" + oldOrder.getId() + ";";

        Mockito.mock(DriverManagerWrapper.class);
        doReturn(mockConnection).when(driverManagerWrapper).getConnection(any(), any(), any());
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        doReturn(newOrder).when(service).getOrderById(oldOrder.getId());

        Order returned = service.updateOrderById(oldOrder.getId(), request);

        Mockito.verify(mockStatement).executeUpdate(captor.capture());
        String capturedSQL = captor.getValue();

        assertEquals(newOrder, returned);
        assertEquals(expectedSQL, capturedSQL);
        verify(driverManagerWrapper, times(1)).getConnection(any(), any(), any());
        verify(mockConnection, times(1)).createStatement();
        verify(mockStatement, times(1)).executeUpdate(anyString());
    }

    @Test
    public void updateOrderStatusById() throws Exception {
        Order oldOrder = new Order(1, 2, "PLACED", Date.valueOf("2012-3-31"));
        Order newOrder = new Order(1, 2, "FULLFILLED", Date.valueOf("2012-4-1"));
        String expectedSQL = "UPDATE null SET orderStatus='" + newOrder.getOrderStatus() + "' WHERE id=" + oldOrder.getId() + ";";

        Mockito.mock(DriverManagerWrapper.class);
        doReturn(mockConnection).when(driverManagerWrapper).getConnection(any(), any(), any());
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        doReturn(newOrder).when(service).getOrderById(oldOrder.getId());

        Order returned = service.updateOrderStatusById(oldOrder.getId(), newOrder.getOrderStatus());

        Mockito.verify(mockStatement).executeUpdate(captor.capture());
        String capturedSQL = captor.getValue();

        assertEquals(newOrder, returned);
        assertEquals(expectedSQL, capturedSQL);
        verify(driverManagerWrapper, times(1)).getConnection(any(), any(), any());
        verify(mockConnection, times(1)).createStatement();
        verify(mockStatement, times(1)).executeUpdate(anyString());
    }
}
