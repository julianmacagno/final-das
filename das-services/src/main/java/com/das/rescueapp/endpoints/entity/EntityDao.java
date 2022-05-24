package com.das.rescueapp.endpoints.entity;

import com.das.rescueapp.core.dao.Dao;
import com.das.rescueapp.core.services.entity.enums.ApiType;
import com.das.rescueapp.endpoints.entity.model.Entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

public class EntityDao extends Dao<Entity, Entity> {
    @Override
    protected Entity make(ResultSet resultSet) throws SQLException {
        Entity entity = new Entity();
        if (this.hasColumn(resultSet, "id")) {
            entity.setId(resultSet.getLong("id"));
        }
        if (this.hasColumn(resultSet, "phone")) {
            entity.setPhone(resultSet.getString("phone"));
        }
        if (this.hasColumn(resultSet, "name")) {
            entity.setName(resultSet.getString("name"));
        }
        if (this.hasColumn(resultSet, "url")) {
            entity.setUrl(resultSet.getString("url"));
        }
        if (this.hasColumn(resultSet, "emergencyService")) {
            entity.setEmergencyService(resultSet.getBoolean("emergencyService"));
        }
        if (this.hasColumn(resultSet, "apiType")) {
            String apiType = resultSet.getString("apiType");
            if (apiType != null) {
                entity.setApiType(apiType.equals("REST") ? ApiType.REST : ApiType.SOAP);
            }
        }
        if (this.hasColumn(resultSet, "username")) {
            entity.setUsername(resultSet.getString("username"));
        }
        if (this.hasColumn(resultSet, "password")) {
            entity.setPassword(resultSet.getString("password"));
        }
        if (this.hasColumn(resultSet, "available")) {
            entity.setAvailable(resultSet.getBoolean("available"));
        }
        return entity;
    }

    @Override
    public Entity insert(Entity entity) throws SQLException {
        return null;
    }

    @Override
    public Entity update(Entity entity) throws SQLException {
        try {
            this.connect();

            this.setProcedure("UpdateEntity(?,?)");
            this.setParameter(1, entity.getId());
            this.setParameter(2, entity.getAvailable());

            if (this.executeUpdate() == 0) {
                throw new SQLException("No rows were updated");
            }

            this.close();
        } catch (SQLException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return entity;
    }

    @Override
    public Entity delete(Entity param) throws SQLException {
        return null;
    }

    @Override
    public List<Entity> select(Entity entity) throws SQLException {
        this.connect();
        this.setProcedure("GetEntity(?,?,?)");
        if (entity.getAvailable() == null) {
            this.setNull(1, Types.BIT);
        } else {
            this.setParameter(1, entity.getAvailable());
        }
        if (entity.getEmergencyService() == null) {
            this.setNull(2, Types.BIT);
        } else {
            this.setParameter(2, entity.getEmergencyService());
        }
        if (entity.getId() == null) {
            this.setNull(3, Types.INTEGER);
        } else {
            this.setParameter(3, entity.getId());
        }

        List<Entity> entityList = this.executeQuery();
        this.close();
        return entityList;
    }

    @Override
    public boolean valid(Entity param) throws SQLException {
        return false;
    }
}
