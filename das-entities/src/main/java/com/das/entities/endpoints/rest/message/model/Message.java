package com.das.entities.endpoints.rest.message.model;


import com.das.entities.commons.enums.MessageType;
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
public class Message {
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
}
