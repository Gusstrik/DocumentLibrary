package com.strelnikov.doclib.repository.jdbc;

import com.strelnikov.doclib.model.roles.*;
import com.strelnikov.doclib.repository.CatalogDao;
import com.strelnikov.doclib.repository.DocFileDao;
import com.strelnikov.doclib.repository.DocumentDao;
import com.strelnikov.doclib.repository.PermissionDao;
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
public class CheckPermissionDaoJdbc implements PermissionDao {

    @Autowired
    private DataSource dataSource;
    @Autowired
    private CatalogDao catalogDao;
    @Autowired
    private DocumentDao documentDao;
    @Autowired
    private DocFileDao docFileDao;

    private final String GET_CLASS_QUERY = "SELECT id FROM sec_object_classes WHERE name = ?";

    @Override
    public Integer getClassId(Class clazz) {
        Integer id = null;
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(GET_CLASS_QUERY);
            statement.setString(1, clazz.getSimpleName());
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                id = rs.getInt(1);
            }
        } catch (SQLException throwables) {
            log.error(throwables.getMessage(), throwables);
        }
        return id;
    }

    private final String GET_OBJECT_QUERY = "SELECT id FROM sec_object WHERE object_table_id = ? AND class_id = ?";

    @Override
    public Integer getObjectSecureId(SecuredObject securedObject) {
        Integer id = null;
        Integer classId = getClassId(securedObject.getClass());
        if (classId != null) {
            try (Connection connection = dataSource.getConnection()) {
                PreparedStatement statement = connection.prepareStatement(GET_OBJECT_QUERY);
                statement.setInt(1, securedObject.getId());
                statement.setInt(2, classId);
                ResultSet rs = statement.executeQuery();
                if (rs.next()) {
                    id = rs.getInt(1);
                }
            } catch (SQLException throwables) {
                log.error(throwables.getMessage(), throwables);
            }
        }
        return id;
    }

    private final String GET_PERMISSION_QUERY = "SELECT permission FROM sec_permission WHERE object_id=? " +
            "AND client_id = ?";

    @Override
    public boolean checkPermission(SecuredObject securedObject, Client client, PermissionType permissionType) {
        Integer id = getObjectSecureId(securedObject);
        int resultPermission = 0;
        if (id != null) {
            try (Connection connection = dataSource.getConnection()) {
                PreparedStatement statement = connection.prepareStatement(GET_PERMISSION_QUERY);
                statement.setInt(1, id);
                statement.setInt(2, client.getId());
                ResultSet rs = statement.executeQuery();
                if (rs.next()) {
                    resultPermission = rs.getInt(1);
                }
            } catch (SQLException throwables) {
                log.error(throwables.getMessage(), throwables);
            }
        }
        return permissionType.check(resultPermission);
    }

    private int getObjClassIdBySecureId(int secureId){
        int classId = 0;
        try (Connection connection = dataSource.getConnection()){
            PreparedStatement statement = connection.prepareStatement("SELECT class_id FROM sec_object WHERE id = ?");
            statement.setInt(1,secureId);
            ResultSet rs = statement.executeQuery();
            if(rs.next()) {
                classId = rs.getInt(1);
            }
        } catch (SQLException throwables) {
            log.error(throwables.getMessage(), throwables);
        }
        return classId;
    }

    private Class getObjClassByClassId(int classId){
        Class clazz = null;
        String className=null;
        int result = getObjClassIdBySecureId(classId);
        try (Connection connection = dataSource.getConnection()){
            PreparedStatement statement = connection.prepareStatement("SELECT name FROM sec_object_classes WHERE id = ?");
            statement.setInt(1,classId);
            ResultSet rs = statement.executeQuery();
            if(rs.next()){
               className=(rs.getString(1));
            }
        } catch (SQLException throwables) {
            log.error(throwables.getMessage(), throwables);
        }
        try {
            clazz = Class.forName("com.strelnikov.doclib.model.documnets."+className);
            return clazz;
        } catch (ClassNotFoundException e) {
            try {
                clazz = Class.forName("com.strelnikov.doclib.model.catalogs."+className);
                return clazz;
            } catch (ClassNotFoundException classNotFoundException) {
                log.error(classNotFoundException.getMessage(), classNotFoundException);
            }
        }
        return null;
    }

    private int getObjectIdBySecureId(int secureId){
        int objId = 0;
        try (Connection connection = dataSource.getConnection()){
            PreparedStatement statement = connection.prepareStatement("SELECT object_table_id FROM sec_object WHERE id = ?");
            statement.setInt(1,secureId);
            ResultSet rs = statement.executeQuery();
            if(rs.next()){
                objId = rs.getInt(1);
            }
        } catch (SQLException throwables) {
            log.error(throwables.getMessage(), throwables);
        }
        return objId;
    }

    @Override
    public  SecuredObject getSecuredObjectBySecureId(int secureId){
        int objId = getObjectIdBySecureId(secureId);
        Class clazz = getObjClassByClassId(secureId);
        switch (clazz.getSimpleName()){
            case "Catalog":
                return FactorySecuredObject.createSecuredObject(catalogDao.loadCatalog(objId));
            case "Document":
                return FactorySecuredObject.createSecuredObject(documentDao.loadDocument(objId));
            case "DocumentFile":
                return FactorySecuredObject.createSecuredObject(docFileDao.getFile(objId));
        }
        return null;
    }
    @Override
    public  SecuredObject getSecuredObjectByObjectName(String objectName, String type){
        switch (type){
            case "Catalog":
                return FactorySecuredObject.createSecuredObject(catalogDao.findCatalogByName(objectName));
            case "Document":
                return FactorySecuredObject.createSecuredObject(documentDao.findByName(objectName));
            case "DocumentFile":
                return FactorySecuredObject.createSecuredObject(docFileDao.getFile(objectName));
        }
        return null;
    }


    private final String GET_LIST_BY_CLIENT_PERMISSION_QUERY = "SELECT object_id, permission FROM sec_permission WHERE client_id=? ";

    @Override
    public List<Permission> getClientPermissions(Client client) {
        List<Permission> permissionList = new ArrayList<>();
            try (Connection connection = dataSource.getConnection()) {
                PreparedStatement statement = connection.prepareStatement(GET_LIST_BY_CLIENT_PERMISSION_QUERY);
                statement.setInt(1, client.getId());
                ResultSet rs = statement.executeQuery();
                while (rs.next()) {
                    Permission permission = new Permission();
                    permission.setClientId(rs.getInt(client.getId()));
                    permission.setSecuredObject(getSecuredObjectBySecureId(rs.getInt(1)));
                    permission.setPermissionList(new ArrayList<>());
                    int resultPermission = rs.getInt(2);
                    for (PermissionType type : PermissionType.values()) {
                        if (type.check(resultPermission)) {
                            permission.getPermissionList().add(type);
                        }
                    }
                    permissionList.add(permission);
                }
            } catch (SQLException throwables) {
                log.error(throwables.getMessage(), throwables);
            }
        return permissionList;
    }


    private final String GET_LIST_BY_OBJ_PERMISSION_QUERY = "SELECT client_id, permission FROM sec_permission WHERE object_id=? ";

    @Override
    public List<Permission> getPermissionsOfSecuredObject(SecuredObject securedObject) {
        Integer id = getObjectSecureId(securedObject);
        List<Permission> permissionList = new ArrayList<>();
        if (id != null) {
            try (Connection connection = dataSource.getConnection()) {
                PreparedStatement statement = connection.prepareStatement(GET_LIST_BY_OBJ_PERMISSION_QUERY);
                statement.setInt(1, id);
                ResultSet rs = statement.executeQuery();
                while (rs.next()) {
                    Permission permission = new Permission();
                    permission.setClientId(rs.getInt(1));
                    permission.setSecuredObject(securedObject);
                    permission.setPermissionList(new ArrayList<>());
                    int resultPermission = rs.getInt(2);
                    for (PermissionType type : PermissionType.values()) {
                        if (type.check(resultPermission)) {
                            permission.getPermissionList().add(type);
                        }
                    }
                    permissionList.add(permission);
                }
            } catch (SQLException throwables) {
                log.error(throwables.getMessage(), throwables);
            }
        }
        return permissionList;
    }
}

