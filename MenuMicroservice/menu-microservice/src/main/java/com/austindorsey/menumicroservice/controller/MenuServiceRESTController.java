package com.austindorsey.menumicroservice.controller;

import java.util.HashMap;
import java.util.Map;

import com.austindorsey.menumicroservice.models.CreateMenuRequest;
import com.austindorsey.menumicroservice.models.Menu;
import com.austindorsey.menumicroservice.models.UpdateMenuRequest;
import com.austindorsey.menumicroservice.services.MenuService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MenuServiceRESTController {

    @Autowired
    private MenuService menuService;

    @RequestMapping(value = "/menu", method = RequestMethod.GET)
    ResponseEntity<?> getMenus() {
        try {
            Menu[] menus = menuService.getMenus();
            return new ResponseEntity<>(menus, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/menu", method = RequestMethod.POST)
    ResponseEntity<?> createNewMenu(@RequestBody CreateMenuRequest request) {
        try {
            Menu newMenu = menuService.createNewMenu(request);
            return new ResponseEntity<>(newMenu, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/menu/{id:^[0-9]+$}", method = RequestMethod.GET)
    ResponseEntity<?> getCurrentMenuById(@PathVariable(value = "id") int id) {
        try {
            Menu newMenu = menuService.getCurrentMenuById(id);
            if (newMenu == null) {
                return new ResponseEntity<>(newMenu, HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(newMenu, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/menu/{id:^[0-9]+$}", method = RequestMethod.POST)
    ResponseEntity<?> updateMenuById(@PathVariable(value = "id") int id, @RequestBody UpdateMenuRequest request) {
        try {
            Map<String,Object> updatePairs = new HashMap<String,Object>();
            updatePairs.put("items", request.getItems());
            Menu newMenu = menuService.updateMenu(id, updatePairs);
            if (newMenu == null) {
                return new ResponseEntity<>(newMenu, HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(newMenu, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @RequestMapping(value = "/menu/{itemName:(?!^\\d+$)^.+$}", method = RequestMethod.GET)
    ResponseEntity<?> getCurrentMenuById(@PathVariable(value = "itemName") String itemName) {
        try {
            Menu newMenu = menuService.getCurrentMenuByName(itemName);
            if (newMenu == null) {
                return new ResponseEntity<>(newMenu, HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(newMenu, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @RequestMapping(value = "/menu/{itemName:(?!^\\d+$)^.+$}", method = RequestMethod.POST)
    ResponseEntity<?> updateMenuByName(@PathVariable(value = "itemName") String itemName, @RequestBody UpdateMenuRequest request) {
        try {
            Map<String,Object> updatePairs = new HashMap<String,Object>();
            updatePairs.put("items", request.getItems());
            Menu newMenu = menuService.updateMenu(itemName, updatePairs);
            if (newMenu == null) {
                return new ResponseEntity<>(newMenu, HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(newMenu, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/menu/{id:^[0-9]+$}/history", method = RequestMethod.GET)
    ResponseEntity<?> getMenuHistoryById(@PathVariable(value = "id") int id) {
        try {
            Menu[] newMenu = menuService.getMenuHistoryById(id);
            if (newMenu == null) {
                return new ResponseEntity<>(newMenu, HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(newMenu, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/menu/{itemName:(?!^\\d+$)^.+$}/history", method = RequestMethod.GET)
    ResponseEntity<?> getMenuHistoryByName(@PathVariable(value = "itemName") String itemName) {
        try {
            Menu[] newMenu = menuService.getMenuHistoryByName(itemName);
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