package com.austindorsey.customermicroservice.services;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.austindorsey.customermicroservice.model.Customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

@Service
@PropertySource("classpath:database.properties")
public class CustomerServiceIMPL implements CustomerService {

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
    @Value("${mysql.tableName.customers}")
    private String tableName;
    private Connection mysql;

    @Autowired private DriverManagerWrapper driverManagerWrapped;

    /**
     * Calls the MySQL server to get all customers.
     * @return Customer[] All customers retrived
     * @throws SQLException SQL errors
     * @throws ClassNotFroundException Only if com.mysql.cj.jdbc.Driver can not be found.
     */
    @Override
    public Customer[] getCustomers() throws SQLException, ClassNotFoundException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName;
            mysql = driverManagerWrapped.getConnection(url, dbUserName, dbPassword);
            Statement statement = mysql.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM " + tableName + ";");
            ArrayList<Customer> list = new ArrayList<>();
            while (result.next()) {
                int id = result.getInt("id");
                String firstName = result.getString("firstName");
                String lastName = result.getString("lastName");
                int numberOfOrders = result.getInt("numberOfOrders");
                Date memberSince = result.getDate("memberSince");
                Customer item = new Customer(id, firstName, lastName, numberOfOrders, memberSince);
                list.add(item);
            }
            return list.toArray(new Customer[list.size()]);
        } catch (SQLException e) {
            throw e;
        } finally {
            if (mysql != null) {
                mysql.close();
            }
        }
    }

    /**
     * Calls the MySQL server to get customer by customer id.
     * @param id Customer id to retrive.
     * @return Customer Returns null if not found.
     * @throws SQLException SQL errors
     * @throws ClassNotFroundException Only if com.mysql.cj.jdbc.Driver can not be found.
     */
    @Override
    public Customer getCustomerByID(int id) throws SQLException, ClassNotFoundException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName;
            mysql = driverManagerWrapped.getConnection(url, dbUserName, dbPassword);
            Customer item = null;
            Statement statement = mysql.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM " + tableName + " WHERE id='" + id + "';");
            if (result.next()) {
                String firstName = result.getString("firstName");
                String lastName = result.getString("lastName");
                int numberOfOrders = result.getInt("numberOfOrders");
                Date memberSince = result.getDate("memberSince");
                item = new Customer(id, firstName, lastName, numberOfOrders, memberSince);
            }
            return item;
        } catch (SQLException e) {
            throw e;
        } finally {
            mysql.close();
        }
    }
    
    /**
     * Calls the MySQL server to get customers by name.
     * @param searchFirstName First name to filter by.
     * @param searchFirstName Last name to filter by
     * @return Customer[] Customers returned after filtering by first and last name.
     * @throws SQLException SQL errors
     * @throws ClassNotFroundException Only if com.mysql.cj.jdbc.Driver can not be found.
     */
    @Override
    public Customer[] getCustomersByName(String searchFirstName, String searchLastName) throws SQLException, ClassNotFoundException {
        if (searchFirstName == null && searchLastName == null) {
            return getCustomers();
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName;
            mysql = driverManagerWrapped.getConnection(url, dbUserName, dbPassword);
            Statement statement = mysql.createStatement();
            ResultSet result;
            if (searchFirstName != null && searchLastName == null) {
                result = statement.executeQuery("SELECT * FROM " + tableName + " WHERE firstName LIKE '" + searchFirstName + "';");
            } else if (searchLastName != null && searchFirstName == null) {
                result = statement.executeQuery("SELECT * FROM " + tableName + " WHERE lastName LIKE '" + searchLastName + "';");
            } else {
                result = statement.executeQuery("SELECT * FROM " + tableName + " WHERE firstName LIKE '" + searchFirstName + "' AND lastName LIKE '" + searchLastName + "';");
            }
            ArrayList<Customer> list = new ArrayList<>();
            while (result.next()) {
                int id = result.getInt("id");
                String firstName = result.getString("firstName");
                String lastName = result.getString("lastName");
                int numberOfOrders = result.getInt("numberOfOrders");
                Date memberSince = result.getDate("memberSince");
                Customer item = new Customer(id, firstName, lastName, numberOfOrders, memberSince);
                list.add(item);
            }
            return list.toArray(new Customer[list.size()]);
        } catch (SQLException e) {
            throw e;
        } finally {
            if (mysql != null) {
                mysql.close();
            }
        }
    }
    
    /**
     * Calls the MySQL server to delete customers by id.
     * @param id Customer id to delete.
     * @return int Number of rows deleted
     * @throws SQLException SQL errors
     * @throws ClassNotFroundException Only if com.mysql.cj.jdbc.Driver can not be found.
     */
    @Override
    public int deleteCustomer(int id) throws SQLException, ClassNotFoundException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName;
            mysql = driverManagerWrapped.getConnection(url, dbUserName, dbPassword);
            Statement statement = mysql.createStatement();
            int rowsChanged = statement.executeUpdate("DELETE FROM " + tableName + " WHERE id='" + id + "';");
            return rowsChanged;
        } catch (SQLException e) {
            throw e;
        } finally {
            mysql.close();
        }
    }

    /**
     * Calls the MySQL server to increase the order cound of the customer
     * @param id Customer id to incriment the order cound of.
     * @return int New order cound of customer. Returns -1 if customer can't be found.
     * @throws SQLException SQL errors
     * @throws ClassNotFroundException Only if com.mysql.cj.jdbc.Driver can not be found.
     */
    @Override
    public int incrementOrderCount(int id) throws SQLException, ClassNotFoundException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName;
            mysql = driverManagerWrapped.getConnection(url, dbUserName, dbPassword);
            Statement statement = mysql.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM " + tableName + " WHERE id='" + id + "';");
            if (result.next()) {
                int newNumberOfOrders = result.getInt("numberOfOrders") + 1;
                statement.executeUpdate("UPDATE " + tableName + " SET numberOfOrders='" + newNumberOfOrders + "' WHERE id='" + id + "';");
                return newNumberOfOrders;
            }
            else {
                return -1;
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            mysql.close();
        }
    }

    /**
     * Calls the MySQL server to update the name of the customer
     * @param id Id of the customer whos name to update.
     * @param firstName New first name.
     * @param lastName New last name.
     * @return String New first and last name.
     * @throws SQLException SQL errors
     * @throws ClassNotFroundException Only if com.mysql.cj.jdbc.Driver can not be found.
     */
    @Override
    public String changeName(int id, String firstName, String lastName) throws SQLException, ClassNotFoundException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName;
            mysql = driverManagerWrapped.getConnection(url, dbUserName, dbPassword);
            Statement statement = mysql.createStatement();
            
            if (firstName != null) {
                int rowsChanged = statement.executeUpdate("UPDATE " + tableName + " SET firstName='" + firstName + "' WHERE id='" + id + "';");
                if (rowsChanged == 0) {
                    return null;
                }
            }

            if (lastName != null) {
                int rowsChanged = statement.executeUpdate("UPDATE " + tableName + " SET lastName='" + lastName + "' WHERE id='" + id + "';");
                if (rowsChanged == 0) {
                    return null;
                }
            }

            ResultSet result = statement.executeQuery("SELECT * FROM " + tableName + " WHERE id='" + id + "';");
            if (result.next()) {
                return (result.getString("firstName") != null ? result.getString("firstName") : "Last Name:") + 
                       (result.getString("lastName") != null ? " " + result.getString("lastName") : "");
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            mysql.close();
        }
    }

    /**
     * Calls the MySQL server to create a new customer.
     * @param firstName First name of the new customer.
     * @param lastName Last name of the new customer.
     * @return Customer Newly created customer.
     * @throws SQLException SQL errors
     * @throws ClassNotFroundException Only if com.mysql.cj.jdbc.Driver can not be found.
     */
    @Override
    public Customer addCustomer(String firstName, String lastName) throws SQLException, ClassNotFoundException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName;
            mysql = driverManagerWrapped.getConnection(url, dbUserName, dbPassword);
            Statement statement = mysql.createStatement();
            if (firstName != null && lastName == null) {
                statement.executeUpdate("INSERT INTO " + tableName + " (firstName) VALUES ('" + firstName + "');");
            } else if (lastName != null && firstName == null) {
                statement.executeUpdate("INSERT INTO " + tableName + " (lastName) VALUES ('" + lastName + "');");
            } else if (firstName != null && lastName != null) {
                statement.executeUpdate("INSERT INTO " + tableName + " (firstName, lastName) VALUES ('" + firstName + "', '" + lastName + "');");
            } else {
                statement.executeUpdate("INSERT INTO " + tableName + " () VALUES ();");
            }
            Customer[] customersByThatName = getCustomersByName(firstName, lastName);
            return customersByThatName[customersByThatName.length-1];
        } catch (SQLException e) {
            throw e;
        } finally {
            mysql.close();
        }
    }
}