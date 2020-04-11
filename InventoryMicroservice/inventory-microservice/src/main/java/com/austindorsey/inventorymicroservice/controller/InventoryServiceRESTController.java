package com.austindorsey.inventorymicroservice.controller;

import com.austindorsey.inventorymicroservice.model.InventoryItem;
import com.austindorsey.inventorymicroservice.model.InventoryItemRequest;
import com.austindorsey.inventorymicroservice.model.RestockRequest;
import com.austindorsey.inventorymicroservice.services.InventoryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InventoryServiceRESTController {

    @Autowired
    private InventoryService inventoryService;

    @RequestMapping(value = "/api/inventory", method = RequestMethod.GET)
    ResponseEntity<?> getInventory() {
        try {
            InventoryItem[] inventory = null;
            inventory = inventoryService.getInventory();
            return new ResponseEntity<>(inventory, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/api/inventory/{id:^[0-9]+$}", method = RequestMethod.GET)
    ResponseEntity<?> getItemByID(@PathVariable(value = "id") int id) {
        try {
            InventoryItem item = inventoryService.getItemByID(id);
            if (item == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(item, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/api/inventory/{itemName:(?!^\\d+$)^.+$}", method = RequestMethod.GET)
    ResponseEntity<?> getItemByName(@PathVariable(value = "itemName") String itemName) {
        try {
            InventoryItem item = inventoryService.getItemByName(itemName);
            if (item == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(item, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/api/inventory/restock/{id:^[0-9]+$}", method = RequestMethod.POST)
    ResponseEntity<?> restockItemByID(@PathVariable(value = "id") int id, @RequestBody RestockRequest request) {
        try {
            double newAmount = inventoryService.restockItemByID(id, request.getUnitsAddedDouble());
            if (newAmount >= 0) {
                return new ResponseEntity<>(newAmount, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/api/inventory/restock/{itemName:(?!^\\d+$)^.+$}", method = RequestMethod.POST)
    ResponseEntity<?> restockItemByName(@PathVariable(value = "itemName") String itemName, @RequestBody RestockRequest request) {
        try {
            double newAmount = inventoryService.restockItemByName(itemName, request.getUnitsAddedDouble());
            if (newAmount >= 0) {
                return new ResponseEntity<>(newAmount, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/api/inventory", method = RequestMethod.POST)
    ResponseEntity<?> addItemToInventory(@RequestBody InventoryItemRequest request) {
        try{
            InventoryItem item = new InventoryItem(request);
            InventoryItem savedItem = inventoryService.addItemToInventory(item);
            return new ResponseEntity<>(savedItem, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/api/inventory/{itemName:(?!^\\d+$)^.+$}", method = RequestMethod.POST)
    ResponseEntity<?> addItemToInventory(@PathVariable(value = "itemName") String itemName) {
        try {
            InventoryItem savedItem = inventoryService.addItemToInventory(itemName);
            return new ResponseEntity<>(savedItem, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/api/inventory/{id:^[0-9]+$}", method = RequestMethod.DELETE)
    ResponseEntity<?> removeItemFromInventoryByID(@PathVariable(value = "id") int id) {
        try {
            int rowsChanged = inventoryService.removeItemFromInventoryByID(id);
            if (rowsChanged > 0) {
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @RequestMapping(value = "/api/inventory/{itemName:(?!^\\d+$)^.+$}", method = RequestMethod.DELETE)
    ResponseEntity<?> removeItemFromInventoryByName(@PathVariable(value = "itemName") String itemName) {
        try {
            int rowsChanged = inventoryService.removeItemFromInventoryByName(itemName);
            if (rowsChanged > 0) {
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/api/inventory/{id:^[0-9]+$}", method = RequestMethod.PUT)
    ResponseEntity<?> updateItemInInventoryByID(@PathVariable(value = "id") int id, @RequestBody InventoryItemRequest request) {
        try {
            InventoryItem item = new InventoryItem(request);
            InventoryItem updatedItem = inventoryService.updateItemInInventoryByID(id, item);
            if (updatedItem != null) {
                return new ResponseEntity<>(updatedItem, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @RequestMapping(value = "/api/inventory/{itemName:(?!^\\d+$)^.+$}", method = RequestMethod.PUT)
    ResponseEntity<?> updateItemInInventoryByName(@PathVariable(value = "itemName") String itemName, @RequestBody InventoryItemRequest request) {
        try{
            InventoryItem item = new InventoryItem(request);
            InventoryItem updatedItem = inventoryService.updateItemInInventoryByName(itemName, item);
            if (updatedItem != null) {
                return new ResponseEntity<>(updatedItem, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}