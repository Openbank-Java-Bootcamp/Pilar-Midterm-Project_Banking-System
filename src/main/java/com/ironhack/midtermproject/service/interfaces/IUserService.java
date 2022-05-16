package com.ironhack.midtermproject.service.interfaces;

import com.ironhack.midtermproject.model.AccountHolder;
import com.ironhack.midtermproject.model.Admin;

public interface IUserService {
    AccountHolder createAccountHolder(AccountHolder accountHolder);
    Admin createAdmin(Admin admin);
}
