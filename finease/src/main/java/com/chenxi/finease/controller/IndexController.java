package com.chenxi.finease.controller;

import java.io.IOException;
import java.math.BigDecimal;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.chenxi.finease.model.CurrentAccount;
import com.chenxi.finease.model.CurrentTransaction;
import com.chenxi.finease.model.SavingsAccount;
import com.chenxi.finease.model.SavingsTransaction;
import com.chenxi.finease.model.User;
import com.chenxi.finease.service.AccountService;
import com.chenxi.finease.service.TransactionService;
import com.chenxi.finease.service.UserService;
import com.itextpdf.text.DocumentException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class IndexController {

	@Autowired
	private UserService userService;

	@Autowired
	private AccountService accountService;

	@Autowired
	private TransactionService transactionService;

	@RequestMapping("/")
	public String home() {

		return "redirect:/index";

	}

	@RequestMapping("/index")
	public String index() {

		return "main/index";

	}

	@GetMapping("/about")
	public String about() {

		return "main/about";
	}

	@GetMapping("/service")
	public String service() {

		return "main/service";
	}

	@GetMapping("/team")
	public String team() {

		return "main/team";
	}

	// login and register
	@GetMapping("/login")
	public String showLoginForm() {
		return "user/login";
	}

	@PostMapping("/login")
	public String login(HttpServletRequest request, HttpSession session) {
		String username = request.getParameter("username");
		String password = request.getParameter("password");

		if (userService.validateUser(username, password)) {
			session.setAttribute("username", username);

			if (username.equals("admin")) {
				return "redirect:/manageuser";
			} else {
				return "redirect:/dashboard";
			}
		} else {
			return "redirect:/login?error=InvalidCredentials";
		}
	}

	@GetMapping("/register")
	public String showRegistrationForm(Model model) {
		User user = new User();
		model.addAttribute("user", user);
		System.out.println(user);
		return "user/register";
	}

	@PostMapping("/register")
	public String registerUser(@ModelAttribute User user, Model model) {
		// Check if the username or email already exists
		if (userService.checkUserExists(user.getUsername(), user.getEmail())) {
			model.addAttribute("error", "Username already exists");
			return "redirect:/register?error=UserExist";
		}
		userService.createUser(user);
		return "redirect:/login"; // Redirect to login page after successful registration
	}

	// user

	@GetMapping("/dashboard")
	public String showDashboard(HttpSession session, Model model) {
		// Retrieve the username from the session
		String username = (String) session.getAttribute("username");

		// Check if the username is not null
		if (username != null) {
			// Retrieve the user object from the database using the username
			User user = userService.findByUsername(username);

			// Assuming the user object contains information about current and savings
			// accounts
			// You can retrieve the account numbers and balances from the user object
			int currentAccountNumber = user.getCurrentAccount().getAccountNumber();
			BigDecimal currentBalance = user.getCurrentAccount().getAccountBalance();
			int savingsAccountNumber = user.getSavingsAccount().getAccountNumber();
			BigDecimal savingsBalance = user.getSavingsAccount().getAccountBalance();

			// Pass the account details to the dashboard page
			model.addAttribute("currentAccountNumber", currentAccountNumber);
			model.addAttribute("currentBalance", currentBalance);
			model.addAttribute("savingsAccountNumber", savingsAccountNumber);
			model.addAttribute("savingsBalance", savingsBalance);
		} else {
			// If the username is null, redirect to the login page
			return "redirect:/login";
		}
		// Return the view name for the dashboard page
		return "user/dashboard";
	}

	@GetMapping("/deposit")
	public String showDepositForm() {

		return "user/deposit";
	}

	@PostMapping("/deposit")
	public String deposit(@ModelAttribute("bankAccount") String accountType,
			@ModelAttribute("depositAmount") double amount,
			HttpSession session) {
		// Retrieve the username from the session
		String username = (String) session.getAttribute("username");

		// Use the service method to fetch the user object from the database using the
		// username
		User user = userService.findByUsername(username);

		if (user != null) {
			try {
				accountService.deposit(accountType, amount, user);
			} catch (DocumentException | IOException e) {
				e.printStackTrace();
			}
		} else {
			return "redirect:/login";
		}

		return "redirect:/deposit?transaction=Success";
	}

	@GetMapping("/withdraw")
	public String showWithdrawForm() {

		return "user/withdraw";
	}

	@PostMapping("/withdraw")
	public String withdraw(@ModelAttribute("bankAccount") String accountType,
			@ModelAttribute("withdrawAmount") double amount,
			HttpSession session,
			Model model) {
		String username = (String) session.getAttribute("username");

		User user = userService.findByUsername(username);

		if (user != null) {
			try {
				if (!accountService.isBalanceSufficient(accountType, amount, user)) {
					return "redirect:/withdraw?error=InsufficientBal";
				}

				accountService.withdraw(accountType, amount, user);
			} catch (DocumentException | IOException e) {
				e.printStackTrace();
			}
		} else {
			return "redirect:/login";
		}

		return "redirect:/withdraw?transaction=Success";
	}

	@GetMapping("/transfer")
	public String showTransferForm(HttpSession session, Model model) {
		String username = (String) session.getAttribute("username");

		if (username != null) {
			List<User> otherUsers = userService.getAllUsersExceptCurrentUser(username);
			model.addAttribute("transferTo", otherUsers);
			model.addAttribute("accountType", "");
			model.addAttribute("transferAmount", "");
		} else {
			return "redirect:/login";
		}

		return "user/transfer";
	}

	@PostMapping("/transfer")
	public String transfer(@ModelAttribute("accountType") String accountType,
			@ModelAttribute("transferTo") String transferToUsername,
			@ModelAttribute("transferAmount") String transferAmount,
			HttpSession session, Model model) {
		String username = (String) session.getAttribute("username");
		User transferTo = userService.findByUsername(transferToUsername);

		if (username != null) {
			User currentUser = userService.findByUsername(username);
			CurrentAccount currentAccount = currentUser.getCurrentAccount();
			SavingsAccount savingsAccount = currentUser.getSavingsAccount();
			BigDecimal transferAmt = new BigDecimal(transferAmount);

			if (("Current".equalsIgnoreCase(accountType)
					&& currentAccount.getAccountBalance().compareTo(transferAmt) < 0) ||
					("Savings".equalsIgnoreCase(accountType)
							&& savingsAccount.getAccountBalance().compareTo(transferAmt) < 0)) {
				return "redirect:/transfer?error=InsufficientBal";
			}

			transactionService.toSomeoneElseTransfer(transferTo, accountType, transferAmount, currentAccount,
					savingsAccount);
		} else {
			return "redirect:/login";
		}

		return "redirect:/transfer?transaction=Success";
	}

	@GetMapping("/history")
	public String showTransactionHistory(HttpSession session, Model model) {
		// Retrieve the username from the session
		String username = (String) session.getAttribute("username");

		if (username != null) {
			List<CurrentTransaction> currentTransactions = transactionService.findCurrentTransactionList(username);
			List<SavingsTransaction> savingsTransactions = transactionService.findSavingsTransactionList(username);

			model.addAttribute("currentTransactions", currentTransactions);
			model.addAttribute("savingsTransactions", savingsTransactions);
			// model.addAttribute("savingsTransactions", savingsTransactions);

			return "user/history";
		} else {
			// If the username is null, redirect to the login page
			return "redirect:/login";
		}
	}

	@GetMapping("/profile")
	public String showProfile(HttpSession session, Model model) {
		// Retrieve the username from the session
		String username = (String) session.getAttribute("username");

		// Check if the username is not null
		if (username != null) {
			// Retrieve the user object from the database using the username
			User user = userService.findByUsername(username);

			// Pass the user object attributes to the profile page
			model.addAttribute("firstName", user.getFirstName());
			model.addAttribute("lastName", user.getLastName());
			model.addAttribute("username", user.getUsername());
			model.addAttribute("email", user.getEmail());
			model.addAttribute("phoneNumber", user.getPhoneNumber());
			model.addAttribute("password", user.getPassword());
		} else {
			// If the username is null, redirect to the login page
			return "redirect:/login";
		}
		// Return the view name for the profile page
		return "user/profile";
	}

	@PostMapping("/profile/update")
	public String updateProfile(@RequestBody User user) {
		userService.updateUser(user);
		return "redirect:/profile?action=Success";
	}

	// admin
	@GetMapping("/manageuser")
	public String showManageUser(HttpSession session, Model model) {
		String username = (String) session.getAttribute("username");

		if (username != null) {
			List<User> users = userService.findUserList();

			List<CurrentAccount> currentAccounts = accountService.findAllCurrentAccountList();
			List<SavingsAccount> savingsAccounts = accountService.findAllSavingsAccountList();

			model.addAttribute("users", users);
			model.addAttribute("currentAccounts", currentAccounts);
			model.addAttribute("savingsAccounts", savingsAccounts);

			return "admin/manageuser";
		} else {
			return "redirect:/login";
		}
	}

	@GetMapping("/adduser")
	public String addUserForm(Model model) {
		User user = new User();
		model.addAttribute("user", user);
		System.out.println(user);

		return "admin/adduser";
	}

	@PostMapping("/adduser")
	public String addUser(@ModelAttribute User user) {
		System.out.println(user);
		userService.createUser(user);

		return "redirect:/adduser?action=Success";
	}

	// edit user
	@GetMapping("/edituser")
	public String editUserForm(Model model) {
		if (!model.containsAttribute("userFrom")) {
			List<User> userlist = userService.getAllUsersExceptCurrentUser("admin");
			model.addAttribute("userlist", userlist);
		}
		return "admin/edituser";
	}

	@PostMapping("/editusersearch")
	public String processEditUserSearchForm(Model model, @RequestParam("userlist") String username) {
		if (username != null) {
			User user = userService.findByUsername(username);
			model.addAttribute("userId", user.getUserId());
			model.addAttribute("firstName", user.getFirstName());
			model.addAttribute("lastName", user.getLastName());
			model.addAttribute("username", user.getUsername());
			model.addAttribute("email", user.getEmail());
			model.addAttribute("phoneNumber", user.getPhoneNumber());
			model.addAttribute("password", user.getPassword());

			if (!model.containsAttribute("userEdit")) {
				List<User> userlist = userService.getAllUsersExceptCurrentUser("admin");
				model.addAttribute("userlist", userlist);
			}
		} else {
			return "redirect:/admin/edituser";
		}

		return "admin/edituser";
	}

	@PostMapping("/edituser/submit")
	public String processEditUserSubmitForm(@ModelAttribute("userEdit") User user) {
		userService.updateUser(user);
		return "redirect:/edituser?action=Success";
	}

	// delete user
	@GetMapping("/deleteuser")
	public String deleteUserForm(Model model) {
		if (!model.containsAttribute("userDelete")) {
			List<User> userlist = userService.getAllUsersExceptCurrentUser("admin");
			model.addAttribute("userlist", userlist);
		}
		return "admin/deleteuser";
	}

	@PostMapping("/deleteusersearch")
	public String processDeleteUserSearchForm(Model model, @RequestParam("userlist") String username) {
		if (username != null) {
			User user = userService.findByUsername(username);
			model.addAttribute("userId", user.getUserId());
			model.addAttribute("firstName", user.getFirstName());
			model.addAttribute("lastName", user.getLastName());
			model.addAttribute("username", user.getUsername());
			model.addAttribute("email", user.getEmail());
			model.addAttribute("phoneNumber", user.getPhoneNumber());
			model.addAttribute("password", user.getPassword());

			if (!model.containsAttribute("userDelete")) {
				List<User> userlist = userService.getAllUsersExceptCurrentUser("admin");
				model.addAttribute("userlist", userlist);
			}
		} else {
			return "redirect:/admin/deleteuser";
		}
		return "admin/deleteuser";
	}

	@PostMapping("/deleteuser/submit")
	public String processDeleteUserSubmitForm(@ModelAttribute("userDelete") User user) {
		userService.deleteUser(user);
		return "redirect:/deleteuser?action=Success";
	}

	@GetMapping("/mudeposit")
	public String showMUDepositForm(Model model) {
		if (!model.containsAttribute("userlist")) {
			List<User> userList = userService.getAllUsersExceptCurrentUser("admin");
			model.addAttribute("userlist", userList);
		}
		return "admin/mudeposit";
	}

	@PostMapping("/mudeposit")
	public String mudeposit(@ModelAttribute("userlist") User user,
			@ModelAttribute("bankAccount") String accountType,
			@ModelAttribute("depositAmount") double amount,
			HttpSession session) {

		if (user != null) {
			try {
				accountService.deposit(accountType, amount, user);
			} catch (DocumentException | IOException e) {
				e.printStackTrace();
			}
		} else {
			return "redirect:/login";
		}

		return "redirect:/mudeposit?transaction=Success";
	}

	@GetMapping("/muwithdraw")
	public String showMUWithdrawForm(Model model) {
		if (!model.containsAttribute("userlist")) {
			List<User> userList = userService.getAllUsersExceptCurrentUser("admin");
			model.addAttribute("userlist", userList);
		}
		return "admin/muwithdraw";
	}

	@PostMapping("/muwithdraw")
	public String muwithdraw(@ModelAttribute("userlist") User user,
			@ModelAttribute("bankAccount") String accountType,
			@ModelAttribute("withdrawAmount") double amount,
			HttpSession session,
			Model model) {

		if (user != null) {
			try {
				if (!accountService.isBalanceSufficient(accountType, amount, user)) {
					return "redirect:/withdraw?error=InsufficientBal";
				}

				accountService.withdraw(accountType, amount, user);
			} catch (DocumentException | IOException e) {
				e.printStackTrace();
			}
		} else {
			return "redirect:/login";
		}

		return "redirect:/muwithdraw?transaction=Success";
	}

	@GetMapping("/mutransfer")
	public String showMUTransferForm(Model model) {
		if (!model.containsAttribute("transferFrom")) {
			List<User> userList = userService.getAllUsersExceptCurrentUser("admin");
			model.addAttribute("transferFrom", userList);
			model.addAttribute("transferTo", userList);
		}

		return "admin/mutransfer";
	}

	@PostMapping("/mutransfer")
	public String mutransfer(@RequestParam("transferFrom") String transferFrom,
			@RequestParam("transferTo") String transferToUsername,
			@RequestParam("accountType") String accountType,
			@RequestParam("transferAmount") String transferAmount,
			HttpSession session, Model model) {
	
		if (transferFrom != null && !transferFrom.equals(transferToUsername)) {
			User transferFromUser = userService.findByUsername(transferFrom);
			CurrentAccount currentAccount = transferFromUser.getCurrentAccount();
			SavingsAccount savingsAccount = transferFromUser.getSavingsAccount();
			BigDecimal transferAmt = new BigDecimal(transferAmount);
	
			if (("Current".equalsIgnoreCase(accountType)
					&& currentAccount.getAccountBalance().compareTo(transferAmt) < 0) ||
					("Savings".equalsIgnoreCase(accountType)
							&& savingsAccount.getAccountBalance().compareTo(transferAmt) < 0)) {
				return "redirect:/mutransfer?error=InsufficientBal";
			}
			User transferTo = userService.findByUsername(transferToUsername);
	
			transactionService.toSomeoneElseTransfer(transferTo, accountType, transferAmount, currentAccount,
					savingsAccount);
	
			System.out.println("Transfer From: " + transferFromUser.getUsername());
			System.out.println("Transfer To: " + transferTo.getUsername());
			System.out.println("Account Type: " + accountType);
			System.out.println("Transfer Amount: " + transferAmount);
		} else {
			return "redirect:/mutransfer?transaction=Failed";
		}
		return "redirect:/mutransfer?transaction=Success";
	}
	

	@GetMapping("/activitylog")
	public String activitylog(HttpSession session, Model model) {
		String username = (String) session.getAttribute("username");

		if (username != "admin") {
			List<CurrentTransaction> currentTransactions = transactionService.findAllCurrentTransactionList();
			List<SavingsTransaction> savingsTransactions = transactionService.findAllSavingsTransactionList();

			model.addAttribute("currentTransactions", currentTransactions);
			model.addAttribute("savingsTransactions", savingsTransactions);

			return "admin/activitylog";
		} else {
			// If the username is null, redirect to the login page
			return "redirect:/login";
		}
	}

	@GetMapping("/systemreport")
	public String systemreport(Model model) {
		int numberOfUsers = userService.getTotalNumberOfUsers();
		int numberOfCurrentAccounts = accountService.getTotalNumberOfCurrentAccounts();
		int numberOfSavingsAccounts = accountService.getTotalNumberOfSavingsAccounts();
		int numberOfCurrentTransactions = transactionService.getTotalNumberOfCurrentTransactions();
		int numberOfSavingsTransactions = transactionService.getTotalNumberOfSavingsTransactions();
		int totalTransactions = numberOfCurrentTransactions + numberOfSavingsTransactions;
		int numberOfDeposits = transactionService.getTotalNumberOfDeposits();
		int numberOfWithdrawals = transactionService.getTotalNumberOfDWithdraws();
		int numberOfTransfers = transactionService.getTotalNumberOfTransfers();

		model.addAttribute("numberOfUsers", numberOfUsers);
		model.addAttribute("numberOfCurrentAccounts", numberOfCurrentAccounts);
		model.addAttribute("numberOfSavingsAccounts", numberOfSavingsAccounts);
		model.addAttribute("numberOfCurrentTransactions", numberOfCurrentTransactions);
		model.addAttribute("numberOfSavingsTransactions", numberOfSavingsTransactions);
		model.addAttribute("totalTransactions", totalTransactions);
		model.addAttribute("numberOfDeposits", numberOfDeposits);
		model.addAttribute("numberOfWithdrawals", numberOfWithdrawals);
		model.addAttribute("numberOfTransfers", numberOfTransfers);

		return "admin/systemreport";
	}

	// common
	@GetMapping("/mainheader")
	public String mainheader() {

		return "common/mainheader";
	}

	@GetMapping("/userheader")
	public String userheader() {

		return "common/userheader";
	}

	@GetMapping("/adminheader")
	public String adminheader() {

		return "common/adminheader";
	}

	@GetMapping("/accesswidget")
	public String accesswidget() {

		return "common/accesswidget";
	}

}