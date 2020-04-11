package com.austindorsey.recipemicroservice.controller;

import com.austindorsey.recipemicroservice.exceptions.InventoryAPIError;
import com.austindorsey.recipemicroservice.models.RecipeIngredient;
import com.austindorsey.recipemicroservice.models.RecipeIngredientRequest;
import com.austindorsey.recipemicroservice.services.RecipeIngredientService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RecipeIngredientRESTController {
    
    @Autowired
    private RecipeIngredientService recipeService;

    @RequestMapping(value = "/api/recipe", method = RequestMethod.GET)
    ResponseEntity<?> getUniqueMenuItemIds() {
        try {
            int[] menusItemIds = recipeService.getUniqueMenuItemIds();
            return new ResponseEntity<>(menusItemIds, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/api/recipe/{menuItemId}", method = RequestMethod.GET)
    ResponseEntity<?> getRecipe(@PathVariable(value = "menuItemId") int menuItemId) {
        try {
            RecipeIngredient[] recipeIngredients = recipeService.getRecipe(menuItemId);
            if (recipeIngredients.length > 0) {
                return new ResponseEntity<>(recipeIngredients, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/api/recipe/{menuItemId}", method = RequestMethod.POST)
    ResponseEntity<?> createRecipe(@PathVariable(value = "menuItemId") int menuItemId, @RequestBody RecipeIngredientRequest[] requestBody) {
        try {
            RecipeIngredient[] recipeIngredients = recipeService.createRecipe(menuItemId, requestBody);
            if (recipeIngredients.length > 0) {
                return new ResponseEntity<>(recipeIngredients, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/api/recipe/{menuItemId}/fire", method = RequestMethod.POST)
    ResponseEntity<?> fireRecipe(@PathVariable(value = "menuItemId") int menuItemId) {
        try {
            recipeService.fireRecipe(menuItemId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (InventoryAPIError e) {
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/api/recipe/ingredient/{id}", method = RequestMethod.GET)
    ResponseEntity<?> getRecipeIngredient(@PathVariable(value = "id") int id) {
        try {
            RecipeIngredient recipeIngredients = recipeService.getRecipeIngredient(id);
            if (recipeIngredients != null) {
                return new ResponseEntity<>(recipeIngredients, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/api/recipe/ingredient/{id}", method = RequestMethod.PUT)
    ResponseEntity<?> updateRecipeIngredient(@PathVariable(value = "id") int id, @RequestBody RecipeIngredientRequest requestBody) {
        try {
            RecipeIngredient recipeIngredients = recipeService.updateRecipeIngredient(id, requestBody);
            if (recipeIngredients != null) {
                return new ResponseEntity<>(recipeIngredients, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/api/recipe/ingredient/{id}", method = RequestMethod.DELETE)
    ResponseEntity<?> removeIngredientFromRecipe(@PathVariable(value = "id") int id) {
        try {
            int rowsDeleted = recipeService.removeIngredientFromRecipe(id);
            if (rowsDeleted > 0) {
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/api/recipe/ingredient/{id}/fire", method = RequestMethod.POST)
    ResponseEntity<?> fireRecipeIngredient(@PathVariable(value = "id") int id) {
        try {
            recipeService.fireRecipeIngredient(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}