package com.ironhack.midtermproject.DTO;


import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RoleToUserDTO {
    @NotNull
    private String username;
    @NotNull
    private String roleName;
}
