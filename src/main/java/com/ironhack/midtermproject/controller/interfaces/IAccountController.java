package com.ironhack.midtermproject.controller.interfaces;

import com.ironhack.midtermproject.DTO.CheckingAccountDTO;
import com.ironhack.midtermproject.DTO.CreditAccountDTO;
import com.ironhack.midtermproject.DTO.OwnerTransferDTO;
import com.ironhack.midtermproject.DTO.SavingAccountDTO;
import com.ironhack.midtermproject.model.Account;
import com.ironhack.midtermproject.model.Checking;
import com.ironhack.midtermproject.model.CreditCard;
import com.ironhack.midtermproject.model.Savings;

public interface IAccountController {

    void createCheckingAccount(CheckingAccountDTO checkingAccountDTO);
    void createSavingsAccount(SavingAccountDTO savingAccountDTO);
    void createCreditAccount(CreditAccountDTO creditAccountDTO);
    void transferMoney(OwnerTransferDTO ownerTransferDTO);
}
