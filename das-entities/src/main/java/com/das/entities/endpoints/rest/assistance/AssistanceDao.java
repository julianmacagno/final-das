package com.das.entities.endpoints.rest.assistance;


import com.das.entities.commons.enums.AssistanceStatus;
import com.das.entities.core.dao.Dao;
import com.das.entities.endpoints.rest.assistance.model.Assistance;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

public class AssistanceDao extends Dao<Assistance, Assistance> {
    @Override
    public Assistance make(ResultSet resultSet) throws SQLException {
        Assistance assistance = new Assistance();
        if (this.hasColumn(resultSet, "id")) {
            assistance.setId(resultSet.getLong("id"));
        }
        if (this.hasColumn(resultSet, "entity_id")) {
            assistance.setEntityId(resultSet.getLong("entity_id"));
        }
        if (this.hasColumn(resultSet, "user_id")) {
            assistance.setUserId(resultSet.getLong("user_id"));
        }
        if (this.hasColumn(resultSet, "assistance_status")) {
            assistance.setAssistanceStatus(AssistanceStatus.valueOf(resultSet.getString("assistance_status").toUpperCase()));
        }
        return assistance;
    }

    @Override
    public Assistance insert(Assistance assistance) throws SQLException {
        try {
            this.connect();

            this.setProcedure("InsertAsistance(?,?,?)");
            this.setParameter(1, assistance.getId());
            this.setParameter(2, assistance.getUserId());
            this.setParameter(3, assistance.getAssistanceStatus().getValue());

            if (this.executeUpdate() == 0) {
                throw new SQLException("No rows were updated");
            }

            this.close();
        } catch (SQLException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return assistance;
    }

    @Override
    public Assistance update(Assistance assistance) throws SQLException {
        try {
            this.connect();
            this.setProcedure("UpdateAssistanceStatus(?,?,?)");
            this.setParameter(1, assistance.getUserId());
            this.setParameter(2, assistance.getAssistanceStatus().getValue());
            if (assistance.getCancellationReason() == null) {
                this.setNull(3, Types.VARCHAR);
            } else {
                this.setParameter(3, assistance.getCancellationReason());
            }

            if (this.executeUpdate() == 0) {
                throw new SQLException("No rows were updated");
            }

            this.close();
        } catch (SQLException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return assistance;
    }

    @Override
    public Assistance delete(Assistance param) throws SQLException {
        return null;
    }

    @Override
    public List<Assistance> select(Assistance assistance) throws SQLException {
        return null;
    }

    @Override
    public boolean valid(Assistance param) throws SQLException {
        return false;
    }

    public Assistance setValoration(Assistance assistance) throws SQLException {
        try {
            this.connect();
            this.setProcedure("setValoration(?, ?)");
            this.setParameter(1, assistance.getId());
            this.setParameter(2, assistance.getValoration());

            if (this.executeUpdate() == 0) {
                throw new SQLException("No rows were updated");
            }

            this.close();
        } catch (SQLException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return assistance;
    }
}
