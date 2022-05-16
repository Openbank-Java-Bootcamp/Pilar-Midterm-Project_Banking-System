package com.ironhack.midtermproject.DTO;

import com.ironhack.midtermproject.model.Money;

import java.math.BigDecimal;

public class ThirdPartyTransferDTO {
    private BigDecimal amount;
    private Long accountId;
    private String accountSecretKey;

    public ThirdPartyTransferDTO(BigDecimal amount, Long accountId, String accountSecretKey) {
        this.amount = amount;
        this.accountId = accountId;
        this.accountSecretKey = accountSecretKey;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getAccountSecretKey() {
        return accountSecretKey;
    }

    public void setAccountSecretKey(String accountSecretKey) {
        this.accountSecretKey = accountSecretKey;
    }
}
