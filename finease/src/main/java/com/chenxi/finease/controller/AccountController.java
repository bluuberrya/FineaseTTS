// package com.chenxi.finease.controller;

// import java.security.Principal;
// import java.util.List;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.context.annotation.Lazy;
// import org.springframework.stereotype.Controller;
// import org.springframework.ui.Model;
// import org.springframework.web.bind.annotation.ModelAttribute;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestMethod;

// import com.chenxi.finease.model.CurrentAccount;
// import com.chenxi.finease.model.CurrentTransaction;
// import com.chenxi.finease.model.SavingsAccount;
// import com.chenxi.finease.model.SavingsTransaction;
// import com.chenxi.finease.model.User;
// import com.chenxi.finease.service.AccountService;
// import com.chenxi.finease.service.TransactionService;
// import com.chenxi.finease.service.UserService;

// @Controller
// @RequestMapping("/account")
// public class AccountController {

//     @Autowired
//     @Lazy
//     private UserService userService;

//     @Autowired
//     private AccountService accountService;

//     @Autowired
//     private TransactionService transactionService;

    
//     @RequestMapping("/currentAccount")
//     public String currentAccount(Model model, Principal principal) {
       
//     	List<CurrentTransaction> currentTransactionList = transactionService.findCurrentTransactionList(principal.getName());

//         User user = userService.findByUsername(principal.getName());
//         CurrentAccount currentAccount = user.getCurrentAccount();

//         model.addAttribute("currentAccount", currentAccount);
//         model.addAttribute("currentTransactionList", currentTransactionList);

//         return "currentAccount";
        
//     }

//     @RequestMapping("/savingsAccount")
//     public String savingsAccount(Model model, Principal principal) {
        
//     	List<SavingsTransaction> savingsTransactionList = transactionService.findSavingsTransactionList(principal.getName());
//         User user = userService.findByUsername(principal.getName());
//         SavingsAccount savingsAccount = user.getSavingsAccount();

//         model.addAttribute("savingsAccount", savingsAccount);
//         model.addAttribute("savingsTransactionList", savingsTransactionList);

//         return "savingsAccount";
        
//     }

//     @RequestMapping(value = "/deposit", method = RequestMethod.GET)
//     public String deposit(Model model) {
        
//     	model.addAttribute("accountType", "");
//         model.addAttribute("amount", "");

//         return "deposit";
        
//     }

//     @RequestMapping(value = "/deposit", method = RequestMethod.POST)
//     public String depositPOST(@ModelAttribute("amount") String amount, @ModelAttribute("accountType") String accountType, Principal principal) {
        
//     	accountService.deposit(accountType, Double.parseDouble(amount), principal);

//         return "redirect:/userFront";
        
//     }

//     @RequestMapping(value = "/withdraw", method = RequestMethod.GET)
//     public String withdraw(Model model) {
    	
//         model.addAttribute("accountType", "");
//         model.addAttribute("amount", "");

//         return "withdraw";
        
//     }

//     @RequestMapping(value = "/withdraw", method = RequestMethod.POST)
//     public String withdrawPOST(@ModelAttribute("amount") String amount, @ModelAttribute("accountType") String accountType, Principal principal) {
        
//     	accountService.withdraw(accountType, Double.parseDouble(amount), principal);

//         return "redirect:/userFront";
        
//     }
    
// }