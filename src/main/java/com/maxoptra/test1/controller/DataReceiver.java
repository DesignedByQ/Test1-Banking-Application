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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class DataReceiver {

    @Autowired
    BankingService bankingService;

    // GET request to show the form and create a new empty BankingDetails object
    @GetMapping("/")
    public String dataContainer(Model model) {
        model.addAttribute("BankingDetailsStorage", new BankingDetails());
        return "form";
    }

    // POST request to submit form data and store it in the session
    @PostMapping("/submitdata")
    public String submit(@ModelAttribute BankingDetails userInput, HttpSession session, Model model) {

        // Validate the card number
        if (!bankingService.validateCardNumber(userInput)) {
            // Add an error message to the model if validation fails
            model.addAttribute("errorMessage", "Please enter a valid card number, example: 1111-1111-1111-1111 or 2222-2222-2222-222 for Amex cards");

            // Add the user input back to the form so the user doesn't have to re-enter everything
            model.addAttribute("BankingDetailsStorage", userInput);

            // Return the form view to allow the user to correct their input
            return "form";
        }

//        // Format expiryDate
//        Date expiryDate = userInput.getExpiryDate();
//        SimpleDateFormat sdf = new SimpleDateFormat("MMM-yyyy");
//
//        String formattedExpiryDate = null;
//        if (expiryDate != null) {
//            formattedExpiryDate = sdf.format(expiryDate);
//        }
//
//        // Add to model or session
//        model.addAttribute("formattedExpiryDate", formattedExpiryDate);

        // Retrieve the existing list from the session, if it exists
        List<BankingDetails> bankDetailsList = (List<BankingDetails>) session.getAttribute("BankingDetailsList");

        // If no list exists in the session, create a new one
        if (bankDetailsList == null) {
            bankDetailsList = new ArrayList<>();
        }

        // Add the new BankingDetails object to the list
        bankDetailsList.add(userInput);

        // Sort the list before saving and displaying it
        List<BankingDetails> sortedBankingDetails = bankingService.sortingEntries(bankDetailsList);

        // Store the updated list in the session. session is called "BankingDetailsList"
        session.setAttribute("BankingDetailsList", sortedBankingDetails);

        // Add the list to the model so it can be displayed, model is called "BankingDetailsList"
        model.addAttribute("BankingDetailsList", sortedBankingDetails);

        return "display";
    }

    @GetMapping("/display")
    public String display(HttpSession session, Model model) {

        // Retrieve the list of bank details from the session
        List<BankingDetails> bankDetailsList = (List<BankingDetails>) session.getAttribute("BankingDetailsList");

        // If the list is null, initialize it (this could happen if no data has been submitted yet)
        if (bankDetailsList == null) {
            bankDetailsList = new ArrayList<>();
        }

        // Sort the list (if needed)
        List<BankingDetails> sortedBankingDetails = bankingService.sortingEntries(bankDetailsList);

        // Add the sorted list to the model for display
        model.addAttribute("BankingDetailsList", sortedBankingDetails);

        return "display";
    }

    @PostMapping("/uploadcsv")
    public String uploadCSV(@RequestParam("csvFile") MultipartFile csvFile, HttpSession session, Model model) {
        try {
            List<BankingDetails> bankDetailsList = (List<BankingDetails>) session.getAttribute("BankingDetailsList");

            if (bankDetailsList == null) {
                bankDetailsList = new ArrayList<>();
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(csvFile.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");

                BankingDetails bankingDetails = new BankingDetails();
                bankingDetails.setBank(data[0].trim());
                bankingDetails.setCardNumber(data[1].trim());
                bankingDetails.setExpiryDate(bankingService.parseDate(data[2].trim()));

                // Validate each card number before adding it to the list
                if (!bankingService.validateCardNumber(bankingDetails)) {
                    model.addAttribute("errorMessage", "Invalid card number in the CSV file: " + bankingDetails.getCardNumber());
                    return "form";  // Return to form with an error message if validation fails
                }

                // If valid, add the entry
                bankDetailsList.add(bankingDetails);
            }

            // Sort the list
            List<BankingDetails> sortedBankingDetails = bankingService.sortingEntries(bankDetailsList);

            // Store updated list in session
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
