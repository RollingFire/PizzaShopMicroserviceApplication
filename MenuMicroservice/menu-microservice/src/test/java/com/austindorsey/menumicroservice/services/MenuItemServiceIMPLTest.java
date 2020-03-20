package com.austindorsey.menumicroservice.services;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.austindorsey.menumicroservice.models.MenuItem;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
public class MenuItemServiceIMPLTest {
    @InjectMocks private MenuItemService service = Mockito.spy(MenuItemServiceIMPL.class);
    @InjectMocks private DriverManagerWrapper driverManagerWrapper = Mockito.spy(DriverManagerWrapper.class);
    @Mock private Connection mockConnection;
    @Mock private Statement mockStatement;
    @Mock private ResultSet mockResult;
    @Captor ArgumentCaptor<String> captor;

    @Test
    public void getMenuItems_4Found() throws Exception {
        MenuItem menuItem1 = new MenuItem(1, "Apps", "Fried Pickles", "", 4.25, Date.valueOf("2020-03-16"));
        MenuItem menuItem2 = new MenuItem(2, "Pizza", "Pepperoni Pizza", "", 12.0, Date.valueOf("2018-9-27"));
        MenuItem menuItem3 = new MenuItem(3, "Pasta", "Sausage Alla Vodka", "Sage Sausage, shell pasta with carimelized shallots in house made vodka sauce. Served with garlic toast.", 18.0, Date.valueOf("2018-05-6"));
        MenuItem menuItem4 = new MenuItem(5, "", "Brown Sage Butter",  "",  0.0, Date.valueOf("2020-03-16"));
        MenuItem[] menuItems = {menuItem1, menuItem2, menuItem3, menuItem4};

        Mockito.mock(DriverManagerWrapper.class);
        doReturn(mockConnection).when(driverManagerWrapper).getConnection(any(), any(), any());
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResult);
        when(mockResult.next())
            .thenReturn(true)
            .thenReturn(true)
            .thenReturn(true)
            .thenReturn(true)
            .thenReturn(false);
        when(mockResult.getInt("id"))
            .thenReturn(menuItem1.getId())
            .thenReturn(menuItem2.getId())
            .thenReturn(menuItem3.getId())
            .thenReturn(menuItem4.getId());
        when(mockResult.getString("catagory"))
            .thenReturn(menuItem1.getCatagory())
            .thenReturn(menuItem2.getCatagory())
            .thenReturn(menuItem3.getCatagory())
            .thenReturn(menuItem4.getCatagory());
        when(mockResult.getString("name"))
            .thenReturn(menuItem1.getName())
            .thenReturn(menuItem2.getName())
            .thenReturn(menuItem3.getName())
            .thenReturn(menuItem4.getName());
        when(mockResult.getString("discription"))
            .thenReturn(menuItem1.getDiscription())
            .thenReturn(menuItem2.getDiscription())
            .thenReturn(menuItem3.getDiscription())
            .thenReturn(menuItem4.getDiscription());
        when(mockResult.getDouble("cost"))
            .thenReturn(menuItem1.getCost().doubleValue())
            .thenReturn(menuItem2.getCost().doubleValue())
            .thenReturn(menuItem3.getCost().doubleValue())
            .thenReturn(menuItem4.getCost().doubleValue());
        when(mockResult.getDate("revisionDate"))
            .thenReturn(menuItem1.getRevisionDate())
            .thenReturn(menuItem2.getRevisionDate())
            .thenReturn(menuItem3.getRevisionDate())
            .thenReturn(menuItem4.getRevisionDate());

        MenuItem[] returnedMenuItems = service.getMenuItems();

