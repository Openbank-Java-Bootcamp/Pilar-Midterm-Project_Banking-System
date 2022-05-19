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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.*;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;

import static java.math.RoundingMode.HALF_EVEN;
import static java.math.RoundingMode.HALF_UP;

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
            int yearDOB = dob.getYear();
            int yearNow = now.getYear();
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

        Account currentAccount = accountRepository.findById(ownerTransferDTO.getOwnAccountId()).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "Account Id not found"));
        if(!currentAccount.getPrimaryOwner().getId().equals(accountHolderId) && (currentAccount.getSecondaryOwner()==null ||  !currentAccount.getSecondaryOwner().getId().equals(accountHolderId))){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "This is not your account");
        }


        //en esta parte hay un error que me da la siguiente exepcion: nested exception is java.lang.NullPointerException: temporal
        //comprobar fraude antes de hacer transfer

        /*LocalDateTime lastTransferDate = transferRepository.findLastTransferDateByAccountId(currentAccount.getId());
        if(lastTransferDate != null){
            Duration duration = Duration.between(LocalDateTime.now(), lastTransferDate);
            if(currentAccount instanceof Savings && duration.getSeconds()<= 1){
                ((Savings) currentAccount).setStatus(Status.FROZEN);
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"In order to prevent fraud your Account has been frozen and the transfer haven't been processed");
            }
            if(currentAccount instanceof StudentChecking && duration.getSeconds()<= 1){
                ((StudentChecking) currentAccount).setStatus(Status.FROZEN);
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"In order to prevent fraud your Account has been frozen and the transfer haven't been processed");
            }
            if(currentAccount instanceof Checking && duration.getSeconds()<= 1){
                ((Checking) currentAccount).setStatus(Status.FROZEN);
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"In order to prevent fraud your Account has been frozen and the transfer haven't been processed");
            }
        }*/

        //tengo que agrupar por dia y calcular el maximo amount transferido en un dia,
        // y comparar con el total del dia en que se esta haciendo la transferencia.

        /*BigDecimal maxDaily = transferRepository.findMaxDailyTransferAmount(currentAccount.getId());
        BigDecimal actualDaily = null;
        if(transferRepository.findAmountTransferedLastDayFromNow(LocalDateTime.now(), currentAccount.getId()) != null){
            actualDaily = transferRepository.findAmountTransferedLastDayFromNow(LocalDateTime.now(), currentAccount.getId()).add(ownerTransferDTO.getTransferAmount().getAmount());
        }
        if(maxDaily!=null && actualDaily != null && maxDaily.compareTo(BigDecimal.ZERO)!=0){
            BigDecimal percentage = actualDaily.multiply(new BigDecimal(100).divide(maxDaily,2,HALF_UP));
            if(percentage.compareTo(new BigDecimal(150))==1 && currentAccount instanceof Savings){
                ((Savings) currentAccount).setStatus(Status.FROZEN);
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"In order to prevent fraud your Account has been frozen and the transfer haven't been processed");
            }
            if(percentage.compareTo(new BigDecimal(150))==1 && currentAccount instanceof Checking){
                ((Checking) currentAccount).setStatus(Status.FROZEN);
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"In order to prevent fraud your Account has been frozen and the transfer haven't been processed");
            }
            if(percentage.compareTo(new BigDecimal(150))==1 && currentAccount instanceof StudentChecking){
                ((StudentChecking) currentAccount).setStatus(Status.FROZEN);
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"In order to prevent fraud your Account has been frozen and the transfer haven't been processed");
            }
        }
*/

        //si no hay fraude, buscar la cuenta destino a ver si existe y entonces hacer la transferencia
        Account targetAccount = accountRepository.findById(ownerTransferDTO.getTargetAccountId()).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "Id not found"));

        if(!targetAccount.getPrimaryOwner().getName().equals(ownerTransferDTO.getOwnerTargetName()) && (targetAccount.getSecondaryOwner() == null || !targetAccount.getSecondaryOwner().getName().equals(ownerTransferDTO.getOwnerTargetName()))){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"The provided id or owner of the target account is wrong");
        } else {
            BigDecimal actualBalance = currentAccount.getBalance().getAmount();
            BigDecimal amountToTransfer = ownerTransferDTO.getTransferAmount().getAmount();
            if(currentAccount instanceof CreditCard && actualBalance.subtract(amountToTransfer).compareTo(((CreditCard) currentAccount).getCreditLimit().getAmount().negate()) == -1){
                throw new ResponseStatusException(HttpStatus.FORBIDDEN,"You have reached your credit limit");
            }else if(!(currentAccount instanceof CreditCard) && actualBalance.compareTo(amountToTransfer) == -1){
                throw new ResponseStatusException(HttpStatus.FORBIDDEN,"You have reached your credit limit");
            } else {
                currentAccount.setBalance(new Money(actualBalance.subtract(ownerTransferDTO.getTransferAmount().getAmount()),Currency.getInstance("EUR")));
                targetAccount.setBalance(new Money(targetAccount.getBalance().getAmount().add(ownerTransferDTO.getTransferAmount().getAmount()),Currency.getInstance("EUR")));
            }
        }

        //tambien necesito saber el tipo de la cuenta de origen para ver si baja del minimumbalance
        if(currentAccount instanceof Savings && currentAccount.getBalance().getAmount().compareTo(((Savings) currentAccount).getMinimumBalance().getAmount()) == -1){
            currentAccount.setBalance(new Money(currentAccount.getBalance().getAmount().subtract(currentAccount.getPenaltyFee().getAmount()),currentAccount.getBalance().getCurrency()));
        } else if(currentAccount instanceof Checking && currentAccount.getBalance().getAmount().compareTo(((Checking) currentAccount).getMinimumBalance().getAmount()) == -1){
            currentAccount.setBalance(new Money(currentAccount.getBalance().getAmount().subtract(currentAccount.getPenaltyFee().getAmount()),currentAccount.getBalance().getCurrency()));
        }


        //cada vez que se hace una transferencia agregarla a la tabla transfer
        Transfer transfer = new Transfer(currentAccount.getId(),ownerTransferDTO.getTransferAmount().getAmount(), LocalDateTime.now());
        transferRepository.save(transfer);


        //me falta guardar las cuentas actualizadas tambien
        accountRepository.save(currentAccount);
        accountRepository.save(targetAccount);
    }



    public Money getBalance(Long accountId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = null;
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            currentUsername = authentication.getName();
        }
        User currentUser = userRepository.findByUsername(currentUsername);
        Long accountHolderId = currentUser.getId();

        Account currentAccount = accountRepository.findById(accountId).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "Account Id not found"));
        if(!currentAccount.getPrimaryOwner().getId().equals(accountHolderId) && (currentAccount.getSecondaryOwner()==null || !currentAccount.getSecondaryOwner().getId().equals(accountHolderId))){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "This is not your account");
        }

        checkInterestAndMaintenance(currentAccount);

        return currentAccount.getBalance();
    }

    public void checkInterestAndMaintenance(Account currentAccount){
        if(currentAccount instanceof Savings) {
            Period period = Period.between(((Savings) currentAccount).getAddedInterest(), LocalDate.now());
            int years = Math.abs(period.getYears());
            int months = Math.abs(period.getMonths());
            int days = Math.abs(period.getDays());

            if(years>0) {
                BigDecimal interest = currentAccount.getBalance().getAmount().multiply(((Savings) currentAccount).getInterestRate()).multiply(BigDecimal.valueOf(years));
                currentAccount.setBalance(new Money(currentAccount.getBalance().getAmount().add(interest), Currency.getInstance("EUR")));
                ((Savings) currentAccount).setAddedInterest(LocalDate.now());
                accountRepository.save(currentAccount);
            }
        }
        if(currentAccount instanceof CreditCard){
            Period period = Period.between(((CreditCard) currentAccount).getAddedInterest(), LocalDate.now());
            int years = Math.abs(period.getYears());
            int months = Math.abs(period.getMonths());
            int days = Math.abs(period.getDays());
            int totalDiffMonths= months + years/12;

            if(totalDiffMonths>=1) {
                BigDecimal monthRate = ((CreditCard) currentAccount).getInterestRate().divide(new BigDecimal(12), 2, RoundingMode.CEILING);
                BigDecimal interest = currentAccount.getBalance().getAmount().multiply(monthRate).multiply(BigDecimal.valueOf(months));
                currentAccount.setBalance(new Money(currentAccount.getBalance().getAmount().add(interest), Currency.getInstance("EUR")));
                ((CreditCard) currentAccount).setAddedInterest(LocalDate.now());
                accountRepository.save(currentAccount);
            }
        }
        if(currentAccount instanceof Checking){
            Period period = Period.between(((Checking) currentAccount).getMaintenanceCharged(), LocalDate.now());
            int years = Math.abs(period.getYears());
            int months = Math.abs(period.getMonths());
            int days = Math.abs(period.getDays());
            int totalDiffMonths= months + years/12;

            if(totalDiffMonths>=1) {
                BigDecimal totalMaintenance = ((Checking) currentAccount).getMonthlyMaintenanceFee().getAmount().multiply(BigDecimal.valueOf(months));
                currentAccount.setBalance(new Money(currentAccount.getBalance().getAmount().add(totalMaintenance), Currency.getInstance("EUR")));
                ((Checking) currentAccount).setMaintenanceCharged(LocalDate.now());
                accountRepository.save(currentAccount);
            }
        }

    }

    public Money getBalanceAdmin(Long accountId){
        Account currentAccount = accountRepository.findById(accountId).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "Id not found"));

        checkInterestAndMaintenance(currentAccount);

        return currentAccount.getBalance();
    }

    public void changeBalance(Long accountId, BigDecimal newBalance){
        Account accountFromDB = accountRepository.findById(accountId).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));
        accountFromDB.setBalance(new Money(newBalance,accountFromDB.getBalance().getCurrency()));
        accountRepository.save(accountFromDB);
    }

    public void deleteAccount(@RequestParam Long accountId){
        Account accountFromDB= accountRepository.findById(accountId).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));
        accountRepository.deleteById(accountId);
    }


}

