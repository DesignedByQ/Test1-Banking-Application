package com.maxoptra.test1.service;

import com.maxoptra.test1.model.BankingDetails;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BankingService {

    public Boolean validateCardNumber(BankingDetails details){

        String regex;
        if(!details.getBank().equalsIgnoreCase("american express")){

            regex = "^\\d{4}-\\d{4}-\\d{4}-\\d{4}$";

        } else {

            regex = "^\\d{4}-\\d{4}-\\d{4}-\\d{3}$";

        }
        return details.getCardNumber().matches(regex);

    }

    // Sort the list by expiryDate in descending order
    public List<BankingDetails> sortingEntries(List<BankingDetails> bankingDetailsList) {
        return bankingDetailsList.stream()
                .sorted((bd1, bd2) -> bd2.getExpiryDate().compareTo(bd1.getExpiryDate())) // Sort by expiryDate in descending order
                .collect(Collectors.toList());
    }

    // Method to parse a date string (e.g., "2017-03-01") into a Date object
    public Date parseDate(String dateString) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.parse(dateString);
    }

}
