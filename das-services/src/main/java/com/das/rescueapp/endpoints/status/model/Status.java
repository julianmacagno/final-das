package com.das.rescueapp.endpoints.status.model;

import com.das.rescueapp.endpoints.asistance.model.Assistance;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class Status {
    private Long userId;
    private Boolean userCanceled;
    private List<Assistance> currentAssistanceList;

    public Status(Long userId) {
        this.userId = userId;
    }
}
