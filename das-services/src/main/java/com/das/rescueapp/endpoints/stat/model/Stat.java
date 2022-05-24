package com.das.rescueapp.endpoints.stat.model;

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
public class Stat {
    private long userId;
    private Date batchStart;
    private Date batchEnd;
    private long openAssistanceCount;
    private long finishedAssistanceCount;
    private long canceledAssistanceCount;
    private long messageCount;
}
