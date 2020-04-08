package com.austindorsey.recipemicroservice.services;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import com.austindorsey.recipemicroservice.exceptions.InventoryAPIError;
import com.austindorsey.recipemicroservice.models.RecipeIngredient;
import com.austindorsey.recipemicroservice.models.RecipeIngredientRequest;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RecipeIngredientServiceIMPLTest {
    @InjectMocks private RecipeIngredientServiceIMPL service = Mockito.spy(RecipeIngredientServiceIMPL.class);
    @InjectMocks private DriverManagerWrapper driverManagerWrapper = Mockito.spy(DriverManagerWrapper.class);
    @InjectMocks private InventoryAPIInterface inventoryInterface = Mockito.mock(InventoryAPIInterface.class);
    @Mock private Connection mockConnection;
    @Mock private Statement mockStatement;
    @Mock private ResultSet mockResult;
    @Captor ArgumentCaptor<String> captor;

    @Test
    public void getUniqueMenuItemIds__3MenuIds() throws Exception {
        int[] expected = {1,2,3,8};

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
        when(mockResult.getInt("menuItemId"))
            .thenReturn(expected[0])
            .thenReturn(expected[1])
            .thenReturn(expected[2])
            .thenReturn(expected[3]);

        int[] returnedMenuIds = service.getUniqueMenuItemIds();

        assertArrayEquals(expected, returnedMenuIds);
        verify(driverManagerWrapper, times(1)).getConnection(any(), any(), any());
        verify(mockConnection, times(1)).createStatement();
        verify(mockStatement, times(1)).executeQuery(anyString());
    }

    @Test
    public void getUniqueMenuItemIds__0MenuIds() throws Exception {
        int[] expected = {};

        Mockito.mock(DriverManagerWrapper.class);
        doReturn(mockConnection).when(driverManagerWrapper).getConnection(any(), any(), any());
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResult);
        when(mockResult.next())
            .thenReturn(false);

        int[] returnedMenuIds = service.getUniqueMenuItemIds();

        assertArrayEquals(expected, returnedMenuIds);
        verify(driverManagerWrapper, times(1)).getConnection(any(), any(), any());
        verify(mockConnection, times(1)).createStatement();
        verify(mockStatement, times(1)).executeQuery(anyString());
    }

    @Test
    public void getRecipe_3Items() throws Exception {
        RecipeIngredient ingredient1 = new RecipeIngredient(1, 6, 1, 1.0);
        RecipeIngredient ingredient4 = new RecipeIngredient(4, 6, 3, 8.0);
        RecipeIngredient ingredient5 = new RecipeIngredient(5, 6, 3, 0.5);
        RecipeIngredient[] expected = {ingredient1, ingredient4, ingredient5};

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
            .thenReturn(ingredient1.getId())
            .thenReturn(ingredient4.getId())
            .thenReturn(ingredient5.getId());
        when(mockResult.getInt("inventoryItemId"))
            .thenReturn(ingredient1.getInventoryItemId())
            .thenReturn(ingredient4.getInventoryItemId())
            .thenReturn(ingredient5.getInventoryItemId());
        when(mockResult.getDouble("quantityUsed"))
            .thenReturn(ingredient1.getQuantityUsed().doubleValue())
            .thenReturn(ingredient4.getQuantityUsed().doubleValue())
            .thenReturn(ingredient5.getQuantityUsed().doubleValue());

        RecipeIngredient[] returnedRecipe = service.getRecipe(ingredient1.getMenuItemId());

        assertArrayEquals(expected, returnedRecipe);
        verify(driverManagerWrapper, times(1)).getConnection(any(), any(), any());
        verify(mockConnection, times(1)).createStatement();
        verify(mockStatement, times(1)).executeQuery(anyString());
    }
    @Test
    public void getRecipe_1Item() throws Exception {
        RecipeIngredient ingredient1 = new RecipeIngredient(1, 6, 1, 1.0);
        RecipeIngredient[] expected = {ingredient1};

        Mockito.mock(DriverManagerWrapper.class);
        doReturn(mockConnection).when(driverManagerWrapper).getConnection(any(), any(), any());
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResult);
        when(mockResult.next())
            .thenReturn(true)
            .thenReturn(false);
        when(mockResult.getInt("id"))
            .thenReturn(ingredient1.getId());
        when(mockResult.getInt("inventoryItemId"))
            .thenReturn(ingredient1.getInventoryItemId());
        when(mockResult.getDouble("quantityUsed"))
            .thenReturn(ingredient1.getQuantityUsed().doubleValue());

        RecipeIngredient[] returnedRecipe = service.getRecipe(ingredient1.getMenuItemId());

        assertArrayEquals(expected, returnedRecipe);
        verify(driverManagerWrapper, times(1)).getConnection(any(), any(), any());
        verify(mockConnection, times(1)).createStatement();
        verify(mockStatement, times(1)).executeQuery(anyString());
    }
    @Test
    public void getRecipe_0Items() throws Exception {
        int idThatDoesntExist = 0;
        RecipeIngredient[] expected = {};

        Mockito.mock(DriverManagerWrapper.class);
        doReturn(mockConnection).when(driverManagerWrapper).getConnection(any(), any(), any());
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResult);
        when(mockResult.next())
            .thenReturn(false);

        RecipeIngredient[] returnedRecipe = service.getRecipe(idThatDoesntExist);

        assertArrayEquals(expected, returnedRecipe);
        verify(driverManagerWrapper, times(1)).getConnection(any(), any(), any());
        verify(mockConnection, times(1)).createStatement();
        verify(mockStatement, times(1)).executeQuery(anyString());
    }
    @Test
    public void createRecipe_3Made() throws Exception {
        RecipeIngredientRequest ingredient1 = new RecipeIngredientRequest(1, 1.0);
        RecipeIngredientRequest ingredient2 = new RecipeIngredientRequest(8, 1.0);
        RecipeIngredientRequest ingredient3 = new RecipeIngredientRequest(3, 2.0);
        RecipeIngredientRequest[] allIngredients = {ingredient1, ingredient2, ingredient3};
        int menuItemId = 2;
        String expectedSQL1 = "INSERT INTO null (menuItemId, inventoryItemId, quantityUsed) VALUES (" + 
                menuItemId + "," + ingredient1.getInventoryItemId() + "," + ingredient1.getQuantityUsed() + ");";
        String expectedSQL2 = "INSERT INTO null (menuItemId, inventoryItemId, quantityUsed) VALUES (" + 
                menuItemId + "," + ingredient2.getInventoryItemId() + "," + ingredient2.getQuantityUsed() + ");";
        String expectedSQL3 = "INSERT INTO null (menuItemId, inventoryItemId, quantityUsed) VALUES (" + 
                menuItemId + "," + ingredient3.getInventoryItemId() + "," + ingredient3.getQuantityUsed() + ");";
        RecipeIngredient expectedIngredient = new RecipeIngredient(1, menuItemId, 3, 4.0);
        RecipeIngredient[] expected = {expectedIngredient, expectedIngredient, expectedIngredient};
        
        doReturn(expected).when(service).getRecipe(menuItemId);
        Mockito.mock(DriverManagerWrapper.class);
        doReturn(mockConnection).when(driverManagerWrapper).getConnection(any(), any(), any());
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeUpdate(anyString())).thenReturn(1);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResult);
        when(mockResult.next())
            .thenReturn(true)
            .thenReturn(true)
            .thenReturn(true)
            .thenReturn(false);
        when(mockResult.getInt("id"))
            .thenReturn(expectedIngredient.getId());
        when(mockResult.getInt("menuItemId"))
            .thenReturn(expectedIngredient.getMenuItemId());
        when(mockResult.getInt("inventoryItemId"))
            .thenReturn(expectedIngredient.getInventoryItemId());
        when(mockResult.getDouble("quantityUsed"))
            .thenReturn(expectedIngredient.getQuantityUsed().doubleValue());

        RecipeIngredient[] returnedRecipes = service.createRecipe(menuItemId, allIngredients);

        verify(mockStatement, times(3)).executeUpdate(captor.capture());
        List<String> capturedSQLs = captor.getAllValues();
        assertEquals(3, capturedSQLs.size(), "ExecuteUpdate was not called the correct number of times.");
        if (capturedSQLs.size() == 3) {
            assertEquals(capturedSQLs.get(0), expectedSQL1);
            assertEquals(capturedSQLs.get(1), expectedSQL2);
            assertEquals(capturedSQLs.get(2), expectedSQL3);
        }

        assertArrayEquals(expected, returnedRecipes);
        verify(driverManagerWrapper, times(1)).getConnection(any(), any(), any());
        verify(mockConnection, times(1)).createStatement();
        verify(mockStatement, times(3)).executeQuery(anyString());
    }
    @Test
    public void createRecipe_1Made() throws Exception {
        RecipeIngredientRequest ingredient1 = new RecipeIngredientRequest(1, 1.0);
        RecipeIngredientRequest[] allIngredients = {ingredient1};
        int menuItemId = 2;
        String expectedSQL1 = "INSERT INTO null (menuItemId, inventoryItemId, quantityUsed) VALUES (" + 
                menuItemId + "," + ingredient1.getInventoryItemId() + "," + ingredient1.getQuantityUsed() + ");";
        RecipeIngredient expectedIngredient = new RecipeIngredient(1, menuItemId, 3, 4.0);
        RecipeIngredient[] expected = {expectedIngredient};
        
        doReturn(expected).when(service).getRecipe(menuItemId);
        Mockito.mock(DriverManagerWrapper.class);
        doReturn(mockConnection).when(driverManagerWrapper).getConnection(any(), any(), any());
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeUpdate(anyString())).thenReturn(1);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResult);
        when(mockResult.next())
            .thenReturn(true)
            .thenReturn(true)
            .thenReturn(true)
            .thenReturn(false);
        when(mockResult.getInt("id"))
            .thenReturn(expectedIngredient.getId());
        when(mockResult.getInt("menuItemId"))
            .thenReturn(expectedIngredient.getMenuItemId());
        when(mockResult.getInt("inventoryItemId"))
            .thenReturn(expectedIngredient.getInventoryItemId());
        when(mockResult.getDouble("quantityUsed"))
            .thenReturn(expectedIngredient.getQuantityUsed().doubleValue());

        RecipeIngredient[] returnedRecipes = service.createRecipe(menuItemId, allIngredients);

        verify(mockStatement, times(1)).executeUpdate(captor.capture());
        List<String> capturedSQLs = captor.getAllValues();
        assertEquals(1, capturedSQLs.size(), "ExecuteUpdate was not called the correct number of times.");
        if (capturedSQLs.size() == 1) {
            assertEquals(capturedSQLs.get(0), expectedSQL1);
        }

        assertArrayEquals(expected, returnedRecipes);
        verify(driverManagerWrapper, times(1)).getConnection(any(), any(), any());
        verify(mockConnection, times(1)).createStatement();
        verify(mockStatement, times(1)).executeQuery(anyString());
    }
    @Test
    public void createRecipe_0Made() throws Exception {
        RecipeIngredientRequest[] allIngredients = {};
        int menuItemId = 2;
        RecipeIngredient[] expected = {};

        
        doReturn(expected).when(service).getRecipe(menuItemId);
        Mockito.mock(DriverManagerWrapper.class);
        doReturn(mockConnection).when(driverManagerWrapper).getConnection(any(), any(), any());
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeUpdate(anyString())).thenReturn(1);

        RecipeIngredient[] returnedRecipes = service.createRecipe(menuItemId, allIngredients);

        verify(mockStatement, times(0)).executeUpdate(captor.capture());
        assertArrayEquals(expected, returnedRecipes);
        verify(driverManagerWrapper, times(1)).getConnection(any(), any(), any());
        verify(mockConnection, times(1)).createStatement();
        verify(mockStatement, times(0)).executeQuery(anyString());
    }
    @Test
    public void getRecipeIngredient_found() throws Exception {
        RecipeIngredient expected = new RecipeIngredient(5, 6, 1, 1.0);

        Mockito.mock(DriverManagerWrapper.class);
        doReturn(mockConnection).when(driverManagerWrapper).getConnection(any(), any(), any());
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResult);
        when(mockResult.next())
            .thenReturn(true)
            .thenReturn(false);
        when(mockResult.getInt("menuItemId"))
            .thenReturn(expected.getMenuItemId());
        when(mockResult.getInt("inventoryItemId"))
            .thenReturn(expected.getInventoryItemId());
        when(mockResult.getDouble("quantityUsed"))
            .thenReturn(expected.getQuantityUsed().doubleValue());

        RecipeIngredient returnedRecipe = service.getRecipeIngredient(expected.getId());

        assertEquals(expected, returnedRecipe);
        verify(driverManagerWrapper, times(1)).getConnection(any(), any(), any());
        verify(mockConnection, times(1)).createStatement();
        verify(mockStatement, times(1)).executeQuery(anyString());
    }
    @Test
    public void getRecipeIngredient_notFound() throws Exception {
        int idThatDoesntExist = 0;
        RecipeIngredient expected = null;

        Mockito.mock(DriverManagerWrapper.class);
        doReturn(mockConnection).when(driverManagerWrapper).getConnection(any(), any(), any());
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResult);
        when(mockResult.next())
            .thenReturn(false);

        RecipeIngredient returnedRecipe = service.getRecipeIngredient(idThatDoesntExist);

        assertEquals(expected, returnedRecipe);
        verify(driverManagerWrapper, times(1)).getConnection(any(), any(), any());
        verify(mockConnection, times(1)).createStatement();
        verify(mockStatement, times(1)).executeQuery(anyString());
    }

    @Test
    public void fireRecipe_success() throws Exception {
        RecipeIngredient ingredient1 = new RecipeIngredient(1, 6, 1, 1.0);
        RecipeIngredient ingredient4 = new RecipeIngredient(4, 6, 3, 8.0);
        RecipeIngredient ingredient5 = new RecipeIngredient(5, 6, 3, 0.5);
        RecipeIngredient[] allIngredients = {ingredient1, ingredient4, ingredient5};

        doReturn(allIngredients).when(service).getRecipe(ingredient1.getMenuItemId());

        doNothing().when(service).fireRecipeIngredient(ingredient1);
        doNothing().when(service).fireRecipeIngredient(ingredient4);
        doNothing().when(service).fireRecipeIngredient(ingredient5);

        service.fireRecipe(ingredient1.getMenuItemId());

        verify(service, times(3)).fireRecipeIngredient(any());
    }
    @Test
    public void fireRecipe_halfwayFail_reverted() throws Exception {
        RecipeIngredient ingredient1 = new RecipeIngredient(1, 6, 1, 1.0);
        RecipeIngredient ingredient4 = new RecipeIngredient(4, 6, 3, 8.0);
        RecipeIngredient ingredient5 = new RecipeIngredient(5, 6, 3, 0.5);
        RecipeIngredient[] allIngredients = {ingredient1, ingredient4, ingredient5};

        doReturn(allIngredients).when(service).getRecipe(ingredient1.getMenuItemId());

        //Normal opportation
        doNothing().when(service).fireRecipeIngredient(ingredient1);
        doThrow(new InventoryAPIError(InventoryAPIError.FailureType.ITEM_FAILED, "")).when(service).fireRecipeIngredient(ingredient4);

        //Revert changes
        doReturn(200).when(inventoryInterface).removeUnitsFromInventory(ingredient1.getInventoryItemId(), -ingredient1.getQuantityUsed().doubleValue());
        //doReturn(200).when(inventoryInterface).removeUnitsFromInventory(any(), any());

        try {
            service.fireRecipe(ingredient1.getMenuItemId());
            fail("InventoryAPIError error wasn't thrown.");
        } catch (InventoryAPIError e) {
            assertEquals(InventoryAPIError.FailureType.REVERTED, e.getFailureType());
            verify(service, times(1)).fireRecipeIngredient(ingredient1);
            verify(service, times(1)).fireRecipeIngredient(ingredient4);
            verify(service, times(0)).fireRecipeIngredient(ingredient5);
            verify(inventoryInterface, times(1)).removeUnitsFromInventory(ingredient1.getInventoryItemId(), -ingredient1.getQuantityUsed().doubleValue());
            verify(inventoryInterface, times(0)).removeUnitsFromInventory(ingredient4.getInventoryItemId(), -ingredient4.getQuantityUsed().doubleValue());
            verify(inventoryInterface, times(0)).removeUnitsFromInventory(ingredient5.getInventoryItemId(), -ingredient5.getQuantityUsed().doubleValue());
        }
    }
    @Test
    public void fireRecipe_halfwayFail_failedToReverted() throws Exception {
        RecipeIngredient ingredient1 = new RecipeIngredient(1, 6, 1, 1.0);
        RecipeIngredient ingredient4 = new RecipeIngredient(4, 6, 3, 8.0);
        RecipeIngredient ingredient5 = new RecipeIngredient(5, 6, 3, 0.5);
        RecipeIngredient[] allIngredients = {ingredient1, ingredient4, ingredient5};

        doReturn(allIngredients).when(service).getRecipe(ingredient1.getMenuItemId());

        //Normal opportation
        doNothing().when(service).fireRecipeIngredient(ingredient1);
        doNothing().when(service).fireRecipeIngredient(ingredient4);
        doThrow(new InventoryAPIError(InventoryAPIError.FailureType.ITEM_FAILED, "")).when(service).fireRecipeIngredient(ingredient5);

        //Revert changes
        doReturn(200).when(inventoryInterface).removeUnitsFromInventory(ingredient4.getInventoryItemId(), -ingredient4.getQuantityUsed().doubleValue());
        doThrow(new InventoryAPIError(InventoryAPIError.FailureType.UNREVERTED, "")).when(inventoryInterface).removeUnitsFromInventory(ingredient1.getInventoryItemId(), -ingredient1.getQuantityUsed().doubleValue());

        try {
            service.fireRecipe(ingredient1.getMenuItemId());
            fail("InventoryAPIError error wasn't thrown.");
        } catch (InventoryAPIError e) {
            assertEquals(InventoryAPIError.FailureType.UNREVERTED, e.getFailureType());
            verify(service, times(1)).fireRecipeIngredient(ingredient1);
            verify(service, times(1)).fireRecipeIngredient(ingredient4);
            verify(service, times(1)).fireRecipeIngredient(ingredient5);
            verify(inventoryInterface, times(1)).removeUnitsFromInventory(ingredient1.getInventoryItemId(), -ingredient1.getQuantityUsed().doubleValue());
            verify(inventoryInterface, times(1)).removeUnitsFromInventory(ingredient4.getInventoryItemId(), -ingredient4.getQuantityUsed().doubleValue());
            verify(inventoryInterface, times(0)).removeUnitsFromInventory(ingredient5.getInventoryItemId(), -ingredient5.getQuantityUsed().doubleValue());
        }
    }
}
