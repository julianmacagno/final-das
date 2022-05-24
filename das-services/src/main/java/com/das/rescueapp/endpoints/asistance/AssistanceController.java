package com.das.rescueapp.endpoints.asistance;

import com.das.rescueapp.core.config.constant.Routes;
import com.das.rescueapp.endpoints.asistance.dto.AssistanceDto;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("")
@Api(tags = "Assistance")
@Tag(name = "Assistance")
public class AssistanceController {
    private final Logger logger = LoggerFactory.getLogger(AssistanceController.class);
    @Autowired
    private AssistanceService assistanceService;

    @GetMapping(Routes.Api.Assistance.ASSISTANCE_BY_USER_ID_BY_ENTITY_ID)
    public AssistanceDto getCurrentAssistance(@PathVariable Long userId, @PathVariable Long entityId) {
//        this.logger.info("Begun method getCurrentAssistance - UserId: {}, EntityId: {}", userId, entityId);
        return this.assistanceService.getCurrentAssistance(userId, entityId);
    }

    @GetMapping(Routes.Api.Assistance.ASSISTANCE_BY_USER_ID)
    public List<AssistanceDto> getAssistanceListByUserId(@PathVariable Long userId) {
//        this.logger.info("Begun method getAsistanceListByUserId - UserId: {}", userId);
        return this.assistanceService.getAssistanceListByUserId(userId);
    }
}
