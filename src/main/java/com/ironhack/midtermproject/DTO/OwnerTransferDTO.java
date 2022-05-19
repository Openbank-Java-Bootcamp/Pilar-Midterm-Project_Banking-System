package com.ironhack.midtermproject.DTO;

import com.ironhack.midtermproject.model.Money;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class OwnerTransferDTO {
    @NotBlank(message = "Transfer amount may not be blank")
    private Money transferAmount;
    @NotBlank(message = "Name of the owner of the destination account may not be blank")
    private String ownerTargetName;
    @NotBlank(message = "Id of the destination account may not be blank")
    private Long targetAccountId;
    @NotBlank(message = "Your account id may not be blank")
    private Long ownAccountId;

    public OwnerTransferDTO(Money transferAmount, String ownerTargetName, Long targetAccountId, Long ownAccountId) {
        this.transferAmount = transferAmount;
        this.ownerTargetName = ownerTargetName;
        this.targetAccountId = targetAccountId;
        this.ownAccountId = ownAccountId;
    }

    public Money getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(Money transferAmount) {
        this.transferAmount = transferAmount;
    }

    public String getOwnerTargetName() {
        return ownerTargetName;
    }

    public void setOwnerTargetName(String ownerTargetId) {
        this.ownerTargetName = ownerTargetName;
    }

    public Long getTargetAccountId() {
        return targetAccountId;
    }

    public void setTargetAccountId(Long targetAccountId) {
        this.targetAccountId = targetAccountId;
    }

    public Long getOwnAccountId() {
        return ownAccountId;
    }

    public void setOwnAccountId(Long ownAccountId) {
        this.ownAccountId = ownAccountId;
    }
}
