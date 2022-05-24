package com.das.rescueapp.core.services.entity.service;

import com.das.rescueapp.commons.enums.IssueType;
import com.das.rescueapp.commons.exceptions.PortalError;
import com.das.rescueapp.commons.exceptions.RescueAppException;
import com.das.rescueapp.core.services.entity.client.RestClient;
import com.das.rescueapp.endpoints.entity.model.Entity;
import com.das.rescueapp.endpoints.message.dto.MessageDto;
import com.das.rescueapp.endpoints.reason.dto.ReasonDto;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestEntityService {
    private final Logger logger = LoggerFactory.getLogger(RestEntityService.class);

    @Autowired
    RestClient restClient;

    public Boolean getAvailabilityRest(Entity entity) {
//        this.logger.info("Begun method getAvailabilityRest - Entity: {}", entity);
        String url = entity.getUrl() + "/rest/availability";
        try {
            ResponseEntity<Boolean> stringResponseEntity = this.restClient.get(url, Boolean.class, entity);
            if (stringResponseEntity != null && stringResponseEntity.getStatusCode().equals(HttpStatus.OK) && stringResponseEntity.getBody() != null) {
                return stringResponseEntity.getBody();
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<MessageDto> synchronizeMessage(Entity entity, List<MessageDto> messageDtoList) throws Exception {
//        this.logger.info("Begun method synchronizeMessage - Entity: {}, MessageDtoList: {}", entity, messageDtoList);
        String url = entity.getUrl() + "/rest/message";
        try {
            String body = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").create().toJson(messageDtoList);
            ResponseEntity<MessageDto[]> messageArrayResponseEntity = this.restClient.post(url, body, MessageDto[].class, entity);
            if (messageArrayResponseEntity.getStatusCode().equals(HttpStatus.OK) && messageArrayResponseEntity.getBody() != null) {
                return List.of(messageArrayResponseEntity.getBody());
            } else {
                throw new RescueAppException(IssueType.INTERNAL_SERVER, "Could not synchronize messages");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public List<ReasonDto> getReason(Entity entity) throws Exception {
        this.logger.info("Begun method getReason - Entity: {}", entity);
        String url = entity.getUrl() + "/rest/reason";
        try {
            ResponseEntity<ReasonDto[]> reasonDtoResponseEntity = this.restClient.get(url, ReasonDto[].class, entity);
            if (reasonDtoResponseEntity != null && reasonDtoResponseEntity.getStatusCode().equals(HttpStatus.OK) && reasonDtoResponseEntity.getBody() != null) {
                return List.of(reasonDtoResponseEntity.getBody());
            } else {
                throw new RescueAppException(IssueType.NOT_FOUND, PortalError.COULD_NOT_RETRIEVE_REASON);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
