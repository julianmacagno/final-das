package com.das.rescueapp.core.config.security;

import com.auth0.jwt.interfaces.Claim;
import com.das.rescueapp.endpoints.authentication.model.User;
import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

public class AuthenticationToken extends AbstractAuthenticationToken {
    private transient User user;

    public AuthenticationToken() {
        super(Collections.emptyList());
        super.setAuthenticated(false);
    }

    public AuthenticationToken(Map<String, Claim> claimMap) {
        super(Collections.emptyList());
        this.user = new User();
        super.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return this.user.getCuil();
    }

    @Override
    public Object getPrincipal() {
        return this.user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AuthenticationToken)) return false;
        if (!super.equals(o)) return false;
        AuthenticationToken that = (AuthenticationToken) o;
        return user.equals(that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), user);
    }
}


