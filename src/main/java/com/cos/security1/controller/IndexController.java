package com.cos.security1.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cos.security1.model.User;
import com.cos.security1.repository.UserRepository;

@Controller
public class IndexController {
	@Autowired 
	private UserRepository userRepo;
	@Autowired
	private BCryptPasswordEncoder encoder;
	
	
	@GetMapping({ "/", "" })
	public String index() {
		return "index";
	}
	
	@GetMapping("/user")
	@ResponseBody
	public String user() {
		return "user";
	}
	
	@GetMapping("/manager")
	@ResponseBody
	public String manager() {
		return "manager";
	}
	
	@GetMapping("/admin")
	@ResponseBody
	public String admin() {
		return "admin";
	}
	
	@GetMapping("/login")
	public String login() {
		System.out.println("/login 실행시간 " + LocalDateTime.now());
		return "loginForm";
	}
	
	
	@PostMapping("/join")
	public String join(User user) {
		System.out.println(user);
		user.setRole("USER");
		String rawPw = user.getPassword();
		String encryptedPw = encoder.encode(rawPw);
		user.setPassword(encryptedPw);
		userRepo.save(user);
		return "redirect:/login";
	}
	
	@GetMapping("/joinForm")
	public String joinForm() {
		return "joinForm";
	}
	
//	@Secured("ROLE_ADMIN") //deprecated
	@PreAuthorize("hasAuthority('ROLE_ADMIN')") //as new
	@GetMapping("/info")
	@ResponseBody
	public String info() {
		return "info";
	}
}
