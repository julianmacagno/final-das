package com.das.rescueapp.endpoints.announcement.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class Announcement {
    private Long id;
    private String content;
}
