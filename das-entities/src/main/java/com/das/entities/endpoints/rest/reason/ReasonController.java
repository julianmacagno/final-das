package com.das.entities.endpoints.rest.reason;

import com.das.entities.core.config.constant.Routes;
import com.das.entities.endpoints.rest.reason.dto.ReasonDto;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping(Routes.Rest.Reason.reason)
    public List<ReasonDto> getReasons() {
        this.logger.info("Begun method getReasons");
        return this.reasonService.getReason();
    }
}
