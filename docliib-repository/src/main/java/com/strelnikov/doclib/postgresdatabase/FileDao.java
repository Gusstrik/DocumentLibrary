package com.strelnikov.doclib.postgresdatabase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class FileDao {
    private static final Logger log;
    static {
        log = LoggerFactory.getLogger(FileDao.class);
    }

    private final String FILE_ADD_QUERY = "INSERT INTO file VALUES" +
            "(nextval('file_id_seq'),?,?,?)";

    public void addNewFile(String fileName, int documentId, String filePath) {
        try {
            Connection connection = DatabaseConnector.getConnectionFromPool();
            PreparedStatement statement = connection.prepareStatement(FILE_ADD_QUERY);
            statement.setString(1, fileName);
            statement.setInt(2, documentId);
            statement.setString(3,filePath);
            statement.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            log.error(e.getMessage(), e);

        }
    }

    private final String FILE_DELETE_QUERY = "DELETE FROM file where name = ?";

    public void deleteFile(String name) {
        try {
            Connection connection = DatabaseConnector.getConnectionFromPool();
            PreparedStatement statement = connection.prepareStatement(FILE_DELETE_QUERY);
            statement.setString(1,name);
            statement.executeUpdate();
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
    }

    private final String FILE_CHANGE_NAME_QUERY = "UPDATE file SET name=? where name = ?";

    public void renameFile(String newName, String oldName) {
        try {
            Connection connection = DatabaseConnector.getConnectionFromPool();
            PreparedStatement statement = connection.prepareStatement(FILE_CHANGE_NAME_QUERY);
            statement.setString(1,newName);
            statement.setString(2,oldName);
            statement.executeUpdate();
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
    }

    private final String FILE_GET_DOCUMNET_LIST_QUERY = "SELECT name from file where document_id=?";

    public ArrayList<String> getFilesList(int document_id) {
        ArrayList<String> list = new ArrayList();
        try {
            Connection connection = DatabaseConnector.getConnectionFromPool();
            PreparedStatement statement = connection.prepareStatement(FILE_GET_DOCUMNET_LIST_QUERY);
            statement.setInt(1,document_id);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                list.add(rs.getString(1));
            }
            connection.close();
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
        return list;
    }
}

