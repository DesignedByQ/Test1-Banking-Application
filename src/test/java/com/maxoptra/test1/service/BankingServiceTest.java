package com.maxoptra.test1.service;

import com.maxoptra.test1.model.BankingDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class BankingServiceTest {

    //Inject the BankingService class
    private BankingService bankingService;

    @BeforeEach
    public void setup() {
        //Initialize the service before each test
        bankingService = new BankingService();
    }

    @Test
    public void validateCardNumberTest() {

        //Create several BankingDetails objects with different card numbers
        BankingDetails bd1 = new BankingDetails();
        bd1.setBank("Halifax");
        bd1.setCardNumber("5555-2222-5555-6666");

        BankingDetails bd2 = new BankingDetails();
        bd2.setBank("Halifax");
        bd2.setCardNumber("5555222255556666");

        BankingDetails bd3 = new BankingDetails();
        bd3.setBank("Halifax");
        bd3.setCardNumber("55c5-2G22-55A5-66h6");

        BankingDetails bd4 = new BankingDetails();
        bd4.setBank("Halifax");
        bd4.setCardNumber("");

        BankingDetails bd5 = new BankingDetails();
        bd5.setBank("Halifax");
        bd5.setCardNumber("5555-2222-5555-66667");

        BankingDetails bd6 = new BankingDetails();
        bd6.setBank("Halifax");
        bd6.setCardNumber("5555-2222-5555-666");

        BankingDetails bd7 = new BankingDetails();
        bd7.setBank("American Express");
        bd7.setCardNumber("5555-2222-5555-666");

        BankingDetails bd8 = new BankingDetails();
        bd8.setBank("American Express");
        bd8.setCardNumber("5555-2222-5555-6666");

        assertTrue(bankingService.validateCardNumber(bd1), "Card number input correctly");
        assertFalse(bankingService.validateCardNumber(bd2), "Check for missing hyphens");
        assertFalse(bankingService.validateCardNumber(bd3), "Check against alphabetic characters");
        assertFalse(bankingService.validateCardNumber(bd4), "Check empty entry");
        assertFalse(bankingService.validateCardNumber(bd5), "Check for number being too long");
        assertFalse(bankingService.validateCardNumber(bd6), "Check for number being too short");
        assertTrue(bankingService.validateCardNumber(bd7), "Check Amex unique card length");
        assertFalse(bankingService.validateCardNumber(bd8), "Check Amex card number is too long");
    }

    @Test
    public void sortingEntries() throws ParseException {

        //Create several BankingDetails objects with different expiry dates
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        BankingDetails bd1 = new BankingDetails();
        bd1.setExpiryDate(sdf.parse("2024-10-01"));  //Future date

        BankingDetails bd2 = new BankingDetails();
        bd2.setExpiryDate(sdf.parse("2022-05-01"));  //Older date

        BankingDetails bd3 = new BankingDetails();
        bd3.setExpiryDate(sdf.parse("2023-08-01"));  //Mid-range date

        //Add the objects to a list
        List<BankingDetails> bankDetailsList = new ArrayList<>();
        bankDetailsList.add(bd1);
        bankDetailsList.add(bd2);
        bankDetailsList.add(bd3);

        //Sort the objects using the service method
        List<BankingDetails> sortedList = bankingService.sortingEntries(bankDetailsList);

        //Check that the list is sorted in descending order of expiry dates
        assertEquals("2024-10-01", sdf.format(sortedList.get(0).getExpiryDate()));
        assertEquals("2023-08-01", sdf.format(sortedList.get(1).getExpiryDate()));
        assertEquals("2022-05-01", sdf.format(sortedList.get(2).getExpiryDate()));
    }

    @Test
    public void parseDateTest() throws ParseException {

        String testDate = ("2000-01-01");
        Date test1 = bankingService.parseDate(testDate);

        String fakeDate = ("3000-10-k");

        assertTrue(test1 instanceof Date);
        //Test exception will trigger
        assertThrows(ParseException.class, () -> {
            bankingService.parseDate(fakeDate);
        });
    }
}