package com.das.entities.endpoints.rest.ping;

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
@Tag(name = "Ping")
@Api(tags = "Ping")
public class PingController {
    private final Logger logger = LoggerFactory.getLogger(PingController.class);

    @GetMapping(Routes.Rest.Ping.PING)
    public String ping() {
        this.logger.info("Begun method ping");
        return "Pong";
    }

    @GetMapping(Routes.Rest.Ping.SECURED_PING)
    public String securedPing() {
        this.logger.info("Begun method securedPing");
        return "Secured Pong";
    }
}
