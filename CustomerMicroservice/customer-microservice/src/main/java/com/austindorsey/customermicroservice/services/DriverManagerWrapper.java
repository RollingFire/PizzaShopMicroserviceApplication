package com.austindorsey.customermicroservice.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.springframework.stereotype.Service;

@Service
public class DriverManagerWrapper {
    public Connection getConnection(String url, String user, String password) throws SQLException
    {
        return DriverManager.getConnection(url, user, password);
    }
}