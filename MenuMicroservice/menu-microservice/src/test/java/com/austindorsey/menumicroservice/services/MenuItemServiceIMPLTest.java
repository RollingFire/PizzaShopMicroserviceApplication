package com.austindorsey.menumicroservice.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import com.austindorsey.menumicroservice.models.MenuItem;

import org.junit.jupiter.api.Test;
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
    private MenuItemService service = new MenuItemServiceIMPL();
    @InjectMocks private DriverManagerWrapper driverManagerWrapper = Mockito.spy(DriverManagerWrapper.class);
    @Mock private Connection mockConnection;
    @Mock private Statement mockStatement;
    @Mock private ResultSet mockResult;

    @Test
    public void getMenuItems_3Found() {
        fail("Not implimented");
        MenuItem menuItem1 = new MenuItem(1, "Apps", "Fried Pickles", "", 4.25, Date.valueOf("2020-03-16"));
        MenuItem menuItem2 = new MenuItem(2, "Pizza", "Pepperoni Pizza", "", 12, Date.valueOf("2018-9-27"));
        MenuItem menuItem3 = new MenuItem(3, "Pasta", "Sausage Alla Vodka", "Sage Sausage, shell pasta with carimelized shallots in house made vodka sauce. Served with garlic toast.", 18, Date.valueOf("2018-05-6"));
        MenuItem menuItem4 = new MenuItem(5, "", "", "Brown Sage Butter", null, Date.valueOf("2020-03-16"));
        MenuItem[] menuItems = {menuItem1, menuItem2, menuItem3, menuItem4};

        MenuItem[] returnedMenuItems = service.getMenuItems();

        assertEquals(menuItems, returnedMenuItems);
    }
    @Test
    public void getMenuItems_1Found() {
        fail("Not implimented");
        MenuItem menuItem = new MenuItem(1, "Pizza", "Pepperoni Pizza", "", 12, Date.valueOf("2018-9-27"));
        MenuItem[] menuItems = {menuItem};

        MenuItem[] returnedMenuItems = service.getMenuItems();

        assertEquals(menuItems, returnedMenuItems);
    }
    @Test
    public void getMenuItems_0Found() {
        fail("Not implimented");
        MenuItem[] menuItems = {};

        MenuItem[] returnedMenuItems = service.getMenuItems();

        assertEquals(menuItems, returnedMenuItems);
    }

    @Test
    public void getMenuItemByID_found() {
        fail("Not implimented");
        MenuItem menuItem = new MenuItem(2, "Pizza", "Pepperoni Pizza", "", 12, Date.valueOf("2018-9-27"));

        MenuItem returnedMenuItem = service.getMenuItemByID(menuItem.getId());

        assertEquals(menuItem, returnedMenuItem);
    }
    @Test
    public void getMenuItemByID_notFound() {
        fail("Not implimented");
        int notFoundID = 42;

        MenuItem returnedMenuItem = service.getMenuItemByID(notFoundID);

        assertEquals(returnedMenuItem, returnedMenuItem);
    }

    @Test
    public void getMenuItemHistoryById_foundHistory() {
        fail("Not implimented");
        MenuItem menuItemCurrent = new MenuItem(3, "Pasta", "Sausage Alla Vodka", "Sage Sausage, shell pasta with carimelized shallots in house made vodka sauce. Served with garlic toast.", 18, Date.valueOf("2019-11-10"));
        MenuItem menuItemLast = new MenuItem(3, "Entree", "Sausage Alla Vodka", "Sage Sausage, shell pasta with carimelized shallots in house made vodka sauce. Served with garlic toast.", 18, Date.valueOf("2019-05-6"));
        MenuItem menuItem2Last = new MenuItem(3, "Entree", "Sausage Alla Vodka", "Sage Sausage, shell pasta, and shallots in house made vodka sauce.", 16, Date.valueOf("2018-05-6"));
        MenuItem[] menuItems = {menuItemCurrent, menuItemLast, menuItem2Last};

        MenuItem[] returnedHistory = service.getMenuItemHistoryById(menuItemCurrent.getId());

        assertEquals(menuItems, returnedHistory);
    }
    @Test
    public void getMenuItemHistoryById_foundNoHistory() {
        fail("Not implimented");
        MenuItem menuItemCurrent = new MenuItem(2, "Pizza", "Pepperoni Pizza", "", 12, Date.valueOf("2018-9-27"));
        MenuItem[] menuItems = {menuItemCurrent};

        MenuItem[] returnedHistory = service.getMenuItemHistoryById(menuItemCurrent.getId());

        assertEquals(menuItems, returnedHistory);
    }
    @Test
    public void getMenuItemHistoryById_notFound() {
        fail("Not implimented");
        int notFoundID = 42;

        MenuItem[] returnedHistory = service.getMenuItemHistoryById(notFoundID);

        assertEquals(null, returnedHistory);
    }

    @Test
    public void searchMenuItemsByName_wordsThatStartWith() {
        fail("Not implimented");
        MenuItem menuItem1 = new MenuItem(1, "Dessert", "Pie", "", 3.5, Date.valueOf("2020-3-14"));
        MenuItem menuItem2 = new MenuItem(2, "Toppings", "pepperoni", "Pork, Fennal, Pepper, Garlic", null, Date.valueOf("2020-03-16"));
        MenuItem menuItem3 = new MenuItem(3, "", "", "", null, Date.valueOf("2020-03-16"));
        MenuItem menuItem4 = new MenuItem(4, "Apps", "Fried Pickles", "", 4.25, Date.valueOf("2020-03-16"));
        MenuItem menuItem5 = new MenuItem(5, "Pasta", "Sausage Alla Vodka", "Sage Sausage, shell pasta with carimelized shallots in house made vodka sauce. Served with garlic toast.", 18, Date.valueOf("2018-05-6"));

        String search = "P";
        MenuItem[] expected = {menuItem1, menuItem2, menuItem4};

        MenuItem[] returnedSearch = service.searchMenuItemsByName(search);

        assertEquals(expected, returnedSearch);
    }
    @Test
    public void searchMenuItemsByName_wordsContainStartWith() {
        fail("Not implimented");
        MenuItem menuItem5 = new MenuItem(1, "", "Aged Gouda", "Smoky cheese!", 24, Date.valueOf("1100-1-1"));
        MenuItem menuItem1 = new MenuItem(2, "Breakfast", "Eggs", "", 0.5, null);
        MenuItem menuItem2 = new MenuItem(3, "Pasta", "Sausage Alla Vodka", "Sage Sausage, shell pasta with carimelized shallots in house made vodka sauce. Served with garlic toast.", 18, Date.valueOf("2018-05-6"));
        MenuItem menuItem3 = new MenuItem(4, "", "", "", null, Date.valueOf("2020-03-16"));
        MenuItem menuItem4 = new MenuItem(5, "", "", "Brown Sage Butter", null, Date.valueOf("2020-03-16"));

        String search = "age";
        MenuItem[] expected = {menuItem5, menuItem2, menuItem4};

        MenuItem[] returnedSearch = service.searchMenuItemsByName(search);

        assertEquals(expected, returnedSearch);
    }

    @Test
    public void searchMenuItemsByName_sortByBestMatched() {
        fail("Not implimented");
        MenuItem menuItem1 = new MenuItem(1, "Breakfast", "Eggs", "", 0.5, null);
        MenuItem menuItem2 = new MenuItem(2, "Pasta", "Sausage Alla Vodka", "Sage Sausage, shell pasta with carimelized shallots in house made vodka sauce. Served with garlic toast.", 18, Date.valueOf("2018-05-6"));
        MenuItem menuItem3 = new MenuItem(3, "", "", "", null, Date.valueOf("2020-03-16"));
        MenuItem menuItem4 = new MenuItem(4, "", "", "Brown Sage Butter", null, Date.valueOf("2020-03-16"));
        MenuItem menuItem5 = new MenuItem(5, "", "Aged Gouda", "Smoky cheese!", 24, Date.valueOf("1100-1-1"));

        String search = "age";
        MenuItem[] expected = {menuItem5, menuItem2, menuItem4};

        MenuItem[] returnedSearch = service.searchMenuItemsByName(search);

        assertEquals(expected, returnedSearch);
    }
    @Test
    public void searchMenuItemsByName_oneFoundWeirdCase() {
        fail("Not implimented");
        MenuItem menuItem1 = new MenuItem(1, "Apps", "Fried Pickles", "", 4.25, Date.valueOf("2020-03-16"));
        MenuItem menuItem2 = new MenuItem(2, "Pizza", "Pepperoni Pizza", "", 12, Date.valueOf("2018-9-27"));
        MenuItem menuItem3 = new MenuItem(3, "Pasta", "Sausage Alla Vodka", "Sage Sausage, shell pasta with carimelized shallots in house made vodka sauce. Served with garlic toast.", 18, Date.valueOf("2018-05-6"));

        String search = "PIzzA";
        MenuItem[] expected = {menuItem2};

        MenuItem[] returnedSearch = service.searchMenuItemsByName(search);
        
        assertEquals(expected, returnedSearch);
    }
    @Test
    public void searchMenuItemsByName_noneFound() {
        fail("Not implimented");
        MenuItem menuItem1 = new MenuItem(1, "Apps", "Fried Pickles", "", 4.25, Date.valueOf("2020-03-16"));
        MenuItem menuItem2 = new MenuItem(2, "Pizza", "Pepperoni Pizza", "", 12, Date.valueOf("2018-9-27"));
        MenuItem menuItem3 = new MenuItem(5, "Pasta", "Sausage Alla Vodka", "Sage Sausage, shell pasta with carimelized shallots in house made vodka sauce. Served with garlic toast.", 18, Date.valueOf("2018-05-6"));

        String search = "Chicken";
        MenuItem[] expected = {};

        MenuItem[] returnedSearch = service.searchMenuItemsByName(search);
        
        assertEquals(expected, returnedSearch);
    }


    @Test
    public void updateMenuItem_noUpdate() {
        MenuItem currentItem = new MenuItem(5, "Pasta", "Sausage Alla Vodka", "Sage Sausage, shell pasta with carimelized shallots in house made vodka sauce. Served with garlic toast.", 18, Date.valueOf("2018-05-6"));
        MenuItem updatedItem = currentItem;
        Map<String,String> request = new HashMap<String,String>();

        MenuItem returnedItem = service.updateMenuItem(currentItem.getId(), request);

        assertEquals(updatedItem, returnedItem);
    }
    @Test
    public void updateMenuItem_catagoryUpdate() {
        MenuItem currentItem = new MenuItem(5, "Entrees", "Sausage Alla Vodka", "Sage Sausage, shell pasta with carimelized shallots in house made vodka sauce. Served with garlic toast.", 18, Date.valueOf("2018-05-6"));
        MenuItem updatedItem = new MenuItem(5, "Pasta", "Sausage Alla Vodka", "Sage Sausage, shell pasta with carimelized shallots in house made vodka sauce. Served with garlic toast.", 18, Date.valueOf("2020-03-19"));
        Map<String,String> request = new HashMap<String,String>();
        request.put("catagory", updatedItem.getCatagory());

        MenuItem returnedItem = service.updateMenuItem(currentItem.getId(), request);

        assertEquals(updatedItem, returnedItem);
    }
    @Test
    public void updateMenuItem_nameUpdate() {
        MenuItem currentItem = new MenuItem(5, "Pasta", "Sausage Vodka Pasta", "Sage Sausage, shell pasta with carimelized shallots in house made vodka sauce. Served with garlic toast.", 18, Date.valueOf("2018-05-6"));
        MenuItem updatedItem = new MenuItem(5, "Pasta", "Sausage Alla Vodka", "Sage Sausage, shell pasta with carimelized shallots in house made vodka sauce. Served with garlic toast.", 18, Date.valueOf("2020-03-19"));
        Map<String,String> request = new HashMap<String,String>();
        request.put("name", updatedItem.getName());

        MenuItem returnedItem = service.updateMenuItem(currentItem.getId(), request);

        assertEquals(updatedItem, returnedItem);
    }
    @Test
    public void updateMenuItem_discriptionUpdate() {
        MenuItem currentItem = new MenuItem(5, "Pasta", "Sausage Alla Vodka", "Sage Sausage and shell pasta in vodka sauce. Served with garlic toast.", 18, Date.valueOf("2018-05-6"));
        MenuItem updatedItem = new MenuItem(5, "Pasta", "Sausage Alla Vodka", "Sage Sausage, shell pasta with carimelized shallots in house made vodka sauce. Served with garlic toast.", 18, Date.valueOf("2020-03-19"));
        Map<String,String> request = new HashMap<String,String>();
        request.put("discription", updatedItem.getDiscription());

        MenuItem returnedItem = service.updateMenuItem(currentItem.getId(), request);

        assertEquals(updatedItem, returnedItem);
    }
    @Test
    public void updateMenuItem_costUpdate() {
        MenuItem currentItem = new MenuItem(5, "Pasta", "Sausage Alla Vodka", "Sage Sausage, shell pasta with carimelized shallots in house made vodka sauce. Served with garlic toast.", 14, Date.valueOf("2018-05-6"));
        MenuItem updatedItem = new MenuItem(5, "Pasta", "Sausage Alla Vodka", "Sage Sausage, shell pasta with carimelized shallots in house made vodka sauce. Served with garlic toast.", 18, Date.valueOf("2020-03-19"));
        Map<String,String> request = new HashMap<String,String>();
        request.put("cost", updatedItem.getCost().toString());

        MenuItem returnedItem = service.updateMenuItem(currentItem.getId(), request);

        assertEquals(updatedItem, returnedItem);
    }
    @Test
    public void updateMenuItem_discriptionCostUpdate() {
        MenuItem currentItem = new MenuItem(5, "Pasta", "Sausage Alla Vodka", "Sage Sausage and shell pasta in vodka sauce. Served with garlic toast.", 14, Date.valueOf("2018-05-6"));
        MenuItem updatedItem = new MenuItem(5, "Pasta", "Sausage Alla Vodka", "Sage Sausage, shell pasta with carimelized shallots in house made vodka sauce. Served with garlic toast.", 18, Date.valueOf("2020-03-19"));
        Map<String,String> request = new HashMap<String,String>();
        request.put("discription", updatedItem.getDiscription());
        request.put("cost", updatedItem.getCost().toString());

        MenuItem returnedItem = service.updateMenuItem(currentItem.getId(), request);

        assertEquals(updatedItem, returnedItem);
    }
    @Test
    public void updateMenuItem_allUpdate() {
        MenuItem currentItem = new MenuItem(5, "Entrees", "Sausage Vodka Pasta", "Sage Sausage and shell pasta in vodka sauce. Served with garlic toast.", 14.5, Date.valueOf("2018-05-6"));
        MenuItem updatedItem = new MenuItem(5, "Pasta", "Sausage Alla Vodka", "Sage Sausage, shell pasta with carimelized shallots in house made vodka sauce. Served with garlic toast.", 18, Date.valueOf("2020-03-19"));
        Map<String,String> request = new HashMap<String,String>();
        request.put("discription", updatedItem.getDiscription());
        request.put("cost", updatedItem.getCost().toString());
        request.put("name", updatedItem.getName());
        request.put("catagory", updatedItem.getCatagory());

        MenuItem returnedItem = service.updateMenuItem(currentItem.getId(), request);

        assertEquals(updatedItem, returnedItem);
    }
}