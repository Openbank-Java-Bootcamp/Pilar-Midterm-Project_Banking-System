package com.ironhack.midtermproject.DTO;

import com.ironhack.midtermproject.model.Money;

import java.math.BigDecimal;

public class OwnerTransferDTO {
    private BigDecimal amount;
    private String ownerTargetName;
    private Long targetAccountId;

    public OwnerTransferDTO(BigDecimal amount, String ownerTargetName, Long targetAccountId) {
        this.amount = amount;
        this.ownerTargetName = ownerTargetName;
        this.targetAccountId = targetAccountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getOwnerTargetId() {
        return ownerTargetName;
    }

    public void setOwnerTargetId(String ownerTargetId) {
        this.ownerTargetName = ownerTargetName;
    }

    public Long getTargetAccountId() {
        return targetAccountId;
    }

    public void setTargetAccountId(Long targetAccountId) {
        this.targetAccountId = targetAccountId;
    }
}
