package com.chenxi.finease.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.chenxi.finease.model.User;

@Controller
public class IndexController {

	@GetMapping("/index")
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


	//dash

	@GetMapping("/login")
	public String login() {

		return "main/login";
	}

	@GetMapping("/register")
	public String register() {

		return "main/register";
	}

	@GetMapping("/dashboard")
	public String dashboard() {

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
	
	@GetMapping("/profile")
	public String profile() {

		return "user/profile";
	}

	
}
