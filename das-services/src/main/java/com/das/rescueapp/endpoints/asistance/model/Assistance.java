package com.das.rescueapp.endpoints.asistance.model;


import com.das.rescueapp.commons.enums.AssistanceStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class Assistance {
    private Long id;
    private Long entityId;
    private Long userId;
    private AssistanceStatus assistanceStatus;
    private String entityName;
    private String timestamp;

    public Assistance(Long entityId, Long userId, AssistanceStatus assistanceStatus) {
        this.entityId = entityId;
        this.userId = userId;
        this.assistanceStatus = assistanceStatus;
    }

    public Assistance(Long id, AssistanceStatus assistanceStatus) {
        this.id = id;
        this.assistanceStatus = assistanceStatus;
    }

    public Assistance(Long userId) {
        this.userId = userId;
    }
}
