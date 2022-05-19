package com.ironhack.midtermproject.service.impl;


import com.ironhack.midtermproject.model.Role;
import com.ironhack.midtermproject.model.User;
import com.ironhack.midtermproject.repository.RoleRepository;
import com.ironhack.midtermproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    public Role saveRole(Role role){
        log.info("Saving a new role {} to the database",role.getName());
        return roleRepository.save(role);
    }

    public void addRoleToUser(String username, String roleName){
        User user = userRepository.findByUsername(username);
        Role role = roleRepository.findByName(roleName);
        if(user==null || role==null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"User or Role not found");
        }
        user.getRoles().add(role);
        userRepository.save(user);
    }
}
