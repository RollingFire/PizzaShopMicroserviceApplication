package com.austindorsey.menumicroservice.services;

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
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import com.austindorsey.menumicroservice.models.Menu;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MenuServiceIMPLTest {
    @InjectMocks private MenuService service = Mockito.spy(MenuServiceIMPL.class);
    @InjectMocks private DriverManagerWrapper driverManagerWrapper = Mockito.spy(DriverManagerWrapper.class);
    @Mock private Connection mockConnection;
    @Mock private Statement mockStatement;
    @Mock private ResultSet mockResult;
    @Captor ArgumentCaptor<String> captor;

    @Test
    public void getMenus_3Fourd() throws SQLException, ClassNotFoundException {
        Menu menu1 = new Menu(1, "General", "{[1,2,4,5,6,7]}", Date.valueOf("2020-3-18"));
        Menu menu2 = new Menu(2, "Weekend", "{[1,2,5,6,7,12]}", Date.valueOf("2020-1-12"));
        Menu menu3 = new Menu(3, "Christmas", "{[11,21,41,42,43]}", Date.valueOf("2019-12-18"));
        Menu[] expected = {menu1, menu2, menu3};

        
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
            .thenReturn(menu1.getId())
            .thenReturn(menu2.getId())
            .thenReturn(menu3.getId());
        when(mockResult.getString("menuName"))
            .thenReturn(menu1.getName())
            .thenReturn(menu2.getName())
            .thenReturn(menu3.getName());
        when(mockResult.getString("items"))
            .thenReturn(menu1.getItems())
            .thenReturn(menu2.getItems())
            .thenReturn(menu3.getItems());
        when(mockResult.getDate("revisionDate"))
            .thenReturn(menu1.getRevisionDate())
            .thenReturn(menu2.getRevisionDate())
            .thenReturn(menu3.getRevisionDate());

        Menu[] returned = service.getMenus();

        assertArrayEquals(expected, returned);
        verify(driverManagerWrapper, times(1)).getConnection(any(), any(), any());
        verify(mockConnection, times(1)).createStatement();
        verify(mockStatement, times(1)).executeQuery(anyString());
    }
    @Test
    public void getMenus_1Fourd() throws SQLException, ClassNotFoundException {
        Menu menu = new Menu(1, "General", "{[1,2,4,5,6,7]}", Date.valueOf("2020-3-18"));
        Menu[] expected = {menu};

        Mockito.mock(DriverManagerWrapper.class);
        doReturn(mockConnection).when(driverManagerWrapper).getConnection(any(), any(), any());
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResult);
        when(mockResult.next())
            .thenReturn(true)
            .thenReturn(false);
        when(mockResult.getInt("id"))
            .thenReturn(menu.getId());
        when(mockResult.getString("menuName"))
            .thenReturn(menu.getName());
        when(mockResult.getString("items"))
            .thenReturn(menu.getItems());
        when(mockResult.getDate("revisionDate"))
            .thenReturn(menu.getRevisionDate());

        Menu[] returned = service.getMenus();

        assertArrayEquals(expected, returned);
        verify(driverManagerWrapper, times(1)).getConnection(any(), any(), any());
        verify(mockConnection, times(1)).createStatement();
        verify(mockStatement, times(1)).executeQuery(anyString());
    }
    @Test
    public void getMenus_0Fourd() throws SQLException, ClassNotFoundException {
        Menu[] expected = {};

        Mockito.mock(DriverManagerWrapper.class);
        doReturn(mockConnection).when(driverManagerWrapper).getConnection(any(), any(), any());
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResult);
        when(mockResult.next())
            .thenReturn(false);

        Menu[] returned = service.getMenus();

        assertArrayEquals(expected, returned);
        verify(driverManagerWrapper, times(1)).getConnection(any(), any(), any());
        verify(mockConnection, times(1)).createStatement();
        verify(mockStatement, times(1)).executeQuery(anyString());
    }
    @Test
    public void getCurrentMenuById_found() throws SQLException, ClassNotFoundException {
        Menu menu = new Menu(1, "General", "{[1,2,4,5,6,7]}", Date.valueOf("2020-3-18"));

        Mockito.mock(DriverManagerWrapper.class);
        doReturn(mockConnection).when(driverManagerWrapper).getConnection(any(), any(), any());
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResult);
        when(mockResult.next())
            .thenReturn(true);
        when(mockResult.getInt("id"))
            .thenReturn(menu.getId());
        when(mockResult.getString("menuName"))
            .thenReturn(menu.getName());
        when(mockResult.getString("items"))
            .thenReturn(menu.getItems());
        when(mockResult.getDate("revisionDate"))
            .thenReturn(menu.getRevisionDate());

        Menu returnedMenu = service.getCurrentMenuById(menu.getId());

        assertEquals(menu, returnedMenu);
        verify(driverManagerWrapper, times(1)).getConnection(any(), any(), any());
        verify(mockConnection, times(1)).createStatement();
        verify(mockStatement, times(1)).executeQuery(anyString());
    }
    @Test
    public void getCurrentMenuById_notFound() throws SQLException, ClassNotFoundException {
        int idNotFound = 42;

        Mockito.mock(DriverManagerWrapper.class);
        doReturn(mockConnection).when(driverManagerWrapper).getConnection(any(), any(), any());
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResult);
        when(mockResult.next())
            .thenReturn(false);

        Menu returnedMenu = service.getCurrentMenuById(idNotFound);

        assertEquals(null, returnedMenu);
        verify(driverManagerWrapper, times(1)).getConnection(any(), any(), any());
        verify(mockConnection, times(1)).createStatement();
        verify(mockStatement, times(1)).executeQuery(anyString());
    }
    @Test
    public void getCurrentMenuByName_found() throws SQLException, ClassNotFoundException {
        Menu menu = new Menu(1, "General", "{[1,2,4,5,6,7]}", Date.valueOf("2020-3-18"));

        Mockito.mock(DriverManagerWrapper.class);
        doReturn(mockConnection).when(driverManagerWrapper).getConnection(any(), any(), any());
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResult);
        when(mockResult.next())
            .thenReturn(true);
        when(mockResult.getInt("id"))
            .thenReturn(menu.getId());
        when(mockResult.getString("menuName"))
            .thenReturn(menu.getName());
        when(mockResult.getString("items"))
            .thenReturn(menu.getItems());
        when(mockResult.getDate("revisionDate"))
            .thenReturn(menu.getRevisionDate());

        Menu returned = service.getCurrentMenuByName(menu.getName());

        assertEquals(menu, returned);
        verify(driverManagerWrapper, times(1)).getConnection(any(), any(), any());
        verify(mockConnection, times(1)).createStatement();
        verify(mockStatement, times(1)).executeQuery(anyString());
    }
    @Test
    public void getCurrentMenuByName_notFound() throws SQLException, ClassNotFoundException {
        String nameNotFound = "This is not the name you are looking for.";

        Mockito.mock(DriverManagerWrapper.class);
        doReturn(mockConnection).when(driverManagerWrapper).getConnection(any(), any(), any());
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResult);
        when(mockResult.next())
            .thenReturn(false);

        Menu returned = service.getCurrentMenuByName(nameNotFound);
        
        assertEquals(null, returned);
        verify(driverManagerWrapper, times(1)).getConnection(any(), any(), any());
        verify(mockConnection, times(1)).createStatement();
        verify(mockStatement, times(1)).executeQuery(anyString());
    }
    @Test
    public void getMenuHistoryById_historyFound() throws SQLException, ClassNotFoundException {
        Menu menuCurrent = new Menu(1, "General", "{[1,2,4,7,8]}", Date.valueOf("2020-3-18"));
        Menu menuLast = new Menu(1, "General", "{[1,2,4,5,7]}", Date.valueOf("2020-2-28"));
        Menu menu2Last = new Menu(1, "General", "{[1,2,4,5]}", Date.valueOf("2020-2-10"));
        Menu[] expected = {menuCurrent, menuLast, menu2Last};

        doReturn(menuCurrent).when(service).getCurrentMenuById(menuCurrent.getId());
        Mockito.mock(DriverManagerWrapper.class);
        doReturn(mockConnection).when(driverManagerWrapper).getConnection(any(), any(), any());
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResult);
        when(mockResult.next())
            .thenReturn(true)
            .thenReturn(true)
            .thenReturn(false);
        when(mockResult.getInt("id"))
            .thenReturn(menuLast.getId())
            .thenReturn(menu2Last.getId());
        when(mockResult.getString("menuName"))
            .thenReturn(menuLast.getName())
            .thenReturn(menu2Last.getName());
        when(mockResult.getString("items"))
            .thenReturn(menuLast.getItems())
            .thenReturn(menu2Last.getItems());
        when(mockResult.getDate("revisionDate"))
            .thenReturn(menuLast.getRevisionDate())
            .thenReturn(menu2Last.getRevisionDate());

        Menu[] returned = service.getMenuHistoryById(menuCurrent.getId());

        assertArrayEquals(expected, returned);
        verify(driverManagerWrapper, times(1)).getConnection(any(), any(), any());
        verify(mockConnection, times(1)).createStatement();
        verify(mockStatement, times(1)).executeQuery(anyString());
    }
    @Test
    public void getMenuHistoryById_onlyCurrent() throws SQLException, ClassNotFoundException {
        Menu menuCurrent = new Menu(1, "General", "{[1,2,4,7,8]}", Date.valueOf("2020-3-18"));
        Menu[] expected = {menuCurrent};

        doReturn(menuCurrent).when(service).getCurrentMenuById(menuCurrent.getId());
        Mockito.mock(DriverManagerWrapper.class);
        doReturn(mockConnection).when(driverManagerWrapper).getConnection(any(), any(), any());
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResult);
        when(mockResult.next())
            .thenReturn(false);

        Menu[] returned = service.getMenuHistoryById(menuCurrent.getId());

        assertArrayEquals(expected, returned);
        verify(driverManagerWrapper, times(1)).getConnection(any(), any(), any());
        verify(mockConnection, times(1)).createStatement();
        verify(mockStatement, times(1)).executeQuery(anyString());
    }
    @Test
    public void getMenuHistoryById_notFound() throws SQLException, ClassNotFoundException {
        int idNotFound = 42;

        doReturn(null).when(service).getCurrentMenuById(idNotFound);

        Menu[] returned = service.getMenuHistoryById(idNotFound);

        assertEquals(null, returned);
        verify(driverManagerWrapper, times(0)).getConnection(any(), any(), any());
        verify(mockConnection, times(0)).createStatement();
        verify(mockStatement, times(0)).executeQuery(anyString());
    }
    @Test
    public void getMenuHistoryByName_historyFound() throws SQLException, ClassNotFoundException {
        Menu menuCurrent = new Menu(1, "General", "{[1,2,4,7,8]}", Date.valueOf("2020-3-18"));
        Menu menuLast = new Menu(1, "General", "{[1,2,4,5,7]}", Date.valueOf("2020-2-28"));
        Menu menu2Last = new Menu(1, "General", "{[1,2,4,5]}", Date.valueOf("2020-2-10"));
        Menu[] expected = {menuCurrent, menuLast, menu2Last};

        doReturn(menuCurrent).when(service).getCurrentMenuByName(menuCurrent.getName());
        Mockito.mock(DriverManagerWrapper.class);
        doReturn(mockConnection).when(driverManagerWrapper).getConnection(any(), any(), any());
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResult);
        when(mockResult.next())
            .thenReturn(true)
            .thenReturn(true)
            .thenReturn(false);
        when(mockResult.getInt("origenalId"))
            .thenReturn(menuLast.getId())
            .thenReturn(menu2Last.getId());
        when(mockResult.getString("menuName"))
            .thenReturn(menuLast.getName())
            .thenReturn(menu2Last.getName());
        when(mockResult.getString("items"))
            .thenReturn(menuLast.getItems())
            .thenReturn(menu2Last.getItems());
        when(mockResult.getDate("revisionDate"))
            .thenReturn(menuLast.getRevisionDate())
            .thenReturn(menu2Last.getRevisionDate());

        Menu[] returned = service.getMenuHistoryByName(menuCurrent.getName());

        assertArrayEquals(expected, returned);
        verify(driverManagerWrapper, times(1)).getConnection(any(), any(), any());
        verify(mockConnection, times(1)).createStatement();
        verify(mockStatement, times(1)).executeQuery(anyString());
    }
    @Test
    public void getMenuHistoryByName_onlyCurrent() throws SQLException, ClassNotFoundException {
        Menu menuCurrent = new Menu(1, "General", "{[1,2,4,7,8]}", Date.valueOf("2020-3-18"));
        Menu[] expected = {menuCurrent};

        doReturn(menuCurrent).when(service).getCurrentMenuByName(menuCurrent.getName());
        Mockito.mock(DriverManagerWrapper.class);
        doReturn(mockConnection).when(driverManagerWrapper).getConnection(any(), any(), any());
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResult);
        when(mockResult.next())
            .thenReturn(false);

        Menu[] returned = service.getMenuHistoryByName(menuCurrent.getName());

        assertArrayEquals(expected, returned);
        verify(driverManagerWrapper, times(1)).getConnection(any(), any(), any());
        verify(mockConnection, times(1)).createStatement();
        verify(mockStatement, times(1)).executeQuery(anyString());
    }
    @Test
    public void getMenuHistoryByName_notFound() throws SQLException, ClassNotFoundException {
        String nameNotFound = "This is not the name you are looking for.";

        doReturn(null).when(service).getCurrentMenuByName(nameNotFound);

        Menu[] returned = service.getMenuHistoryByName(nameNotFound);

        assertEquals(null, returned);
        verify(driverManagerWrapper, times(0)).getConnection(any(), any(), any());
        verify(mockConnection, times(0)).createStatement();
        verify(mockStatement, times(0)).executeQuery(anyString());
    }
    @Test
    public void updateMenu_noUpdate() throws SQLException, ClassNotFoundException {
        Menu startMenu = new Menu(1, "General", "{[1,2,4,7,8]}", Date.valueOf("2019-2-12"));
        Map<String,Object> request = new HashMap<String,Object>();

        doReturn(startMenu).when(service).getCurrentMenuById(startMenu.getId());

        Menu returned = service.updateMenu(startMenu.getId(), request);

        assertEquals(startMenu, returned);
        verify(driverManagerWrapper, times(0)).getConnection(any(), any(), any());
        verify(mockConnection, times(0)).createStatement();
        verify(mockStatement, times(0)).executeQuery(anyString());
    }
    @Test
    public void updateMenu_itemsUpdate() throws SQLException, ClassNotFoundException {
        Menu startMenu = new Menu(1, "General", "{[1,2,4,7,8]}", Date.valueOf("2019-2-12"));
        Menu endMenu = new Menu(1, "General", "{[1,2,4,7,8,12]}", Date.valueOf("2020-3-18"));
        Map<String,Object> request = new HashMap<String,Object>();
        request.put("items", endMenu.getItems());
        String expectedSQL = "UPDATE null SET items='" + endMenu.getItems() + "' WHERE id=" + endMenu.getId() + ";";

        doReturn(endMenu).when(service).getCurrentMenuById(startMenu.getId());
        doReturn(mockConnection).when(driverManagerWrapper).getConnection(any(), any(), any());
        Mockito.mock(DriverManagerWrapper.class);
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeUpdate(anyString())).thenReturn(1);

        Menu returned = service.updateMenu(startMenu.getId(), request);

        verify(mockStatement).executeUpdate(captor.capture());
        String capturedSQL = captor.getValue();

        assertEquals(endMenu, returned);
        assertEquals(expectedSQL, capturedSQL);
        verify(driverManagerWrapper, times(1)).getConnection(any(), any(), any());
        verify(mockConnection, times(1)).createStatement();
        verify(mockStatement, times(1)).executeUpdate(anyString());
    }
}