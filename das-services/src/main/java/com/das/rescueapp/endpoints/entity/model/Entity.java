package com.das.rescueapp.endpoints.entity.model;

import com.das.rescueapp.core.services.entity.enums.ApiType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Entity {
    private Long id;
    private String phone;
    private String name;
    private Boolean available;
    private String url;
    private Boolean emergencyService;
    private ApiType apiType;
    private String username;
    private String password;

    public Entity(Long entityId, Boolean available) {
        this.id = entityId;
        this.available = available;
    }

    public Entity(Boolean emergencyService) {
        this.emergencyService = emergencyService;
    }

    public Entity(Boolean available, Boolean emergencyService) {
        this.available = available;
        this.emergencyService = emergencyService;
    }
}
