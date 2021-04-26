package com.strelnikov.doclib.database.jdbc;

import com.strelnikov.doclib.database.Interface.TypeDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;

public class TypeDaoJdbc implements TypeDao {
    private static Logger log ;
    static {
        log= LoggerFactory.getLogger(TypeDaoJdbc.class);
    }

    private static final String TYPE_ADD_QUERY = "INSERT INTO types VALUES (nextval('types_id_seq'),?)";

    public void addType(String type) {
        try {
            Connection connection = DatabaseConnectorJdbc.getConnectionFromPool();
            PreparedStatement statement = connection.prepareStatement(TYPE_ADD_QUERY);
            statement.setString(1, type);
            statement.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
    }

    private final String TYPE_DELETE_QUERY =
            "DELETE FROM types where (type = ?)";

    public void deleteType(String type) {
        try {
            Connection connection = DatabaseConnectorJdbc.getConnectionFromPool();
            PreparedStatement statement = connection.prepareStatement(TYPE_DELETE_QUERY);
            statement.setString(1, type);
            statement.executeUpdate();
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
    }

    private final String TYPE_GET_LIST_QUERY = "SELECT* FROM types";

    public ArrayList<String> getTypesList() {
        ArrayList<String> list = new ArrayList();
        try {
            Connection connection = DatabaseConnectorJdbc.getConnectionFromPool();
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(TYPE_GET_LIST_QUERY);
            while (rs.next()) {
                list.add(rs.getString(2));
            }
            connection.close();
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
        return list;
    }

}
