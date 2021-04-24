package postgresdatabase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DocumentDao {
    private static final Logger log;
    static {
        log=LoggerFactory.getLogger(DocumentDao.class);
    }

    private final String DOCUMENT_ADD_QUERY = "INSERT INTO document VALUES" +
            "(nextval('document_id_seq'),?,?,?,?,?,false);";

    public void addNewDocument(String name, String type, int version, String description, int catalogId) {
        try {
            Connection connection = DatabaseConnector.getConnectionFromPool();
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

    private final String DOCUMENT_GET_ID_QUERY="SELECT id FROM document WHERE name= ? AND" +
            "version= ? AND type= ?";

    public int getDocumentId(String name, String type, int version){
        int id=-1;
        try {
            Connection connection = DatabaseConnector.getConnectionFromPool();
            PreparedStatement statement = connection.prepareStatement(DOCUMENT_DELETE_QUERY);
            statement.setString(1, name);
            statement.setInt(2,version);
            statement.setString(3,type);
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
            Connection connection = DatabaseConnector.getConnectionFromPool();
            PreparedStatement statement = connection.prepareStatement(DOCUMENT_DELETE_QUERY);
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
    }
}
