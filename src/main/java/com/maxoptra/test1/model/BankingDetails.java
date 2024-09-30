package com.maxoptra.test1.model;

import org.springframework.format.annotation.DateTimeFormat;

import java.text.SimpleDateFormat;
import java.time.LocalDate;


public class BankingDetails {

    private String bank;
    private String cardNumber;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate expiryDate;

    public BankingDetails(){

    }

    public BankingDetails(String bank, String cardNumber, LocalDate expiryDate) {
        this.bank = bank;
        this.cardNumber = cardNumber;

        // Create SimpleDateFormat with the desired format "MMM-yyyy"
        SimpleDateFormat formatter = new SimpleDateFormat("MMM-yyyy");

        // Format the date
        String formattedDate = formatter.format(expiryDate);
        this.expiryDate = expiryDate ;
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

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }
}
