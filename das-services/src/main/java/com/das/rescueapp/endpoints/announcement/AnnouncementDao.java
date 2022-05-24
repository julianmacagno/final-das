package com.das.rescueapp.endpoints.announcement;


import com.das.rescueapp.core.dao.Dao;
import com.das.rescueapp.endpoints.announcement.model.Announcement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class AnnouncementDao extends Dao<Announcement, Announcement> {
    @Override
    public Announcement make(ResultSet resultSet) throws SQLException {
        Announcement announcement = new Announcement();
        if (this.hasColumn(resultSet, "id")) {
            announcement.setId(resultSet.getLong("id"));
        }
        if (this.hasColumn(resultSet, "content")) {
            announcement.setContent(resultSet.getString("content"));
        }
        return announcement;
    }

    @Override
    public Announcement insert(Announcement announcement) throws SQLException {
        return null;
    }

    @Override
    public Announcement update(Announcement announcement) throws SQLException {
        return null;
    }

    @Override
    public Announcement delete(Announcement param) throws SQLException {
        return null;
    }

    @Override
    public List<Announcement> select(Announcement announcement) throws SQLException {
        this.connect();
        this.setProcedure("GetAnnouncement()");
        List<Announcement> announcementList = this.executeQuery();
        this.close();
        return announcementList;
    }

    @Override
    public boolean valid(Announcement param) throws SQLException {
        return false;
    }
}
