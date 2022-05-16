package com.ironhack.midtermproject.service.impl;


import com.ironhack.midtermproject.DTO.OwnerTransferDTO;
import com.ironhack.midtermproject.model.*;
import com.ironhack.midtermproject.repository.AccountRepository;
import com.ironhack.midtermproject.repository.TransferRepository;
import com.ironhack.midtermproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Currency;
import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TransferRepository transferRepository;

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

    public void transferMoney(OwnerTransferDTO ownerTransferDTO){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = null;
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            currentUsername = authentication.getName();
        }
        User currentUser = userRepository.findByUsername(currentUsername);
        Long accountHolderId = currentUser.getId();
        Account currentAccount = accountRepository.findByPrimaryOwnerId(accountHolderId).orElse(null);
        if(currentAccount == null){
            currentAccount = accountRepository.findBySecondaryOwnerId(accountHolderId).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));
        }
        //buscar la cuenta destino antes de hacer la transferencia
        Account targetAccount = accountRepository.findById(ownerTransferDTO.getTargetAccountId()).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "Id not found"));
        if(targetAccount.getPrimaryOwner().equals(ownerTransferDTO.getOwnerTargetId()) || targetAccount.getSecondaryOwner().equals(ownerTransferDTO.getOwnerTargetId())){
            //ahora intentar la transferencia
            BigDecimal actualBalance = currentAccount.getBalance().getAmount();
            if(actualBalance.compareTo(ownerTransferDTO.getAmount()) == -1){
                throw new IllegalArgumentException("Not sufficient funds");
            } else {
                currentAccount.setBalance(new Money(actualBalance.subtract(ownerTransferDTO.getAmount()),Currency.getInstance("EUR")));
                targetAccount.setBalance(new Money(targetAccount.getBalance().getAmount().add(ownerTransferDTO.getAmount()),Currency.getInstance("EUR")));
            }
        } else {
            throw new IllegalArgumentException("The provided id or owner of the target account is wrong");
        }
        //tambien necesito saber el tipo de la cuenta de origen para ver si baja del minimumbalance
        if(currentAccount instanceof Savings && currentAccount.getBalance().getAmount().compareTo(((Savings) currentAccount).getMinimumBalance()) == -1){
            currentAccount.setBalance(new Money(currentAccount.getBalance().getAmount().subtract(currentAccount.getPenaltyFee()),Currency.getInstance("EUR")));
        } else if(currentAccount instanceof Checking && currentAccount.getBalance().getAmount().compareTo(((Checking) currentAccount).getMinimumBalance()) == -1){
            currentAccount.setBalance(new Money(currentAccount.getBalance().getAmount().subtract(currentAccount.getPenaltyFee()),Currency.getInstance("EUR")));
        }

        //comprobar fraude, cada vez que se hace una transferencia agregarla a la tabla transfer
        Transfer transfer = new Transfer(currentAccount.getId(),ownerTransferDTO.getAmount(),new Date());
        transferRepository.save(transfer);

        //tengo que agrupar por dia y calcular el maximo amount transferido en un dia, y comparar con el total del dia en que se esta haciendo la transferencia.
        //tambien comparar la diferencia de tiempo con la ultima transferencia, seleccionar la fecha mas grande¿¿???
    }

    public BigDecimal getBalance(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = null;
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            currentUsername = authentication.getName();
        }
        User currentUser = userRepository.findByUsername(currentUsername);
        Long accountHolderId = currentUser.getId();
        Account currentAccount = accountRepository.findByPrimaryOwnerId(accountHolderId).orElse(null);
        if(currentAccount == null){
            currentAccount = accountRepository.findBySecondaryOwnerId(accountHolderId).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));
        }
        return currentAccount.getBalance().getAmount();

        //si la cuenta es savings o credit comparar la fecha de hoy con la fecha de creacion o desde que fue agregado interes.
    }

    public BigDecimal getBalance(Long accountId){
        Account accountFromDB = accountRepository.findById(accountId).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "Id not found"));
        return accountFromDB.getBalance().getAmount();
    }

    public void changeBalance(Long accountId, BigDecimal newBalance){
        Account accountFromDB = accountRepository.findById(accountId).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "Id not found"));
        accountFromDB.setBalance(new Money(newBalance,Currency.getInstance("EUR")));
        accountRepository.save(accountFromDB);
    }
}

