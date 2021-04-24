
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import postgresdatabase.*;

import java.sql.*;
import java.util.ArrayList;


public class PostgresTest {
    private static final Logger log;

    static {
        log = LoggerFactory.getLogger("PostgresTest");
    }

    @Test
    public void connectAndCreationTest() {
        DatabaseCreator creator = new DatabaseCreator();
        creator.createDatabse();
        String homeCatName = "";
        log.info("Database was successfully created");
        try {
            Connection connection = DatabaseConnector.getConnectionFromPool();
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("Select name from catalog where name='/'");
            rs.next();
            homeCatName = rs.getString(1);
        } catch (SQLException e) {
            log.error(e.getMessage(), e.getSQLState());
        }
        Assert.assertEquals("/", homeCatName);
    }

    @Test
    public void typeDaoTest() {
        TypeDao typeDao = new TypeDao();
        typeDao.addType("order");
        typeDao.addType("mail");
        typeDao.addType("fax");
        typeDao.deleteType("order");
        ArrayList<String> expected = new ArrayList();
        expected.add("mail");
        expected.add("fax");
        ArrayList<String> actual = typeDao.getTypesList();
        typeDao.deleteType("mail");
        typeDao.deleteType("fax");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void fileDaoTest() {
        FileDao fileDao = new FileDao();
        fileDao.addNewFile("simple", 1, "x");
        fileDao.addNewFile("tests", 1, "y");
        fileDao.addNewFile("files", 1, "z");
        fileDao.deleteFile("tests");
        fileDao.renameFile("SIMPLEST", "simple");
        ArrayList<String> expected = new ArrayList();
        expected.add("files");
        expected.add("SIMPLEST");
        ArrayList<String> actual = fileDao.getFilesList(1);
        fileDao.deleteFile("SIMPLEST");
        fileDao.deleteFile("tests");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void DocumentDaoTest() {
        DocumentDao documentDao = new DocumentDao();
        documentDao.addNewDocument("test_doc1", "test_type", 1, "test desciption", 1);
        documentDao.addNewDocuemnt("test_doc1", "test_type", 2);
        documentDao.addNewDocuemnt("test_doc1", 3, "test_type", 1);
        documentDao.deleteDocument(documentDao.getDocumentId("test_doc1","test_type",2));
        ArrayList<Integer> actual = new ArrayList();
        ArrayList<Integer> expected = new ArrayList();
        expected.add(1);
        expected.add(3);
        try {
            Connection connection = DatabaseConnector.getConnectionFromPool();
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT version where name='test_doc1';");
            while (rs.next()) {
                actual.add(rs.getInt(1));
            }
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
        Assert.assertEquals(expected,actual);
    }

}
