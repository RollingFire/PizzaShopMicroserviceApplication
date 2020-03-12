package com.austindorsey.customermicroservice.controller;

import com.austindorsey.customermicroservice.model.Customer;
import com.austindorsey.customermicroservice.services.CustomerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerServiceRESTController {
    
    @Autowired
    private CustomerService customerService;

    @RequestMapping(value = "/customer", method = RequestMethod.GET)
    ResponseEntity<?> getInventory(@RequestParam(required=false) String firstName, 
                                   @RequestParam(required=false) String lastName) {
        try {
            Customer[] customers;
            if (firstName == null && lastName == null) {
                customers = customerService.getCustomers();
            } else {
                customers = customerService.getCustomersByName(firstName, lastName);
            }
            return new ResponseEntity<>(customers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/customer/{id:^[0-9]+$}", method = RequestMethod.GET)
    ResponseEntity<?> getCustomerByID(@PathVariable(value = "id") int id) {
        try {
            Customer customer = customerService.getCustomerByID(id);
            if (customer == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(customer, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/customer", method = RequestMethod.POST)
    ResponseEntity<?> addCustomer(@RequestParam(required=false) String firstName, 
                                  @RequestParam(required=false) String lastName) {
        try {
            Customer customers = customerService.addCustomer(firstName, lastName);
            return new ResponseEntity<>(customers, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/customer/{id:^[0-9]+$}", method = RequestMethod.POST)
    ResponseEntity<?> incrementOrderCount(@PathVariable(value = "id") int id) {
        try {
            int newNumberOfOrders = customerService.incrementOrderCount(id);
            if (newNumberOfOrders == -1) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(newNumberOfOrders, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/customer/{id:^[0-9]+$}", method = RequestMethod.DELETE)
    ResponseEntity<?> deleteCustomer(@PathVariable(value = "id") int id) {
        try {
            int numberOfRowsChanged = customerService.deleteCustomer(id);
            if (numberOfRowsChanged == 0) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/customer/{id:^[0-9]+$}", method = RequestMethod.PUT)
    ResponseEntity<?> changeName(@PathVariable(value = "id") int id,
                                 @RequestParam(required=false) String firstName, 
                                 @RequestParam(required=false) String lastName) {
        try {
            String newName = customerService.changeName(id, firstName, lastName);
            if (newName != null) {
                return new ResponseEntity<>(newName, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}