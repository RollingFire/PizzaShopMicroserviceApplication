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

    @RequestMapping(value = "/api/menu", method = RequestMethod.GET)
    ResponseEntity<?> getMenus() {
        try {
            Menu[] menus = menuService.getMenus();
            return new ResponseEntity<>(menus, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/api/menu", method = RequestMethod.POST)
    ResponseEntity<?> createNewMenu(@RequestBody CreateMenuRequest request) {
        try {
            Menu menu = menuService.createNewMenu(request);
            return new ResponseEntity<>(menu, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/api/menu/{id:^[0-9]+$}", method = RequestMethod.GET)
    ResponseEntity<?> getCurrentMenuById(@PathVariable(value = "id") int id) {
        try {
            Menu menu = menuService.getCurrentMenuById(id);
            if (menu == null) {
                return new ResponseEntity<>(menu, HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(menu, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/api/menu/{id:^[0-9]+$}", method = RequestMethod.PUT)
    ResponseEntity<?> updateMenuById(@PathVariable(value = "id") int id, @RequestBody UpdateMenuRequest request) {
        try {
            Map<String,Object> updatePairs = new HashMap<String,Object>();
            updatePairs.put("items", request.getItems());
            Menu menu = menuService.updateMenu(id, updatePairs);
            if (menu == null) {
                return new ResponseEntity<>(menu, HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(menu, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @RequestMapping(value = "/api/menu/{itemName:(?!^\\d+$)^.+$}", method = RequestMethod.GET)
    ResponseEntity<?> getCurrentMenuById(@PathVariable(value = "itemName") String itemName) {
        try {
            Menu menu = menuService.getCurrentMenuByName(itemName);
            if (menu == null) {
                return new ResponseEntity<>(menu, HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(menu, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @RequestMapping(value = "/api/menu/{itemName:(?!^\\d+$)^.+$}", method = RequestMethod.PUT)
    ResponseEntity<?> updateMenuByName(@PathVariable(value = "itemName") String itemName, @RequestBody UpdateMenuRequest request) {
        try {
            Map<String,Object> updatePairs = new HashMap<String,Object>();
            updatePairs.put("items", request.getItems());
            Menu menu = menuService.updateMenu(itemName, updatePairs);
            if (menu == null) {
                return new ResponseEntity<>(menu, HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(menu, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/api/menu/{id:^[0-9]+$}/history", method = RequestMethod.GET)
    ResponseEntity<?> getMenuHistoryById(@PathVariable(value = "id") int id) {
        try {
            Menu[] menus = menuService.getMenuHistoryById(id);
            if (menus == null) {
                return new ResponseEntity<>(menus, HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(menus, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/api/menu/{itemName:(?!^\\d+$)^.+$}/history", method = RequestMethod.GET)
    ResponseEntity<?> getMenuHistoryByName(@PathVariable(value = "itemName") String itemName) {
        try {
            Menu[] menus = menuService.getMenuHistoryByName(itemName);
            if (menus == null) {
                return new ResponseEntity<>(menus, HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(menus, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}