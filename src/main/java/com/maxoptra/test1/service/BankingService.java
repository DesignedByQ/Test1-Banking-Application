package com.maxoptra.test1.service;

import com.maxoptra.test1.model.BankingDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BankingService {

    public String validateCardNumber(BankingDetails details){

        if(!details.getBank().equalsIgnoreCase("american express")){

            String regex = "^\\d{4}-\\d{4}-\\d{4}-\\d{4}$";

            if(!details.getCardNumber().matches(regex)){
                return "Please enter a valid card number, example 1111-1111-1111-1111";
            }

        } else {

            String regex = "^\\d{4}-\\d{4}-\\d{4}-\\d{3}$";

            if(!details.getCardNumber().matches(regex)){
                return "Please enter a valid card number, example 1111-1111-1111-1111";
            }

        }

        return "";
    }

    // Sort the list by expiryDate in descending order
    public List<BankingDetails> sortingEntries(List<BankingDetails> bankingDetailsList) {
        return bankingDetailsList.stream()
                .sorted((bd1, bd2) -> bd2.getExpiryDate().compareTo(bd1.getExpiryDate())) // Sort by expiryDate in descending order
                .collect(Collectors.toList());
    }



}
