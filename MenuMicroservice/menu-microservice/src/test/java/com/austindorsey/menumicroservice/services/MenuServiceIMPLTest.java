package com.austindorsey.menumicroservice.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import com.austindorsey.menumicroservice.models.Menu;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MenuServiceIMPLTest {
    private MenuService service = new MenuServiceIMPL();
    @InjectMocks private DriverManagerWrapper driverManagerWrapper = Mockito.spy(DriverManagerWrapper.class);
    @Mock private Connection mockConnection;
    @Mock private Statement mockStatement;
    @Mock private ResultSet mockResult;

    @Test
    public void getMenus_3Fourd() {
        Menu menu1 = new Menu(1, "General", "{[1,2,4,5,6,7]}", Date.valueOf("2020-3-18"));
        Menu menu2 = new Menu(2, "Weekend", "{[1,2,5,6,7,12]}", Date.valueOf("2020-1-12"));
        Menu menu3 = new Menu(3, "Christmas", "{[11,21,41,42,43]}", Date.valueOf("2019-12-18"));
        Menu[] expected = {menu1, menu2, menu3};

        Menu[] returned = service.getMenus();

        assertEquals(expected, returned);
    }
    @Test
    public void getMenus_1Fourd() {
        Menu menu = new Menu(1, "General", "{[1,2,4,5,6,7]}", Date.valueOf("2020-3-18"));
        Menu[] expected = {menu};

        Menu[] returned = service.getMenus();

        assertEquals(expected, returned);
    }
    @Test
    public void getMenus_0Fourd() {
        Menu[] returned = service.getMenus();

        assertEquals(null, returned);
    }
    @Test
    public void getCurrentMenuById_found() {
        Menu menu = new Menu(1, "General", "{[1,2,4,5,6,7]}", Date.valueOf("2020-3-18"));

        Menu returned = service.getCurrentMenuById(menu.getId());

        assertEquals(menu, returned);
    }
    @Test
    public void getCurrentMenuById_notFound() {
        int idNotFound = 42;

        Menu returned = service.getCurrentMenuById(idNotFound);

        assertEquals(null, returned);
    }
    @Test
    public void getCurrentMenuByName_found() {
        Menu menu = new Menu(1, "General", "{[1,2,4,5,6,7]}", Date.valueOf("2020-3-18"));

        Menu returned = service.getCurrentMenuByName(menu.getName());

        assertEquals(menu, returned);
    }
    @Test
    public void getCurrentMenuByName_notFound() {
        String nameNotFound = "This is not the name you are looking for.";

        Menu returned = service.getCurrentMenuByName(nameNotFound);
        
        assertEquals(null, returned);
    }
    @Test
    public void getMenuHistoryById_historyFound() {
        Menu menuCurrent = new Menu(1, "General", "{[1,2,4,7,8]}", Date.valueOf("2020-3-18"));
        Menu menuLast = new Menu(1, "General", "{[1,2,4,5,7]}", Date.valueOf("2020-2-28"));
        Menu menu2Last = new Menu(1, "General", "{[1,2,4,5]}", Date.valueOf("2020-2-10"));
        Menu[] expected = {menuCurrent, menuLast, menu2Last};

        Menu[] returned = service.getMenuHistoryById(menuCurrent.getId());

        assertEquals(expected, returned);
    }
    @Test
    public void getMenuHistoryById_onlyCurrent() {
        Menu menuCurrent = new Menu(1, "General", "{[1,2,4,7,8]}", Date.valueOf("2020-3-18"));
        Menu[] expected = {menuCurrent};

        Menu[] returned = service.getMenuHistoryById(menuCurrent.getId());

        assertEquals(expected, returned);
    }
    @Test
    public void getMenuHistoryById_notFound() {
        int idNotFound = 42;

        Menu[] returned = service.getMenuHistoryById(idNotFound);

        assertEquals(null, returned);
    }
    @Test
    public void getMenuHistoryByName_historyFound() {
        Menu menuCurrent = new Menu(1, "General", "{[1,2,4,7,8]}", Date.valueOf("2020-3-18"));
        Menu menuLast = new Menu(1, "General", "{[1,2,4,5,7]}", Date.valueOf("2020-2-28"));
        Menu menu2Last = new Menu(1, "General", "{[1,2,4,5]}", Date.valueOf("2020-2-10"));
        Menu[] expected = {menuCurrent, menuLast, menu2Last};

        Menu[] returned = service.getMenuHistoryByName(menuCurrent.getName());

        assertEquals(expected, returned);
    }
    @Test
    public void getMenuHistoryByName_onlyCurrent() {
        Menu menuCurrent = new Menu(1, "General", "{[1,2,4,7,8]}", Date.valueOf("2020-3-18"));
        Menu[] expected = {menuCurrent};

        Menu[] returned = service.getMenuHistoryByName(menuCurrent.getName());

        assertEquals(expected, returned);
    }
    @Test
    public void getMenuHistoryByName_notFound() {
        String nameNotFound = "This is not the name you are looking for.";

        Menu[] returned = service.getMenuHistoryByName(nameNotFound);

        assertEquals(null, returned);
    }
    @Test
    public void updateMenu_noUpdate() {
        Menu startMenu = new Menu(1, "General", "{[1,2,4,7,8]}", Date.valueOf("2019-2-12"));
        Map<String,String> request = new HashMap<String,String>();

        Menu returned = service.updateMenu(startMenu.getId(), request);

        assertEquals(startMenu, returned);
    }
    @Test
    public void updateMenu_itemsUpdate() {
        Menu startMenu = new Menu(1, "General", "{[1,2,4,7,8]}", Date.valueOf("2019-2-12"));
        Menu endMenu = new Menu(1, "General", "{[1,2,4,7,8,12]}", Date.valueOf("2020-3-18"));
        Map<String,String> request = new HashMap<String,String>();
        request.put("items", endMenu.getItems());

        Menu returned = service.updateMenu(startMenu.getId(), request);

        assertEquals(endMenu, returned);
    }
}