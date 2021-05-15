package com.strelnikov.doclib.repository.jdbc;

import com.strelnikov.doclib.model.conception.Permission;
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
        try (Connection connection = dataSource.getConnection()){
            PreparedStatement statement = connection.prepareStatement(GET_CLASS_QUERY);
            statement.setString(1,clazz.getSimpleName());
            ResultSet rs = statement.executeQuery();
            if(rs.next()){
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
        if (classId!=null){
            try (Connection connection = dataSource.getConnection()){
                PreparedStatement statement = connection.prepareStatement(GET_OBJECT_QUERY);
                statement.setInt(1,objectId);
                statement.setInt(2,classId);
                ResultSet rs = statement.executeQuery();
                if(rs.next()){
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
    public boolean checkPermission(int objectId, Class clazz, Client client, Permission permission) {
        Integer id = getSecuredObjectId(objectId, clazz);
        int resultPermission = 0;
        if (id!=null) {
            try (Connection connection = dataSource.getConnection()) {
                PreparedStatement statement = connection.prepareStatement(GET_PERMISSION_QUERY);
                statement.setInt(1,id);
                statement.setInt(2,client.getId());
                ResultSet rs = statement.executeQuery();
                if (rs.next()){
                    resultPermission = rs.getInt(1);
                }
            } catch (SQLException throwables) {
                log.error(throwables.getMessage(), throwables);
            }
        }
        return permission.check(resultPermission);
    }


    @Override
    public List<Permission> getPermissions(int objectId, Class clazz, Client client) {
        List<Permission> permissionList = new ArrayList<>();
        for (Permission permission:Permission.values()){
            if (checkPermission(objectId,clazz,client,permission)){
                permissionList.add(permission);
            }
        }
        return permissionList;
    }
}
