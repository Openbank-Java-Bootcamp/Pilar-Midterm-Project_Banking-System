package com.ironhack.midtermproject.DTO;

import com.ironhack.midtermproject.model.Money;

import java.math.BigDecimal;

public class OwnerTransferDTO {
    private BigDecimal amount;
    private String ownerTargetId;
    private Long targetAccountId;

    public OwnerTransferDTO(BigDecimal amount, String ownerTargetId, Long targetAccountId) {
        this.amount = amount;
        this.ownerTargetId = ownerTargetId;
        this.targetAccountId = targetAccountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getOwnerTargetId() {
        return ownerTargetId;
    }

    public void setOwnerTargetId(String ownerTargetId) {
        this.ownerTargetId = ownerTargetId;
    }

    public Long getTargetAccountId() {
        return targetAccountId;
    }

    public void setTargetAccountId(Long targetAccountId) {
        this.targetAccountId = targetAccountId;
    }
}
