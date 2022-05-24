package com.das.rescueapp.endpoints.authentication.model;

import com.das.rescueapp.endpoints.authentication.dto.LoginDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class User {
    private Long id;
    private String cuil;
    private String name;
    private String email;
    private String password;
    private Boolean validated;
    private Boolean canceled;
    private String language;

    public User(LoginDto loginDTO) {
        this.cuil = loginDTO.getCuil();
        this.password = loginDTO.getPassword();
    }

    public User(Long id) {
        this.id = id;
    }
}
