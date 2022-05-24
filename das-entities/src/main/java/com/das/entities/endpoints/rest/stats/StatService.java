package com.das.entities.endpoints.rest.stats;

import java.sql.SQLException;
import java.util.List;

import com.das.entities.core.config.context.Mapper;
import com.das.entities.core.dao.DaoFactory;
import com.das.entities.endpoints.rest.stats.model.Stat;
import com.das.soap.StatDto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StatService {
    private final Logger logger = LoggerFactory.getLogger(StatService.class);

    private StatDao statDao = null;

    @Autowired
    private Mapper mapper;

    public StatService() {
        try {
            this.statDao = (StatDao) DaoFactory.getDao(StatDao.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insertStats(List<StatDto> statDtoList) {
        this.logger.info("Begun method insertStats: StatDtoList: {}", statDtoList);
        List<Stat> statList = statDtoList.stream()
                .map(statDto -> (Stat) mapper.map(statDto, Stat.class))
                .toList();

        statList.forEach(stat -> {
            try {
                this.statDao.insert(stat);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
}
