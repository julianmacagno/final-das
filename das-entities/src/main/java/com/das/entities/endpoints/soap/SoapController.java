package com.das.entities.endpoints.soap;

import com.das.entities.core.config.constant.Routes;
import com.das.entities.core.config.context.Mapper;
import com.das.entities.endpoints.rest.message.MessageService;
import com.das.entities.endpoints.rest.reason.ReasonService;
import com.das.entities.endpoints.rest.stats.StatService;
import com.das.soap.GetAvailabilityRequest;
import com.das.soap.GetAvailabilityResponse;
import com.das.soap.MessageRequest;
import com.das.soap.MessageResponse;
import com.das.soap.ReasonRequest;
import com.das.soap.ReasonResponse;
import com.das.soap.ReceiveStatsRequest;
import com.das.soap.ReceiveStatsResponse;
import com.das.soap.ServiceAvailability;
import com.das.soap.SoapMessageDto;
import com.das.soap.SoapReasonDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.util.List;

@Endpoint
public class SoapController {
    private final Logger logger = LoggerFactory.getLogger(SoapController.class);

    @Autowired
    MessageService messageService;

    @Autowired
    StatService statService;

    @Autowired
    ReasonService reasonService;

    @Autowired
    Mapper mapper;

    @PayloadRoot(namespace = Routes.Soap.NAMESPACE_URI, localPart = "getAvailabilityRequest")
    @ResponsePayload
    public GetAvailabilityResponse getAvailability(@RequestPayload GetAvailabilityRequest request) {
//        this.logger.info("Begun method getAvailability - GetAvailabilityRequest: {}", request);

        ServiceAvailability sa = new ServiceAvailability();
        sa.setStatus(true);

        GetAvailabilityResponse response = new GetAvailabilityResponse();
        response.setServiceAvailability(sa);

        return response;
    }

    @PayloadRoot(namespace = Routes.Soap.NAMESPACE_URI, localPart = "messageRequest")
    @ResponsePayload
    public MessageResponse message(@RequestPayload MessageRequest request) {
//        this.logger.info("Begun method insertMessage - MessageRequest: {}", request);

        List<SoapMessageDto> messageDtoList = this.messageService.insertSoapMessage(request.getMessageDto());
        MessageResponse response = new MessageResponse();
        response.setMessageDto(messageDtoList);

        return response;
    }

    @PayloadRoot(namespace = Routes.Soap.NAMESPACE_URI, localPart = "receiveStatsRequest")
    @ResponsePayload
    public ReceiveStatsResponse receiveStats(@RequestPayload ReceiveStatsRequest request) {
        // this.logger.info("Begun method receiveStats - ReceiveStatsRequest: {}", request);

        this.statService.insertStats(request.getStatsDto());

        return new ReceiveStatsResponse();
    }

    @PayloadRoot(namespace = Routes.Soap.NAMESPACE_URI, localPart = "reasonRequest")
    @ResponsePayload
    public ReasonResponse reason(@RequestPayload ReasonRequest request) {
         this.logger.info("Begun method reason - ReasonRequest: {}", request);

        List<SoapReasonDto> soapReasonDtoList = this.reasonService.getReason().stream()
                .map(reason -> (SoapReasonDto) this.mapper.map(reason, SoapReasonDto.class)).toList();

        ReasonResponse reasonResponse = new ReasonResponse();
        reasonResponse.setSoapReasonDto(soapReasonDtoList);
        return reasonResponse;
    }
}
