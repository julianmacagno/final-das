package com.das.entities.core.authentication;

import com.das.entities.commons.enums.IssueType;
import com.das.entities.commons.exceptions.EntitiesException;
import com.das.entities.commons.exceptions.PortalError;
import com.das.entities.core.authentication.dto.AuthenticationDto;
import com.das.entities.core.authentication.dto.LoginDto;
import com.das.entities.core.config.security.JwtUser;
import com.das.entities.core.config.security.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
public class AuthenticationService {
    private final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);
    private final JwtUtil jwtUtil;
    @Value("${entity.username}")
    private String username;
    @Value("${entity.password}")
    private String password;

    public AuthenticationService(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    public AuthenticationDto login(LoginDto loginDTO) throws EntitiesException {
        this.logger.info("Begun method validateUserAndPassword - LoginDto: {}", loginDTO);

        try {
            if (loginDTO.getUsername().equals(this.username) && loginDTO.getPassword().equals(this.password)) {
                String jwtToken = this.jwtUtil.generateToken(getJwtUser(loginDTO));
                return new AuthenticationDto(jwtToken);
            } else {
                throw new EntitiesException(IssueType.UNAUTHORIZED, PortalError.USER_NOT_FOUND);
            }
        } catch (EntitiesException e) {
            this.logger.error("User not authorized: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            this.logger.error("User not authorized: {}", e.getMessage());
            throw new EntitiesException(IssueType.INTERNAL_SERVER, PortalError.INTERNAL_SERVER_ERROR);
        }
    }

    private JwtUser getJwtUser(LoginDto loginDto) {
        if (loginDto == null) {
            return null;
        }

        try {
            return new JwtUser(loginDto, new HashSet<>());
        } catch (Exception e) {
            this.logger.error("Login failed with username {}. Details => {}", loginDto.getUsername(), e.getMessage());
            return null;
        }
    }

    public AuthenticationDto refreshToken(String token) {
        if (this.jwtUtil.canTokenBeRefreshed(token)) {
            return new AuthenticationDto(this.jwtUtil.refreshToken(token));
        }
        throw new EntitiesException(IssueType.UNAUTHORIZED, PortalError.USER_NOT_AUTHORIZED);
    }
}
