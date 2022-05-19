package com.ironhack.midtermproject.controller.impl;


import com.ironhack.midtermproject.DTO.ThirdPartyTransferDTO;
import com.ironhack.midtermproject.controller.interfaces.IThirdPartyController;
import com.ironhack.midtermproject.model.AccountHolder;
import com.ironhack.midtermproject.model.ThirdParty;
import com.ironhack.midtermproject.service.impl.ThirdPartyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ThirdPartyController implements IThirdPartyController {

    @Autowired
    ThirdPartyService thirdPartyService;

    @PostMapping("/thirdparties")
    @ResponseStatus(HttpStatus.CREATED)
    public void createAThirdParty(@RequestBody ThirdParty thirdParty){
        thirdPartyService.saveThirdParty(thirdParty);
    }

    @PatchMapping("/thirdtransfer")
    @ResponseStatus(HttpStatus.OK)
    public void transferMoneyThirdParty(@RequestParam String hashedKey, @RequestBody ThirdPartyTransferDTO thirdPartyTransferDTO){
        thirdPartyService.transferMoneyThirdParty(hashedKey, thirdPartyTransferDTO);
    }

    @PatchMapping("/thirdtake")
    @ResponseStatus(HttpStatus.OK)
    public void takeMoneyThirdParty(@RequestParam String hashedKey, @RequestBody ThirdPartyTransferDTO thirdPartyTransferDTO){
        thirdPartyService.takeMoneyThirdParty(hashedKey, thirdPartyTransferDTO);
    }

    @DeleteMapping("/thirdparties")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteThirdParty(@RequestParam Long thirdPartyId){
        thirdPartyService.deleteThirdParty(thirdPartyId);
    }
}
