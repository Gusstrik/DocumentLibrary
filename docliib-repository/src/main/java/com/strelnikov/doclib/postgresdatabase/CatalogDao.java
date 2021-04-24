package com.strelnikov.doclib.postgresdatabase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import com.strelnikov.doclib.service.conception.*;
import com.strelnikov.doclib.service.catalogs.Catalog;
import com.strelnikov.doclib.service.documnets.Documnet;

public class CatalogDao {
    private static final Logger log;

    static {
        log = LoggerFactory.getLogger(CatalogDao.class);
    }

    private final String CATALOG_ADD_QUERY = "INSERT INTO catalog VALUES" +
            "(nextval('catalog_id_seq'),?,?)";

    public void addNewCatalog(String name, String parent) {
        try {
            Connection connection = DatabaseConnector.getConnectionFromPool();
            PreparedStatement statement = connection.prepareStatement(CATALOG_ADD_QUERY);
            statement.setString(1, name);
            statement.setString(2, parent);
            statement.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
    }

    public void addNewCatalog(String name) {
        addNewCatalog(name, "/");
    }

    private final String CATALOG_DELETE_QUERY =
            "DELETE FROM catalog where (name = ?) or (parent = ?)";

    public void deleteCatalog(String name) {
        try {
            Connection connection = DatabaseConnector.getConnectionFromPool();
            PreparedStatement statement = connection.prepareStatement(CATALOG_DELETE_QUERY);
            statement.setString(1, name);
            statement.setString(2, name);
            statement.executeUpdate();
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
    }

    private final String CATALOG_SHOW_CATALOGS =
            "SELECT name from catalog where parent =?";

    private ArrayList<Entity> getListCatalogs(String curentCatalog) {
        ArrayList<Entity> list = new ArrayList();
        try {
            Connection connection = DatabaseConnector.getConnectionFromPool();
            PreparedStatement statement = connection.prepareStatement(CATALOG_SHOW_CATALOGS);
            statement.setString(1, curentCatalog);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                list.add(new Catalog(rs.getString(1)));
            }
            connection.close();
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
        return list;
    }

    private final String CATALOG_SHOW_DOCUMENTS =
            "SELECT name from document where catalog =?";

    private ArrayList<Entity> getListDocuments(int catalog_id) {
        ArrayList<Entity> list = new ArrayList();
        try {
            Connection connection = DatabaseConnector.getConnectionFromPool();
            PreparedStatement statement = connection.prepareStatement(CATALOG_SHOW_DOCUMENTS);
            statement.setInt(1, catalog_id);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                list.add(new Documnet(rs.getString(1)));
            }
            connection.close();
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
        return list;
    }

    public ArrayList<Entity> getContentList(String currentCatalog) {
        ArrayList<Entity> list = getListCatalogs(currentCatalog);
        list.addAll(getListDocuments(getCatalogId(currentCatalog)));
        return list;
    }

    private final String CATALOG_GET_ID =
            "SELECT id from catalog where name =?";
    public int getCatalogId(String name){
        int id=-1;
        try {
            Connection connection = DatabaseConnector.getConnectionFromPool();
            PreparedStatement statement = connection.prepareStatement(CATALOG_GET_ID);
            statement.setString(1, name);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                id=rs.getInt(1);
            }
            connection.close();
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
        return id;
    }
}
