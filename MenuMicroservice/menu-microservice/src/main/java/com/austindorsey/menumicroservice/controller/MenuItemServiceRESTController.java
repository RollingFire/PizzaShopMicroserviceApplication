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

    @RequestMapping(value = "/menuItem/{id:^[0-9]+$}", method = RequestMethod.POST)
    ResponseEntity<?> updateMenuById(@PathVariable(value = "id") int id,
                                     @PathVariable(value = "catagory", required = false) String catagory,
                                     @PathVariable(value = "name", required = false) String name,
                                     @PathVariable(value = "discription", required = false) String discription,
                                     @PathVariable(value = "cost", required = false) Double cost) {
        try {
            Map<String,Object> updatePairs = new HashMap<String,Object>();
            if (catagory != null) {updatePairs.put("catagory", catagory);}
            if (catagory != null) {updatePairs.put("name", name);}
            if (catagory != null) {updatePairs.put("discription", discription);}
            if (catagory != null) {updatePairs.put("cost", cost);}

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