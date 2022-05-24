package com.das.entities.endpoints.rest.assistance.dto;

import com.das.entities.commons.enums.AssistanceStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class AssistanceDto {
    private Long id;
    private Long entityId;
    private Long userId;
    private AssistanceStatus assistanceStatus;
    private String cancellationReason;
    private String valoration;
}
