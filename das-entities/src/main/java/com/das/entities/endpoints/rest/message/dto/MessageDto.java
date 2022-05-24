package com.das.entities.endpoints.rest.message.dto;


import com.das.entities.commons.enums.MessageType;
import com.das.entities.core.utils.DateUtils;
import com.das.soap.SoapMessageDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class MessageDto {
    private Long id;
    private Long assistanceId;
    private Long userId;
    private Long entityId;
    private String payload;
    private String attachment;
    private MessageType messageType;
    private Date timestamp;
    private Boolean isFromUser;
    private Boolean isSynchronized;

    public MessageDto(SoapMessageDto soapMessageDto) {
        this.id = soapMessageDto.getId();
        this.assistanceId = soapMessageDto.getAssistanceId();
        this.userId = soapMessageDto.getUserId();
        this.entityId = soapMessageDto.getEntityId();
        this.payload = soapMessageDto.getPayload();
        this.attachment = soapMessageDto.getAttachment();
        this.messageType = MessageType.valueOf(soapMessageDto.getMessageType());
        this.timestamp = DateUtils.xmlGregorianCalendarToDate(soapMessageDto.getTimestamp());
        this.isFromUser = soapMessageDto.isIsFromUser();
        this.isSynchronized = soapMessageDto.isIsSynchronized();
    }
}
