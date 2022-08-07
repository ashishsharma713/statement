package com.bank.statement.dto;

public class ResponseDTO {
    private String accountType;
    private String accountNumber;
    private String datefield;
    private String amount;
    private String accountId;


    public ResponseDTO(String accountType, String accountNumber, String datefield, String amount) {
        this.accountType = accountType;
        this.accountNumber = accountNumber;
        this.datefield = datefield;
        this.amount = amount;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getDatefield() {
        return datefield;
    }

    public void setDatefield(String datefield) {
        this.datefield = datefield;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
}
