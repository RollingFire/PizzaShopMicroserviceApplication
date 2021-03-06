package com.austindorsey.recipemicroservice.services;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.austindorsey.recipemicroservice.exceptions.InventoryAPIError;
import com.austindorsey.recipemicroservice.models.RecipeIngredient;
import com.austindorsey.recipemicroservice.models.RecipeIngredientRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

@Service
@PropertySource("classpath:database.properties")
public class  RecipeIngredientServiceIMPL implements RecipeIngredientService {
    
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
    @Value("${mysql.tableName.inventory}")
    private String tableName;
    private Connection mysql;

    @Autowired private DriverManagerWrapper driverManagerWrapped;
    @Autowired private InventoryAPIInterface inventoryInterface;

    /**
     * Gets the unique menuItemIds from the recipe table.
     * @return int[] Array of unique menuItemIds
     * @throws SQLException SQL errors
     * @throws ClassNotFroundException Only if com.mysql.cj.jdbc.Driver can not be found.
     */
    @Override
    public int[] getUniqueMenuItemIds() throws SQLException, ClassNotFoundException  {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName;
            mysql = driverManagerWrapped.getConnection(url, dbUserName, dbPassword);
            Statement statement = mysql.createStatement();
            ResultSet result = statement.executeQuery("SELECT DISTINCT menuItemId FROM " + tableName + ";");
            List<Integer> list = new ArrayList<>();
            while (result.next()) {
                list.add(result.getInt("menuItemId"));
            }
            Integer[] integerArray = list.toArray(new Integer[list.size()]);
            return Arrays.stream(integerArray).mapToInt(Integer::intValue).toArray();
        } finally {
            if (mysql != null) {
                mysql.close();
            }
        }
    }

    /**
     * Gets the recipe for the menuItem.
     * @param menuItemId id of the menuItem to get the recipe for.
     * @return RecipeIngredient[] Array of all the ingredients. Returns empty list if not found.
     * @throws SQLException SQL errors
     * @throws ClassNotFroundException Only if com.mysql.cj.jdbc.Driver can not be found.
     */
    @Override
    public RecipeIngredient[] getRecipe(int menuItemId) throws SQLException, ClassNotFoundException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName;
            mysql = driverManagerWrapped.getConnection(url, dbUserName, dbPassword);
            Statement statement = mysql.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM " + tableName + " WHERE menuItemId=" + menuItemId + ";");
            List<RecipeIngredient> list = new ArrayList<>();
            while (result.next()) {
                int id = result.getInt("id");
                int inventoryItemId = result.getInt("inventoryItemId");
                Number quantityUsed = result.getDouble("quantityUsed");
                RecipeIngredient item = new RecipeIngredient(id, menuItemId, inventoryItemId, quantityUsed);
                list.add(item);
            }
            return list.toArray(new RecipeIngredient[list.size()]);
        } finally {
            if (mysql != null) {
                mysql.close();
            }
        }
    }

    /**
     * Creates a new recipe for a menuItem.
     * @param menuItemId id of the menuItem to create the recipe for.
     * @param request Array of RecipeIngredientRequests that will make up the recipe.
     * @return RecipeIngredient[] Array of all the new ingredients.
     * @throws SQLException SQL errors
     * @throws ClassNotFroundException Only if com.mysql.cj.jdbc.Driver can not be found.
     */
    @Override
    public RecipeIngredient[] createRecipe(int menuItemId, RecipeIngredientRequest[] request) throws SQLException, ClassNotFoundException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName;
            mysql = driverManagerWrapped.getConnection(url, dbUserName, dbPassword);
            Statement statement = mysql.createStatement();
            List<RecipeIngredient> newItems = new ArrayList<>();
            for (int i = 0; i < request.length; i++) {
                statement.executeUpdate("INSERT INTO " + tableName +
                                " (menuItemId, inventoryItemId, quantityUsed) VALUES (" + 
                                menuItemId + "," + request[i].getInventoryItemId() + "," + request[i].getQuantityUsed() + ");");
                ResultSet result = statement.executeQuery("SELECT * FROM " + tableName +
                                    " WHERE menuItemId=" + menuItemId + " AND inventoryItemId=" + request[i].getInventoryItemId() + 
                                    " AND quantityUsed=" + request[i].getQuantityUsed() + " ORDER BY id DESC LIMIT 1;");
                if (result.next()) {
                    int id = result.getInt("id");
                    int inventoryItemId = result.getInt("inventoryItemId");
                    Number quantityUsed = result.getDouble("quantityUsed");
                    RecipeIngredient item = new RecipeIngredient(id, menuItemId, inventoryItemId, quantityUsed);
                    newItems.add(item);
                }
            }
            return newItems.toArray(new RecipeIngredient[newItems.size()]);
        } finally {
            if (mysql != null) {
                mysql.close();
            }
        }
    }

    /**
     * Get a single resipe ingredient by id.
     * @param id id of the ingredient.
     * @return RecipeIngredient Ingredient with the requested id. Returns null if not found.
     * @throws SQLException SQL errors
     * @throws ClassNotFroundException Only if com.mysql.cj.jdbc.Driver can not be found.
     */
    @Override
    public RecipeIngredient getRecipeIngredient(int id) throws SQLException, ClassNotFoundException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName;
            mysql = driverManagerWrapped.getConnection(url, dbUserName, dbPassword);
            Statement statement = mysql.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM " + tableName + " WHERE id=" + id + ";");
            RecipeIngredient item = null;
            if (result.next()) {
                int menuItemId = result.getInt("menuItemId");
                int inventoryItemId = result.getInt("inventoryItemId");
                Number quantityUsed = result.getDouble("quantityUsed");
                item = new RecipeIngredient(id, menuItemId, inventoryItemId, quantityUsed);
            }
            return item;
        } finally {
            if (mysql != null) {
                mysql.close();
            }
        }
    }

    /**
     * Get a single resipe ingredient by id.
     * @param id id of the ingredient.
     * @return RecipeIngredient Ingredient with the requested id. Returns null if not found.
     * @throws SQLException SQL errors
     * @throws ClassNotFroundException Only if com.mysql.cj.jdbc.Driver can not be found.
     */
    @Override
    public RecipeIngredient updateRecipeIngredient(int id, RecipeIngredientRequest request) throws SQLException, ClassNotFoundException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName;
            mysql = driverManagerWrapped.getConnection(url, dbUserName, dbPassword);
            Statement statement = mysql.createStatement();
            statement.executeUpdate("UPDATE " + tableName + " SET inventoryItemId=" + request.getInventoryItemId() + 
                                        ", quantityUsed=" + request.getQuantityUsed() + " WHERE id=" + id + ";");
            return getRecipeIngredient(id);
        } finally {
            mysql.close();
        }
    }

    /**
     * Remove a recipe ingredient from a recipe.
     * @param id id of the ingredient.
     * @return int Number of rows removed
     * @throws SQLException SQL errors
     * @throws ClassNotFroundException Only if com.mysql.cj.jdbc.Driver can not be found.
     */
    @Override
    public int removeIngredientFromRecipe(int id) throws SQLException, ClassNotFoundException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName;
            mysql = driverManagerWrapped.getConnection(url, dbUserName, dbPassword);
            Statement statement = mysql.createStatement();
            int rowsChanged = statement.executeUpdate("DELETE FROM " + tableName + " WHERE id='" + id + "';");
            return rowsChanged;
        } finally {
            mysql.close();
        }
    }

    /**
     * Removes the number of units from each ingrediant for the recipe from the inventory stock.
     * @param menuItemId Id of the menu item to remove the ingredients from the inventory for.
     * @throws SQLException SQL errors
     * @throws ClassNotFroundException Only if com.mysql.cj.jdbc.Driver can not be found.
     */
    @Override
    public void fireRecipe(int menuItemId) throws InventoryAPIError, ClassNotFoundException, SQLException {
        RecipeIngredient[] recipeIngredients = getRecipe(menuItemId);
        int i = 0;
        try {
            for (; i < recipeIngredients.length; i++) {
                fireRecipeIngredient(recipeIngredients[i]);
            }
        } catch (InventoryAPIError e) {
            try {
                for (; i > 0; i--) {
                    inventoryInterface.removeUnitsFromInventory(recipeIngredients[i-1].getInventoryItemId(), -recipeIngredients[i-1].getQuantityUsed().doubleValue());
                }
                throw new InventoryAPIError(InventoryAPIError.FailureType.REVERTED, "Failed to fire the recipe. Any changes that were made were reverted.");
            } catch (InventoryAPIError e2) {
                throw e2;
            } catch (Exception e2) {
                throw new InventoryAPIError(InventoryAPIError.FailureType.UNREVERTED, "Failed to fire the recipe. There were also errors reveting changed. " + 
                                                                                "Ingredients that were not reverted were: " + Arrays.copyOfRange(recipeIngredients, 0, i));
            }
        }
    }

    /**
     * Removes the number of units from an ingrediant from the inventory stock.
     * @param recipeIngredientId Id of the ingredient to remove the units used from the inventory.
     * @throws SQLException SQL errors
     * @throws ClassNotFroundException Only if com.mysql.cj.jdbc.Driver can not be found.
     */
    @Override
    public void fireRecipeIngredient(int recipeIngredientId) throws InventoryAPIError, ClassNotFoundException, SQLException {
        fireRecipeIngredient(getRecipeIngredient(recipeIngredientId));
    }

    /**
     * Removes the number of units from an ingrediant from the inventory stock.
     * @param recipeIngredient Ingredient to remove the units used from the inventory.
     * @throws SQLException SQL errors
     * @throws ClassNotFroundException Only if com.mysql.cj.jdbc.Driver can not be found.
     */
    public void fireRecipeIngredient(RecipeIngredient recipeIngredient) throws InventoryAPIError {
        try {
            int returnedStatusCode = inventoryInterface.removeUnitsFromInventory(recipeIngredient.getInventoryItemId(), recipeIngredient.getQuantityUsed().doubleValue());
            if (returnedStatusCode != 200) {
                throw new InventoryAPIError(InventoryAPIError.FailureType.ITEM_FAILED, "Failed to remove " + recipeIngredient.getQuantityUsed().doubleValue() +
                                                                " units from the inventory item with the id " + recipeIngredient.getInventoryItemId() + 
                                                                " The status code was " + returnedStatusCode);
            }
        } catch (InventoryAPIError e) {
            throw e;
        } catch (Exception e) {
            throw new InventoryAPIError(InventoryAPIError.FailureType.ITEM_FAILED, "Failed to remove " + recipeIngredient.getQuantityUsed().doubleValue() +
                                                            " units from the inventory item with the id " + recipeIngredient.getInventoryItemId() +
                                                            " Error was " + e.getMessage());
        }
    }
}