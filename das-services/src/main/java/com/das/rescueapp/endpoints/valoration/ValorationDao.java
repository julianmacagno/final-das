package com.das.rescueapp.endpoints.valoration;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.das.rescueapp.core.dao.Dao;
import com.das.rescueapp.endpoints.valoration.model.Valoration;

public class ValorationDao extends Dao<Valoration, Valoration> {
    @Override
    public Valoration make(ResultSet resultSet) throws SQLException {
        Valoration valoration = new Valoration();
        if (this.hasColumn(resultSet, "id")) {
            valoration.setId(resultSet.getLong("id"));
        }
        if (this.hasColumn(resultSet, "value")) {
            valoration.setValue(resultSet.getLong("value"));
        }
        if (this.hasColumn(resultSet, "positive")) {
            valoration.setPositive(resultSet.getBoolean("positive"));
        }
        return valoration;
    }

    @Override
    public Valoration insert(Valoration valoration) throws SQLException {
        return null;
    }

    @Override
    public Valoration update(Valoration valoration) throws SQLException {
        return null;
    }

    @Override
    public Valoration delete(Valoration param) throws SQLException {
        return null;
    }

    @Override
    public List<Valoration> select(Valoration valoration) throws SQLException {
        this.connect();
        this.setProcedure("GetValorationScales");
        List<Valoration> statList = this.executeQuery();
        this.close();
        return statList;
    }

    @Override
    public boolean valid(Valoration param) throws SQLException {
        return false;
    }
}
