package com.das.rescueapp.endpoints.message;

import com.das.rescueapp.core.config.constant.Routes;
import com.das.rescueapp.endpoints.message.dto.MessageDto;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("")
@Api(tags = "Message")
@Tag(name = "Message")
public class MessageController {
    private final Logger logger = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    private MessageService messageService;

    @PostMapping(value = Routes.Api.Message.SEND_MESSAGE)
    public Boolean sendMessage(@Valid @RequestBody MessageDto messageDto) {
//        this.logger.info("Begun method sendMessage - MessageDto: {}", messageDto);
        return messageService.insertMessage(messageDto);
    }

    @GetMapping(value = Routes.Api.Message.GET_MESSAGE)
    public List<MessageDto> getMessage(@PathVariable Long assistanceId) {
//        this.logger.info("Begun method getMessage - AssistanceId: {}", assistanceId);
        return messageService.getMessage(assistanceId);
    }
}
