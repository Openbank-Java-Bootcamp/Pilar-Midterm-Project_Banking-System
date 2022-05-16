package com.ironhack.midtermproject.service.impl;


import com.ironhack.midtermproject.model.*;
import com.ironhack.midtermproject.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {

    @Autowired
    AccountRepository accountRepository;

    public Account createCheckingAccount(Checking checkingAccount){
        Date dob = checkingAccount.getPrimaryOwner().getDateOfBirth();
        Date now = new Date();
        LocalDate localDateDob = dob.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int yearDOB  = localDateDob.getYear();
        LocalDate localDateNow = dob.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int yearNow  = localDateNow.getYear();
        int age = yearNow -yearDOB;
        if(age < 24){
            StudentChecking studentAccount = new StudentChecking(checkingAccount.getBalance(),checkingAccount.getPrimaryOwner(),checkingAccount.getSecondaryOwner(),checkingAccount.getSecretKey());
            return accountRepository.save(studentAccount);
        } else{
            return accountRepository.save(checkingAccount);
        }
    }

    public Savings createSavingsAccount(Savings savingsAccount){
        return accountRepository.save(savingsAccount);
    }

    public CreditCard createCreditAccount(CreditCard creditAccount){
        return accountRepository.save(creditAccount);

    }
}
