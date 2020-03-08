package com.austindorsey.customermicroservice.services;

import java.sql.SQLException;

import com.austindorsey.customermicroservice.model.Customer;

public interface CustomerService {
    public Customer[] getCustomers() throws SQLException, ClassNotFoundException;
    public Customer getCustomerByID(int id) throws SQLException, ClassNotFoundException;
    public Customer[] getCustomersByName(String firstName, String lastName) throws SQLException, ClassNotFoundException;
    public Customer addCustomer(String firstName, String lastName) throws SQLException, ClassNotFoundException;
    public int deleteCustomer(int id) throws SQLException, ClassNotFoundException;
    public int incrementOrderCount(int id) throws SQLException, ClassNotFoundException;
    public String changeName(int id, String firstName, String lastName) throws SQLException, ClassNotFoundException;
}