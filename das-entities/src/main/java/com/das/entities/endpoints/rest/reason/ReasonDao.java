package com.das.entities.endpoints.rest.reason;


import com.das.entities.core.dao.Dao;
import com.das.entities.endpoints.rest.reason.model.Reason;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ReasonDao extends Dao<Reason, Reason> {
    @Override
    public Reason make(ResultSet resultSet) throws SQLException {
        Reason reason = new Reason();
        if (this.hasColumn(resultSet, "id")) {
            reason.setId(resultSet.getLong("id"));
        }
        if (this.hasColumn(resultSet, "name")) {
            reason.setName(resultSet.getString("name"));
        }
        return reason;
    }

    @Override
    public Reason insert(Reason Reason) throws SQLException {
        return null;
    }

    @Override
    public Reason update(Reason Reason) throws SQLException {
        return null;
    }

    @Override
    public Reason delete(Reason Reason) throws SQLException {
        return null;
    }

    @Override
    public List<Reason> select(Reason Reason) throws SQLException {
        this.connect();
        this.setProcedure("selectReason");
        List<Reason> reasonList = this.executeQuery();
        this.close();
        return reasonList;
    }

    @Override
    public boolean valid(Reason Reason) throws SQLException {
        return false;
    }
}
