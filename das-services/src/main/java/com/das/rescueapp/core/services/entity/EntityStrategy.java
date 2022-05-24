package com.das.rescueapp.core.services.entity;

import com.das.rescueapp.core.services.entity.enums.ApiType;
import com.das.rescueapp.core.services.entity.service.RestEntityService;
import com.das.rescueapp.core.services.entity.service.SoapEntityService;
import com.das.rescueapp.endpoints.entity.model.Entity;
import com.das.rescueapp.endpoints.message.dto.MessageDto;
import com.das.rescueapp.endpoints.reason.dto.ReasonDto;
import com.das.soapconsumer.wsdl.StatDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EntityStrategy {
    private final Logger logger = LoggerFactory.getLogger(EntityStrategy.class);
    @Autowired
    private RestEntityService restEntityService;
    @Autowired
    private SoapEntityService soapEntityService;

    private Object getInstance(Entity entity) {
        if (entity.getApiType().equals(ApiType.REST)) {
            return this.restEntityService;
        } else {
            return this.soapEntityService;
        }
    }

    public Boolean getAvailability(Entity entity) {
//        this.logger.info("Begun method getAvailability - Entity: {}", entity);
        Object entityInstance = this.getInstance(entity);
        if (entityInstance instanceof RestEntityService) {
            return ((RestEntityService) entityInstance).getAvailabilityRest(entity);
        } else {
            return ((SoapEntityService) entityInstance).getAvailabilitySoap(entity);
        }
    }

    public List<MessageDto> synchronizeMessage(Entity entity, List<MessageDto> messageList) throws Exception {
//        this.logger.info("Begun method synchronizeMessage - Entity: {}, MessageList: {}", entity, messageList);
        Object entityInstance = this.getInstance(entity);
        if (entityInstance instanceof RestEntityService) {
            return ((RestEntityService) entityInstance).synchronizeMessage(entity, messageList);
        } else {
            return ((SoapEntityService) entityInstance).synchronizeMessage(entity, messageList);
        }
    }

    public void sendStats(Entity entity, List<StatDto> stats) throws Exception {
        Object entityInstance = this.getInstance(entity);
        if (entityInstance instanceof RestEntityService) {
            return;
        } else {
            ((SoapEntityService) entityInstance).sendStats(entity, stats);
        }
    }

    public List<ReasonDto> getReason(Entity entity) throws Exception {
        this.logger.info("Begun method getReason - Entity: {}", entity);
        Object entityInstance = this.getInstance(entity);
        if (entityInstance instanceof RestEntityService) {
            return ((RestEntityService) entityInstance).getReason(entity);
        } else {
            return ((SoapEntityService) entityInstance).getReason(entity);
        }
    }
}
