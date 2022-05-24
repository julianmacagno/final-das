package com.das.rescueapp.endpoints.reason;

import com.das.rescueapp.core.config.constant.Routes;
import com.das.rescueapp.endpoints.reason.dto.ReasonDto;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/")
@Tag(name = "Reason")
@Api(tags = "Reason")
public class ReasonController {
    private final Logger logger = LoggerFactory.getLogger(ReasonController.class);
    @Autowired
    ReasonService reasonService;

    @PostMapping(Routes.Api.Reason.REASON)
    public List<ReasonDto> getReasons(@PathVariable Long entityId) {
        this.logger.info("Begun method getReasons - EntityId: {}", entityId);
        return this.reasonService.getReason(entityId);
    }
}
