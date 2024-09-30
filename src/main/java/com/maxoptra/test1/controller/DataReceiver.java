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



@Controller
public class DataReceiver {

    @Autowired
    BankingService bankingService;

    //Model is an object/list used to store data, multiple entries.
    @GetMapping("/")
    public String dataContainer(Model model) {
        //Creates a new entry with the string BankingDetails used as the key/name for the new object
        model.addAttribute("BankingDetailsStorage", new BankingDetails());
        //Returns the name of the view ie form.html
        return "form";
    }

    //Store the entry in the model and session
    @PostMapping("/submitdata")
    public String submit(@ModelAttribute BankingDetails userInput, HttpSession session, Model model){
        //i want to store the entry in a session called BankingDetailsStorage
        session.setAttribute("BankingDetailsStorage", userInput);
        //i want to store the entry in the list called BankingDetailsStorage
        model.addAttribute("BankingDetailsStorage", userInput);
        return "display";
    }

    @GetMapping("/display")
    public String display(HttpSession session, Model model) {

        //get the session
        BankingDetails bd = (BankingDetails) session.getAttribute("BankingDetailsStorage");

        //sort the entries
        BankingDetails sortedBankingDetails = bankingService.sortingEntries(bd);

        //display the data from the session
        model.addAttribute("BankingDetailsStorage", sortedBankingDetails);
        return "display";
    }


}
