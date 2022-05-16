package com.ironhack.midtermproject.controller.interfaces;

import com.ironhack.midtermproject.model.Account;
import com.ironhack.midtermproject.model.Checking;
import com.ironhack.midtermproject.model.CreditCard;
import com.ironhack.midtermproject.model.Savings;

public interface IAccountController {

    void createCheckingAccount(Checking checkingAccount);
    void createSavingsAccount(Savings savingsAccount);
    void createCreditAccount(CreditCard creditAccount);
}
