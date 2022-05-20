package com.ironhack.midtermproject.service.interfaces;

import com.ironhack.midtermproject.DTO.CheckingAccountDTO;
import com.ironhack.midtermproject.DTO.CreditAccountDTO;
import com.ironhack.midtermproject.DTO.OwnerTransferDTO;
import com.ironhack.midtermproject.DTO.SavingAccountDTO;
import com.ironhack.midtermproject.model.*;

import java.math.BigDecimal;

public interface IAccountService {
    Account createCheckingAccount(CheckingAccountDTO account);
    Savings createSavingsAccount(SavingAccountDTO savingsAccount);
    CreditCard createCreditAccount(CreditAccountDTO creditAccount);
    void transferMoney(OwnerTransferDTO ownerTransferDTO);
    void checkInterestAndMaintenance(Account account);
    Money getBalance(Long accountId);
    Money getBalanceAdmin(Long accountId);
    void changeBalance(Long accountId, BigDecimal newBalance);
    void deleteAccount(Long accountId);

}
