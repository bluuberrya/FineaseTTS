package com.chenxi.finease.controller;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.chenxi.finease.model.User;
import com.chenxi.finease.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class IndexController {

    @Autowired
    private UserService userService;
	
	// @GetMapping("/index")
	// public String index() {

	// 	return "main/index";
	// }

    @RequestMapping("/")
    public String home() {
    	
        return "redirect:/index";
        
    }

    @RequestMapping("/index")
    public String index() {
    	
        return "main/index";
        
    }


	// @PostMapping("/first")
	// public String userRegistration(@ModelAttribute User user, Model model) {
	// // System.out.println(user.toString());
	// // // validate
	// // System.out.println(user.getFname());
	// // System.out.println(user.getLname());
	// // System.out.println(user.getDob());
	// // System.out.println(user.getEmail());
	// // model.addAttribute("firstname", user.getFname());
	// // model.addAttribute("lastname", user.getLname());
	// return "reload";
	// }

	@GetMapping("/test")
	public String test() {

		return "main/test";
	}

	@GetMapping("/about")
	public String about() {

		return "main/about";
	}

	@GetMapping("/service")
	public String service() {

		return "main/service";
	}

	@GetMapping("/why")
	public String why() {

		return "main/why";
	}

	@GetMapping("/team")
	public String team() {

		return "main/team";
	}

//login and register

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
        return "user/register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user) {
		System.out.println(user);
        userService.createUser(user);
        return "redirect:/login"; // Redirect to login page after registration
    }

//user

	@GetMapping("/dashboard")
	public String showDashboard(HttpSession session, Model model) {
		// Retrieve the username from the session
		String username = (String) session.getAttribute("username");

		// Check if the username is not null
		if (username != null) {
			// Retrieve the user object from the database using the username
			User user = userService.findByUsername(username);

			// Assuming the user object contains information about current and savings accounts
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

			System.out.println("\n\n\n"+ currentAccountNumber);
		} else {
			// If the username is null, redirect to the login page
			return "redirect:/login";
		}
		// Return the view name for the dashboard page
		return "user/dashboard";
	}

	@GetMapping("/deposit")
	public String deposit() {

		return "user/deposit";
	}

	@GetMapping("/withdraw")
	public String withdraw() {

		return "user/withdraw";
	}

	@GetMapping("/transfer")
	public String transfer() {

		return "user/transfer";
	}

	@GetMapping("/history")
	public String history() {

		return "user/history";
	}
	
	// @GetMapping("/profile")
	// public String profile() {

	// 	return "user/profile";
	// }

	// @GetMapping("/profile")
	// public String showProfile(HttpSession session, Model model) {
	// 	// Retrieve the username from the session
	// 	String username = (String) session.getAttribute("username");

	// 	// Pass the username to the profile page
	// 	model.addAttribute("username", username);

	// 	// Return the view name for the profile page
	// 	return "user/profile";
	// }

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
        return "redirect:/profile";
    }

//admin

	@GetMapping("/manageuser")
	public String manageuser() {

		return "admin/manageuser";
	}

	@GetMapping("/activitylog")
	public String activitylog() {

		return "admin/activitylog";
	}

	@GetMapping("/systemreport")
	public String systemreport() {

		return "admin/systemreport";
	}


//common
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