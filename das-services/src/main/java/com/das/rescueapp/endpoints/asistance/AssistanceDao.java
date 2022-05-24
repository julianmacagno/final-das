package com.das.rescueapp.endpoints.asistance;


import com.das.rescueapp.commons.enums.AssistanceStatus;
import com.das.rescueapp.core.dao.Dao;
import com.das.rescueapp.endpoints.asistance.model.Assistance;

import java.sql.ResultSet;
import java.sql.SQLException;
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
        if (this.hasColumn(resultSet, "entity_name")) {
            assistance.setEntityName(resultSet.getString("entity_name"));
        }
        if (this.hasColumn(resultSet, "timestamp")) {
            assistance.setTimestamp(resultSet.getString("timestamp"));
        }
        return assistance;
    }

    @Override
    public Assistance insert(Assistance assistance) throws SQLException {
        List<Assistance> addedMessageList = null;
        try {
            this.connect();

            this.setProcedure("InsertAsistance(?,?,?)");
            this.setParameter(1, assistance.getEntityId());
            this.setParameter(2, assistance.getUserId());
            this.setParameter(3, assistance.getAssistanceStatus().getValue());

            addedMessageList = this.executeUpdateQuery();
            if (addedMessageList == null || addedMessageList.size() <= 0) {
                throw new SQLException("No rows were updated");
            }

            this.close();
        } catch (SQLException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return addedMessageList != null ? addedMessageList.get(0) : null;
    }

    @Override
    public Assistance update(Assistance assistance) throws SQLException {
        try {
            this.connect();
            this.setProcedure("UpdateAssistanceStatus(?,?)");
            this.setParameter(1, assistance.getId());
            this.setParameter(2, assistance.getAssistanceStatus().getValue());

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
        this.connect();
        this.setProcedure("GetAssistance(?,?,?)");
        this.setParameter(1, assistance.getEntityId());
        this.setParameter(2, assistance.getUserId());
        this.setParameter(3, assistance.getAssistanceStatus().getValue());
        List<Assistance> assistanceList = this.executeQuery();
        this.close();
        return assistanceList;
    }

    public List<Assistance> selectActiveForUser(Long userId) throws SQLException {
        this.connect();
        this.setProcedure("GetActiveAssistanceForUser(?)");
        this.setParameter(1, userId);
        List<Assistance> assistanceList = this.executeQuery();
        this.close();
        return assistanceList;
    }

    public List<Assistance> selectAllForUser(Assistance assistance) throws SQLException {
        this.connect();
        this.setProcedure("GetAssistanceListForUser(?)");
        this.setParameter(1, assistance.getUserId());
        List<Assistance> assistanceList = this.executeQuery();
        this.close();
        return assistanceList;
    }

    @Override
    public boolean valid(Assistance param) throws SQLException {
        return false;
    }
}
