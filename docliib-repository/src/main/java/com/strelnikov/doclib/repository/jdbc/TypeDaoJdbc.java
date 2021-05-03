package com.strelnikov.doclib.repository.jdbc;

import com.strelnikov.doclib.repository.TypeDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
public class TypeDaoJdbc implements TypeDao {

    private final DataSource dataSource;

    public TypeDaoJdbc(@Autowired DataSource dataSource){
        this.dataSource = dataSource;
    }


    private static final String TYPE_ADD_QUERY = "INSERT INTO doc_types VALUES (nextval('doc_types_id_seq'),?)";

    public void addType(String type) {
        try (Connection connection = dataSource.getConnection()){
            PreparedStatement statement = connection.prepareStatement(TYPE_ADD_QUERY);
            statement.setString(1, type);
            statement.executeUpdate();
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
    }

    private final String TYPE_DELETE_QUERY =
            "DELETE FROM doc_types where (name = ?)";

    public void deleteType(String type) {
        try (Connection connection = dataSource.getConnection()){
            PreparedStatement statement = connection.prepareStatement(TYPE_DELETE_QUERY);
            statement.setString(1, type);
            statement.executeUpdate();
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
    }

    private final String TYPE_GET_LIST_QUERY = "SELECT* FROM doc_types";

    public List<String> getTypesList() {
        ArrayList<String> list = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()){
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(TYPE_GET_LIST_QUERY);
            while (rs.next()) {
                list.add(rs.getString(2));
            }
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
        return list;
    }

}
