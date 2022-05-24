package com.das.rescueapp.endpoints.authentication;

import com.das.rescueapp.core.dao.Dao;
import com.das.rescueapp.endpoints.authentication.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class AuthenticationDao extends Dao<User, User> {
    @Override
    public User make(ResultSet resultSet) throws SQLException {
        User user = new User();
        if (this.hasColumn(resultSet, "id")) user.setId(resultSet.getLong("id"));
        if (this.hasColumn(resultSet, "cuil")) user.setCuil(resultSet.getString("cuil"));
        if (this.hasColumn(resultSet, "name")) user.setName(resultSet.getString("name"));
        if (this.hasColumn(resultSet, "email")) user.setEmail(resultSet.getString("email"));
        if (this.hasColumn(resultSet, "password")) user.setPassword(resultSet.getString("password"));
        if (this.hasColumn(resultSet, "validated")) user.setValidated(resultSet.getBoolean("validated"));
        if (this.hasColumn(resultSet, "canceled")) user.setCanceled(resultSet.getBoolean("canceled"));
        return user;
    }

    @Override
    public User insert(User user) throws SQLException {
        try {
            this.connect();

            this.setProcedure("InsertUser(?,?,?,?)");
            this.setParameter(1, user.getCuil());
            this.setParameter(2, user.getName());
            this.setParameter(3, user.getEmail());
            this.setParameter(4, user.getPassword());

            if (this.executeUpdate() == 0) {
                throw new SQLException("No rows were updated");
            }

            this.close();
        } catch (SQLException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public User update(User user) throws SQLException {
        return null;
    }

    @Override
    public User delete(User param) throws SQLException {
        return null;
    }

    @Override
    public List<User> select(User user) throws SQLException {
        this.connect();
        this.setProcedure("GetUserById(?)");
        this.setParameter(1, user.getId());
        List<User> userList = this.executeQuery();
        this.close();
        return userList;
    }

    @Override
    public boolean valid(User param) throws SQLException {
        return false;
    }

    public List<User> login(User user) throws SQLException {
        this.connect();
        this.setProcedure("LoginUser(?,?)");
        this.setParameter(1, user.getCuil());
        this.setParameter(2, user.getPassword());
        List<User> userList = this.executeQuery();
        this.close();
        return userList;
    }

    public User validateUser(User user) throws SQLException {
        try {
            this.connect();

            this.setProcedure("ValidateUser(?)");
            this.setParameter(1, user.getCuil());

            if (this.executeUpdate() == 0) {
                throw new SQLException("No rows were updated");
            }
            this.close();
        } catch (SQLException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }
}
