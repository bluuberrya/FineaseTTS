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

		return "index";
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

	@GetMapping("/about")
	public String about() {

		return "about";
	}

	@GetMapping("/login")
	public String login() {

		return "login";
	}

	@GetMapping("/register")
	public String register() {

		return "register";
	}

	@GetMapping("/dashboard")
	public String dashboard() {

		return "dashboard";
	}

	@GetMapping("/deposit")
	public String deposit() {

		return "deposit";
	}

	@GetMapping("/withdraw")
	public String withdraw() {

		return "withdraw";
	}

	@GetMapping("/transfer")
	public String transfer() {

		return "transfer";
	}

	@GetMapping("/history")
	public String history() {

		return "history";
	}
}
