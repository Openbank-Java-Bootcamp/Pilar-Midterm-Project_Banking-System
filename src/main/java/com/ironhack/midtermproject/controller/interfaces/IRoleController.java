package com.ironhack.midtermproject.controller.interfaces;

import com.ironhack.midtermproject.DTO.RoleToUserDTO;
import com.ironhack.midtermproject.model.Role;

public interface IRoleController {
    void saveRole(Role role);
    void addRoleToUser(RoleToUserDTO roleToUserDto);
}
