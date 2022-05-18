package com.ironhack.midtermproject.service.impl;


import com.ironhack.midtermproject.DTO.CheckingAccountDTO;
import com.ironhack.midtermproject.DTO.CreditAccountDTO;
import com.ironhack.midtermproject.DTO.OwnerTransferDTO;
import com.ironhack.midtermproject.DTO.SavingAccountDTO;
import com.ironhack.midtermproject.enums.Status;
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
import java.time.*;
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

    public Account createCheckingAccount(CheckingAccountDTO checkingAccountDTO){
        User primaryOwner =userRepository.findById(checkingAccountDTO.getPrimaryAccountOwnerId()).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "Primary Owner id not found"));
        User secondaryOwner = null;
        if(checkingAccountDTO.getSecondaryAccountOwnerId() != null){
            secondaryOwner = userRepository.findById(checkingAccountDTO.getSecondaryAccountOwnerId()).orElse(null);
        }
        if(primaryOwner instanceof AccountHolder && (secondaryOwner instanceof AccountHolder || secondaryOwner == null)){
            LocalDate dob = ((AccountHolder) primaryOwner).getDateOfBirth();
            LocalDate now = LocalDate.now();
            int yearDOB = dob.getMonthValue();
            int yearNow = now.getMonthValue();
            int age = yearNow -yearDOB;
            if(age < 24){
                StudentChecking studentAccount = new StudentChecking(checkingAccountDTO.getBalance(),(AccountHolder) primaryOwner,(AccountHolder) secondaryOwner,checkingAccountDTO.getSecretKey());
                return accountRepository.save(studentAccount);
            } else{
                Checking checkingAccount = new Checking(checkingAccountDTO.getBalance(),(AccountHolder) primaryOwner,(AccountHolder) secondaryOwner,checkingAccountDTO.getSecretKey());
                return accountRepository.save(checkingAccount);
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The primary Owner and/or secondaryOwner weren't found");
        }

    }

    public Savings createSavingsAccount(SavingAccountDTO savingAccountDTO){
        User primaryOwner =  userRepository.findById(savingAccountDTO.getPrimaryAccountOwnerId()).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "Primary Owner id not found"));
        User secondaryOwner = null;
        if(savingAccountDTO.getSecondaryAccountOwnerId() != null){
           secondaryOwner = userRepository.findById(savingAccountDTO.getSecondaryAccountOwnerId()).orElse(null);
        }
        if(primaryOwner instanceof AccountHolder && (secondaryOwner instanceof AccountHolder || secondaryOwner == null)){
            Savings savingAccount = new Savings(savingAccountDTO.getBalance(),(AccountHolder) primaryOwner,(AccountHolder) secondaryOwner, savingAccountDTO.getSecretKey(),savingAccountDTO.getMinimumBalance(), savingAccountDTO.getInterestRate());
            return accountRepository.save(savingAccount);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The primary Owner and/or secondaryOwner weren't found");
        }


    }

    public CreditCard createCreditAccount(CreditAccountDTO creditAccountDTO){
        User primaryOwner =  userRepository.findById(creditAccountDTO.getPrimaryAccountOwnerId()).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "Primary Owner id not found"));
        User secondaryOwner = null;
        if(creditAccountDTO.getSecondaryAccountOwnerId() != null){
            secondaryOwner = userRepository.findById(creditAccountDTO.getSecondaryAccountOwnerId()).orElse(null);
        }
        if(primaryOwner instanceof AccountHolder && (secondaryOwner instanceof AccountHolder || secondaryOwner == null)){
            CreditCard creditAccount = new CreditCard(creditAccountDTO.getBalance(),(AccountHolder) primaryOwner,(AccountHolder) secondaryOwner,creditAccountDTO.getCreditLimit(), creditAccountDTO.getInterestRate());
            return accountRepository.save(creditAccount);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The primary Owner and/or secondaryOwner weren't found");
        }
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
            currentAccount = accountRepository.findBySecondaryOwnerId(accountHolderId).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "Are you sure you have an account?"));
        }

        //comprobar fraude antes de hacer transfer
        LocalDateTime lastTransferDate = transferRepository.findLastTransferDateByAccountId(currentAccount.getId());
        Duration duration = Duration.between(LocalDateTime.now(), lastTransferDate);
        if(currentAccount instanceof Savings && duration.getSeconds()<= 1){
            ((Savings) currentAccount).setStatus(Status.FROZEN);
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE,"In order to prevent fraud your Account has been frozen and the transfer haven't been processed");
        }
        if(currentAccount instanceof StudentChecking && duration.getSeconds()<= 1){
            ((StudentChecking) currentAccount).setStatus(Status.FROZEN);
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE,"In order to prevent fraud your Account has been frozen and the transfer haven't been processed");
        }
        if(currentAccount instanceof Checking && duration.getSeconds()<= 1){
            ((Checking) currentAccount).setStatus(Status.FROZEN);
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE,"In order to prevent fraud your Account has been frozen and the transfer haven't been processed");
        }

        //tengo que agrupar por dia y calcular el maximo amount transferido en un dia,
        // y comparar con el total del dia en que se esta haciendo la transferencia.

        BigDecimal maxDaily = transferRepository.findMaxDailyTransferAmount(currentAccount.getId());
        BigDecimal actualDaily = transferRepository.findAmountTransferedLastDayFromNow(LocalDateTime.now(), currentAccount.getId());
        BigDecimal percentage = actualDaily.multiply(new BigDecimal(100).divide(maxDaily));
        if(percentage.compareTo(new BigDecimal(150))==1 && currentAccount instanceof Savings){
            ((Savings) currentAccount).setStatus(Status.FROZEN);
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE,"In order to prevent fraud your Account has been frozen and the transfer haven't been processed");
        }
        if(percentage.compareTo(new BigDecimal(150))==1 && currentAccount instanceof Checking){
            ((Checking) currentAccount).setStatus(Status.FROZEN);
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE,"In order to prevent fraud your Account has been frozen and the transfer haven't been processed");
        }
        if(percentage.compareTo(new BigDecimal(150))==1 && currentAccount instanceof StudentChecking){
            ((StudentChecking) currentAccount).setStatus(Status.FROZEN);
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE,"In order to prevent fraud your Account has been frozen and the transfer haven't been processed");
        }

        //si no hay fraude, buscar la cuenta destino a ver si existe y entonces hacer la transferencia
        Account targetAccount = accountRepository.findById(ownerTransferDTO.getTargetAccountId()).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "Id not found"));

        if(!targetAccount.getPrimaryOwner().getName().equals(ownerTransferDTO.getOwnerTargetName()) && (targetAccount.getSecondaryOwner() == null || !targetAccount.getSecondaryOwner().getName().equals(ownerTransferDTO.getOwnerTargetName()))){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"The provided id or owner of the target account is wrong");
        } else {
            BigDecimal actualBalance = currentAccount.getBalance().getAmount();
            if(actualBalance.compareTo(ownerTransferDTO.getAmount()) == -1){
                throw new IllegalArgumentException("Not sufficient funds");
            } else {
                currentAccount.setBalance(new Money(actualBalance.subtract(ownerTransferDTO.getAmount()),Currency.getInstance("EUR")));
                targetAccount.setBalance(new Money(targetAccount.getBalance().getAmount().add(ownerTransferDTO.getAmount()),Currency.getInstance("EUR")));
            }
        }

        //tambien necesito saber el tipo de la cuenta de origen para ver si baja del minimumbalance
        if(currentAccount instanceof Savings && currentAccount.getBalance().getAmount().compareTo(((Savings) currentAccount).getMinimumBalance().getAmount()) == -1){
            currentAccount.setBalance(new Money(currentAccount.getBalance().getAmount().subtract(currentAccount.getPenaltyFee().getAmount()),currentAccount.getBalance().getCurrency()));
        } else if(currentAccount instanceof Checking && currentAccount.getBalance().getAmount().compareTo(((Checking) currentAccount).getMinimumBalance().getAmount()) == -1){
            currentAccount.setBalance(new Money(currentAccount.getBalance().getAmount().subtract(currentAccount.getPenaltyFee().getAmount()),currentAccount.getBalance().getCurrency()));
        }


        //cada vez que se hace una transferencia agregarla a la tabla transfer
        Transfer transfer = new Transfer(currentAccount.getId(),ownerTransferDTO.getAmount(), LocalDateTime.now());
        transferRepository.save(transfer);


        //me falta guardar las cuentas actualizadas tambien
        accountRepository.save(currentAccount);
        accountRepository.save(targetAccount);
    }

    public Money getBalance(){
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

        return currentAccount.getBalance();

    }

    public Money getBalance(Long accountId){
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
        return accountFromDB.getBalance();
    }

    public void changeBalance(Long accountId, BigDecimal newBalance){
        Account accountFromDB = accountRepository.findById(accountId).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "Id not found"));
        accountFromDB.setBalance(new Money(newBalance,accountFromDB.getBalance().getCurrency()));
        accountRepository.save(accountFromDB);
    }
}

