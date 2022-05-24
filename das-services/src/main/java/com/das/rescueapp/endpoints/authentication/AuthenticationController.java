package com.das.rescueapp.endpoints.authentication;

import com.das.rescueapp.core.config.constant.Routes;
import com.das.rescueapp.endpoints.authentication.dto.AuthenticationDto;
import com.das.rescueapp.endpoints.authentication.dto.LoginDto;
import com.das.rescueapp.endpoints.authentication.dto.RegisterDto;
import com.das.rescueapp.endpoints.authentication.model.User;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @PostMapping(value = Routes.Api.Authentication.REGISTER)
    public User register(@Valid @RequestBody RegisterDto registerDto) {
        this.logger.info("Begun method register - RegisterDto: {}", registerDto);
        return authenticationService.register(registerDto);
    }

    @GetMapping(value = Routes.Api.Authentication.VALIDATE_USER_CUIL_PARAM)
    public User validateUser(@PathVariable String cuil) {
        this.logger.info("Begun method validateAccount - Cuil: {}", cuil);
        return authenticationService.validateUser(cuil);
    }

    @PostMapping(value = Routes.Api.Authentication.LOGIN)
    public AuthenticationDto login(@Valid @RequestBody LoginDto loginDTO) {
        this.logger.info("Begun method login - LoginDto: {}", loginDTO);
        return authenticationService.login(loginDTO);
    }

    @PostMapping(value = Routes.Api.Authentication.REFRESH_AUTHORIZATION)
    public AuthenticationDto refreshToken(@RequestBody @Valid @NotBlank String token) {
        this.logger.info("Begun method refreshToken - Token: {}", token);
        return authenticationService.refreshToken(token);
    }
}
