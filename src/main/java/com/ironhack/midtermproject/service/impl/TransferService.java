package com.ironhack.midtermproject.service.impl;

import com.ironhack.midtermproject.enums.Status;
import com.ironhack.midtermproject.model.Account;
import com.ironhack.midtermproject.model.Checking;
import com.ironhack.midtermproject.model.Savings;
import com.ironhack.midtermproject.model.StudentChecking;
import com.ironhack.midtermproject.repository.AccountRepository;
import com.ironhack.midtermproject.repository.TransferRepository;
import com.ironhack.midtermproject.service.interfaces.ITransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

import static java.math.RoundingMode.HALF_UP;

@Service
public class TransferService implements ITransferService {

    @Autowired
    TransferRepository transferRepository;

    @Autowired
    AccountRepository accountRepository;
    public void fraudDetectionOne(Account currentAccount, BigDecimal transferAmount) {
        LocalDateTime lastTransferDate = transferRepository.findLastTransferDateByAccountId(currentAccount.getId());
        if (lastTransferDate != null) {
            Duration duration = Duration.between(LocalDateTime.now(), lastTransferDate);
            Long secondsBetween = duration.getSeconds();
            if (currentAccount instanceof Savings && secondsBetween <= 1) {
                ((Savings) currentAccount).setStatus(Status.FROZEN);
                accountRepository.save(currentAccount);
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "In order to prevent fraud your Account has been frozen and the transfer haven't been processed");
            }
            if (currentAccount instanceof StudentChecking && secondsBetween <= 1) {
                ((StudentChecking) currentAccount).setStatus(Status.FROZEN);
                accountRepository.save(currentAccount);
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "In order to prevent fraud your Account has been frozen and the transfer haven't been processed");
            }
            if (currentAccount instanceof Checking && secondsBetween <= 1) {
                ((Checking) currentAccount).setStatus(Status.FROZEN);
                accountRepository.save(currentAccount);
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "In order to prevent fraud your Account has been frozen and the transfer haven't been processed");
            }
        }
    }


    public boolean fraudDetectionTwo(Account currentAccount, BigDecimal transferAmount){
        boolean fraud= false;
        //tengo que agrupar por dia y calcular el maximo amount transferido en un dia,
        // y comparar con el total del dia en que se esta haciendo la transferencia.

        Period period = Period.between(currentAccount.getCreationDate(), LocalDate.now());
        int years = Math.abs(period.getYears());
        int months = Math.abs(period.getMonths());
        int days = Math.abs(period.getDays());

        //Checking this type of fraud only if there is a minimum of 1 transfer per day and the account is at least 7 days old
        if(transferRepository.findCountOfTransactionsByAccountId(currentAccount.getId()) > 7 && days > 7){
            BigDecimal maxDaily = transferRepository.findMaxDailyTransferAmount(currentAccount.getId());
            BigDecimal actualDaily = null;
            //aca si amounttransferedlastdayfrom now es null tengo que asignarle cero en vez de null
            BigDecimal result = transferRepository.findAmountTransferedLastDayFromNow(LocalDateTime.now(), currentAccount.getId());
            if(result == null){
                result = BigDecimal.ZERO;
            }
            actualDaily = result.add(transferAmount);
            if(maxDaily!=null && actualDaily != null && maxDaily.compareTo(BigDecimal.ZERO)!=0){
                BigDecimal percentage = actualDaily.multiply(new BigDecimal(100).divide(maxDaily,2,HALF_UP));
                if(percentage.compareTo(new BigDecimal(150))==1 && currentAccount instanceof Savings){
                    ((Savings) currentAccount).setStatus(Status.FROZEN);
                    fraud = true;
                    //throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"In order to prevent fraud your Account has been frozen and the transfer haven't been processed");
                }
                if(percentage.compareTo(new BigDecimal(150))==1 && currentAccount instanceof Checking){
                    ((Checking) currentAccount).setStatus(Status.FROZEN);
                    fraud = true;
                    //throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"In order to prevent fraud your Account has been frozen and the transfer haven't been processed");
                }
                if(percentage.compareTo(new BigDecimal(150))==1 && currentAccount instanceof StudentChecking){
                    ((StudentChecking) currentAccount).setStatus(Status.FROZEN);
                    fraud = true;
                    //throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"In order to prevent fraud your Account has been frozen and the transfer haven't been processed");
                }
            }
        }
        return fraud;
    }


}
