package com.das.rescueapp.endpoints.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EntityDto {
    private Long id;
    private String phone;
    private String name;
    private Boolean available;
}
