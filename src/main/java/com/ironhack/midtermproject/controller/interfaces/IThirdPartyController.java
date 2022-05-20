package com.ironhack.midtermproject.controller.interfaces;

import com.ironhack.midtermproject.DTO.ThirdPartyTransferDTO;
import com.ironhack.midtermproject.model.ThirdParty;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

public interface IThirdPartyController {
    void createAThirdParty(ThirdParty thirdParty);
    void transferMoneyThirdParty(String hashedKey,ThirdPartyTransferDTO thirdPartyTransferDTO);
    void takeMoneyThirdParty(String hashedKey,ThirdPartyTransferDTO thirdPartyTransferDTO);
    void deleteThirdParty(Long thirdPartyId);
}
