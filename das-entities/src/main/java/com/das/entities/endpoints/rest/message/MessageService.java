package com.das.entities.endpoints.rest.message;

import com.das.entities.commons.enums.MessageType;
import com.das.entities.core.config.context.Mapper;
import com.das.entities.core.dao.DaoFactory;
import com.das.entities.core.utils.DateUtils;
import com.das.entities.endpoints.rest.assistance.AssistanceService;
import com.das.entities.endpoints.rest.message.dto.MessageDto;
import com.das.entities.endpoints.rest.message.model.Message;
import com.das.soap.SoapMessageDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class MessageService {
    private final Logger logger = LoggerFactory.getLogger(MessageService.class);
    private MessageDao messageDao = null;
    @Autowired
    private Mapper mapper;
    @Autowired
    private AssistanceService assistanceService;

    public MessageService() {
        try {
            this.messageDao = (MessageDao) DaoFactory.getDao(MessageDao.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<MessageDto> insertMessage(List<MessageDto> messageDtoList) {
        this.logger.info("Begun method insertMessage: MessageDtoList: {}", messageDtoList);
        List<Message> messageList = messageDtoList.stream()
                .map(messageDto -> (Message) mapper.map(messageDto, Message.class))
                .toList();

        messageList.stream()
                .filter(message -> message.getMessageType().equals(MessageType.CREATION))
                .forEach(message -> {
                    this.assistanceService.createAsistance(message.getAssistanceId(), message.getUserId());
                });

        messageList.stream()
                .filter(message -> message.getMessageType().equals(MessageType.CANCELATION))
                .forEach(message -> {
                    this.assistanceService.cancelAsistance(message.getUserId(), message.getPayload());
                });

        messageList.stream()
                .filter(message -> message.getMessageType().equals(MessageType.VALORATION))
                .forEach(message -> {
                    this.assistanceService.valorateAssistance(message.getAssistanceId(), message.getPayload());
                });

        messageList.forEach(message -> {
            try {
                // TODO: Manejar caso cuando algun mensaje falla? Deberia reintentar o avisar al back que no se envio
                message.setIsSynchronized(true);
                this.messageDao.insert(message);
            } catch (SQLException e) {
                this.logger.error(e.getMessage());
                e.printStackTrace();
            }
        });

        try {
            messageList = this.messageDao.markAsSynchronizedAndRetrieve();
            return messageList.stream().map(message -> (MessageDto) this.mapper.map(message, MessageDto.class)).toList();
        } catch (SQLException e) {
            this.logger.error(e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<SoapMessageDto> insertSoapMessage(List<SoapMessageDto> soapMessageDtoList) {
        List<MessageDto> requestMessageDtoList = soapMessageDtoList.stream().map(MessageDto::new).toList();
        List<MessageDto> responseMessageDtoList = this.insertMessage(requestMessageDtoList);
        return responseMessageDtoList.stream().map(this::newSoapMessageDto).toList();
    }

    private SoapMessageDto newSoapMessageDto(MessageDto messageDto) {
        SoapMessageDto soapMessageDto = new SoapMessageDto();
        soapMessageDto.setId(messageDto.getId());
        soapMessageDto.setAssistanceId(messageDto.getAssistanceId());
        soapMessageDto.setUserId(messageDto.getUserId());
        soapMessageDto.setEntityId(messageDto.getEntityId());
        soapMessageDto.setPayload(messageDto.getPayload());
        soapMessageDto.setAttachment(messageDto.getAttachment());
        soapMessageDto.setMessageType(messageDto.getMessageType().getValue());
        soapMessageDto.setTimestamp(DateUtils.dateToXmlGregorianCalendar(messageDto.getTimestamp()));
        soapMessageDto.setIsFromUser(messageDto.getIsFromUser());
        soapMessageDto.setIsSynchronized(messageDto.getIsSynchronized());
        return soapMessageDto;
    }
}
