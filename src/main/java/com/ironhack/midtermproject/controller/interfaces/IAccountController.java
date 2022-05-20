package com.ironhack.midtermproject.controller.interfaces;

import com.ironhack.midtermproject.DTO.CheckingAccountDTO;
import com.ironhack.midtermproject.DTO.CreditAccountDTO;
import com.ironhack.midtermproject.DTO.OwnerTransferDTO;
import com.ironhack.midtermproject.DTO.SavingAccountDTO;
import com.ironhack.midtermproject.model.*;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

public interface IAccountController {

    void createCheckingAccount(CheckingAccountDTO checkingAccountDTO);
    void createSavingsAccount(SavingAccountDTO savingAccountDTO);
    void createCreditAccount(CreditAccountDTO creditAccountDTO);
    void transferMoney(OwnerTransferDTO ownerTransferDTO);
    Money getBalance(Long accountId);
    Money getBalanceAdmin(Long accountId);
    void changeBalance(Long accountId,BigDecimal newBalance);
    void deleteAccount(Long accountId);
}
