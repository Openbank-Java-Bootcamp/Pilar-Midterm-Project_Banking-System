package com.ironhack.midtermproject.controller.impl;

import com.ironhack.midtermproject.DTO.OwnerTransferDTO;
import com.ironhack.midtermproject.DTO.ThirdPartyTransferDTO;
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

    @PostMapping("/transfer")
    @ResponseStatus(HttpStatus.OK)
    public void transferMoney(OwnerTransferDTO ownerTransferDTO){
        accountService.transferMoney(ownerTransferDTO);
    }


    @GetMapping("/balance") //esto el accountholder
    @ResponseStatus(HttpStatus.OK)
    public BigDecimal getBalance(){
        return accountService.getBalance();
    }

    @GetMapping("/balance/{Id}") //esto solo los admin
    @ResponseStatus(HttpStatus.OK)
    public BigDecimal getBalanceA(Long accountId){
        return accountService.getBalance(accountId);
    }

    @PatchMapping("/setbalance") //esto solo los admin
    @ResponseStatus(HttpStatus.OK)
    public void changeBalance(Long accountId, BigDecimal newBalance){
        accountService.changeBalance(accountId, newBalance);
    }



}
