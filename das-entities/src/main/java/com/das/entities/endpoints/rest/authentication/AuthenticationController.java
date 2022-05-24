package com.das.entities.endpoints.rest.authentication;

import com.das.entities.core.authentication.AuthenticationService;
import com.das.entities.core.authentication.dto.AuthenticationDto;
import com.das.entities.core.authentication.dto.LoginDto;
import com.das.entities.core.config.constant.Routes;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@RestController
@RequestMapping("")
@Api(tags = "Authentication")
@Tag(name = "Authentication")
public class AuthenticationController {
    private final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping(value = Routes.Rest.Authentication.LOGIN, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public AuthenticationDto login(@Valid @RequestBody LoginDto loginDTO) {
        this.logger.info("Begun method login - LoginDto: {}", loginDTO);
        return authenticationService.login(loginDTO);
    }

    @PostMapping(value = Routes.Rest.Authentication.REFRESH_AUTHORIZATION)
    public AuthenticationDto refreshToken(@RequestBody @Valid @NotBlank String token) {
        this.logger.info("Begun method refreshToken - Token: {}", token);
        return authenticationService.refreshToken(token);
    }
}
