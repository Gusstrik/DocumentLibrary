package com.strelnikov.doclib.repository.jdbc;

import com.strelnikov.doclib.repository.DocumentDao;
import com.strelnikov.doclib.model.documnets.Document;
import com.strelnikov.doclib.model.documnets.DocumentType;
import com.strelnikov.doclib.model.documnets.DocumentVersion;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.ArrayList;

@Slf4j
public class DocumentDaoJdbc implements DocumentDao {

    private final String DOCUMENT_ADD_QUERY = "INSERT INTO document VALUES" +
            "(nextval('document_id_seq'),?,?,?,?,?,false);";

    @Override
    public void addNewDocument(Document document, int catalogId) {
        try (Connection connection = DatabaseConnectorJdbc.getConnectionFromPool();) {
            PreparedStatement statement = connection.prepareStatement(DOCUMENT_ADD_QUERY);
            statement.setString(1, document.getName());
            DocumentVersion actualDocument = document.getDocumentVersion();
            statement.setString(2, document.getDocumentType().getCurentType());
            statement.setInt(3, document.getActualVersion());
            statement.setString(4, actualDocument.getDescription());
            statement.setInt(5, catalogId);
            statement.executeUpdate();
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
    }

    private final String DOCUMENT_GET_ID_QUERY = "SELECT id FROM document WHERE name=? AND version=? AND type=?;";

    @Override
    public int getDocumentId(Document document) {
        int id = -1;
        try (Connection connection = DatabaseConnectorJdbc.getConnectionFromPool()) {
            PreparedStatement statement = connection.prepareStatement("SELECT id FROM document WHERE name=? AND version=? AND type=?;");
            statement.setString(1, document.getName());
            statement.setInt(2, document.getActualVersion());
            statement.setString(3, document.getDocumentType().getCurentType());
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                id = rs.getInt(1);
            }
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
        return id;
    }

    private final String DOCUMENT_DELETE_QUERY = "DELETE FROM document WHERE id=?";

    @Override
    public void deleteDocument(int id) {
        try (Connection connection = DatabaseConnectorJdbc.getConnectionFromPool()) {
            PreparedStatement statement = connection.prepareStatement(DOCUMENT_DELETE_QUERY);
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
    }


    private final String DOCUMENT_LOAD_QUERY = "SELECT * FROM document WHERE name=? AND type=?";

    @Override
    public Document loadDocument(String name, String type) {
        Document document = null;
        try (Connection connection = DatabaseConnectorJdbc.getConnectionFromPool()) {
            PreparedStatement statement = connection.prepareStatement(DOCUMENT_LOAD_QUERY);
            statement.setString(1,name);
            statement.setString(2,type);
            ResultSet rs = statement.executeQuery();
            if (rs.next()){
                document =new Document(rs.getString(2));
                document.setDocumentType(new DocumentType(rs.getString(3)));
                document.setVersionsList(new ArrayList<DocumentVersion>());
                document.getVersionsList().add(new DocumentVersion(rs.getString(5),rs.getBoolean(7)));
                document.setActualVersion(rs.getInt(4));
            }
            while(rs.next()){
                document.getVersionsList().add(new DocumentVersion(rs.getString(5),rs.getBoolean(7)));
                document.setActualVersion(rs.getInt(4));
            }
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
        return document;
    }


}