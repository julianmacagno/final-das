package com.das.entities.endpoints.rest.stats;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.List;

import com.das.entities.core.dao.Dao;
import com.das.entities.endpoints.rest.stats.model.Stat;

public class StatDao extends Dao<Stat, Stat> {
    @Override
    public Stat make(ResultSet resultSet) throws SQLException {
        return null;
    }

    @Override
    public Stat insert(Stat stat) throws SQLException {
        try {
            this.connect();

            this.setProcedure("InsertStat(?,?,?,?,?,?,?)");
            this.setParameter(1, stat.getUserId());
            this.setParameter(2, new Date(stat.getBatchStart().getTime()));
            this.setParameter(3, new Date(stat.getBatchEnd().getTime()));
            this.setParameter(4, stat.getOpenAssistanceCount());
            this.setParameter(5, stat.getFinishedAssistanceCount());
            this.setParameter(6, stat.getCanceledAssistanceCount());
            this.setParameter(7, stat.getMessageCount());

            if (this.executeUpdate() == 0) {
                throw new SQLException("No rows were updated");
            }

            this.close();
        } catch (SQLException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stat;
    }

    @Override
    public Stat update(Stat stat) throws SQLException {
        return null;
    }

    @Override
    public Stat delete(Stat stat) throws SQLException {
        return null;
    }

    @Override
    public List<Stat> select(Stat stat) throws SQLException {
        return null;
    }

    @Override
    public boolean valid(Stat stat) throws SQLException {
        return false;
    }
}
