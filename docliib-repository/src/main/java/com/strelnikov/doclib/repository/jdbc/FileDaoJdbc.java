package com.strelnikov.doclib.repository.jdbc;

import com.strelnikov.doclib.repository.FileDao;
import com.strelnikov.doclib.model.documnets.DocumentFile;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class FileDaoJdbc implements FileDao {


    private final String FILE_ADD_QUERY = "INSERT INTO file VALUES" +
            "(nextval('file_id_seq'),?,?,?)";

    @Override
    public void addNewFile(String fileName, int documentId, String filePath) {
        try (Connection connection = DatabaseConnectorJdbc.getConnectionFromPool()) {
            PreparedStatement statement = connection.prepareStatement(FILE_ADD_QUERY);
            statement.setString(1, fileName);
            statement.setInt(2, documentId);
            statement.setString(3, filePath);
            statement.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            log.error(e.getMessage(), e);

        }
    }

    private final String FILE_DELETE_QUERY = "DELETE FROM file where name = ?";

    @Override
    public void deleteFile(String name) {
        try (Connection connection = DatabaseConnectorJdbc.getConnectionFromPool()) {
            PreparedStatement statement = connection.prepareStatement(FILE_DELETE_QUERY);
            statement.setString(1, name);
            statement.executeUpdate();
        } catch (SQLException e) {

            log.error(e.getMessage(), e);

        }

    }

    private final String FILE_LOAD_QUERY = "SELECT name, path FROM file WHERE name=?";

   @Override
    public DocumentFile loadFile(String name) {
       DocumentFile documentFile=null;
        try (Connection connection = DatabaseConnectorJdbc.getConnectionFromPool()) {
            PreparedStatement statement = connection.prepareStatement(FILE_LOAD_QUERY);
            statement.setString(1, name);
            ResultSet rs = statement.executeQuery();
            if(rs.next()){
                documentFile= new DocumentFile(rs.getString(1),rs.getString(2));
            }
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
        return documentFile;
    }

    private final String FILE_GET_DOCUMNET_LIST_QUERY = "SELECT name, path from file where document_id=?";

    @Override
    public List<DocumentFile> getFilesList(int document_id) {
        List<DocumentFile> list = new ArrayList();
        try (Connection connection = DatabaseConnectorJdbc.getConnectionFromPool()) {
            PreparedStatement statement = connection.prepareStatement(FILE_GET_DOCUMNET_LIST_QUERY);
            statement.setInt(1, document_id);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                list.add(new DocumentFile(rs.getString(1), rs.getString(2)));
            }
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
        return list;
    }

    private final String FILE_COPY_IN_TABLE_QUERY = "INSERT INTO file(id,name,document_id,path)\n" +
            "VALUES (nextval('file_id_seq',?,?,?));";

    private void copyFile(DocumentFile docFile, int docId) {
        try (Connection connection = DatabaseConnectorJdbc.getConnectionFromPool()) {
            PreparedStatement statement = connection.prepareStatement(FILE_COPY_IN_TABLE_QUERY);
            statement.setString(1, docFile.getFileName());
            statement.setInt(2, docId);
            statement.setString(3, docFile.getFilePath());
            statement.executeQuery();
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public void copyFilesToNewDoc(List<DocumentFile> fileList, int docId) {
        for (DocumentFile df : fileList) {
            copyFile(df, docId);
        }
    }
}

