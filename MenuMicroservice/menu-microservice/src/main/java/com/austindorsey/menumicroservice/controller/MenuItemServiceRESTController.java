package com.austindorsey.menumicroservice.controller;

import java.util.HashMap;
import java.util.Map;

import com.austindorsey.menumicroservice.models.CreateMenuItemRequest;
import com.austindorsey.menumicroservice.models.MenuItem;
import com.austindorsey.menumicroservice.services.MenuItemService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MenuItemServiceRESTController {

    @Autowired
    private MenuItemService menuItemService;

    @RequestMapping(value = "/menuItem", method = RequestMethod.GET)
    ResponseEntity<?> getMenus() {
        try {
            MenuItem[] menus = menuItemService.getMenuItems();
            return new ResponseEntity<>(menus, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/menuItem", method = RequestMethod.POST)
    ResponseEntity<?> createNewMenu(@RequestBody CreateMenuItemRequest request) {
        try {
            MenuItem newMenu = menuItemService.createNewMenuItem(request);
            return new ResponseEntity<>(newMenu, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/menuItem/{id:^[0-9]+$}", method = RequestMethod.GET)
    ResponseEntity<?> getMenuItemByID(@PathVariable(value = "id") int id) {
        try {
            MenuItem newMenu = menuItemService.getMenuItemByID(id);
            if (newMenu == null) {
                return new ResponseEntity<>(newMenu, HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(newMenu, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/menuItem/{id:^[0-9]+$}", method = RequestMethod.PUT)
    ResponseEntity<?> updateMenuById(@PathVariable(value = "id") int id,
                                     @RequestParam(value = "catagory", required = false) String catagory,
                                     @RequestParam(value = "itemName", required = false) String itemName,
                                     @RequestParam(value = "discription", required = false) String discription,
                                     @RequestParam(value = "cost", required = false) Number cost) {
        try {
            Map<String,Object> updatePairs = new HashMap<String,Object>();
            if (catagory != null) {updatePairs.put("catagory", catagory);}
            if (itemName != null) {updatePairs.put("itemName", itemName);}
            if (discription != null) {updatePairs.put("discription", discription);}
            if (cost != null) {updatePairs.put("cost", cost);}

            MenuItem newMenu = menuItemService.updateMenuItem(id, updatePairs);
            if (newMenu == null) {
                return new ResponseEntity<>(newMenu, HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(newMenu, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @RequestMapping(value = "/menuItem/{itemName:(?!^\\d+$)^.+$}", method = RequestMethod.GET)
    ResponseEntity<?> searchMenuItemsByName(@PathVariable(value = "itemName") String itemName) {
        try {
            MenuItem[] newMenu = menuItemService.searchMenuItemsByName(itemName);
            if (newMenu == null) {
                return new ResponseEntity<>(newMenu, HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(newMenu, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/menuItem/{id:^[0-9]+$}/history", method = RequestMethod.GET)
    ResponseEntity<?> getMenuItemHistoryById(@PathVariable(value = "id") int id) {
        try {
            MenuItem[] newMenu = menuItemService.getMenuItemHistoryById(id);
            if (newMenu == null) {
                return new ResponseEntity<>(newMenu, HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(newMenu, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}