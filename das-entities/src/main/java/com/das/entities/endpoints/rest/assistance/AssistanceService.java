package com.das.entities.endpoints.rest.assistance;

import com.das.entities.commons.enums.AssistanceStatus;
import com.das.entities.commons.enums.IssueType;
import com.das.entities.commons.exceptions.EntitiesException;
import com.das.entities.core.config.context.Mapper;
import com.das.entities.core.dao.DaoFactory;
import com.das.entities.endpoints.rest.assistance.model.Assistance;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
public class AssistanceService {
    private final Logger logger = LoggerFactory.getLogger(AssistanceService.class);
    private AssistanceDao assistanceDao = null;
    @Autowired
    private Mapper mapper;

    public AssistanceService() {
        try {
            this.assistanceDao = (AssistanceDao) DaoFactory.getDao(AssistanceDao.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Boolean createAsistance(Long assistanceId, Long userId) {
        this.logger.info("Begun method createAsistance - AssistanceId: {}, UserId: {}", assistanceId, userId);
        Assistance assistance = new Assistance(assistanceId, userId, AssistanceStatus.IN_PROGRESS);
        try {
            this.assistanceDao.insert(assistance);
            return true;
        } catch (SQLException e) {
            this.logger.error(e.getMessage());
            e.printStackTrace();
            throw new EntitiesException(IssueType.INTERNAL_SERVER, "Error creating assistance");
        }
    }

    public Boolean cancelAsistance(Long userId, String payload) {
        JsonObject jsonObject = new Gson().fromJson(payload, JsonObject.class);
        this.logger.info("Begun method cancelAsistance - UserId: {}", userId);
        Assistance assistance = new Assistance(userId, AssistanceStatus.CANCELED);
        assistance.setCancellationReason(jsonObject.get("cancelReason").getAsString());
        try {
            this.assistanceDao.update(assistance);
            return true;
        } catch (SQLException e) {
            this.logger.error(e.getMessage());
            e.printStackTrace();
            throw new EntitiesException(IssueType.INTERNAL_SERVER, "Error update assistance");
        }
    }

    public Boolean valorateAssistance(Long assistanceId, String valoration) {
        this.logger.info("Begun method valorateAsistance - AssistanceId: {}", assistanceId);
        Assistance assistance = new Assistance(assistanceId, valoration);
        try {
            this.assistanceDao.setValoration(assistance);
            return true;
        } catch (SQLException e) {
            throw new EntitiesException(IssueType.INTERNAL_SERVER, "Error update assistance");
        }
    }
}
