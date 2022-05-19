package com.ironhack.midtermproject.service.impl;

import com.ironhack.midtermproject.DTO.ThirdPartyTransferDTO;
import com.ironhack.midtermproject.model.*;
import com.ironhack.midtermproject.repository.AccountRepository;
import com.ironhack.midtermproject.repository.ThirdPartyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class ThirdPartyService {

    @Autowired
    private ThirdPartyRepository thirdPartyRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public ThirdParty saveThirdParty(ThirdParty thirdParty){
        thirdParty.setHashedKey(passwordEncoder.encode(thirdParty.getHashedKey()));
        return thirdPartyRepository.save(thirdParty);
    }

    public void transferMoneyThirdParty(String hashedKey, ThirdPartyTransferDTO thirdPartyTransferDTO){
        List<ThirdParty> listFromDB = thirdPartyRepository.findAll();
        boolean existThirdParty = false;
        for (ThirdParty i : listFromDB){
            if(passwordEncoder.matches(hashedKey, i.getHashedKey())){
                existThirdParty = true;
                break;
            }
        }
        if(!existThirdParty){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"The provided hashedKey is incorrect");
        } else {
            Long targetAccountId = thirdPartyTransferDTO.getAccountId();
            Account targetAccount = accountRepository.findById(targetAccountId).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));
            String secretKey = accountRepository.findSecretKeyByAccountId(targetAccountId);
            if(secretKey.equals(thirdPartyTransferDTO.getAccountSecretKey())){
                targetAccount.setBalance(new Money(targetAccount.getBalance().getAmount().add(thirdPartyTransferDTO.getAmount()), Currency.getInstance("EUR")));
                accountRepository.save(targetAccount);
            } else{
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,"The provided key does not match the account id");
            }
        }
    }

    public void takeMoneyThirdParty(String hashedKey, ThirdPartyTransferDTO thirdPartyTransferDTO){
        List<ThirdParty> listFromDB = thirdPartyRepository.findAll();
        boolean existThirdParty = false;
        for (ThirdParty i : listFromDB){
            if(passwordEncoder.matches(hashedKey, i.getHashedKey())){
                existThirdParty = true;
                break;
            }
        }
        if(!existThirdParty){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"The provided key does not match the account id");
        } else {
            Long targetAccountId = thirdPartyTransferDTO.getAccountId();
            Account targetAccount = accountRepository.findById(targetAccountId).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));
            String secretKey = accountRepository.findSecretKeyByAccountId(targetAccountId);
            if(secretKey.equals(thirdPartyTransferDTO.getAccountSecretKey())){
                targetAccount.setBalance(new Money(targetAccount.getBalance().getAmount().subtract(thirdPartyTransferDTO.getAmount()), Currency.getInstance("EUR")));
                accountRepository.save(targetAccount);
            } else{
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,"The provided key does not match the account id");
            }
        }
    }

    public void deleteThirdParty(@RequestParam Long thirdPartyId){
        ThirdParty thirdPartyFromDB= thirdPartyRepository.findById(thirdPartyId).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "Third Party not found"));
        thirdPartyRepository.deleteById(thirdPartyId);
    }

}
