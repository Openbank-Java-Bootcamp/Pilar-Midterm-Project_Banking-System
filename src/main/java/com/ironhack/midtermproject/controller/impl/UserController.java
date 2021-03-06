package com.ironhack.midtermproject.controller.impl;


import com.ironhack.midtermproject.controller.interfaces.IUserController;
import com.ironhack.midtermproject.model.AccountHolder;
import com.ironhack.midtermproject.model.Admin;
import com.ironhack.midtermproject.model.Checking;
import com.ironhack.midtermproject.model.User;
import com.ironhack.midtermproject.service.impl.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController implements IUserController {

    @Autowired
    UserService userService;

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public void createAccountHolder(@RequestBody @Valid AccountHolder accountHolder){
        userService.createAccountHolder(accountHolder);
    }

    @PostMapping("/admin")
    @ResponseStatus(HttpStatus.CREATED)
    public void createAdmin(@RequestBody @Valid Admin admin){
        userService.createAdmin(admin);
    }

    @DeleteMapping("/users")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteUser(@RequestParam Long userId){
        userService.deleteUser(userId);
    }
}
