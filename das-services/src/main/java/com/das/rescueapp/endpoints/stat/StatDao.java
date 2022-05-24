package com.das.rescueapp.endpoints.stat;

import com.das.rescueapp.core.dao.Dao;
import com.das.rescueapp.endpoints.stat.model.Stat;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class StatDao extends Dao<Stat, Stat> {
    @Override
    public Stat make(ResultSet resultSet) throws SQLException {
        Stat Stat = new Stat();
        if (this.hasColumn(resultSet, "open_assistance_count")) {
            Stat.setOpenAssistanceCount(resultSet.getLong("open_assistance_count"));
        }
        if (this.hasColumn(resultSet, "finished_assistance_count")) {
            Stat.setFinishedAssistanceCount(resultSet.getLong("finished_assistance_count"));
        }
        if (this.hasColumn(resultSet, "canceled_assistance_count")) {
            Stat.setCanceledAssistanceCount(resultSet.getLong("canceled_assistance_count"));
        }
        if (this.hasColumn(resultSet, "message_count")) {
            Stat.setMessageCount(resultSet.getLong("message_count"));
        }
        if (this.hasColumn(resultSet, "user_id")) {
            Stat.setUserId(resultSet.getLong("user_id"));
        }
        return Stat;
    }

    @Override
    public Stat insert(Stat Stat) throws SQLException {
        return null;
    }

    @Override
    public Stat update(Stat Stat) throws SQLException {
        return null;
    }

    @Override
    public Stat delete(Stat param) throws SQLException {
        return null;
    }

    @Override
    public List<Stat> select(Stat stat) throws SQLException {
        this.connect();
        this.setProcedure("GetStats");
        List<Stat> statList = this.executeQuery();
        this.close();
        return statList;
    }

    @Override
    public boolean valid(Stat param) throws SQLException {
        return false;
    }
}
