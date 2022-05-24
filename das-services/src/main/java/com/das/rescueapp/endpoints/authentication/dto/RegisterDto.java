package com.das.rescueapp.endpoints.authentication.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class RegisterDto {
    private String cuil;
    private String name;
    private String email;
    private String password;
    private String language;
}