        assertArrayEquals(menuItems, returnedMenuItems);
        verify(driverManagerWrapper, times(1)).getConnection(any(), any(), any());
        verify(mockConnection, times(1)).createStatement();
        verify(mockStatement, times(1)).executeQuery(anyString());
    }
    @Test
    public void getMenuItems_1Found() throws Exception {
        MenuItem menuItem = new MenuItem(1, "Pizza", "Pepperoni Pizza", "", 12.0, Date.valueOf("2018-9-27"));
        MenuItem[] menuItems = {menuItem};

        Mockito.mock(DriverManagerWrapper.class);
        doReturn(mockConnection).when(driverManagerWrapper).getConnection(any(), any(), any());
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResult);
        when(mockResult.next())
            .thenReturn(true)
            .thenReturn(false);
        when(mockResult.getInt("id"))
            .thenReturn(menuItem.getId());
        when(mockResult.getString("catagory"))
            .thenReturn(menuItem.getCatagory());
        when(mockResult.getString("name"))
            .thenReturn(menuItem.getName());
        when(mockResult.getString("discription"))
            .thenReturn(menuItem.getDiscription());
        when(mockResult.getDouble("cost"))
            .thenReturn(menuItem.getCost().doubleValue());
        when(mockResult.getDate("revisionDate"))
            .thenReturn(menuItem.getRevisionDate());

        MenuItem[] returnedMenuItems = service.getMenuItems();

        assertArrayEquals(menuItems, returnedMenuItems);
        verify(driverManagerWrapper, times(1)).getConnection(any(), any(), any());
        verify(mockConnection, times(1)).createStatement();
        verify(mockStatement, times(1)).executeQuery(anyString());
    }
    @Test
    public void getMenuItems_0Found() throws Exception {
        MenuItem[] menuItems = {};

        Mockito.mock(DriverManagerWrapper.class);
        doReturn(mockConnection).when(driverManagerWrapper).getConnection(any(), any(), any());
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResult);
        when(mockResult.next())
            .thenReturn(false);

        MenuItem[] returnedMenuItems = service.getMenuItems();

        assertArrayEquals(menuItems, returnedMenuItems);
        verify(driverManagerWrapper, times(1)).getConnection(any(), any(), any());
        verify(mockConnection, times(1)).createStatement();
        verify(mockStatement, times(1)).executeQuery(anyString());
    }

    @Test
    public void getMenuItemByID_found() throws Exception {
        MenuItem menuItem = new MenuItem(2, "Pizza", "Pepperoni Pizza", "", 12.0, Date.valueOf("2018-9-27"));

        Mockito.mock(DriverManagerWrapper.class);
        doReturn(mockConnection).when(driverManagerWrapper).getConnection(any(), any(), any());
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResult);
        when(mockResult.next())
            .thenReturn(true);
        when(mockResult.getInt("id"))
            .thenReturn(menuItem.getId());
        when(mockResult.getString("catagory"))
            .thenReturn(menuItem.getCatagory());
        when(mockResult.getString("name"))
            .thenReturn(menuItem.getName());
        when(mockResult.getString("discription"))
            .thenReturn(menuItem.getDiscription());
        when(mockResult.getDouble("cost"))
            .thenReturn(menuItem.getCost().doubleValue());
        when(mockResult.getDate("revisionDate"))
            .thenReturn(menuItem.getRevisionDate());

        MenuItem returnedMenuItem = service.getMenuItemByID(menuItem.getId());

        assertEquals(menuItem, returnedMenuItem);
        verify(driverManagerWrapper, times(1)).getConnection(any(), any(), any());
        verify(mockConnection, times(1)).createStatement();
        verify(mockStatement, times(1)).executeQuery(anyString());
    }
    @Test
    public void getMenuItemByID_notFound() throws Exception {
        int notFoundID = 42;

        Mockito.mock(DriverManagerWrapper.class);
        doReturn(mockConnection).when(driverManagerWrapper).getConnection(any(), any(), any());
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResult);
        when(mockResult.next())
            .thenReturn(false);

        MenuItem returnedMenuItem = service.getMenuItemByID(notFoundID);

        assertEquals(null, returnedMenuItem);
        verify(driverManagerWrapper, times(1)).getConnection(any(), any(), any());
        verify(mockConnection, times(1)).createStatement();
        verify(mockStatement, times(1)).executeQuery(anyString());
    }

    @Test
    public void getMenuItemHistoryById_foundHistory() throws Exception {
        MenuItem menuItemCurrent = new MenuItem(3, "Pasta", "Sausage Alla Vodka", "Sage Sausage, shell pasta with carimelized shallots in house made vodka sauce. Served with garlic toast.", 18.0, Date.valueOf("2019-11-10"));
        MenuItem menuItemLast = new MenuItem(3, "Entree", "Sausage Alla Vodka", "Sage Sausage, shell pasta with carimelized shallots in house made vodka sauce. Served with garlic toast.", 18.0, Date.valueOf("2019-05-6"));
        MenuItem menuItem2Last = new MenuItem(3, "Entree", "Sausage Alla Vodka", "Sage Sausage, shell pasta, and shallots in house made vodka sauce.", 16.0, Date.valueOf("2018-05-6"));
        MenuItem[] menuItems = {menuItemCurrent, menuItemLast, menuItem2Last};

        doReturn(menuItemCurrent).when(service).getMenuItemByID(menuItemCurrent.getId());
        Mockito.mock(DriverManagerWrapper.class);
        doReturn(mockConnection).when(driverManagerWrapper).getConnection(any(), any(), any());
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResult);
        when(mockResult.next())
            .thenReturn(true)
            .thenReturn(true)
            .thenReturn(false);
        when(mockResult.getInt("id"))
            .thenReturn(menuItemLast.getId())
            .thenReturn(menuItem2Last.getId());
        when(mockResult.getString("catagory"))
            .thenReturn(menuItemLast.getCatagory())
            .thenReturn(menuItem2Last.getCatagory());
        when(mockResult.getString("name"))
            .thenReturn(menuItemLast.getName())
            .thenReturn(menuItem2Last.getName());
        when(mockResult.getString("discription"))
            .thenReturn(menuItemLast.getDiscription())
            .thenReturn(menuItem2Last.getDiscription());
        when(mockResult.getDouble("cost"))
            .thenReturn(menuItemLast.getCost().doubleValue())
            .thenReturn(menuItem2Last.getCost().doubleValue());
        when(mockResult.getDate("revisionDate"))
            .thenReturn(menuItemLast.getRevisionDate())
            .thenReturn(menuItem2Last.getRevisionDate());

        MenuItem[] returnedHistory = service.getMenuItemHistoryById(menuItemCurrent.getId());

        assertArrayEquals(menuItems, returnedHistory);
        verify(driverManagerWrapper, times(1)).getConnection(any(), any(), any());
        verify(mockConnection, times(1)).createStatement();
        verify(mockStatement, times(1)).executeQuery(anyString());
    }
    @Test
    public void getMenuItemHistoryById_foundNoHistory() throws Exception {
        MenuItem menuItemCurrent = new MenuItem(2, "Pizza", "Pepperoni Pizza", "", 12, Date.valueOf("2018-9-27"));
        MenuItem[] menuItems = {menuItemCurrent};

        doReturn(menuItemCurrent).when(service).getMenuItemByID(menuItemCurrent.getId());
        Mockito.mock(DriverManagerWrapper.class);
        doReturn(mockConnection).when(driverManagerWrapper).getConnection(any(), any(), any());
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResult);
        when(mockResult.next())
            .thenReturn(false);

        MenuItem[] returnedHistory = service.getMenuItemHistoryById(menuItemCurrent.getId());

        assertArrayEquals(menuItems, returnedHistory);
        verify(driverManagerWrapper, times(1)).getConnection(any(), any(), any());
        verify(mockConnection, times(1)).createStatement();
        verify(mockStatement, times(1)).executeQuery(anyString());
    }
    @Test
    public void getMenuItemHistoryById_notFound() throws Exception {
        int notFoundID = 42;

        doReturn(null).when(service).getMenuItemByID(notFoundID);

        MenuItem[] returnedHistory = service.getMenuItemHistoryById(notFoundID);

        assertArrayEquals(null, returnedHistory);
        verify(driverManagerWrapper, times(0)).getConnection(any(), any(), any());
        verify(mockConnection, times(0)).createStatement();
        verify(mockStatement, times(0)).executeQuery(anyString());
    }

    @Test
    public void searchMenuItemsByName_wordsThatStartWith() throws Exception {
        MenuItem menuItem1 = new MenuItem(1, "Dessert", "Pie", "", 3.5, Date.valueOf("2020-3-14"));
        MenuItem menuItem2 = new MenuItem(2, "Toppings", "pepperoni", "Pork, Fennal, Pepper, Garlic", null, Date.valueOf("2020-03-16"));
        MenuItem menuItem3 = new MenuItem(3, "", "", "", null, Date.valueOf("2020-03-16"));
        MenuItem menuItem4 = new MenuItem(4, "Apps", "Fried Pickles", "", 4.25, Date.valueOf("2020-03-16"));
        MenuItem menuItem5 = new MenuItem(5, "Pasta", "Sausage Alla Vodka", "Sage Sausage, shell pasta with carimelized shallots in house made vodka sauce. Served with garlic toast.", 18, Date.valueOf("2018-05-6"));

        String search = "P";
        MenuItem[] expected = {menuItem1, menuItem2, menuItem4};
        MenuItem[] allItems = {menuItem1, menuItem2, menuItem3, menuItem4, menuItem5};

        doReturn(allItems).when(service).getMenuItems();

        MenuItem[] returnedSearch = service.searchMenuItemsByName(search);

        assertArrayEquals(expected, returnedSearch);
        verify(service, times(1)).searchMenuItemsByName(search);
    }
    @Test
    public void searchMenuItemsByName_wordsContainStartWith() throws Exception {
        MenuItem menuItem5 = new MenuItem(1, "", "Aged Gouda", "Smoky cheese!", 24, Date.valueOf("1100-1-1"));
        MenuItem menuItem1 = new MenuItem(2, "Breakfast", "Eggs", "", 0.5, null);
        MenuItem menuItem2 = new MenuItem(3, "Pasta", "Sausage Alla Vodka", "Sage Sausage, shell pasta with carimelized shallots in house made vodka sauce. Served with garlic toast.", 18, Date.valueOf("2018-05-6"));
        MenuItem menuItem3 = new MenuItem(4, "", "", "", null, Date.valueOf("2020-03-16"));
        MenuItem menuItem4 = new MenuItem(5, "", "Brown Sage Butter", "", null, Date.valueOf("2020-03-16"));

        String search = "age";
        MenuItem[] expected = {menuItem5, menuItem2, menuItem4};
        MenuItem[] allItems = {menuItem5, menuItem2, menuItem3, menuItem4, menuItem1};

        doReturn(allItems).when(service).getMenuItems();

        MenuItem[] returnedSearch = service.searchMenuItemsByName(search);

        assertArrayEquals(expected, returnedSearch);
        verify(service, times(1)).searchMenuItemsByName(search);
    }

    @Test
    public void searchMenuItemsByName_sortByBestMatched() throws Exception {
        MenuItem menuItem1 = new MenuItem(1, "Breakfast", "Eggs", "", 0.5, null);
        MenuItem menuItem2 = new MenuItem(2, "Pasta", "Sausage Alla Vodka", "Sage Sausage, shell pasta with carimelized shallots in house made vodka sauce. Served with garlic toast.", 18, Date.valueOf("2018-05-6"));
        MenuItem menuItem3 = new MenuItem(3, "", "", "", null, Date.valueOf("2020-03-16"));
        MenuItem menuItem4 = new MenuItem(4, "", "Brown Sage Butter", "", null, Date.valueOf("2020-03-16"));
        MenuItem menuItem5 = new MenuItem(5, "", "Aged Gouda", "Smoky cheese!", 24, Date.valueOf("1100-1-1"));

        String search = "age";
        MenuItem[] expected = {menuItem5, menuItem2, menuItem4};
        MenuItem[] allItems = {menuItem5, menuItem2, menuItem3, menuItem4, menuItem1};

        doReturn(allItems).when(service).getMenuItems();

        MenuItem[] returnedSearch = service.searchMenuItemsByName(search);

        assertArrayEquals(expected, returnedSearch);
        verify(service, times(1)).searchMenuItemsByName(search);
    }
    @Test
    public void searchMenuItemsByName_oneFoundWeirdCase() throws Exception {
        MenuItem menuItem1 = new MenuItem(1, "Apps", "Fried Pickles", "", 4.25, Date.valueOf("2020-03-16"));
        MenuItem menuItem2 = new MenuItem(2, "Pizza", "Pepperoni Pizza", "", 12, Date.valueOf("2018-9-27"));
        MenuItem menuItem3 = new MenuItem(3, "Pasta", "Sausage Alla Vodka", "Sage Sausage, shell pasta with carimelized shallots in house made vodka sauce. Served with garlic toast.", 18, Date.valueOf("2018-05-6"));

        String search = "PIzzA";
        MenuItem[] expected = {menuItem2};
        MenuItem[] allItems = {menuItem2, menuItem3, menuItem1};

        doReturn(allItems).when(service).getMenuItems();

        MenuItem[] returnedSearch = service.searchMenuItemsByName(search);

        assertArrayEquals(expected, returnedSearch);
        verify(service, times(1)).searchMenuItemsByName(search);
    }
    @Test
    public void searchMenuItemsByName_noneFound() throws Exception {
        MenuItem menuItem1 = new MenuItem(1, "Apps", "Fried Pickles", "", 4.25, Date.valueOf("2020-03-16"));
        MenuItem menuItem2 = new MenuItem(2, "Pizza", "Pepperoni Pizza", "", 12, Date.valueOf("2018-9-27"));
        MenuItem menuItem3 = new MenuItem(5, "Pasta", "Sausage Alla Vodka", "Sage Sausage, shell pasta with carimelized shallots in house made vodka sauce. Served with garlic toast.", 18, Date.valueOf("2018-05-6"));

        String search = "Chicken";
        MenuItem[] expected = {};
        MenuItem[] allItems = {menuItem2, menuItem3, menuItem1};

        doReturn(allItems).when(service).getMenuItems();

        MenuItem[] returnedSearch = service.searchMenuItemsByName(search);

        assertArrayEquals(expected, returnedSearch);
        verify(service, times(1)).searchMenuItemsByName(search);
    }

    @Test
    public void updateMenuItem_catagoryUpdate() throws Exception {
        MenuItem currentItem = new MenuItem(5, "Entrees", "Sausage Alla Vodka", "Sage Sausage, shell pasta with carimelized shallots in house made vodka sauce. Served with garlic toast.", 18, Date.valueOf("2018-05-6"));
        MenuItem updatedItem = new MenuItem(5, "Pasta", "Sausage Alla Vodka", "Sage Sausage, shell pasta with carimelized shallots in house made vodka sauce. Served with garlic toast.", 18, Date.valueOf("2020-03-19"));
        Map<String,Object> request = new HashMap<String,Object>();
        request.put("catagory", updatedItem.getCatagory());
        String expectedSQL = "UPDATE null SET catagory='Pasta';";

        doReturn(updatedItem).when(service).getMenuItemByID(currentItem.getId());
        Mockito.mock(DriverManagerWrapper.class);
        doReturn(mockConnection).when(driverManagerWrapper).getConnection(any(), any(), any());
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResult);

        MenuItem returnedItem = service.updateMenuItem(currentItem.getId(), request);

        Mockito.verify(mockStatement).executeUpdate(captor.capture());
        String capturedSQL = captor.getValue();

        assertEquals(updatedItem, returnedItem);
        assertEquals(expectedSQL, capturedSQL);
        verify(service, times(1)).updateMenuItem(currentItem.getId(), request);
    }
    @Test
    public void updateMenuItem_nameUpdate() throws Exception {
        MenuItem currentItem = new MenuItem(5, "Pasta", "Sausage Vodka Pasta", "Sage Sausage, shell pasta with carimelized shallots in house made vodka sauce. Served with garlic toast.", 18, Date.valueOf("2018-05-6"));
        MenuItem updatedItem = new MenuItem(5, "Pasta", "Sausage Alla Vodka", "Sage Sausage, shell pasta with carimelized shallots in house made vodka sauce. Served with garlic toast.", 18, Date.valueOf("2020-03-19"));
        Map<String,Object> request = new HashMap<String,Object>();
        request.put("name", updatedItem.getName());
        String expectedSQL = "UPDATE null SET name='Sausage Alla Vodka';";

        doReturn(updatedItem).when(service).getMenuItemByID(currentItem.getId());
        Mockito.mock(DriverManagerWrapper.class);
        doReturn(mockConnection).when(driverManagerWrapper).getConnection(any(), any(), any());
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResult);

        MenuItem returnedItem = service.updateMenuItem(currentItem.getId(), request);

        Mockito.verify(mockStatement).executeUpdate(captor.capture());
        String capturedSQL = captor.getValue();

        assertEquals(updatedItem, returnedItem);
        assertEquals(expectedSQL, capturedSQL);
        verify(service, times(1)).updateMenuItem(currentItem.getId(), request);
    }
    @Test
    public void updateMenuItem_discriptionUpdate() throws Exception {
        MenuItem currentItem = new MenuItem(5, "Pasta", "Sausage Alla Vodka", "Sage Sausage and shell pasta in vodka sauce. Served with garlic toast.", 18, Date.valueOf("2018-05-6"));
        MenuItem updatedItem = new MenuItem(5, "Pasta", "Sausage Alla Vodka", "Sage Sausage, shell pasta with carimelized shallots in house made vodka sauce. Served with garlic toast.", 18, Date.valueOf("2020-03-19"));
        Map<String,Object> request = new HashMap<String,Object>();
        request.put("discription", updatedItem.getDiscription());
        String expectedSQL = "UPDATE null SET discription='Sage Sausage, shell pasta with carimelized shallots in house made vodka sauce. Served with garlic toast.';";

        doReturn(updatedItem).when(service).getMenuItemByID(currentItem.getId());
        Mockito.mock(DriverManagerWrapper.class);
        doReturn(mockConnection).when(driverManagerWrapper).getConnection(any(), any(), any());
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResult);

        MenuItem returnedItem = service.updateMenuItem(currentItem.getId(), request);

        Mockito.verify(mockStatement).executeUpdate(captor.capture());
        String capturedSQL = captor.getValue();

        assertEquals(updatedItem, returnedItem);
        assertEquals(expectedSQL, capturedSQL);
        verify(service, times(1)).updateMenuItem(currentItem.getId(), request);
    }
    @Test
    public void updateMenuItem_costUpdate() throws Exception {
        MenuItem currentItem = new MenuItem(5, "Pasta", "Sausage Alla Vodka", "Sage Sausage, shell pasta with carimelized shallots in house made vodka sauce. Served with garlic toast.", 14, Date.valueOf("2018-05-6"));
        MenuItem updatedItem = new MenuItem(5, "Pasta", "Sausage Alla Vodka", "Sage Sausage, shell pasta with carimelized shallots in house made vodka sauce. Served with garlic toast.", 18, Date.valueOf("2020-03-19"));
        Map<String,Object> request = new HashMap<String,Object>();
        request.put("cost", updatedItem.getCost());
        String expectedSQL = "UPDATE null SET cost=18;";

        doReturn(updatedItem).when(service).getMenuItemByID(currentItem.getId());
        Mockito.mock(DriverManagerWrapper.class);
        doReturn(mockConnection).when(driverManagerWrapper).getConnection(any(), any(), any());
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResult);

        MenuItem returnedItem = service.updateMenuItem(currentItem.getId(), request);

        Mockito.verify(mockStatement).executeUpdate(captor.capture());
        String capturedSQL = captor.getValue();

        assertEquals(updatedItem, returnedItem);
        assertEquals(expectedSQL, capturedSQL);
        verify(service, times(1)).updateMenuItem(currentItem.getId(), request);
    }
    @Test
    public void updateMenuItem_discriptionCostUpdate() throws Exception {
        MenuItem currentItem = new MenuItem(5, "Pasta", "Sausage Alla Vodka", "Sage Sausage and shell pasta in vodka sauce. Served with garlic toast.", 14, Date.valueOf("2018-05-6"));
        MenuItem updatedItem = new MenuItem(5, "Pasta", "Sausage Alla Vodka", "Sage Sausage, shell pasta with carimelized shallots in house made vodka sauce. Served with garlic toast.", 18, Date.valueOf("2020-03-19"));
        Map<String,Object> request = new HashMap<String,Object>();
        request.put("discription", updatedItem.getDiscription());
        request.put("cost", updatedItem.getCost());
        String expectedSQL = "UPDATE null SET discription='Sage Sausage, shell pasta with carimelized shallots in house made vodka sauce. Served with garlic toast.', cost=18;";

        doReturn(updatedItem).when(service).getMenuItemByID(currentItem.getId());
        Mockito.mock(DriverManagerWrapper.class);
        doReturn(mockConnection).when(driverManagerWrapper).getConnection(any(), any(), any());
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResult);

        MenuItem returnedItem = service.updateMenuItem(currentItem.getId(), request);

        Mockito.verify(mockStatement).executeUpdate(captor.capture());
        String capturedSQL = captor.getValue();

        assertEquals(updatedItem, returnedItem);
        assertEquals(expectedSQL, capturedSQL);
        verify(service, times(1)).updateMenuItem(currentItem.getId(), request);
    }
    @Test
    public void updateMenuItem_allUpdate() throws Exception {
        MenuItem currentItem = new MenuItem(5, "Entrees", "Sausage Vodka Pasta", "Sage Sausage and shell pasta in vodka sauce. Served with garlic toast.", 14.5, Date.valueOf("2018-05-6"));
        MenuItem updatedItem = new MenuItem(5, "Pasta", "Sausage Alla Vodka", "Sage Sausage, shell pasta with carimelized shallots in house made vodka sauce. Served with garlic toast.", 18, Date.valueOf("2020-03-19"));
        Map<String,Object> request = new HashMap<String,Object>();
        request.put("discription", updatedItem.getDiscription());
        request.put("cost", updatedItem.getCost());
        request.put("name", updatedItem.getName());
        request.put("catagory", updatedItem.getCatagory());
        String expectedSQL = "UPDATE null SET " + 
                                "catagory='Pasta', " +
                                "discription='Sage Sausage, shell pasta with carimelized shallots in house made vodka sauce. Served with garlic toast.', " +
                                "cost=18, " +
                                "name='Sausage Alla Vodka'" +
                                ";";

        doReturn(updatedItem).when(service).getMenuItemByID(currentItem.getId());
        Mockito.mock(DriverManagerWrapper.class);
        doReturn(mockConnection).when(driverManagerWrapper).getConnection(any(), any(), any());
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResult);

        MenuItem returnedItem = service.updateMenuItem(currentItem.getId(), request);

        Mockito.verify(mockStatement).executeUpdate(captor.capture());
        String capturedSQL = captor.getValue();

        assertEquals(updatedItem, returnedItem);
        assertEquals(expectedSQL, capturedSQL);
        verify(service, times(1)).updateMenuItem(currentItem.getId(), request);
    }

    @Test
    public void createNewMenuItem() throws Exception {
        MenuItem menu = new MenuItem(5, "Pasta", "Sausage Alla Vodka", "Sage Sausage, shell pasta with carimelized shallots in house made vodka sauce. Served with garlic toast.", 18.0, Date.valueOf("2020-03-19"));
        String expectedSQL = "INSERT INTO null (catagory, name, discription, cost) VALUES (" + 
                                "'Pasta', " +
                                "'Sausage Alla Vodka', " +
                                "'Sage Sausage, shell pasta with carimelized shallots in house made vodka sauce. Served with garlic toast.', " +
                                "18.0" +
                                ");";

        doReturn(mockConnection).when(driverManagerWrapper).getConnection(any(), any(), any());
        Mockito.mock(DriverManagerWrapper.class);
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResult);
        when(mockResult.next())
            .thenReturn(true)
            .thenReturn(false);
        when(mockResult.getInt("id"))
            .thenReturn(menu.getId());
        when(mockResult.getString("catagory"))
            .thenReturn(menu.getCatagory());
        when(mockResult.getString("name"))
            .thenReturn(menu.getName());
        when(mockResult.getString("discription"))
            .thenReturn(menu.getDiscription());
        when(mockResult.getDouble("cost"))
            .thenReturn(menu.getCost().doubleValue());
        when(mockResult.getDate("revisionDate"))
            .thenReturn(menu.getRevisionDate());

        MenuItem returnedItem = service.createNewMenuItem(menu);

        verify(mockStatement).executeUpdate(captor.capture());
        String capturedSQL = captor.getValue();

        assertEquals(menu, returnedItem);
        assertEquals(expectedSQL, capturedSQL);
        verify(driverManagerWrapper, times(1)).getConnection(any(), any(), any());
        verify(service, times(1)).createNewMenuItem(menu);
        verify(mockStatement, times(1)).executeUpdate(anyString());
    }
}