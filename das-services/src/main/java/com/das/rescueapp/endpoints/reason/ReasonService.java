package com.das.rescueapp.endpoints.reason;

import com.das.rescueapp.commons.enums.IssueType;
import com.das.rescueapp.commons.exceptions.PortalError;
import com.das.rescueapp.commons.exceptions.RescueAppException;
import com.das.rescueapp.core.config.context.Mapper;
import com.das.rescueapp.core.dao.DaoFactory;
import com.das.rescueapp.core.services.entity.EntityStrategy;
import com.das.rescueapp.endpoints.entity.EntityDao;
import com.das.rescueapp.endpoints.entity.model.Entity;
import com.das.rescueapp.endpoints.reason.dto.ReasonDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReasonService {
    private final Logger logger = LoggerFactory.getLogger(ReasonService.class);
    EntityDao entityDao;
    @Autowired
    private EntityStrategy entityStrategy;

    public ReasonService() {
        try {
            this.entityDao = (EntityDao) DaoFactory.getDao(EntityDao.class);
        } catch (SQLException e) {
            this.logger.error(e.getMessage());
        }
    }

    public List<ReasonDto> getReason(Long entityId) {
        this.logger.info("Begun method getReasons - EntityId: {}", entityId);
        List<ReasonDto> reasonDtoList = null;
        try {
            Entity entity = this.entityDao.select(new Entity(entityId, true)).get(0);
            reasonDtoList = this.entityStrategy.getReason(entity);
        } catch (SQLException e) {
            this.logger.error(e.getMessage());
            e.printStackTrace();
            throw new RescueAppException(IssueType.NOT_FOUND, PortalError.ENTITY_UNAVAILABLE);
        } catch (Exception e) {
            this.logger.error(e.getMessage());
            e.printStackTrace();
            throw new RescueAppException(IssueType.NOT_FOUND, PortalError.COULD_NOT_RETRIEVE_REASON);
        }
        return reasonDtoList != null ? reasonDtoList : new ArrayList<>();
    }
}
