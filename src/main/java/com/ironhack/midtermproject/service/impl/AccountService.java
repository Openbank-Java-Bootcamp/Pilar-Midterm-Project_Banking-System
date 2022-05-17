package com.ironhack.midtermproject.service.impl;


import com.ironhack.midtermproject.DTO.OwnerTransferDTO;
import com.ironhack.midtermproject.model.*;
import com.ironhack.midtermproject.repository.AccountRepository;
import com.ironhack.midtermproject.repository.TransferRepository;
import com.ironhack.midtermproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
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
        LocalDate dob = checkingAccount.getPrimaryOwner().getDateOfBirth();
        LocalDate now = LocalDate.now();
        int yearDOB = dob.getMonthValue();
        int yearNow = now.getMonthValue();
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
        if(currentAccount instanceof Savings && currentAccount.getBalance().getAmount().compareTo(((Savings) currentAccount).getMinimumBalance().getAmount()) == -1){
            currentAccount.setBalance(new Money(currentAccount.getBalance().getAmount().subtract(currentAccount.getPenaltyFee().getAmount()),currentAccount.getBalance().getCurrency()));
        } else if(currentAccount instanceof Checking && currentAccount.getBalance().getAmount().compareTo(((Checking) currentAccount).getMinimumBalance().getAmount()) == -1){
            currentAccount.setBalance(new Money(currentAccount.getBalance().getAmount().subtract(currentAccount.getPenaltyFee().getAmount()),currentAccount.getBalance().getCurrency()));
        }

        //comprobar fraude, cada vez que se hace una transferencia agregarla a la tabla transfer
        Transfer transfer = new Transfer(currentAccount.getId(),ownerTransferDTO.getAmount(),new Date());
        transferRepository.save(transfer);

        //tengo que agrupar por dia y calcular el maximo amount transferido en un dia, y comparar con el total del dia en que se esta haciendo la transferencia.
        //tambien comparar la diferencia de tiempo con la ultima transferencia, seleccionar la fecha mas grande¿¿???


        //me falta guardar las cuentas actualizadas tambien
        accountRepository.save(currentAccount);
        accountRepository.save(targetAccount);
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

        //si la cuenta es savings o credit comparar la fecha de hoy con la fecha de creacion o desde que fue agregado interes.
        //antes de devolver el balance ver si hay que agregar interes
        //si la cuenta es checking ver si se cobro el interes mensual
        if(currentAccount instanceof Savings) {
            int year1 = ((Savings) currentAccount).getAddedInterest().getYear();
            int year2 = ((Savings) currentAccount).getCreationDate().getYear();
            int actualYear = LocalDate.now().getYear();
            if(year1!=actualYear || year2!=actualYear){
                BigDecimal interest = currentAccount.getBalance().getAmount().multiply(((Savings) currentAccount).getInterestRate());
                currentAccount.setBalance(new Money(currentAccount.getBalance().getAmount().add(interest),Currency.getInstance("EUR")));
                //me falta guardar la cuenta actualizada
                accountRepository.save(currentAccount);
            }
        } else if(currentAccount instanceof CreditCard){
            int month1 = ((CreditCard) currentAccount).getAddedInterest().getMonthValue();
            int month2 = ((CreditCard) currentAccount).getCreationDate().getMonthValue();
            int actualMonth = LocalDate.now().getMonthValue();
            if(month1!=actualMonth || month2!=actualMonth){
                BigDecimal monthRate = ((CreditCard) currentAccount).getInterestRate().divide(new BigDecimal(12));
                BigDecimal interest = currentAccount.getBalance().getAmount().multiply(monthRate);
                currentAccount.setBalance(new Money(currentAccount.getBalance().getAmount().add(interest),Currency.getInstance("EUR")));
                //me falta guardar la cuenta actualizada
                accountRepository.save(currentAccount);
            }
        } else if(currentAccount instanceof Checking){
            int month1 = ((Checking) currentAccount).getMaintenanceCharged().getMonthValue();
            int month2 = ((Checking) currentAccount).getCreationDate().getMonthValue();
            int actualMonth = LocalDate.now().getMonthValue();
            if(month1!=actualMonth || month2!=actualMonth){
                currentAccount.setBalance(new Money(currentAccount.getBalance().getAmount().add(((Checking) currentAccount).getMonthlyMaintenanceFee().getAmount()),Currency.getInstance("EUR")));
                //me falta guardar la cuenta actualizada
                accountRepository.save(currentAccount);
            }
        }

        return currentAccount.getBalance().getAmount();

    }

    public BigDecimal getBalance(Long accountId){
        Account accountFromDB = accountRepository.findById(accountId).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "Id not found"));
        //si la cuenta es savings o credit comparar la fecha de hoy con la fecha de creacion o desde que fue agregado interes.
        //antes de devolver el balance ver si hay que agregar interes
        //si la cuenta es checking ver si se cobro el interes mensual
        if(accountFromDB instanceof Savings) {
            int year1 = ((Savings) accountFromDB).getAddedInterest().getYear();
            int year2 = ((Savings) accountFromDB).getCreationDate().getYear();
            int actualYear = LocalDate.now().getYear();
            if(year1!=actualYear || year2!=actualYear){
                BigDecimal interest = accountFromDB.getBalance().getAmount().multiply(((Savings) accountFromDB).getInterestRate());
                accountFromDB.setBalance(new Money(accountFromDB.getBalance().getAmount().add(interest),Currency.getInstance("EUR")));
                //me falta guardar la cuenta actualizada
                accountRepository.save(accountFromDB);
            }
        } else if(accountFromDB instanceof CreditCard){
            int month1 = ((CreditCard) accountFromDB).getAddedInterest().getMonthValue();
            int month2 = ((CreditCard) accountFromDB).getCreationDate().getMonthValue();
            int actualMonth = LocalDate.now().getMonthValue();
            if(month1!=actualMonth || month2!=actualMonth){
                BigDecimal monthRate = ((CreditCard) accountFromDB).getInterestRate().divide(new BigDecimal(12));
                BigDecimal interest = accountFromDB.getBalance().getAmount().multiply(monthRate);
                accountFromDB.setBalance(new Money(accountFromDB.getBalance().getAmount().add(interest),Currency.getInstance("EUR")));
                //me falta guardar la cuenta actualizada
                accountRepository.save(accountFromDB);
            }
        } else if(accountFromDB instanceof Checking){
            int month1 = ((Checking) accountFromDB).getMaintenanceCharged().getMonthValue();
            int month2 = ((Checking) accountFromDB).getCreationDate().getMonthValue();
            int actualMonth = LocalDate.now().getMonthValue();
            if(month1!=actualMonth || month2!=actualMonth){
                accountFromDB.setBalance(new Money(accountFromDB.getBalance().getAmount().add(((Checking) accountFromDB).getMonthlyMaintenanceFee().getAmount()),Currency.getInstance("EUR")));
            }
            //me falta guardar la cuenta actualizada
            accountRepository.save(accountFromDB);
        }
        return accountFromDB.getBalance().getAmount();
    }

    public void changeBalance(Long accountId, BigDecimal newBalance){
        Account accountFromDB = accountRepository.findById(accountId).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "Id not found"));
        accountFromDB.setBalance(new Money(newBalance,accountFromDB.getBalance().getCurrency()));
        accountRepository.save(accountFromDB);
    }
}

