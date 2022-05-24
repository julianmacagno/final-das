package com.das.rescueapp.core.config.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "auth")
public class SecurityConfig {
    private String secret;
    private Long expiration;
    private List<String> portalsEnabled;

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public Long getExpiration() {
        return expiration;
    }

    public void setExpiration(Long expiration) {
        this.expiration = expiration;
    }

    public List<String> getPortalsEnabled() {
        return portalsEnabled;
    }

    public void setPortalsEnabled(List<String> portalsEnabled) {
        this.portalsEnabled = portalsEnabled;
    }
}
