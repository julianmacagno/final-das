package com.das.entities.endpoints.rest.assistance.model;


import com.das.entities.commons.enums.AssistanceStatus;
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
    private String cancellationReason;
    private String valoration;

    public Assistance(Long assistanceId, Long userId, AssistanceStatus assistanceStatus) {
        this.id = assistanceId;
        this.userId = userId;
        this.assistanceStatus = assistanceStatus;
    }

    public Assistance(Long userId, AssistanceStatus assistanceStatus) {
        this.userId = userId;
        this.assistanceStatus = assistanceStatus;
    }

    public Assistance(Long assistanceId, String valoration) {
        this.id = assistanceId;
        this.valoration = valoration;
    }
}
