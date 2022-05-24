package com.das.entities.endpoints.rest.reason;

import com.das.entities.core.config.context.Mapper;
import com.das.entities.core.dao.DaoFactory;
import com.das.entities.endpoints.rest.reason.dto.ReasonDto;
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
    private ReasonDao reasonDao = null;
    @Autowired
    private Mapper mapper;

    public ReasonService() {
        try {
            this.reasonDao = (ReasonDao) DaoFactory.getDao(ReasonDao.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<ReasonDto> getReason() {
        this.logger.info("Begun method getReasons");
        List<ReasonDto> reasonDtoList = null;
        try {
            reasonDtoList = this.reasonDao.select(null).stream()
                    .map(reason -> (ReasonDto) this.mapper.map(reason, ReasonDto.class)).toList();
        } catch (SQLException e) {
            this.logger.error(e.getMessage());
            e.printStackTrace();
        }
        return reasonDtoList != null ? reasonDtoList : new ArrayList<>();
    }
}
