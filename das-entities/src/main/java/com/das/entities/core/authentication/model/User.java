package com.das.entities.core.authentication.model;

import com.das.entities.core.authentication.dto.LoginDto;
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
    Long id;
    String cuil;
    String name;
    String email;
    String password;
    Boolean validated;
    Boolean canceled;

    public User(LoginDto loginDTO) {
        this.cuil = loginDTO.getUsername();
        this.password = loginDTO.getPassword();
    }

    public User(Long id) {
        this.id = id;
    }
}
