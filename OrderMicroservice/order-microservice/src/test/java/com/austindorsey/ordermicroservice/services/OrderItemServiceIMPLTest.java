package com.austindorsey.ordermicroservice.services;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;

import com.austindorsey.ordermicroservice.modal.OrderItem;
import com.austindorsey.ordermicroservice.modal.OrderItemCreateRequest;
import com.austindorsey.ordermicroservice.modal.OrderItemUpdateRequest;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class OrderItemServiceIMPLTest {
    @InjectMocks private OrderItemService service = Mockito.spy(OrderItemServiceIMPL.class);
    @InjectMocks private DriverManagerWrapper driverManagerWrapper = Mockito.spy(DriverManagerWrapper.class);
    @Mock private Connection mockConnection;
    @Mock private Statement mockStatement;
    @Mock private ResultSet mockResult;
    @Captor ArgumentCaptor<String> captor;

    @Test
    public void getOrderItemByOrderId_3Found() throws Exception {
        OrderItem item1 = new OrderItem(1, 2, 3, 4, "PLACED", 20.14, Date.valueOf("2012-3-31"));
        OrderItem item2 = new OrderItem(2, 2, 4, 5, "PROCESSING", 10.80, Date.valueOf("2012-3-20"));
        OrderItem item3 = new OrderItem(3, 2, 5, 6, "FULFILLED", 5.67, Date.valueOf("2012-2-12"));
        OrderItem[] expected = {item1, item2, item3};
        String expectedSQL = "SELECT * FROM null WHERE orderId=" + item1.getOrderId() + ";";

        OrderItem[] returnedItems = service.getOrderItemByOrderId(item1.getOrderId());

        Mockito.verify(mockStatement).executeQuery(captor.capture());
        String capturedSQL = captor.getValue();

        assertArrayEquals(expected, returnedItems);
        assertEquals(expectedSQL, capturedSQL);
    }
    
    @Test
    public void getOrderItemByOrderId_1Found() throws Exception {
        OrderItem item1 = new OrderItem(1, 2, 3, 4, "PLACED", 20.14, Date.valueOf("2012-3-31"));
        OrderItem[] expected = {item1};
        String expectedSQL = "SELECT * FROM null WHERE orderId=" + item1.getOrderId() + ";";

        OrderItem[] returnedItems = service.getOrderItemByOrderId(item1.getOrderId());

        Mockito.verify(mockStatement).executeQuery(captor.capture());
        String capturedSQL = captor.getValue();

        assertArrayEquals(expected, returnedItems);
        assertEquals(expectedSQL, capturedSQL);
    }
    
    @Test
    public void getOrderItemByOrderId_0Found() throws Exception {
        OrderItem[] expected = {};
        String expectedSQL = "SELECT * FROM null WHERE orderId=0;";

        OrderItem[] returnedItems = service.getOrderItemByOrderId(0);

        Mockito.verify(mockStatement).executeQuery(captor.capture());
        String capturedSQL = captor.getValue();

        assertArrayEquals(expected, returnedItems);
        assertEquals(expectedSQL, capturedSQL);
    }
    
    @Test
    public void addOrderItemToOrderId() throws Exception {
        OrderItem item1 = new OrderItem(1, 2, 3, 4, "PLACED", 20.14, Date.valueOf("2012-3-31"));
        OrderItemCreateRequest request = new OrderItemCreateRequest(2, 3, 4, "PLACED");
        String expectedSQL = "INSERT INTO null (orderId, menuItemId, quantity, orderItemStatus, cost) VALUES (" + 
                                item1.getOrderId() + ", " +
                                item1.getMenuItemId() + ", " +
                                item1.getQuantity() + ", " +
                                item1.getOrderItemStatus() + ", " +
                                item1.getCost().doubleValue() + ", " +
                                ");";

        OrderItem returnedItems = service.addOrderItemToOrderId(item1.getOrderId(), request);

        Mockito.verify(mockStatement).executeUpdate(captor.capture());
        String capturedSQL = captor.getValue();

        assertEquals(item1, returnedItems);
        assertEquals(expectedSQL, capturedSQL);
    }

    @Test
    public void getOrderItemById_found() throws Exception {
        OrderItem item1 = new OrderItem(1, 2, 3, 4, "PLACED", 20.14, Date.valueOf("2012-3-31"));
        String expectedSQL = "SELECT * FROM null WHERE id=" + item1.getId() + ";";

        OrderItem returnedItems = service.getOrderItemById(item1.getId());

        Mockito.verify(mockStatement).executeQuery(captor.capture());
        String capturedSQL = captor.getValue();

        assertEquals(item1, returnedItems);
        assertEquals(expectedSQL, capturedSQL);
    }

    @Test
    public void getOrderItemById_notFound() throws Exception {
        String expectedSQL = "SELECT * FROM null WHERE id=0;";

        OrderItem returnedItems = service.getOrderItemById(0);

        Mockito.verify(mockStatement).executeQuery(captor.capture());
        String capturedSQL = captor.getValue();

        assertEquals(null, returnedItems);
        assertEquals(expectedSQL, capturedSQL);
    }

    @Test
    public void updateOrderItemById() throws Exception {
        OrderItem oldItem = new OrderItem(1, 2, 3, 4, "PLACED", 20.14, Date.valueOf("2012-3-31"));
        OrderItem newItem = new OrderItem(1, 2, 3, 6, "CANCELLED", 31.14, Date.valueOf("2012-4-2"));
        OrderItemUpdateRequest request = new OrderItemUpdateRequest(newItem.getQuantity(), newItem.getOrderItemStatus());
        String expectedSQL = "UPDATE null SET " + 
                            "quantity=" + request.getQuantity() + ", " +
                            "orderItemStatus='" + request.getOrderItemStatus() + "';";

        OrderItem returnedItems = service.updateOrderItemById(oldItem.getId(), request);

        Mockito.verify(mockStatement).executeUpdate(captor.capture());
        String capturedSQL = captor.getValue();

        assertEquals(newItem, returnedItems);
        assertEquals(expectedSQL, capturedSQL);
    }

    @Test
    public void updateOrderItemStatusById() throws Exception {
        OrderItem oldItem = new OrderItem(1, 2, 3, 4, "PLACED", 20.14, Date.valueOf("2012-3-31"));
        OrderItem newItem = new OrderItem(1, 2, 3, 4, "CANCELLED", 20.14, Date.valueOf("2012-4-2"));
        String newStatus = newItem.getOrderItemStatus();
        String expectedSQL = "UPDATE null SET orderItemStatus='" + newStatus + "';";

        OrderItem returnedItems = service.updateOrderItemStatusById(oldItem.getId(), newStatus);

        Mockito.verify(mockStatement).executeUpdate(captor.capture());
        String capturedSQL = captor.getValue();

        assertEquals(newItem, returnedItems);
        assertEquals(expectedSQL, capturedSQL);
    }
    
    @Test
    public void getOrderItemsByStatus_3found() throws Exception {
        OrderItem item1 = new OrderItem(1, 2, 3, 4, "FULFILLED", 20.14, Date.valueOf("2012-3-31"));
        OrderItem item2 = new OrderItem(2, 3, 4, 5, "FULFILLED", 10.80, Date.valueOf("2012-3-20"));
        OrderItem item3 = new OrderItem(3, 4, 5, 6, "FULFILLED", 5.67, Date.valueOf("2012-2-12"));
        OrderItem[] expected = {item1, item2, item3};
        String expectedSQL = "SELECT * FROM null WHERE orderItemStatus LIKE " + item1.getOrderItemStatus() + ";";

        OrderItem[] returnedItems = service.getOrderItemsByStatus(item1.getOrderItemStatus());

        Mockito.verify(mockStatement).executeQuery(captor.capture());
        String capturedSQL = captor.getValue();

        assertArrayEquals(expected, returnedItems);
        assertEquals(expectedSQL, capturedSQL);
    }
    @Test
    public void getOrderItemsByStatus_1found() throws Exception {
        OrderItem item1 = new OrderItem(1, 2, 3, 4, "FULFILLED", 20.14, Date.valueOf("2012-3-31"));
        OrderItem[] expected = {item1};
        String expectedSQL = "SELECT * FROM null WHERE orderItemStatus LIKE " + item1.getOrderItemStatus() + ";";

        OrderItem[] returnedItems = service.getOrderItemsByStatus(item1.getOrderItemStatus());

        Mockito.verify(mockStatement).executeQuery(captor.capture());
        String capturedSQL = captor.getValue();

        assertArrayEquals(expected, returnedItems);
        assertEquals(expectedSQL, capturedSQL);
    }
    @Test
    public void getOrderItemsByStatus_0found() throws Exception {
        String status = "StatusThatDoesntExits.doesntExist.com";
        OrderItem[] expected = {};
        String expectedSQL = "SELECT * FROM null WHERE orderItemStatus LIKE " + status + ";";

        OrderItem[] returnedItems = service.getOrderItemsByStatus(status);

        Mockito.verify(mockStatement).executeQuery(captor.capture());
        String capturedSQL = captor.getValue();

        assertArrayEquals(expected, returnedItems);
        assertEquals(expectedSQL, capturedSQL);
    }
}
