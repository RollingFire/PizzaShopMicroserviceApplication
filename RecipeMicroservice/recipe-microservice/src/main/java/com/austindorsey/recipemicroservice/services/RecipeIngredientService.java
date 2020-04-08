package com.austindorsey.recipemicroservice.services;

import java.sql.SQLException;

import com.austindorsey.recipemicroservice.exceptions.InventoryAPIError;
import com.austindorsey.recipemicroservice.models.RecipeIngredient;
import com.austindorsey.recipemicroservice.models.RecipeIngredientRequest;

public interface RecipeIngredientService {
    public int[] getUniqueMenuItemIds() throws SQLException, ClassNotFoundException;
    public RecipeIngredient[] getRecipe(int menuItemId) throws SQLException, ClassNotFoundException;
    public RecipeIngredient[] createRecipe(int menuItemId, RecipeIngredientRequest[] request) throws SQLException, ClassNotFoundException;
    public RecipeIngredient getRecipeIngredient(int id) throws SQLException, ClassNotFoundException;
    public RecipeIngredient updateRecipeIngredient(int id, RecipeIngredientRequest request) throws SQLException, ClassNotFoundException;
    public int removeIngredientFromRecipe(int id) throws SQLException, ClassNotFoundException;
    public void fireRecipe(int menuItemId) throws InventoryAPIError, SQLException, ClassNotFoundException;
    public void fireRecipeIngredient(int recipeIngredientId) throws Exception;
}