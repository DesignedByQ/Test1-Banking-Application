package com.maxoptra.test1.service;

import com.maxoptra.test1.model.BankingDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
//@SpringBootTest
class BankingServiceTest {

    private BankingService bankingService;

    @BeforeEach
    public void setup() {
        // Initialize the service before each test
        bankingService = new BankingService();
    }

    @Test
    public void validateCardNumberTest() {

        String sampleCard = "5555-2222-5555-6666";
        BankingDetails bd = new BankingDetails();

        bd.setBank("aaaa");
        bd.setCardNumber(sampleCard);

        assertTrue(bankingService.validateCardNumber(bd));
    }

    @Test
    public void sortingEntries() throws ParseException {

        // Create a few BankingDetails objects with different expiry dates
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        BankingDetails bd1 = new BankingDetails();
        bd1.setCardNumber("1111-2222-3333-4444");
        bd1.setExpiryDate(sdf.parse("2024-10-01"));  // Future date

        BankingDetails bd2 = new BankingDetails();
        bd2.setCardNumber("5555-6666-7777-8888");
        bd2.setExpiryDate(sdf.parse("2022-05-01"));  // Older date

        BankingDetails bd3 = new BankingDetails();
        bd3.setCardNumber("9999-8888-7777-6666");
        bd3.setExpiryDate(sdf.parse("2023-08-01"));  // Mid-range date

        // Add these entries to a list
        List<BankingDetails> bankDetailsList = new ArrayList<>();
        bankDetailsList.add(bd1);
        bankDetailsList.add(bd2);
        bankDetailsList.add(bd3);

        // Sort the entries using the service method
        List<BankingDetails> sortedList = bankingService.sortingEntries(bankDetailsList);

        // Check that the list is sorted in descending order of expiry dates
        assertEquals("2024-10-01", sdf.format(sortedList.get(0).getExpiryDate()));
        assertEquals("2023-08-01", sdf.format(sortedList.get(1).getExpiryDate()));
        assertEquals("2022-05-01", sdf.format(sortedList.get(2).getExpiryDate()));
    }
}