package com.ironhack.midtermproject.controller.impl;

import com.ironhack.midtermproject.DTO.*;
import com.ironhack.midtermproject.controller.interfaces.IAccountController;
import com.ironhack.midtermproject.enums.Status;
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
    public void createCheckingAccount(@RequestBody @Valid CheckingAccountDTO checkingAccountDTO){
        accountService.createCheckingAccount(checkingAccountDTO);
    }

    @PostMapping("/saving") //only admin??
    @ResponseStatus(HttpStatus.CREATED)
    public void createSavingsAccount(@RequestBody @Valid SavingAccountDTO savingAccountDTO){
        accountService.createSavingsAccount(savingAccountDTO);
    }

    @PostMapping("/credit") //only admin??
    @ResponseStatus(HttpStatus.CREATED)
    public void createCreditAccount(@RequestBody @Valid CreditAccountDTO creditAccountDTO){
        accountService.createCreditAccount(creditAccountDTO);
    }

    @PatchMapping("/transfer")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void transferMoney(@RequestBody @Valid OwnerTransferDTO ownerTransferDTO){
        accountService.transferMoney(ownerTransferDTO);
    }


    @GetMapping("/balance") //esto el accountholder
    @ResponseStatus(HttpStatus.OK)
    public Money getBalance(@RequestParam Long accountId){
        return accountService.getBalance(accountId);
    }

    @GetMapping("/adminbalance") //esto solo los admin
    @ResponseStatus(HttpStatus.OK)
    public Money getBalanceAdmin(@RequestParam Long accountId){
        return accountService.getBalanceAdmin(accountId);
    }

    @PatchMapping("/setbalance") //esto solo los admin
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changeBalance(@RequestParam Long accountId,@RequestParam BigDecimal newBalance){
        accountService.changeBalance(accountId, newBalance);
    }

    @PatchMapping("/status") //esto solo los admin
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changeStatus(@RequestParam Long accountId, @RequestParam String status){
        accountService.changeStatus(accountId, Status.valueOf(status));
    }

    @DeleteMapping("/account")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAccount(@RequestParam Long accountId){
        accountService.deleteAccount(accountId);
    }



}
