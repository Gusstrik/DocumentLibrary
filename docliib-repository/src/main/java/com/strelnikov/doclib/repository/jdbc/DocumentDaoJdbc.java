package com.strelnikov.doclib.repository.jdbc;

import com.strelnikov.doclib.model.conception.Unit;
import com.strelnikov.doclib.model.conception.UnitType;
import com.strelnikov.doclib.model.documnets.DocumentVersion;
import com.strelnikov.doclib.repository.DocVersionDao;
import com.strelnikov.doclib.repository.DocumentDao;
import com.strelnikov.doclib.model.documnets.Document;
import com.strelnikov.doclib.model.documnets.DocumentType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
public class DocumentDaoJdbc implements DocumentDao {

    private final DataSource dataSource;

    public DocumentDaoJdbc(@Autowired DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Autowired
    private DocVersionDao docVersionDao;

    private final String DOCUMENT_ADD_QUERY = "INSERT INTO documents VALUES (nextval('documents_id_seq'),?,?,?,?)" +
            "RETURNING id;";

    @Override
    public Document insertDocument(Document document) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(DOCUMENT_ADD_QUERY);
            statement.setString(1, document.getName());
            statement.setString(2, document.getDocumentType().getCurentType());
            statement.setInt(3, document.getActualVersion());
            statement.setInt(4, document.getParent_id());
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                document.setId(rs.getInt(1));
            }
            for(DocumentVersion docVersion:document.getVersionsList()){
                docVersion.setDocumentId(document.getId());
                docVersionDao.insertDocVersion(docVersion);
            }
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
        return document;
    }

    private final String DOCUMENT_UPDATE_QUERY = "UPDATE documents SET name=?, type=?, " +
            "actual_version = ?, catalog_id = ? WHERE id = ?;";

    @Override
    public void updateDocument(Document document) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(DOCUMENT_UPDATE_QUERY);
            statement.setString(1, document.getName());
            statement.setString(2, document.getDocumentType().getCurentType());
            statement.setInt(3, document.getActualVersion());
            statement.setInt(4, document.getParent_id());
            statement.setInt(5, document.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
    }

    private final String CATALOG_SHOW_DOCUMENTS =
            "SELECT id,name from documents where catalog_id =?";

    @Override
    public List<Unit> getDocumentsList(int catalogId) {
        List<Unit> list = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(CATALOG_SHOW_DOCUMENTS);
            statement.setInt(1, catalogId);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Unit unit = new Document();
                unit.setId(rs.getInt(1));
                unit.setName(rs.getString(2));
                unit.setUnitType(UnitType.DOCUMENT);
                unit.setParent_id(catalogId);
                list.add(unit);
            }
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
        return list;
    }

    private final String DOCUMENT_DELETE_QUERY = "DELETE FROM documents WHERE id=?";

    @Override
    public void deleteDocument(int id) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(DOCUMENT_DELETE_QUERY);
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
    }

    private final String DOCUMENT_LOAD_QUERY = "SELECT * FROM documents WHERE id = ?";

    @Override
    public Document loadDocument(int docId) {
        Document document = null;
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(DOCUMENT_LOAD_QUERY);
            statement.setInt(1, docId);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                document = new Document();
                document.setId(rs.getInt(1));
                document.setName(rs.getString(2));
                document.setDocumentType(new DocumentType(rs.getString(3)));
                document.setActualVersion(rs.getInt(4));
                document.setParent_id(rs.getInt(5));
                document.setVersionsList(docVersionDao.getDocVersionList(document));
            }

        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
        return document;
    }

//    private final String DOCUMENT_GET_ID_QUERY = "SELECT id FROM document WHERE name=? AND version=? AND type=?;";
//
//    @Override
//    public int getDocumentId(Document document) {
//        int id = -1;
//        try (Connection connection = dataSource.getConnection()) {
//            PreparedStatement statement = connection.prepareStatement(DOCUMENT_GET_ID_QUERY);
//            statement.setString(1, document.getName());
//            statement.setInt(2, document.getActualVersion());
//            statement.setString(3, document.getDocumentType().getCurentType());
//            ResultSet rs = statement.executeQuery();
//            if (rs.next()) {
//                id = rs.getInt(1);
//            }
//        } catch (SQLException e) {
//            log.error(e.getMessage(), e);
//        }
//        return id;
//    }
}
