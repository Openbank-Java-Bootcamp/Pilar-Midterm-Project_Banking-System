package com.ironhack.midtermproject.service.interfaces;

import com.ironhack.midtermproject.DTO.ThirdPartyTransferDTO;
import com.ironhack.midtermproject.model.ThirdParty;
import org.springframework.web.bind.annotation.RequestParam;

public interface IThirdPartyService {
    ThirdParty saveThirdParty(ThirdParty thirdParty);
    void transferMoneyThirdParty(String hashedKey, ThirdPartyTransferDTO thirdPartyTransferDTO);
    void takeMoneyThirdParty(String hashedKey, ThirdPartyTransferDTO thirdPartyTransferDTO);
    void deleteThirdParty(Long thirdPartyId);
}
