package com.das.rescueapp.endpoints.announcement.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class AnnouncementDto {
    private Long id;
    private String content;
}
