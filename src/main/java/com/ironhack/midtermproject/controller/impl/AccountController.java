package com.ironhack.midtermproject.controller.impl;

import com.ironhack.midtermproject.controller.interfaces.IAccountController;
import com.ironhack.midtermproject.model.Account;
import com.ironhack.midtermproject.model.Checking;
import com.ironhack.midtermproject.model.CreditCard;
import com.ironhack.midtermproject.model.Savings;
import com.ironhack.midtermproject.service.impl.AccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accounts")
public class AccountController implements IAccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping("/checking")
    @ResponseStatus(HttpStatus.CREATED)
    public void createCheckingAccount(@RequestBody Checking checkingAccount){
        accountService.createCheckingAccount(checkingAccount);
    }

    @PostMapping("/saving")
    @ResponseStatus(HttpStatus.CREATED)
    public void createSavingsAccount(Savings savingsAccount){
        accountService.createSavingsAccount(savingsAccount);
    }

    @PostMapping("/credit")
    @ResponseStatus(HttpStatus.CREATED)
    public void createCreditAccount(CreditCard creditAccount){
        accountService.createCreditAccount(creditAccount);
    }
}
