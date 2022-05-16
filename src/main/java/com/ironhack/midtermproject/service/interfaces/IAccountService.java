package com.ironhack.midtermproject.service.interfaces;

import com.ironhack.midtermproject.DTO.OwnerTransferDTO;
import com.ironhack.midtermproject.model.Account;
import com.ironhack.midtermproject.model.Checking;
import com.ironhack.midtermproject.model.CreditCard;
import com.ironhack.midtermproject.model.Savings;

public interface IAccountService {
    Account createCheckingAccount(Account account);
    Savings createSavingsAccount(Savings savingsAccount);

    CreditCard createCreditAccount(CreditCard creditAccount);
    void transferMoney(OwnerTransferDTO ownerTransferDTO);

}
