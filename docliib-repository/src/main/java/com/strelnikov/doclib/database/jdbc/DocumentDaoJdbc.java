package com.strelnikov.doclib.database.jdbc;

import com.strelnikov.doclib.database.Interface.DocumentDao;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

@Slf4j
public class DocumentDaoJdbc implements DocumentDao {

    private final String DOCUMENT_ADD_QUERY = "INSERT INTO document VALUES" +
            "(nextval('document_id_seq'),?,?,?,?,?,false);";

    public void addNewDocument(String name, String type, int version, String description, int catalogId) {
        try {
            Connection connection = DatabaseConnectorJdbc.getConnectionFromPool();
            PreparedStatement statement = connection.prepareStatement(DOCUMENT_ADD_QUERY);
            statement.setString(1, name);
            statement.setString(2, type);
            statement.setInt(3,version);
            statement.setString(4,description);
            statement.setInt(5,catalogId);
            statement.executeUpdate();
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
    }
    public void addNewDocuemnt(String name, String type, int version, String description) {
        addNewDocument(name,type,version,description,1);
    }
    public void addNewDocuemnt(String name, int version, String type, int catalogId) {
        addNewDocument(name,type,version,"",catalogId);
    }
    public void addNewDocuemnt(String name, String type, int version){
        addNewDocument(name,type,version,"",1);
    }

    private final String DOCUMENT_GET_ID_QUERY="SELECT id FROM document WHERE name=? AND version=? AND type=?;";

    public int getDocumentId(String name, String type, int version){
        int id=-1;
        try {
            Connection connection = DatabaseConnectorJdbc.getConnectionFromPool();
//            Statement statement = connection.createStatement();
            PreparedStatement statement = connection.prepareStatement("SELECT id FROM document WHERE name=? AND version=? AND type=?;");
            statement.setString(1, name);
            statement.setInt(2,version);
            statement.setString(3,type);
//            ResultSet rs = statement.executeQuery("SELECT id FROM document WHERE name='"+name+"' AND version="+version+" AND type='"+type+"';");
            ResultSet rs = statement.executeQuery();
            if (rs.next()){
                id=rs.getInt(1);
            }
            connection.close();
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
        return id;
    }

    private final String DOCUMENT_DELETE_QUERY="DELETE FROM document WHERE id=?";

    public void deleteDocument(int id){
        try {
            Connection connection = DatabaseConnectorJdbc.getConnectionFromPool();
            PreparedStatement statement = connection.prepareStatement(DOCUMENT_DELETE_QUERY);
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
    }
}
