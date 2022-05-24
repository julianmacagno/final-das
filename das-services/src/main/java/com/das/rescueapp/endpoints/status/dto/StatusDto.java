package com.das.rescueapp.endpoints.status.dto;

import com.das.rescueapp.endpoints.asistance.dto.AssistanceDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class StatusDto {
    private Long userId;
    private Boolean userCanceled;
    private List<AssistanceDto> currentAssistanceList;
}
