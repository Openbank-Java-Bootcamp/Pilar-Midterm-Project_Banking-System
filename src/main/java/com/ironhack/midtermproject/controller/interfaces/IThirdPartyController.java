package com.ironhack.midtermproject.controller.interfaces;

import com.ironhack.midtermproject.model.ThirdParty;
import org.springframework.web.bind.annotation.RequestBody;

public interface IThirdPartyController {
    void createAThirdParty(ThirdParty thirdParty);
}
