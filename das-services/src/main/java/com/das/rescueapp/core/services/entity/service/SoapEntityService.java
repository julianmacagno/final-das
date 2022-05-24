package com.das.rescueapp.core.services.entity.service;

import com.das.rescueapp.commons.enums.IssueType;
import com.das.rescueapp.commons.enums.MessageType;
import com.das.rescueapp.commons.exceptions.PortalError;
import com.das.rescueapp.commons.exceptions.RescueAppException;
import com.das.rescueapp.core.config.context.Mapper;
import com.das.rescueapp.core.services.entity.client.SoapClient;
import com.das.rescueapp.endpoints.entity.model.Entity;
import com.das.rescueapp.endpoints.message.dto.MessageDto;
import com.das.rescueapp.endpoints.reason.dto.ReasonDto;
import com.das.soapconsumer.wsdl.GetAvailabilityRequest;
import com.das.soapconsumer.wsdl.GetAvailabilityResponse;
import com.das.soapconsumer.wsdl.MessageRequest;
import com.das.soapconsumer.wsdl.MessageResponse;
import com.das.soapconsumer.wsdl.ReasonRequest;
import com.das.soapconsumer.wsdl.ReasonResponse;
import com.das.soapconsumer.wsdl.ReceiveStatsRequest;
import com.das.soapconsumer.wsdl.ReceiveStatsResponse;
import com.das.soapconsumer.wsdl.SoapMessageDto;
import com.das.soapconsumer.wsdl.SoapReasonDto;
import com.das.soapconsumer.wsdl.StatDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SoapEntityService {
    private final Logger logger = LoggerFactory.getLogger(SoapEntityService.class);
    @Autowired
    private SoapClient client;
    @Autowired
    Mapper mapper;

    public Boolean getAvailabilitySoap(Entity entity) {
//        this.logger.info("Begun method getAvailabilitySoap - Entity: {}", entity);

        GetAvailabilityRequest request = new GetAvailabilityRequest();

        try {
            GetAvailabilityResponse response = (GetAvailabilityResponse) client.soapRequest(
                    entity.getUrl() + "/soap",
                    request,
                    "http://das.com/soapconsumer/wsdl/GetAvailabilityRequest",
                    entity
            );

            if (response != null) {
                return response.getServiceAvailability().isStatus();
            } else {
                return false;
            }
        } catch (Exception e) {
            this.logger.error(e.getMessage());
            return false;
        }
    }

    public List<MessageDto> synchronizeMessage(Entity entity, List<MessageDto> messageDtoList) throws RescueAppException {
//        this.logger.info("Begun method getAvailabilitySoap - Entity: {}, MessageDtoList: {}", entity, messageDtoList);

        List<SoapMessageDto> soapMessageDtoList = messageDtoList.stream().map(messageDto -> (SoapMessageDto) this.mapper.map(messageDto, SoapMessageDto.class)).toList();
        MessageRequest messageRequest = new MessageRequest();
        messageRequest.setMessageDto(soapMessageDtoList);

        try {
            MessageResponse response = (MessageResponse) client.soapRequest(
                    entity.getUrl() + "/soap",
                    messageRequest,
                    "http://das.com/soapconsumer/wsdl/MessageRequest",
                    entity
            );

            if (response != null) {
                return response.getMessageDto().stream().map(soapMessageDto -> {
                    MessageDto messageDto = (MessageDto) this.mapper.map(soapMessageDto, MessageDto.class);
                    messageDto.setMessageType(MessageType.valueOf(soapMessageDto.getMessageType().toUpperCase()));
                    return messageDto;
                }).toList();
            } else {
                throw new RescueAppException(IssueType.INTERNAL_SERVER, "Could not synchronize messages");
            }
        } catch (Exception e) {
            this.logger.error(e.getMessage());
            throw e;
        }
    }

    public void sendStats(Entity entity, List<StatDto> stats) throws RescueAppException {
        ReceiveStatsRequest request = new ReceiveStatsRequest();
        request.setStatsDto(stats);

        try {
            ReceiveStatsResponse response = (ReceiveStatsResponse) client.soapRequest(
                    entity.getUrl() + "/soap",
                    request,
                    "http://das.com/soapconsumer/wsdl/ReceiveStatsRequest",
                    entity
            );
        } catch (Exception e) {
            this.logger.error(e.getMessage());
            throw e;
        }
    }

    public List<ReasonDto> getReason(Entity entity) {
        this.logger.info("Begun method getReason - Entity: {}", entity);
        ReasonRequest reasonRequest = new ReasonRequest();

        try {
            ReasonResponse response = (ReasonResponse) client.soapRequest(
                    entity.getUrl() + "/soap",
                    reasonRequest,
                    "http://das.com/soapconsumer/wsdl/ReasonRequest",
                    entity
            );

            if (response != null) {
                return response.getSoapReasonDto().stream().map(soapReasonDto -> {
                    ReasonDto messageDto = (ReasonDto) this.mapper.map(soapReasonDto, ReasonDto.class);
//                    messageDto.setMessageType(MessageType.valueOf(soapReasonDto.getMessageType().toUpperCase()));
                    return messageDto;
                }).toList();
            } else {
                throw new RescueAppException(IssueType.NOT_FOUND, PortalError.COULD_NOT_RETRIEVE_REASON);
            }
        } catch (Exception e) {
            this.logger.error(e.getMessage());
            throw e;
        }
    }
}
