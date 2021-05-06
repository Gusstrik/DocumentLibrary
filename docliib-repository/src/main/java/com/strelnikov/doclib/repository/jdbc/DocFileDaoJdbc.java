package com.strelnikov.doclib.repository.jdbc;

import com.strelnikov.doclib.model.documnets.DocumentVersion;
import com.strelnikov.doclib.repository.DocFileDao;
import com.strelnikov.doclib.model.documnets.DocumentFile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository("DocFileJdbc")
public class DocFileDaoJdbc implements DocFileDao {

    private final DataSource dataSource;

    public DocFileDaoJdbc(@Autowired DataSource dataSource){
        this.dataSource = dataSource;
    }

    private final String FILE_ADD_QUERY = "INSERT INTO doc_files VALUES" +
            "(nextval('doc_files_id_seq'),?,?,?) RETURNING id";

    @Override
    public DocumentFile insertFile(DocumentFile file) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(FILE_ADD_QUERY);
            statement.setString(1, file.getFileName());
            statement.setInt(2, file.getDocVersion().getId());
            statement.setString(3, file.getFilePath());
            ResultSet rs = statement.executeQuery();
            if(rs.next()){
                file.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
        return file;
    }

    private final String FILE_DELETE_QUERY = "DELETE FROM doc_files where id = ?";


    @Override
    public void deleteFile(int fileId) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(FILE_DELETE_QUERY);
            statement.setInt(1, fileId);
            statement.executeUpdate();
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
    }

//    private final String FILE_LOAD_QUERY = "SELECT name, path FROM file WHERE id=?";

//   @Override
//    public DocumentFile loadFile(int fileId) {
//       DocumentFile documentFile=null;
//        try (Connection connection = dataSource.getConnection()) {
//            PreparedStatement statement = connection.prepareStatement(FILE_LOAD_QUERY);
//            statement.setInt(1, fileId);
//            ResultSet rs = statement.executeQuery();
//            if(rs.next()){
//                documentFile= new DocumentFile(rs.getString(1),rs.getString(2));
//            }
//        } catch (SQLException e) {
//            log.error(e.getMessage(), e);
//        }
//        return documentFile;
//    }

    private final String FILE_GET_LIST_QUERY = "SELECT * from doc_files where document_id=?";

    @Override
    public List<DocumentFile> getFilesList(DocumentVersion documentVersion) {
        List<DocumentFile> list = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(FILE_GET_LIST_QUERY);
            statement.setInt(1, documentVersion.getId());
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                DocumentFile documentFile=new DocumentFile();
                documentFile.setId(rs.getInt(1));
                documentFile.setFileName(rs.getString(2));
                documentFile.setFilePath(rs.getString(3));
                list.add(documentFile);
            }
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
        return list;
    }


//    private final String FILE_COPY_IN_TABLE_QUERY = "INSERT INTO file(id,name,document_id,path)\n" +
//            "VALUES (nextval('file_id_seq'),?,?,?);";
//
//    private void copyFile(DocumentFile docFile, int docId) {
//        try (Connection connection = dataSource.getConnection()) {
//            PreparedStatement statement = connection.prepareStatement(FILE_COPY_IN_TABLE_QUERY);
//            statement.setString(1, docFile.getFileName());
//            statement.setInt(2, docId);
//            statement.setString(3, docFile.getFilePath());
//            statement.executeQuery();
//        } catch (SQLException e) {
//            log.error(e.getMessage(), e);
//        }
//    }
//
//    @Override
//    public void copyFilesToNewDoc(DocumentVersion documentVersion) {
//        for (DocumentFile df : documentVersion.getFilesList()) {
//            copyFile(df, docVersionDao.getDocVersionId(documentVersion));
//        }
//    }
}

