package com.strelnikov.doclib.repository.jdbc;

import com.strelnikov.doclib.model.conception.UnitType;
import com.strelnikov.doclib.repository.CatalogDao;
import com.strelnikov.doclib.model.conception.Unit;
import com.strelnikov.doclib.model.catalogs.Catalog;
import com.strelnikov.doclib.repository.DocumentDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository("CatalogJdbc")
@Slf4j
public class CatalogDaoJdbc implements CatalogDao {

    private final DataSource dataSource;


    public CatalogDaoJdbc(@Autowired DataSource dataSource) {
        this.dataSource = dataSource;
    }
    @Autowired
    @Qualifier("DocumentJdbc")
    private DocumentDao documentDao;


    private final String CATALOG_INSERT_QUERY = "INSERT INTO catalogs VALUES" +
            "(nextval('catalogs_id_seq'),?,?) RETURNING id";

    @Override
    public Catalog insertCatalog(Catalog catalog) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(CATALOG_INSERT_QUERY);
            statement.setString(1, catalog.getName());
            statement.setInt(2, catalog.getCatalogId());
            ResultSet rs = statement.executeQuery();
            if(rs.next()){
                catalog.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
        return catalog;
    }

    private final String CATALOG_UPDATE_QUERY = "UPDATE catalogs SET name=?, catalog_id=? WHERE id = ?;";

    @Override
    public void updateCatalog(Catalog catalog) {
        try(Connection connection = dataSource.getConnection()){
            PreparedStatement statement = connection.prepareStatement(CATALOG_UPDATE_QUERY);
            statement.setString(1,catalog.getName());
            statement.setInt(2,catalog.getCatalogId());
            statement.setInt(3,catalog.getId());
            statement.executeUpdate();
        }catch (SQLException e){
            log.error(e.getMessage(),e);
        }
    }

    private final String CATALOG_DELETE_QUERY =
            "DELETE FROM catalogs where (id = ?) or (catalog_id = ?)";

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
            "SELECT id, name FROM catalogs WHERE catalog_id =?";

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
                unit.setCatalogId(catalogId);
                list.add(unit);
            }
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
        return list;
    }




    private List<Unit> getContentList(int catalogId) {
        List<Unit> list = getListCatalogs(catalogId);
        list.addAll(documentDao.getDocumentsList(catalogId));
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
                catalog.setCatalogId(rs.getInt(3));
                catalog.setContentList(getContentList(catalogId));
            }
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
        return catalog;
    }

}
