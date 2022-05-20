package com.ironhack.midtermproject.service.interfaces;

import com.ironhack.midtermproject.model.Account;

import java.math.BigDecimal;

public interface ITransferService {
    void fraudDetectionOne(Account currentAccount, BigDecimal transferAmount);
    boolean fraudDetectionTwo(Account currentAccount, BigDecimal transferAmount);
}
