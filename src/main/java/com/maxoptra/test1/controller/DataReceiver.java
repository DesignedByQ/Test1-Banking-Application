package com.maxoptra.test1.controller;

import com.maxoptra.test1.model.BankingDetails;
import com.maxoptra.test1.service.BankingService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class DataReceiver {

    @Autowired
    BankingService bankingService;

    //Show the form and create a new empty BankingDetails object
    @GetMapping("/")
    public String dataContainer(Model model) {
        model.addAttribute("BankingDetailsStorage", new BankingDetails());
        return "form";
    }

    //Submit form data and store it in the session
    @PostMapping("/submitdata")
    public String submit(@ModelAttribute BankingDetails userInput, HttpSession session, Model model) {

        //Validate the card number
        if (!bankingService.validateCardNumber(userInput)) {
            //Add an error message to the model if validation fails
            model.addAttribute("errorMessage", "Please enter a valid card number, example: 1111-1111-1111-1111 or 2222-2222-2222-222 for Amex cards");

            //Add the user input back to the form so the user doesn't have to re-enter everything
            model.addAttribute("BankingDetailsStorage", userInput);

            //Return the form view to allow the user to correct their input
            return "form";
        }

        //Retrieve the existing list from the session, if it exists
        List<BankingDetails> bankDetailsList = (List<BankingDetails>) session.getAttribute("BankingDetailsList");

        //If no list exists in the session, create a new one
        if (bankDetailsList == null) {
            bankDetailsList = new ArrayList<>();
        }

        //Add the new BankingDetails object to the list
        bankDetailsList.add(userInput);

        //Sort the list by date before saving and displaying it
        List<BankingDetails> sortedBankingDetails = bankingService.sortingEntries(bankDetailsList);

        //Store the updated list in the session
        session.setAttribute("BankingDetailsList", sortedBankingDetails);

        //Display the sorted list using the model
        model.addAttribute("BankingDetailsList", sortedBankingDetails);

        return "display";
    }

    @GetMapping("/display")
    public String display(HttpSession session, Model model) {

        //Retrieve the list of bank details from the session
        List<BankingDetails> bankDetailsList = (List<BankingDetails>) session.getAttribute("BankingDetailsList");

        //If no data has been submitted yet then initialize the list
        if (bankDetailsList == null) {
            bankDetailsList = new ArrayList<>();
        }

        //Sort the list by date
        List<BankingDetails> sortedBankingDetails = bankingService.sortingEntries(bankDetailsList);

        //Add the sorted list to the model for display
        model.addAttribute("BankingDetailsList", sortedBankingDetails);

        return "display";
    }

    @PostMapping("/uploadcsv")
    public String uploadCSV(@RequestParam("csvFile") MultipartFile csvFile, HttpSession session, Model model) {
        try {
            //Check if the csv file contains data
            if (csvFile.isEmpty()) {
                model.addAttribute("errorMessage", "CSV file is empty. Please upload a valid file.");
                return "form";
            }

            List<BankingDetails> bankDetailsList = (List<BankingDetails>) session.getAttribute("BankingDetailsList");
            if (bankDetailsList == null) {
                bankDetailsList = new ArrayList<>();
            }

            //Read each row of the csv
            BufferedReader reader = new BufferedReader(new InputStreamReader(csvFile.getInputStream()));

            String line;

            //Collect any errors found in this list
            List<String> errors = new ArrayList<>();

            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");

                BankingDetails bankingDetails = new BankingDetails();
                bankingDetails.setBank(data[0].trim());
                bankingDetails.setCardNumber(data[1].trim());

                try {
                    bankingDetails.setExpiryDate(bankingService.parseDate(data[2].trim()));
                } catch (ParseException e) {
                    errors.add("Invalid date format in CSV: " + data[2].trim());
                    continue;
                }

                if (!bankingService.validateCardNumber(bankingDetails)) {
                    errors.add("Invalid card number in CSV: " + bankingDetails.getCardNumber());
                    continue;
                }

                bankDetailsList.add(bankingDetails);
            }

            //If errors were found, display them on the form page
            if (!errors.isEmpty()) {
                model.addAttribute("BankingDetailsStorage", new BankingDetails()); // Add this line
                model.addAttribute("errorMessage", String.join(", ", errors));
                return "form";
            }

            //If no errors were found, sort the list by expiry date
            List<BankingDetails> sortedBankingDetails = bankingService.sortingEntries(bankDetailsList);
            session.setAttribute("BankingDetailsList", sortedBankingDetails);
            model.addAttribute("BankingDetailsList", sortedBankingDetails);

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", "Error processing CSV file: " + e.getMessage());
            return "form";
        }

        return "display";
    }

}
