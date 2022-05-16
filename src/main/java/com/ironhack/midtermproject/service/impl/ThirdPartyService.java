package com.ironhack.midtermproject.service.impl;

import com.ironhack.midtermproject.DTO.ThirdPartyTransferDTO;
import com.ironhack.midtermproject.model.ThirdParty;
import com.ironhack.midtermproject.repository.AccountRepository;
import com.ironhack.midtermproject.repository.ThirdPartyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


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

    }
}
