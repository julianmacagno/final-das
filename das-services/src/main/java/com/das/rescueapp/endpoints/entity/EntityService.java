package com.das.rescueapp.endpoints.entity;

import com.das.rescueapp.commons.enums.IssueType;
import com.das.rescueapp.commons.exceptions.PortalError;
import com.das.rescueapp.commons.exceptions.RescueAppException;
import com.das.rescueapp.core.config.context.Mapper;
import com.das.rescueapp.core.dao.DaoFactory;
import com.das.rescueapp.core.services.entity.EntityStrategy;
import com.das.rescueapp.endpoints.entity.dto.EntityDto;
import com.das.rescueapp.endpoints.entity.model.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class EntityService {
    private final Logger logger = LoggerFactory.getLogger(EntityService.class);

    @Autowired
    private EntityStrategy entityStrategy;

    @Autowired
    private Mapper mapper;

    public EntityService() {
    }

    public List<EntityDto> getEntityAvailability() {
//        this.logger.info("Begun method getEntityAvailability");
        try {
            EntityDao entityDao = (EntityDao) DaoFactory.getDao(EntityDao.class);
            List<Entity> entityList = entityDao.select(new Entity(true));
            if (entityList != null) {
                return entityList.stream().map(entity -> ((EntityDto) this.mapper.map(entity, EntityDto.class))).toList();
            } else {
                throw new RescueAppException(IssueType.NOT_FOUND, PortalError.ENTITY_NOT_FOUND);
            }
        } catch (SQLException e) {
            throw new RescueAppException(IssueType.NOT_FOUND, PortalError.ENTITY_NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Scheduled(cron = "*/5 * * * * *")
    private void updateEntityAvailability() {
//        this.logger.info("Begun method updateEntityAvailability");
        try {
            EntityDao entityDao = (EntityDao) DaoFactory.getDao(EntityDao.class);
            List<Entity> entityList = entityDao.select(new Entity());

            if (entityList != null) {
                entityList.forEach(entity -> {
                    Boolean available = entityStrategy.getAvailability(entity);
                    if (available != null) {
                        entity.setAvailable(available);
                        try {
                            entityDao.update(entity);
                        } catch (SQLException e) {
                            this.logger.error(e.getMessage());
                            e.printStackTrace();
                        }
                    }
                });
            }
        } catch (SQLException e) {
            this.logger.error(e.getMessage());
            e.printStackTrace();
        }
    }
}
