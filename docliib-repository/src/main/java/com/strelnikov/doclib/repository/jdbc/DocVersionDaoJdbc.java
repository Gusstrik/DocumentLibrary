package com.strelnikov.doclib.repository.jdbc;

import com.strelnikov.doclib.model.documnets.Document;
import com.strelnikov.doclib.model.documnets.DocumentFile;
import com.strelnikov.doclib.model.documnets.DocumentVersion;
import com.strelnikov.doclib.model.documnets.Importance;
import com.strelnikov.doclib.repository.DocVersionDao;
import com.strelnikov.doclib.repository.DocFileDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


@Repository
@Slf4j
public class DocVersionDaoJdbc implements DocVersionDao {

    @Autowired
    private DocFileDao fileDao;

    private final DataSource dataSource;

    public DocVersionDaoJdbc(@Autowired DataSource dataSource) {
        this.dataSource = dataSource;
    }

//    private final String VERSION_LOAD_QUERY = "SELECT * FROM documents_versions WHERE " +
//            "document_id = ? AND version = ?;";
//
//    @Override
//    public DocumentVersion loadDocVersion(int documentId, int docVersion) {
//        DocumentVersion documentVersion = null;
//        try (Connection connection = dataSource.getConnection()) {
//            PreparedStatement statement = connection.prepareStatement(VERSION_LOAD_QUERY);
//            statement.setInt(1, documentId);
//            statement.setInt(2, docVersion);
//            ResultSet rs = statement.executeQuery();
//            if (rs.next()) {
//                documentVersion = new DocumentVersion();
//                documentVersion.setVersion(rs.getInt("version"));
//                documentVersion.setModerated(rs.getBoolean("is_moderated"));
//                documentVersion.setDescription(rs.getString("description"));
//                documentVersion.setImportance(Importance.valueOf(rs.getString("importance")));
//                documentVersion.setFilesList(fileDao.getFilesList(getDocVersionId(documentVersion, documentId)));
//            }
//
//        } catch (SQLException e) {
//            log.error(e.getMessage(), e);
//        }
//        return documentVersion;
//    }

    private final String VERSION_ADD_QUERY = "INSERT INTO documents_versions " +
            "VALUES (nextval('documents_versions_id_seq'),?,?,?,?,?) RETURNING id;";

    @Override
    public DocumentVersion insertDocVersion(DocumentVersion documentVersion) {
        try (Connection connection = dataSource.getConnection()){
            PreparedStatement statement = connection.prepareStatement(VERSION_ADD_QUERY);
            statement.setInt(1,documentVersion.getDocumentId());
            statement.setInt(2,documentVersion.getVersion());
            statement.setString(3,documentVersion.getDescription());
            statement.setString(4,documentVersion.getImportance().toString());
            statement.setBoolean(5,documentVersion.isModerated());
            ResultSet rs = statement.executeQuery();
            if(rs.next()){
                documentVersion.setId(rs.getInt(1));
            }
            for (DocumentFile file:documentVersion.getFilesList()){
                file.setDocVersionId(documentVersion.getId());
                fileDao.insertFile(file);
            }
        }catch (SQLException e){
            log.error(e.getMessage(),e);
        }
        return documentVersion;
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

    private final String VERSION_GET_LIST_QUERY = "SELECT * FROM documents_versions WHERE document_id=?";

    @Override
    public List<DocumentVersion> getDocVersionList(Document document) {
        List<DocumentVersion> list = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()){
            PreparedStatement statement=connection.prepareStatement(VERSION_GET_LIST_QUERY);
            statement.setInt(1,document.getId());
            ResultSet rs = statement.executeQuery();
            while(rs.next()){
                DocumentVersion docVersion = new DocumentVersion();
                docVersion.setId(rs.getInt(1));
                docVersion.setDocumentId(rs.getInt(2));
                docVersion.setVersion(rs.getInt(3));
                docVersion.setDescription(rs.getString(4));
                docVersion.setImportance(Importance.valueOf(rs.getString(5)));
                docVersion.setModerated(rs.getBoolean(6));
                list.add(docVersion);
            }
        }catch (SQLException e){
            log.error(e.getMessage(),e);
        }
        return list;
    }


}
