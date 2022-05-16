package com.ironhack.midtermproject.controller.interfaces;

import com.ironhack.midtermproject.model.AccountHolder;
import com.ironhack.midtermproject.model.Admin;
import com.ironhack.midtermproject.model.User;
import org.springframework.web.bind.annotation.RequestBody;

public interface IUserController {

    void createAccountHolder(AccountHolder accountHolder);
    void createAdmin(Admin admin);
}
