package com.strelnikov.doclib.repository.jdbc;

import com.strelnikov.doclib.model.roles.*;
import com.strelnikov.doclib.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@Slf4j
public class PermissionJdbc implements PermissionDao {

    @Autowired
    private DataSource dataSource;
    @Autowired
    private CatalogDao catalogDao;
    @Autowired
    private DocumentDao documentDao;
    @Autowired
    private DocFileDao docFileDao;
    @Autowired
    private ClientDao clientDao;

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


    private final String UPDATE_PERMISSION_QUERY = "UPDATE sec_permission SET permission = ? " +
            "WHERE object_id = ? AND client_id = ?;";

    @Override
    public void updatePermission(SecuredObject securedObject, Client client, int permission) {
        int secureId = getObjectSecureId(securedObject);
        try (Connection connection = dataSource.getConnection()){
            PreparedStatement statement = connection.prepareStatement(UPDATE_PERMISSION_QUERY);
            statement.setInt(1,permission);
            statement.setInt(2,secureId);
            statement.setInt(3,client.getId());
            statement.executeUpdate();
        } catch (SQLException throwables) {
            log.error(throwables.getMessage(), throwables);
        }
    }


    private List<Integer> getAllSecureId(){
        List<Integer> idList = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()){
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT id FROM sec_object");
            while(rs.next()){
                idList.add(rs.getInt(1));
            }
        } catch (SQLException throwables) {
            log.error(throwables.getMessage(), throwables);
        }
        return idList;
    }

    private final String ADD_CLIENT_QUERY = "INSERT INTO sec_permission (object_id,client_id,permission) " +
            "VALUES(?, ?, 0);";

    @Override
    public void addClientToSecureTables(Client client) {
        List<Integer> idList = getAllSecureId();
        int clientId = client.getId();
        try (Connection connection = dataSource.getConnection()){
            for (Integer id:idList){
                PreparedStatement statement = connection.prepareStatement(ADD_CLIENT_QUERY);
                statement.setInt(1,id);
                statement.setInt(2,clientId);
                statement.executeUpdate();
            }
        } catch (SQLException throwables) {
            log.error(throwables.getMessage(), throwables);
        }
    }

    private final String INSERT_SECURE_OBJECT_QUERY = "INSERT INTO sec_object (object_table_id, class_id) " +
            "VALUES (?,?)";

    private void insertNewSecureObject(SecuredObject securedObject){
        int classId = getClassId(securedObject.getClass());
        try (Connection connection = dataSource.getConnection()){
            PreparedStatement statement = connection.prepareStatement(INSERT_SECURE_OBJECT_QUERY);
            statement.setInt(1, securedObject.getId());
            statement.setInt(2,classId);
            statement.executeUpdate();
        } catch (SQLException throwables) {
            log.error(throwables.getMessage(), throwables);
        }
    }

    private List<Integer> getClientListId(){
        List<Integer> idList = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()){
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT id FROM clients");
            while(rs.next()){
                idList.add(rs.getInt(1));
            }
        } catch (SQLException throwables) {
            log.error(throwables.getMessage(), throwables);
        }
        return idList;
    }

    private final String ADD_OBJECT_QUERY = "INSERT INTO sec_permission (object_id, client_id,permission) " +
            "VALUES(?,?,0)";

    @Override
    public void addObjectToSecureTables(SecuredObject securedObject) {
        insertNewSecureObject(securedObject);
        int secureId = getObjectSecureId(securedObject);
        List<Integer> idList = getClientListId();
        try(Connection connection = dataSource.getConnection()){
            for (int clientId : idList){
                PreparedStatement statement = connection.prepareStatement(ADD_OBJECT_QUERY);
                statement.setInt(1,secureId);
                statement.setInt(2,clientId);
                statement.executeUpdate();
            }
        } catch (SQLException throwables) {
            log.error(throwables.getMessage(), throwables);
        }
    }


    private final String DELETE_OBJECT_QUERY = "DELETE FROM sec_object WHERE id = ?";

    @Override
    public void removeObjectFromSecureTables(SecuredObject securedObject) {
        int secureId =getObjectSecureId(securedObject);
        try (Connection connection = dataSource.getConnection()){
            PreparedStatement statement = connection.prepareStatement(DELETE_OBJECT_QUERY);
            statement.setInt(1,secureId);
            statement.executeUpdate();
        } catch (SQLException throwables) {
            log.error(throwables.getMessage(), throwables);
        }
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
                    permission.setClient(client);
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
                    permission.setClient(clientDao.findById(rs.getInt(1)));
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

