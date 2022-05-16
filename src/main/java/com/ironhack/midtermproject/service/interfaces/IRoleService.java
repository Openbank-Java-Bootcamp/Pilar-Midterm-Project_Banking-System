package com.ironhack.midtermproject.service.interfaces;

import com.ironhack.midtermproject.model.Role;

public interface IRoleService {
    Role saveRole(Role role);
    void addRoleToUser(String username, String roleName);
}
