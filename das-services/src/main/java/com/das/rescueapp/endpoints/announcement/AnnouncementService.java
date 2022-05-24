package com.das.rescueapp.endpoints.announcement;

import com.das.rescueapp.commons.enums.IssueType;
import com.das.rescueapp.commons.exceptions.PortalError;
import com.das.rescueapp.commons.exceptions.RescueAppException;
import com.das.rescueapp.core.config.context.Mapper;
import com.das.rescueapp.core.dao.DaoFactory;
import com.das.rescueapp.endpoints.announcement.dto.AnnouncementDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class AnnouncementService {
    private final Logger logger = LoggerFactory.getLogger(AnnouncementService.class);
    private AnnouncementDao announcementDao = null;
    @Autowired
    private Mapper mapper;

    public AnnouncementService() {
        try {
            this.announcementDao = (AnnouncementDao) DaoFactory.getDao(AnnouncementDao.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<AnnouncementDto> getAnnouncement() {
        this.logger.info("Begun method createAsistance");
        try {
            return this.announcementDao.select(null).stream().map(announcement -> (AnnouncementDto) this.mapper.map(announcement, AnnouncementDto.class)).toList();
        } catch (SQLException e) {
            throw new RescueAppException(IssueType.INTERNAL_SERVER, PortalError.ERROR_GETTING_ANNOUNCEMENT);
        }
    }
}
