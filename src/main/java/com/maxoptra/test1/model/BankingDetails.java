package com.maxoptra.test1.model;

import java.util.Date;

public class BankingDetails {

    private String bank;
    private String cardNumber;
    private Date expiryDate;

    public BankingDetails(){

    }

    public BankingDetails(String bank, String cardNumber, Date expiryDate) {
        this.bank = bank;
        this.cardNumber = cardNumber;
        this.expiryDate = expiryDate;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }
}
