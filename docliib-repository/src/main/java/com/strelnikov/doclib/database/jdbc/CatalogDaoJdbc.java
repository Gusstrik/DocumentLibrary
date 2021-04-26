package com.strelnikov.doclib.database.jdbc;

import com.strelnikov.doclib.database.Interface.CatalogDao;
import com.strelnikov.doclib.model.conception.Unit;
import com.strelnikov.doclib.model.catalogs.Catalog;
import com.strelnikov.doclib.model.documnets.Documnet;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class CatalogDaoJdbc implements CatalogDao {

    private final String CATALOG_ADD_QUERY = "INSERT INTO catalog VALUES" +
            "(nextval('catalog_id_seq'),?,?)";

    @Override
    public void addNewCatalog(String name, String parent) {
        try (Connection connection = DatabaseConnectorJdbc.getConnectionFromPool()) {
            PreparedStatement statement = connection.prepareStatement(CATALOG_ADD_QUERY);
            statement.setString(1, name);
            statement.setString(2, parent);
            statement.executeUpdate();
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
    }

    public void addNewCatalog(String name) {
        addNewCatalog(name, "/");
    }

    private final String CATALOG_DELETE_QUERY =
            "DELETE FROM catalog where (name = ?) or (parent = ?)";

    @Override
    public void deleteCatalog(String name) {
        try (Connection connection = DatabaseConnectorJdbc.getConnectionFromPool()){
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

    private List<Unit> getListCatalogs(String curentCatalog) {
        List<Unit> list = new ArrayList();
        try (Connection connection = DatabaseConnectorJdbc.getConnectionFromPool()){
            PreparedStatement statement = connection.prepareStatement(CATALOG_SHOW_CATALOGS);
            statement.setString(1, curentCatalog);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                list.add(new Catalog(rs.getString(1)));
            }
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
        return list;
    }

    private final String CATALOG_SHOW_DOCUMENTS =
            "SELECT name from document where catalog =?";

    private List<Unit> getListDocuments(int catalog_id) {
        List<Unit> list = new ArrayList();
        try (Connection connection = DatabaseConnectorJdbc.getConnectionFromPool()){
            PreparedStatement statement = connection.prepareStatement(CATALOG_SHOW_DOCUMENTS);
            statement.setInt(1, catalog_id);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                list.add(new Documnet(rs.getString(1)));
            }
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
        return list;
    }

    @Override
    public List<Unit> getContentList(String currentCatalog) {
        List<Unit> list = getListCatalogs(currentCatalog);
        list.addAll(getListDocuments(getCatalogId(currentCatalog)));
        return list;
    }

    private final String CATALOG_GET_ID =
            "SELECT id from catalog where name =?";
    public int getCatalogId(String name){
        int id=-1;
        try (Connection connection = DatabaseConnectorJdbc.getConnectionFromPool()){
            PreparedStatement statement = connection.prepareStatement(CATALOG_GET_ID);
            statement.setString(1, name);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                id=rs.getInt(1);
            }
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
        return id;
    }
}
