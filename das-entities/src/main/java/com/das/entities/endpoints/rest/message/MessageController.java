package com.das.entities.endpoints.rest.message;

import com.das.entities.core.config.constant.Routes;
import com.das.entities.endpoints.rest.message.dto.MessageDto;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/")
@Tag(name = "Message")
@Api(tags = "Message")
public class MessageController {
    private final Logger logger = LoggerFactory.getLogger(MessageController.class);
    @Autowired
    MessageService messageService;

    @PostMapping(Routes.Rest.Message.message)
    public List<MessageDto> insertMessage(@RequestBody List<MessageDto> messageDtoList) {
//        this.logger.info("Begun method insertMessage: {}", messageDtoList);
        return this.messageService.insertMessage(messageDtoList);
    }
}
