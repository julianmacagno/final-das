package com.das.rescueapp.endpoints.status;

import com.das.rescueapp.core.config.constant.Routes;
import com.das.rescueapp.endpoints.status.dto.StatusDto;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("")
@Api(tags = "Status")
@Tag(name = "Status")
public class StatusController {
    private final Logger logger = LoggerFactory.getLogger(StatusController.class);

    @Autowired
    private StatusService statusService;

    @GetMapping(Routes.Api.Status.STATUS_FOR_USER)
    public StatusDto getStatusForUser(@PathVariable Long userId) {
        // this.logger.info("Begun method getStatusForUser - UserId: {}", userId);
        return this.statusService.getStatusForUser(userId);
    }
}
