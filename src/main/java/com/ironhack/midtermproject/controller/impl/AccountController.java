package com.ironhack.midtermproject.controller.impl;

import com.ironhack.midtermproject.DTO.*;
import com.ironhack.midtermproject.controller.interfaces.IAccountController;
import com.ironhack.midtermproject.model.*;
import com.ironhack.midtermproject.service.impl.AccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api")
public class AccountController implements IAccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping("/checking")
    @ResponseStatus(HttpStatus.CREATED)
    public void createCheckingAccount(@RequestBody CheckingAccountDTO checkingAccountDTO){
        accountService.createCheckingAccount(checkingAccountDTO);
    }

    @PostMapping("/saving") //only admin??
    @ResponseStatus(HttpStatus.CREATED)
    public void createSavingsAccount(@RequestBody SavingAccountDTO savingAccountDTO){
        accountService.createSavingsAccount(savingAccountDTO);
    }

    @PostMapping("/credit") //only admin??
    @ResponseStatus(HttpStatus.CREATED)
    public void createCreditAccount(@RequestBody CreditAccountDTO creditAccountDTO){
        accountService.createCreditAccount(creditAccountDTO);
    }

    @PostMapping("/transfer")
    @ResponseStatus(HttpStatus.OK)
    public void transferMoney(@RequestBody OwnerTransferDTO ownerTransferDTO){
        accountService.transferMoney(ownerTransferDTO);
    }


    @GetMapping("/balance") //esto el accountholder
    @ResponseStatus(HttpStatus.OK)
    public BigDecimal getBalance(){
        return accountService.getBalance();
    }

    @GetMapping("/adminbalance") //esto solo los admin
    @ResponseStatus(HttpStatus.OK)
    public BigDecimal getBalanceA(@RequestParam Long accountId){
        return accountService.getBalance(accountId);
    }

    @PatchMapping("/setbalance") //esto solo los admin
    @ResponseStatus(HttpStatus.OK)
    public void changeBalance(@RequestParam Long accountId,@RequestParam BigDecimal newBalance){
        accountService.changeBalance(accountId, newBalance);
    }



}
