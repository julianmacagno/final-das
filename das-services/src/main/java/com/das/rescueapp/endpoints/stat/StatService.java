package com.das.rescueapp.endpoints.stat;

import com.das.rescueapp.commons.enums.IssueType;
import com.das.rescueapp.commons.exceptions.PortalError;
import com.das.rescueapp.commons.exceptions.RescueAppException;
import com.das.rescueapp.core.config.context.Mapper;
import com.das.rescueapp.core.dao.DaoFactory;
import com.das.rescueapp.core.services.entity.EntityStrategy;
import com.das.rescueapp.core.utils.DateUtils;
import com.das.rescueapp.endpoints.entity.EntityDao;
import com.das.rescueapp.endpoints.entity.model.Entity;
import com.das.rescueapp.endpoints.stat.model.Stat;
import com.das.soapconsumer.wsdl.StatDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class StatService {
    private final Logger logger = LoggerFactory.getLogger(StatService.class);
    private StatDao statDao = null;
    private EntityDao entityDao = null;

    @Autowired
    private Mapper mapper;

    @Autowired
    private EntityStrategy entityStrategy;

    public StatService() {
        try {
            this.statDao = (StatDao) DaoFactory.getDao(StatDao.class);
            this.entityDao = (EntityDao) DaoFactory.getDao(EntityDao.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Scheduled(cron = "*/30 * * * * *")
    public void sendStats() {
        // this.logger.info("Begun method sendStats");

        Long batchLengthInSeconds = 30L;

        Date batchEnd = new Date();
        Date batchStart = new Date();
        batchStart.setTime(batchEnd.getTime() - batchLengthInSeconds * 1000);

        List<StatDto> calculatedStats = this.calculateStats(batchStart, batchEnd);

        try {
            Entity entity = new Entity();
            entity.setAvailable(true);
            entity.setEmergencyService(false);
            List<Entity> entityList = this.entityDao.select(entity);

            this.entityStrategy.sendStats(entityList.get(0), calculatedStats);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<StatDto> calculateStats(Date batchStart, Date batchEnd) {
        // this.logger.info("Begun method getStats batchStart: {}, batchEnd: {}", batchStart, batchEnd);

        try {
            List<Stat> stats = this.statDao.select(new Stat());
            return stats.stream().map(
                    (stat) -> {
                        StatDto mapped = (StatDto) this.mapper.map(stat, StatDto.class);
                        mapped.setBatchStart(DateUtils.dateToXmlGregorianCalendar(batchStart));
                        mapped.setBatchEnd(DateUtils.dateToXmlGregorianCalendar(batchEnd));
                        return mapped;
                    }
            ).toList();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RescueAppException(IssueType.INTERNAL_SERVER, PortalError.ERROR_GETTING_STATS);
        }
    }
}
