package com.das.rescueapp.endpoints.status;

import com.das.rescueapp.commons.enums.IssueType;
import com.das.rescueapp.commons.exceptions.PortalError;
import com.das.rescueapp.commons.exceptions.RescueAppException;
import com.das.rescueapp.core.config.context.Mapper;
import com.das.rescueapp.core.dao.DaoFactory;
import com.das.rescueapp.endpoints.asistance.AssistanceDao;
import com.das.rescueapp.endpoints.asistance.dto.AssistanceDto;
import com.das.rescueapp.endpoints.authentication.AuthenticationDao;
import com.das.rescueapp.endpoints.authentication.model.User;
import com.das.rescueapp.endpoints.status.dto.StatusDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatusService {
    private final Logger logger = LoggerFactory.getLogger(StatusService.class);

    @Autowired
    Mapper mapper;

    public StatusService() {
    }

    public StatusDto getStatusForUser(Long userId) {
        try {
            AuthenticationDao authenticationDao = (AuthenticationDao) DaoFactory.getDao(AuthenticationDao.class);
            AssistanceDao assistanceDao = (AssistanceDao) DaoFactory.getDao(AssistanceDao.class);

            User user = new User();
            user.setId(userId);
            user = authenticationDao.select(user).get(0);

            List<AssistanceDto> assistanceDtoList = null;
            assistanceDtoList = assistanceDao.selectActiveForUser(userId).stream()
                    .map(assistance -> (AssistanceDto) this.mapper.map(assistance, AssistanceDto.class)).toList();

            StatusDto statusDto = new StatusDto();
            statusDto.setUserId(userId);
            statusDto.setUserCanceled(user.getCanceled());
            statusDto.setCurrentAssistanceList(assistanceDtoList);

            return statusDto;
        } catch (Exception e) {
            this.logger.error(e.getMessage());
            e.printStackTrace();
            throw new RescueAppException(IssueType.INTERNAL_SERVER, PortalError.ERROR_GETTING_ASSISTANCE_STATUS);
        }
    }
}
