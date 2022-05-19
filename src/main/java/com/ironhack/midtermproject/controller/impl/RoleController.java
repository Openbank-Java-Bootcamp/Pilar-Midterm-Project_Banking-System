package com.ironhack.midtermproject.controller.impl;


import com.ironhack.midtermproject.DTO.RoleToUserDTO;
import com.ironhack.midtermproject.controller.interfaces.IRoleController;
import com.ironhack.midtermproject.model.Role;
import com.ironhack.midtermproject.service.impl.RoleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class RoleController implements IRoleController {

    @Autowired
    RoleService roleService;

    @PostMapping("/roles")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveRole(@RequestBody @Valid Role role){
        roleService.saveRole(role);
    }


    @PostMapping("/roletouser")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addRoleToUser(@RequestBody @Valid  RoleToUserDTO roleToUserDto){
        roleService.addRoleToUser(roleToUserDto.getUsername(),roleToUserDto.getRoleName());
    }
}
