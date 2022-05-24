package com.das.rescueapp.endpoints.asistance;

import com.das.rescueapp.commons.enums.AssistanceStatus;
import com.das.rescueapp.commons.enums.IssueType;
import com.das.rescueapp.commons.exceptions.PortalError;
import com.das.rescueapp.commons.exceptions.RescueAppException;
import com.das.rescueapp.core.config.context.Mapper;
import com.das.rescueapp.core.dao.DaoFactory;
import com.das.rescueapp.endpoints.asistance.dto.AssistanceDto;
import com.das.rescueapp.endpoints.asistance.model.Assistance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

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

    public Assistance createAsistance(Long entityId, Long userId) {
        this.logger.info("Begun method createAsistance - EntityId: {}, UserId: {}", entityId, userId);
        Assistance assistance = new Assistance(entityId, userId, AssistanceStatus.IN_PROGRESS);
        try {
            return this.assistanceDao.insert(assistance);
        } catch (SQLException e) {
            throw new RescueAppException(IssueType.INTERNAL_SERVER, PortalError.ERROR_INSERTING_ASSISTANCE);
        }
    }

    public AssistanceDto getCurrentAssistance(Long userId, Long entityId) {
        this.logger.info("Begun method getCurrentAssistance - UserId: {}, EntityId: {}", userId, entityId);
        Assistance assistance = new Assistance(entityId, userId, AssistanceStatus.IN_PROGRESS);
        try {
            List<Assistance> assistanceList = this.assistanceDao.select(assistance);
            if (assistanceList != null && assistanceList.size() > 0) {
                return (AssistanceDto) this.mapper.map(assistanceList.get(0), AssistanceDto.class);
            } else {
                throw new RescueAppException(IssueType.NOT_FOUND, PortalError.ASSISTANCE_NOT_FOUND);
            }
        } catch (SQLException e) {
            throw new RescueAppException(IssueType.NOT_FOUND, PortalError.ASSISTANCE_NOT_FOUND);
        }
    }

    public Assistance cancelCurrentAssistance(Long assistanceId) {
        this.logger.info("Begun method cancelCurrentAssistance - AssistanceId: {}", assistanceId);
        Assistance assistance = new Assistance(assistanceId, AssistanceStatus.CANCELED);
        try {
            assistance = this.assistanceDao.update(assistance);
        } catch (SQLException e) {
            throw new RescueAppException(IssueType.NOT_FOUND, PortalError.CANNOT_CANCEL_ASSISTANCE);
        }
        return assistance;
    }

    public Assistance finalizeCurrentAssistance(Long assistanceId) {
        this.logger.info("Begun method finalizeCurrentAssistance - AssistanceId: {}", assistanceId);
        Assistance assistance = new Assistance(assistanceId, AssistanceStatus.FINALIZED);
        try {
            assistance = this.assistanceDao.update(assistance);
        } catch (SQLException e) {
            throw new RescueAppException(IssueType.NOT_FOUND, PortalError.CANNOT_FINALIZE_ASSISTANCE);
        }
        return assistance;
    }

    public List<AssistanceDto> getAssistanceListByUserId(Long userId) {
//        this.logger.info("Begun method getAssistanceListByUserId - UserId: {}", userId);
        Assistance assistance = new Assistance(userId);
        try {
            List<Assistance> assistanceList = this.assistanceDao.selectAllForUser(assistance);
            if (assistanceList != null && assistanceList.size() > 0) {
                // TODO: Check why it is not working
                return assistanceList.stream().map(a -> (AssistanceDto) this.mapper.map(a, AssistanceDto.class)).toList();
            } else {
                throw new RescueAppException(IssueType.NOT_FOUND, PortalError.ASSISTANCE_STATUS_LIST_NOT_FOUND);
            }
        } catch (SQLException e) {
            this.logger.error(e.getMessage());
            throw new RescueAppException(IssueType.NOT_FOUND, PortalError.ERROR_GETTING_ASSISTANCE_STATUS_LIST);
        }
    }
}
