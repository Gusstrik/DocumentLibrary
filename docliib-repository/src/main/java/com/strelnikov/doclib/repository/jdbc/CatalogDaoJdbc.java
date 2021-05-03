package com.strelnikov.doclib.repository.jdbc;

import com.strelnikov.doclib.model.conception.UnitType;
import com.strelnikov.doclib.repository.CatalogDao;
import com.strelnikov.doclib.model.conception.Unit;
import com.strelnikov.doclib.model.catalogs.Catalog;
import com.strelnikov.doclib.model.documnets.Document;
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

@Repository
@Slf4j
public class CatalogDaoJdbc implements CatalogDao {

    private final DataSource dataSource;


    public CatalogDaoJdbc(@Autowired DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private final String CATALOG_INSERT_QUERY = "INSERT INTO catalogs VALUES" +
            "(nextval('catalogs_id_seq'),?,?) RETURNING id";

    @Override
    public Catalog insertCatalog(Catalog catalog) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(CATALOG_INSERT_QUERY);
            statement.setString(1, catalog.getName());
            statement.setInt(2, catalog.getParent_id());
            ResultSet rs = statement.executeQuery();
            if(rs.next()){
                catalog.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
        return catalog;
    }

    private final String CATALOG_UPDATE_QUERY = "UPDATE catalogs SET name=?, parent=? WHERE id = ?;";

    @Override
    public void updateCatalog(Catalog catalog) {
        try(Connection connection = dataSource.getConnection()){
            PreparedStatement statement = connection.prepareStatement(CATALOG_UPDATE_QUERY);
            statement.setString(1,catalog.getName());
            statement.setInt(2,catalog.getParent_id());
            statement.setInt(3,catalog.getId());
            statement.executeUpdate();
        }catch (SQLException e){
            log.error(e.getMessage(),e);
        }
    }

    private final String CATALOG_DELETE_QUERY =
            "DELETE FROM catalogs where (id = ?) or (parent = ?)";

    @Override
    public void deleteCatalog(int catalogId) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(CATALOG_DELETE_QUERY);
            statement.setInt(1, catalogId);
            statement.setInt(2, catalogId);
            statement.executeUpdate();
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
    }


    private final String CATALOG_SHOW_CATALOGS =
            "SELECT id, name FROM catalogs WHERE parent =?";

    private List<Unit> getListCatalogs(int catalogId) {
        List<Unit> list = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(CATALOG_SHOW_CATALOGS);
            statement.setInt(1, catalogId);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Unit unit = new Catalog();
                unit.setId(rs.getInt(1));
                unit.setName(rs.getString(2));
                unit.setUnitType(UnitType.CATALOG);
                unit.setParent_id(catalogId);
                list.add(unit);
            }
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
        return list;
    }

    private final String CATALOG_SHOW_DOCUMENTS =
            "SELECT id,name from documents where catalog_id =?";

    private List<Unit> getListDocuments(int catalog_id) {
        List<Unit> list = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(CATALOG_SHOW_DOCUMENTS);
            statement.setInt(1, catalog_id);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Unit unit = new Document();
                unit.setId(rs.getInt(1));
                unit.setName(rs.getString(2));
                unit.setUnitType(UnitType.DOCUMENT);
                unit.setParent_id(catalog_id);
                list.add(unit);
            }
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
        return list;
    }


    private List<Unit> getContentList(int catalogId) {
        List<Unit> list = getListCatalogs(catalogId);
        list.addAll(getListDocuments(catalogId));
        return list;
    }

    private final String CATALOG_LOAD_QUERY = "SELECT * FROM catalogs WHERE id=?";

    @Override
    public Catalog loadCatalog(int catalogId) {
        Catalog catalog = null;
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(CATALOG_LOAD_QUERY);
            statement.setInt(1, catalogId);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                catalog = new Catalog();
                catalog.setId(catalogId);
                catalog.setName(rs.getString(2));
                catalog.setParent_id(rs.getInt(3));
                catalog.setContentList(getContentList(catalogId));
            }
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
        return catalog;
    }

}
