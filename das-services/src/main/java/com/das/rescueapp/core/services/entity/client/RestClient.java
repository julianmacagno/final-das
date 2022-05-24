package com.das.rescueapp.core.services.entity.client;

import com.das.rescueapp.core.config.security.JwtUtil;
import com.das.rescueapp.endpoints.authentication.dto.AuthenticationDto;
import com.das.rescueapp.endpoints.entity.model.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class RestClient {
    private final Logger logger = LoggerFactory.getLogger(RestClient.class);
    Map<String, String> tokenMap = new HashMap<>();

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private JwtUtil jwtUtil;

    @Retryable(value = {Exception.class}, backoff = @Backoff(delay = 500), maxAttempts = 3)
    public <T> ResponseEntity<T> post(String url, String body, Class<T> responseType, Entity entity) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        if (tokenMap.get(entity.getName()) == null || this.jwtUtil.getExpirationDateFromToken(tokenMap.get(entity.getName())) == null || this.jwtUtil.isTokenExpired(tokenMap.get(entity.getName()))) {
            this.doLogin(entity);
        }
        headers.set("Authorization", "Bearer " + tokenMap.get(entity.getName()));
        headers.set("Content-Type", "application/json");

        HttpEntity<String> httpEntity = new HttpEntity<>(body, headers);
        try {

            ResponseEntity<T> responseEntity = restTemplate.postForEntity(url, httpEntity, responseType);
            if (responseEntity.getStatusCode().equals(HttpStatus.UNAUTHORIZED) || responseEntity.getStatusCode().equals(HttpStatus.FORBIDDEN)) {
                this.doLogin(entity);
                return this.post(url, body, responseType, entity);
            } else {
                return responseEntity;
            }
        } catch (ResourceAccessException e) {
            this.logger.error("RestClient failed: " + e.getMessage());
            throw e;
        }
    }

    @Retryable(value = {Exception.class}, backoff = @Backoff(delay = 500), maxAttempts = 3)
    public <T> ResponseEntity<T> get(String url, Class<T> responseType, Entity entity) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        if (tokenMap.get(entity.getName()) == null || this.jwtUtil.getExpirationDateFromToken(tokenMap.get(entity.getName())) == null || this.jwtUtil.isTokenExpired(tokenMap.get(entity.getName()))) {
            this.logger.info("Token null or expired. Doing login for entity: {}", entity.getName());
            this.doLogin(entity);
        }
        headers.set("Authorization", "Bearer " + tokenMap.get(entity.getName()));

        try {
            HttpEntity<String> httpEntity = new HttpEntity<>(headers);
            ResponseEntity<T> responseEntity = restTemplate.exchange(url, HttpMethod.GET, httpEntity, responseType);
            if (responseEntity.getStatusCode().equals(HttpStatus.UNAUTHORIZED) || responseEntity.getStatusCode().equals(HttpStatus.FORBIDDEN)) {
                this.logger.info("Token invalid, null or expired. Doing login for entity: {}", entity.getName());
                this.doLogin(entity);
                return this.get(url, responseType, entity);
            } else {
                return responseEntity;
            }
        } catch (ResourceAccessException e) {
            this.logger.error("RestClient failed: " + e.getMessage());
            throw e;
        }
    }

    @Retryable(value = {Exception.class}, backoff = @Backoff(delay = 500), maxAttempts = 3)
    private void doLogin(Entity entity) {
        String body = "{\"username\":\"" + entity.getUsername() + "\",\"password\":\"" + entity.getPassword() + "\"}";
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> httpEntity = new HttpEntity<>(body, httpHeaders);
        try {
            ResponseEntity<AuthenticationDto> loginResponseEntity = restTemplate.postForEntity(entity.getUrl() + "/rest/authentication/login", httpEntity, AuthenticationDto.class);
            if (loginResponseEntity.getStatusCode().equals(HttpStatus.OK) && loginResponseEntity.getBody() != null) {
                this.logger.info("Login successfull for entity: {} - Token: {}", entity.getName(), loginResponseEntity.getBody().getAccessToken());
                this.tokenMap.put(entity.getName(), loginResponseEntity.getBody().getAccessToken());
            } else {
                this.logger.warn("Login failed for entity: {}", entity.getName());
            }
        } catch (ResourceAccessException e) {
            this.logger.error("RestClient failed: " + e.getMessage());
            throw e;
        }
    }
}
