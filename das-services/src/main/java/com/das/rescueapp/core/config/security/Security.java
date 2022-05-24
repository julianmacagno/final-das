package com.das.rescueapp.core.config.security;

import com.das.rescueapp.core.config.constant.Routes;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@ConfigurationProperties(prefix = "cors")
public class Security extends WebSecurityConfigurerAdapter {

    private String enabledUrl;

    public String getEnabledUrl() {
        return enabledUrl;
    }

    public void setEnabledUrl(String enabledUrl) {
        this.enabledUrl = enabledUrl;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors()
                .and()
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(Routes.Api.Authentication.LOGIN).permitAll()
                .antMatchers(Routes.Api.Authentication.REGISTER).permitAll()
                .antMatchers(Routes.Api.Authentication.VALIDATE_USER_CUIL_PARAM).permitAll()
                .antMatchers(Routes.Api.Ping.PING).permitAll()
                .antMatchers("/swagger-ui.html").permitAll()
                .antMatchers("/swagger-resources/**").permitAll()
                .antMatchers("/v3/**").permitAll()
                .antMatchers("/doc/**").permitAll()
                .antMatchers("/assets/locales/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(new JwtFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(getEnabledUrl().split(",")));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token"));
        configuration.setExposedHeaders(Collections.singletonList("x-auth-token"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
