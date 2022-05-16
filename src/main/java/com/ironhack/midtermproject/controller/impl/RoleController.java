package com.ironhack.midtermproject.controller.impl;


import com.ironhack.midtermproject.DTO.RoleToUserDTO;
import com.ironhack.midtermproject.controller.interfaces.IRoleController;
import com.ironhack.midtermproject.model.Role;
import com.ironhack.midtermproject.service.impl.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RoleController implements IRoleController {

    @Autowired
    RoleService roleService;

    @PostMapping("/roles")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveRole(Role role){
        roleService.saveRole(role);
    }


    @PostMapping("/roles/addtouser")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addRoleToUser(RoleToUserDTO roleToUserDto){
        roleService.addRoleToUser(roleToUserDto.getUsername(),roleToUserDto.getRoleName());
    }
}
