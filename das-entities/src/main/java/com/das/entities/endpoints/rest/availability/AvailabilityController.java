package com.das.entities.endpoints.rest.availability;

import com.das.entities.core.config.constant.Routes;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@Tag(name = "Availability")
@Api(tags = "Availability")
public class AvailabilityController {
    private final Logger logger = LoggerFactory.getLogger(AvailabilityController.class);

    @GetMapping(Routes.Rest.Availability.AVAILABILITY)
    public Boolean getAvailability() {
//        this.logger.info("Begun method getAvailability");
        return true;
    }
}
