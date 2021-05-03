package com.strelnikov.doclib.repository.jdbc;

import com.strelnikov.doclib.model.documnets.DocumentVersion;
import com.strelnikov.doclib.model.documnets.Importance;
import com.strelnikov.doclib.repository.DocVersionDao;
import com.strelnikov.doclib.repository.FileDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;


@Repository
@Slf4j
public class DocVersionDaoJdbc implements DocVersionDao {

    @Autowired
    private FileDao fileDao;

    private final DataSource dataSource;

    public DocVersionDaoJdbc(@Autowired DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private final String VERSION_LOAD_QUERY = "SELECT * FROM documents_versions WHERE " +
            "document_id = ? AND version = ?;";

    @Override
    public DocumentVersion loadDocVersion(int documentId, int docVersion) {
        DocumentVersion documentVersion = null;
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(VERSION_LOAD_QUERY);
            statement.setInt(1, documentId);
            statement.setInt(2, docVersion);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                documentVersion = new DocumentVersion();
                documentVersion.setVersion(rs.getInt("version"));
                documentVersion.setModerated(rs.getBoolean("is_moderated"));
                documentVersion.setDescription(rs.getString("description"));
                documentVersion.setImportance(Importance.valueOf(rs.getString("importance")));
                documentVersion.setFilesList(fileDao.getFilesList(getDocVersionId(documentVersion, documentId)));
            }

        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
        return documentVersion;
    }

    private final String VERSION_ADD_QUERY = "INSERT INTO documents_versions " +
            "VALUES (nextval('documents_versions_id_seq'),?,?,?,?,?);";

    @Override
    public void addNewDocVersion(DocumentVersion documentVersion, int documentId) {
        try (Connection connection = dataSource.getConnection()){
            PreparedStatement statement = connection.prepareStatement(VERSION_ADD_QUERY);
            statement.setInt(1,documentId);
            statement.setInt(2,documentVersion.getVersion());
            statement.setString(3,documentVersion.getDescription());
            statement.setString(4,documentVersion.getImportance().toString());
            statement.setBoolean(5,documentVersion.isModerated());
            statement.executeUpdate();
        }catch (SQLException e){
            log.error(e.getMessage(),e);
        }
    }

    private final String VERSION_DELETE_QUERY = "DELETE FROM documents_versions WHERE id = ?; ";

    @Override
    public void deleteDocVersion(int versionId) {
        try (Connection connection = dataSource.getConnection()){
            PreparedStatement statement = connection.prepareStatement(VERSION_DELETE_QUERY);
            statement.setInt(1,versionId);
            statement.executeUpdate();
        }catch (SQLException e){
            log.error(e.getMessage(),e);
        }
    }

    private final String VERSION_GET_ID_QUERY = "SELECT id FROM documents_versions WHERE " +
            "(version = ?) AND (document_id = ?);";

    @Override
    public int getDocVersionId(DocumentVersion documentVersion, int documentId) {
        int docVersionId = -1;
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(VERSION_GET_ID_QUERY);
            statement.setInt(1, documentVersion.getVersion());
            statement.setInt(2, documentId);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                docVersionId = rs.getInt("id");
            }

        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
        return docVersionId;
    }
}
