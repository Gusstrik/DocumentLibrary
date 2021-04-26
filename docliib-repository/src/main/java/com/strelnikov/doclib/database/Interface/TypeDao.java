package com.strelnikov.doclib.database.Interface;

import com.strelnikov.doclib.database.jdbc.DatabaseConnectorJdbc;

import java.sql.*;
import java.util.ArrayList;

public interface TypeDao {
    void addType(String type);


    void deleteType(String type);


    ArrayList<String> getTypesList();
}
