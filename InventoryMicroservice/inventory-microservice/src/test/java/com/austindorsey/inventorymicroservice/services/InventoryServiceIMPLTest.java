package com.austindorsey.inventorymicroservice.services;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import com.austindorsey.inventorymicroservice.model.InventoryItem;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class InventoryServiceIMPLTest {
    @InjectMocks private InventoryService service = new InventoryServiceIMPL();
    @InjectMocks private DriverManagerWrapper driverManagerWrapper = Mockito.spy(DriverManagerWrapper.class);
    @Mock private Connection mockConnection;
    @Mock private Statement mockStatement;
    @Mock private ResultSet mockResult;

    @Test
    public void getInventory__3Items() throws Exception {
        InventoryItem item1_passed = new InventoryItem(1, "Dough", 6.0, "COUNT", null, null);
        InventoryItem item1_expected = new InventoryItem(1, "Dough", 6.0, "COUNT", 0.0, 0.0);
        InventoryItem item2 = new InventoryItem(5, "Pizza Sauce", 10.1, "kg", 2.0, 16.0);
        InventoryItem item3 = new InventoryItem(12, "Pepperoni", 0.0, "lb", 2.0, 4.0);
        InventoryItem[] items = { item1_expected, item2, item3 };

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
            .thenReturn(item1_passed.getId())
            .thenReturn(item2.getId())
            .thenReturn(item3.getId());
        when(mockResult.getString("itemName"))
            .thenReturn(item1_passed.getName())
            .thenReturn(item2.getName())
            .thenReturn(item3.getName());
        when(mockResult.getDouble("units"))
            .thenReturn(item1_passed.getUnits().doubleValue())
            .thenReturn(item2.getUnits().doubleValue())
            .thenReturn(item3.getUnits().doubleValue());
        when(mockResult.getString("unitType"))
            .thenReturn(item1_passed.getUnitType())
            .thenReturn(item2.getUnitType())
            .thenReturn(item3.getUnitType());
        when(mockResult.getDouble("restockAt"))
            .thenReturn((item1_passed.getRestockAt() != null ? item1_passed.getRestockAt().doubleValue():0))
            .thenReturn((item2.getRestockAt() != null ? item2.getRestockAt().doubleValue():0))
            .thenReturn((item3.getRestockAt() != null ? item3.getRestockAt().doubleValue():0));
        when(mockResult.getDouble("restockAmount"))
            .thenReturn((item1_passed.getRestockAmount() != null ? item1_passed.getRestockAmount().doubleValue():0))
            .thenReturn((item2.getRestockAmount() != null ? item2.getRestockAmount().doubleValue():0))
            .thenReturn((item3.getRestockAmount() != null ? item3.getRestockAmount().doubleValue():0));

        InventoryItem[] returnedInventory = service.getInventory();

        assertArrayEquals(items, returnedInventory);
        verify(driverManagerWrapper, times(1)).getConnection(any(), any(), any());
        verify(mockConnection, times(1)).createStatement();
        verify(mockStatement, times(1)).executeQuery(anyString());
    }

    @Test
    public void getInventory__empty() throws Exception {
        InventoryItem[] items = {};

        Mockito.mock(DriverManagerWrapper.class);
        doReturn(mockConnection).when(driverManagerWrapper).getConnection(any(), any(), any());
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResult);
        when(mockResult.next())
            .thenReturn(false);
            
        InventoryItem[] returnedInventory = service.getInventory();
        
        assertArrayEquals(items, returnedInventory);
        verify(driverManagerWrapper, times(1)).getConnection(any(), any(), any());
        verify(mockConnection, times(1)).createStatement();
        verify(mockStatement, times(1)).executeQuery(anyString());
    }

    @Test
    public void getItemByName__itemFound() throws Exception {
        InventoryItem item = new InventoryItem(5, "Pizza Sauce", 10.1, "kg", 2.4, 16.2);

        Mockito.mock(DriverManagerWrapper.class);
        doReturn(mockConnection).when(driverManagerWrapper).getConnection(any(), any(), any());
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResult);
        when(mockResult.next())
            .thenReturn(true)
            .thenReturn(false);
        when(mockResult.getInt("id"))
            .thenReturn(item.getId());
        when(mockResult.getString("itemName"))
            .thenReturn(item.getName());
        when(mockResult.getDouble("units"))
            .thenReturn(item.getUnits().doubleValue());
        when(mockResult.getString("unitType"))
            .thenReturn(item.getUnitType());
        when(mockResult.getDouble("restockAt"))
            .thenReturn((item.getRestockAt() != null ? item.getRestockAt().doubleValue():0));
        when(mockResult.getDouble("restockAmount"))
            .thenReturn((item.getRestockAmount() != null ? item.getRestockAmount().doubleValue():0));


        InventoryItem returnedItem = service.getItemByName(item.getName());

        assertEquals(item, returnedItem);
        verify(driverManagerWrapper, times(1)).getConnection(any(), any(), any());
        verify(mockConnection, times(1)).createStatement();
        verify(mockStatement, times(1)).executeQuery(anyString());
    }
    
    @Test
    public void getItemByName__itemNotFound() throws Exception {
        String itemName = "Pizza Sauce";

        Mockito.mock(DriverManagerWrapper.class);
        doReturn(mockConnection).when(driverManagerWrapper).getConnection(any(), any(), any());
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResult);
        when(mockResult.next())
            .thenReturn(false);

        InventoryItem returnedItem = service.getItemByName(itemName);

        assertEquals(null, returnedItem);
        verify(driverManagerWrapper, times(1)).getConnection(any(), any(), any());
        verify(mockConnection, times(1)).createStatement();
        verify(mockStatement, times(1)).executeQuery(anyString());
    }

    @Test
    public void getItemByID__itemFound() throws Exception {
        InventoryItem item = new InventoryItem(5, "Pizza Sauce", 10.1, "kg", 2.4, 16.2);

        Mockito.mock(DriverManagerWrapper.class);
        doReturn(mockConnection).when(driverManagerWrapper).getConnection(any(), any(), any());
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResult);
        when(mockResult.next())
            .thenReturn(true)
            .thenReturn(false);
        when(mockResult.getInt("id"))
            .thenReturn(item.getId());
        when(mockResult.getString("itemName"))
            .thenReturn(item.getName());
        when(mockResult.getDouble("units"))
            .thenReturn(item.getUnits().doubleValue());
        when(mockResult.getString("unitType"))
            .thenReturn(item.getUnitType());
        when(mockResult.getDouble("restockAt"))
            .thenReturn((item.getRestockAt() != null ? item.getRestockAt().doubleValue():0));
        when(mockResult.getDouble("restockAmount"))
            .thenReturn((item.getRestockAmount() != null ? item.getRestockAmount().doubleValue():0));


        InventoryItem returnedItem = service.getItemByID(item.getId());

        assertEquals(item, returnedItem);
        verify(driverManagerWrapper, times(1)).getConnection(any(), any(), any());
        verify(mockConnection, times(1)).createStatement();
        verify(mockStatement, times(1)).executeQuery(anyString());
    }
    
    @Test
    public void getItemByID__itemNotFound() throws Exception {
        int id = 12;

        Mockito.mock(DriverManagerWrapper.class);
        doReturn(mockConnection).when(driverManagerWrapper).getConnection(any(), any(), any());
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResult);
        when(mockResult.next())
            .thenReturn(false);

        InventoryItem returnedItem = service.getItemByID(id);

        assertEquals(null, returnedItem);
        verify(driverManagerWrapper, times(1)).getConnection(any(), any(), any());
        verify(mockConnection, times(1)).createStatement();
        verify(mockStatement, times(1)).executeQuery(anyString());
    }

    @Test
    public void restockItemByName_itemFound_AddingAmount() throws Exception {
        InventoryItem item = new InventoryItem(5, "Pizza Sauce", 10.1, "kg", 2.4, 16.2);
        double expectedNewAmount = item.getUnits().doubleValue() + item.getRestockAmount().doubleValue();

        Mockito.mock(DriverManagerWrapper.class);
        doReturn(mockConnection).when(driverManagerWrapper).getConnection(any(), any(), any());
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResult);
        when(mockResult.next())
            .thenReturn(true)
            .thenReturn(false);
        when(mockResult.getInt("id"))
            .thenReturn(item.getId());
        when(mockResult.getString("itemName"))
            .thenReturn(item.getName());
        when(mockResult.getDouble("units"))
            .thenReturn(item.getUnits().doubleValue());
        when(mockResult.getString("unitType"))
            .thenReturn(item.getUnitType());
        when(mockResult.getDouble("restockAt"))
            .thenReturn((item.getRestockAt() != null ? item.getRestockAt().doubleValue():0));
        when(mockResult.getDouble("restockAmount"))
            .thenReturn((item.getRestockAmount() != null ? item.getRestockAmount().doubleValue():0));
        when(mockStatement.executeUpdate(anyString())).thenReturn(1);

        double returnedItem = service.restockItemByName(item.getName(), item.getRestockAmount().doubleValue());

        assertEquals(expectedNewAmount, returnedItem);
        verify(driverManagerWrapper, times(1)).getConnection(any(), any(), any());
        verify(mockConnection, times(1)).createStatement();
        verify(mockStatement, times(1)).executeQuery(anyString());
        verify(mockStatement, times(1)).executeUpdate(anyString());
    }
    
    @Test
    public void restockItemByName_itemFound_RemovingAmount_MoreThan0() throws Exception {
        InventoryItem item = new InventoryItem(5, "Pizza Sauce", 10.1, "kg", 2.4, -2.1);
        double expectedNewAmount = item.getUnits().doubleValue() + item.getRestockAmount().doubleValue();

        Mockito.mock(DriverManagerWrapper.class);
        doReturn(mockConnection).when(driverManagerWrapper).getConnection(any(), any(), any());
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResult);
        when(mockResult.next())
            .thenReturn(true)
            .thenReturn(false);
        when(mockResult.getInt("id"))
            .thenReturn(item.getId());
        when(mockResult.getString("itemName"))
            .thenReturn(item.getName());
        when(mockResult.getDouble("units"))
            .thenReturn(item.getUnits().doubleValue());
        when(mockResult.getString("unitType"))
            .thenReturn(item.getUnitType());
        when(mockResult.getDouble("restockAt"))
            .thenReturn((item.getRestockAt() != null ? item.getRestockAt().doubleValue():0));
        when(mockResult.getDouble("restockAmount"))
            .thenReturn((item.getRestockAmount() != null ? item.getRestockAmount().doubleValue():0));
        when(mockStatement.executeUpdate(anyString())).thenReturn(1);

        double returnedItem = service.restockItemByName(item.getName(), item.getRestockAmount().doubleValue());

        assertEquals(expectedNewAmount, returnedItem);
        verify(driverManagerWrapper, times(1)).getConnection(any(), any(), any());
        verify(mockConnection, times(1)).createStatement();
        verify(mockStatement, times(1)).executeQuery(anyString());
        verify(mockStatement, times(1)).executeUpdate(anyString());
    }
        
    @Test
    public void restockItemByName_itemFound_RemovingAmount_LessThan0() throws Exception {
        InventoryItem item = new InventoryItem(5, "Pizza Sauce", 0.8, "kg", 2.4, -2.1);
        double expectedNewAmount = 0;

        Mockito.mock(DriverManagerWrapper.class);
        doReturn(mockConnection).when(driverManagerWrapper).getConnection(any(), any(), any());
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResult);
        when(mockResult.next())
            .thenReturn(true)
            .thenReturn(false);
        when(mockResult.getInt("id"))
            .thenReturn(item.getId());
        when(mockResult.getString("itemName"))
            .thenReturn(item.getName());
        when(mockResult.getDouble("units"))
            .thenReturn(item.getUnits().doubleValue());
        when(mockResult.getString("unitType"))
            .thenReturn(item.getUnitType());
        when(mockResult.getDouble("restockAt"))
            .thenReturn((item.getRestockAt() != null ? item.getRestockAt().doubleValue():0));
        when(mockResult.getDouble("restockAmount"))
            .thenReturn((item.getRestockAmount() != null ? item.getRestockAmount().doubleValue():0));
        when(mockStatement.executeUpdate(anyString())).thenReturn(1);

        double returnedItem = service.restockItemByName(item.getName(), item.getRestockAmount().doubleValue());

        assertEquals(expectedNewAmount, returnedItem);
        verify(driverManagerWrapper, times(1)).getConnection(any(), any(), any());
        verify(mockConnection, times(1)).createStatement();
        verify(mockStatement, times(1)).executeQuery(anyString());
        verify(mockStatement, times(1)).executeUpdate(anyString());
    }
    
    @Test
    public void restockItemByName_itemNotFound() throws Exception {
        String itemName = "Pizza Sauce";
        double restockAmount = 2.1;
        double expectedNewAmount = -1;

        Mockito.mock(DriverManagerWrapper.class);
        doReturn(mockConnection).when(driverManagerWrapper).getConnection(any(), any(), any());
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResult);
        when(mockResult.next())
            .thenReturn(false);
        when(mockStatement.executeUpdate(anyString())).thenReturn(1);

        double returnedItem = service.restockItemByName(itemName, restockAmount);

        assertEquals(expectedNewAmount, returnedItem);
        verify(driverManagerWrapper, times(1)).getConnection(any(), any(), any());
        verify(mockConnection, times(1)).createStatement();
        verify(mockStatement, times(1)).executeQuery(anyString());
        verify(mockStatement, never()).executeUpdate(anyString());
    }

    @Test
    public void restockItemByID_itemFound_AddingAmount() throws Exception {
        InventoryItem item = new InventoryItem(5, "Pizza Sauce", 10.1, "kg", 2.4, 16.2);
        double expectedNewAmount = item.getUnits().doubleValue() + item.getRestockAmount().doubleValue();

        Mockito.mock(DriverManagerWrapper.class);
        doReturn(mockConnection).when(driverManagerWrapper).getConnection(any(), any(), any());
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResult);
        when(mockResult.next())
            .thenReturn(true)
            .thenReturn(false);
        when(mockResult.getInt("id"))
            .thenReturn(item.getId());
        when(mockResult.getString("itemName"))
            .thenReturn(item.getName());
        when(mockResult.getDouble("units"))
            .thenReturn(item.getUnits().doubleValue());
        when(mockResult.getString("unitType"))
            .thenReturn(item.getUnitType());
        when(mockResult.getDouble("restockAt"))
            .thenReturn((item.getRestockAt() != null ? item.getRestockAt().doubleValue():0));
        when(mockResult.getDouble("restockAmount"))
            .thenReturn((item.getRestockAmount() != null ? item.getRestockAmount().doubleValue():0));
        when(mockStatement.executeUpdate(anyString())).thenReturn(1);

        double returnedItem = service.restockItemByID(item.getId(), item.getRestockAmount().doubleValue());

        assertEquals(expectedNewAmount, returnedItem);
        verify(driverManagerWrapper, times(1)).getConnection(any(), any(), any());
        verify(mockConnection, times(1)).createStatement();
        verify(mockStatement, times(1)).executeQuery(anyString());
        verify(mockStatement, times(1)).executeUpdate(anyString());
    }
    
    @Test
    public void restockItemByID_itemFound_RemovingAmount_MoreThan0() throws Exception {
        InventoryItem item = new InventoryItem(5, "Pizza Sauce", 10.1, "kg", 2.4, -2.1);
        double expectedNewAmount = item.getUnits().doubleValue() + item.getRestockAmount().doubleValue();

        Mockito.mock(DriverManagerWrapper.class);
        doReturn(mockConnection).when(driverManagerWrapper).getConnection(any(), any(), any());
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResult);
        when(mockResult.next())
            .thenReturn(true)
            .thenReturn(false);
        when(mockResult.getInt("id"))
            .thenReturn(item.getId());
        when(mockResult.getString("itemName"))
            .thenReturn(item.getName());
        when(mockResult.getDouble("units"))
            .thenReturn(item.getUnits().doubleValue());
        when(mockResult.getString("unitType"))
            .thenReturn(item.getUnitType());
        when(mockResult.getDouble("restockAt"))
            .thenReturn((item.getRestockAt() != null ? item.getRestockAt().doubleValue():0));
        when(mockResult.getDouble("restockAmount"))
            .thenReturn((item.getRestockAmount() != null ? item.getRestockAmount().doubleValue():0));
        when(mockStatement.executeUpdate(anyString())).thenReturn(1);

        double returnedItem = service.restockItemByID(item.getId(), item.getRestockAmount().doubleValue());

        assertEquals(expectedNewAmount, returnedItem);
        verify(driverManagerWrapper, times(1)).getConnection(any(), any(), any());
        verify(mockConnection, times(1)).createStatement();
        verify(mockStatement, times(1)).executeQuery(anyString());
        verify(mockStatement, times(1)).executeUpdate(anyString());
    }
        
    @Test
    public void restockItemByID_itemFound_RemovingAmount_LessThan0() throws Exception {
        InventoryItem item = new InventoryItem(5, "Pizza Sauce", 0.8, "kg", 2.4, -2.1);
        double expectedNewAmount = 0;

        Mockito.mock(DriverManagerWrapper.class);
        doReturn(mockConnection).when(driverManagerWrapper).getConnection(any(), any(), any());
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResult);
        when(mockResult.next())
            .thenReturn(true)
            .thenReturn(false);
        when(mockResult.getInt("id"))
            .thenReturn(item.getId());
        when(mockResult.getString("itemName"))
            .thenReturn(item.getName());
        when(mockResult.getDouble("units"))
            .thenReturn(item.getUnits().doubleValue());
        when(mockResult.getString("unitType"))
            .thenReturn(item.getUnitType());
        when(mockResult.getDouble("restockAt"))
            .thenReturn((item.getRestockAt() != null ? item.getRestockAt().doubleValue():0));
        when(mockResult.getDouble("restockAmount"))
            .thenReturn((item.getRestockAmount() != null ? item.getRestockAmount().doubleValue():0));
        when(mockStatement.executeUpdate(anyString())).thenReturn(1);

        double returnedItem = service.restockItemByID(item.getId(), item.getRestockAmount().doubleValue());

        assertEquals(expectedNewAmount, returnedItem);
        verify(driverManagerWrapper, times(1)).getConnection(any(), any(), any());
        verify(mockConnection, times(1)).createStatement();
        verify(mockStatement, times(1)).executeQuery(anyString());
        verify(mockStatement, times(1)).executeUpdate(anyString());
    }
    
    @Test
    public void restockItemByID_itemNotFound() throws Exception {
        int itemId = 7;
        double restockAmount = 2.1;
        double expectedNewAmount = -1;

        Mockito.mock(DriverManagerWrapper.class);
        doReturn(mockConnection).when(driverManagerWrapper).getConnection(any(), any(), any());
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResult);
        when(mockResult.next())
            .thenReturn(false);
        when(mockStatement.executeUpdate(anyString())).thenReturn(1);

        double returnedItem = service.restockItemByID(itemId, restockAmount);

        assertEquals(expectedNewAmount, returnedItem);
        verify(driverManagerWrapper, times(1)).getConnection(any(), any(), any());
        verify(mockConnection, times(1)).createStatement();
        verify(mockStatement, times(1)).executeQuery(anyString());
        verify(mockStatement, never()).executeUpdate(anyString());
    }
}