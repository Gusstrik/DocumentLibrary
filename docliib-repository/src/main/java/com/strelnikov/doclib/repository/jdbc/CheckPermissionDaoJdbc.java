package com.strelnikov.doclib.repository.jdbc;

import com.strelnikov.doclib.model.roles.Permission;
import com.strelnikov.doclib.model.roles.PermissionType;
import com.strelnikov.doclib.model.roles.Client;
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
    public Integer getSecuredObjectId(int objectId, Class clazz) {
        Integer id = null;
        Integer classId = getClassId(clazz);
        if (classId != null) {
            try (Connection connection = dataSource.getConnection()) {
                PreparedStatement statement = connection.prepareStatement(GET_OBJECT_QUERY);
                statement.setInt(1, objectId);
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
    public boolean checkPermission(int objectId, Class clazz, Client client, PermissionType permissionType) {
        Integer id = getSecuredObjectId(objectId, clazz);
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


    private Class getObjClassById(int id){
        Class clazz = null;
        try (Connection connection = dataSource.getConnection()){
            PreparedStatement statement = connection.prepareStatement("SELECT class_id FROM sec_object WHERE id = ?");
            statement.setInt(1,id);
            ResultSet rs = statement.executeQuery();
            if(rs.next()){
               int classId = rs.getInt(1);
                statement = connection.prepareStatement("SELECT name FROM sec_object_classes WHERE id = ?");
                statement.setInt(1,classId);
                rs = statement.executeQuery();
                if(rs.next()){
                    clazz=Class.forName(rs.getString(1));
                }
            }
        } catch (SQLException | ClassNotFoundException throwables) {
            log.error(throwables.getMessage(), throwables);
        }
        return clazz;
    }

    private int getObjIdById(int id){
        int objId = 0;
        try (Connection connection = dataSource.getConnection()){
            PreparedStatement statement = connection.prepareStatement("SELECT object_table_id FROM sec_object WHERE id = ?");
            statement.setInt(1,id);
            ResultSet rs = statement.executeQuery();
            if(rs.next()){
                objId = rs.getInt(1);
            }
        } catch (SQLException throwables) {
            log.error(throwables.getMessage(), throwables);
        }
        return objId;
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
                    int objectId = rs.getInt(1);
                    Permission permission = new Permission();
                    permission.setClientId(rs.getInt(client.getId()));
                    permission.setObjectId(getObjIdById(objectId));
                    permission.setClazz(getObjClassById(objectId));
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
    public List<Permission> getPermissionsByObj(int objectId, Class clazz) {
        Integer id = getSecuredObjectId(objectId, clazz);
        List<Permission> permissionList = new ArrayList<>();
        if (id != null) {
            try (Connection connection = dataSource.getConnection()) {
                PreparedStatement statement = connection.prepareStatement(GET_LIST_BY_OBJ_PERMISSION_QUERY);
                statement.setInt(1, id);
                ResultSet rs = statement.executeQuery();
                while (rs.next()) {
                    Permission permission = new Permission();
                    permission.setClientId(rs.getInt(1));
                    permission.setObjectId(objectId);
                    permission.setClazz(clazz);
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

