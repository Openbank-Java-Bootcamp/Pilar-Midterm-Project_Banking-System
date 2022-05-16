package com.ironhack.midtermproject.controller.impl;


import com.ironhack.midtermproject.DTO.ThirdPartyTransferDTO;
import com.ironhack.midtermproject.controller.interfaces.IThirdPartyController;
import com.ironhack.midtermproject.model.AccountHolder;
import com.ironhack.midtermproject.model.ThirdParty;
import com.ironhack.midtermproject.service.impl.ThirdPartyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ThirdPartyController implements IThirdPartyController {

    @Autowired
    ThirdPartyService thirdPartyService;

    @PostMapping("/thirdparties/")
    @ResponseStatus(HttpStatus.CREATED)
    public void createAThirdParty(@RequestBody ThirdParty thirdParty){
        thirdPartyService.saveThirdParty(thirdParty);
    }

    @PostMapping("/thirdparties/{hashedKey}")
    @ResponseStatus(HttpStatus.OK)
    public void transferMoneyThirdParty(String hashedKey, ThirdPartyTransferDTO thirdPartyTransferDTO){
        thirdPartyService.transferMoneyThirdParty(hashedKey, thirdPartyTransferDTO);
    }
}