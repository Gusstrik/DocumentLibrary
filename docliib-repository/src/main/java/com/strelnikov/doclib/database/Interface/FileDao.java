package com.strelnikov.doclib.database.Interface;

import com.strelnikov.doclib.database.jdbc.DatabaseConnectorJdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public interface FileDao {
    void addNewFile(String fileName, int documentId, String filePath);

    void deleteFile(String name);

    void renameFile(String newName, String oldName);

    ArrayList<String> getFilesList(int document_id);
}
