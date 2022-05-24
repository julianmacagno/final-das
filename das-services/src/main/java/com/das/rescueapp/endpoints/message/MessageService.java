package com.das.rescueapp.endpoints.message;

import com.das.rescueapp.commons.enums.IssueType;
import com.das.rescueapp.commons.exceptions.PortalError;
import com.das.rescueapp.commons.exceptions.RescueAppException;
import com.das.rescueapp.core.config.context.Mapper;
import com.das.rescueapp.core.dao.DaoFactory;
import com.das.rescueapp.core.services.entity.EntityStrategy;
import com.das.rescueapp.endpoints.asistance.AssistanceService;
import com.das.rescueapp.endpoints.asistance.model.Assistance;
import com.das.rescueapp.endpoints.entity.EntityDao;
import com.das.rescueapp.endpoints.entity.model.Entity;
import com.das.rescueapp.endpoints.message.dto.MessageDto;
import com.das.rescueapp.endpoints.message.model.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {
    private final Logger logger = LoggerFactory.getLogger(MessageService.class);
    @Autowired
    private Mapper mapper;
    @Autowired
    private AssistanceService assistanceService;
    @Autowired
    private EntityStrategy entityStrategy;

    public MessageService() {
    }

    public Boolean insertMessage(MessageDto messageDto) {
        this.logger.info("Begun method insertMessage - MessageDto: {}", messageDto);
        switch (messageDto.getMessageType()) {
            case CREATION:
                Assistance assistance = assistanceService.createAsistance(messageDto.getEntityId(), messageDto.getUserId());
                messageDto.setAssistanceId(assistance.getId());
                break;
            case CANCELATION:
                assistanceService.cancelCurrentAssistance(messageDto.getAssistanceId());
                break;
            case FINALIZATION:
                assistanceService.finalizeCurrentAssistance(messageDto.getAssistanceId());
                break;
            case MESSAGE:
                break;
        }

        Message message = (Message) this.mapper.map(messageDto, Message.class);
        try {
            MessageDao messageDao = (MessageDao) DaoFactory.getDao(MessageDao.class);
            messageDao.insert(message);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RescueAppException(IssueType.INTERNAL_SERVER, PortalError.ERROR_INSERTING_MESSAGE);
        }
    }

    public List<MessageDto> getMessage(Long assistanceId) {
//        this.logger.info("Begun method getMessage - AssistanceId: {}", assistanceId);
        Message message = new Message(assistanceId);
        try {
            MessageDao messageDao = (MessageDao) DaoFactory.getDao(MessageDao.class);
            List<Message> messageList = messageDao.select(message);
            return messageList.stream().map(messageToMap -> (MessageDto) this.mapper.map(messageToMap, MessageDto.class)).toList();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RescueAppException(IssueType.INTERNAL_SERVER, PortalError.MESSAGE_NOT_FOUND);
        }
    }

    @Scheduled(cron = "*/5 * * * * *")
    private void synchronizeMessage() {
//        this.logger.info("Begun method synchronizeMessage");
        try {
            MessageDao messageDao = (MessageDao) DaoFactory.getDao(MessageDao.class);
            EntityDao entityDao = (EntityDao) DaoFactory.getDao(EntityDao.class);
            List<Message> messageList = messageDao.getNotSynchronizedMessages();
            List<MessageDto> messageDtoList = messageList.stream()
                    .map(message -> (MessageDto) this.mapper.map(message, MessageDto.class)).toList();

            List<Entity> entityList = entityDao.select(new Entity(true, true));
            entityList.forEach(entity -> {
                List<MessageDto> messageListToSend = messageDtoList.stream()
                        .filter(message -> message.getEntityId().equals(entity.getId())).toList();

                try {
                    List<MessageDto> responseMessageList = this.entityStrategy.synchronizeMessage(entity, messageListToSend);
                    if (responseMessageList != null) {
                        messageDao.markAsSynchronized(entity.getId());
                        responseMessageList.forEach(messageDto -> {
                            messageDto.setEntityId(entity.getId());
                            messageDto.setIsSynchronized(true);
                            messageDto.setIsFromUser(false);
                            this.insertMessage(messageDto);
                        });
                    }
                } catch (Exception e) {
                    this.logger.error(e.getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
