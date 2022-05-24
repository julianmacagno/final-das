package com.das.entities.core.config.security;

import com.das.entities.core.authentication.dto.LoginDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class JwtUser implements UserDetails {

    private final String username;
    private final LoginDto loginDto;
    private final Collection<? extends GrantedAuthority> authorities;
    private final boolean passwordElapsed;

    public JwtUser(LoginDto loginDto, Collection<? extends GrantedAuthority> authorities) {
        this.username = loginDto.getUsername();
        this.loginDto = loginDto;
        this.authorities = authorities;
        this.passwordElapsed = false;
    }

    public JwtUser(String username, Collection<? extends GrantedAuthority> authorities) {
        this.username = username;
        this.loginDto = null;
        this.authorities = authorities;
        this.passwordElapsed = false;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public String getPassword() {
        return null;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return true;
    }

    @JsonIgnore
    public LoginDto getLoginDto() {
        return loginDto;
    }
}
