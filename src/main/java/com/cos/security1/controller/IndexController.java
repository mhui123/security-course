package com.cos.security1.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cos.security1.config.auth.PrincipalDetails;
import com.cos.security1.model.User;
import com.cos.security1.repository.UserRepository;

@Controller
public class IndexController {
	@Autowired 
	private UserRepository userRepo;
	@Autowired
	private BCryptPasswordEncoder encoder;
	
	@ResponseBody
	@GetMapping("/test/login")
	public String loadingTest(
			Authentication auth,
			@AuthenticationPrincipal PrincipalDetails userDetails
		) {
		System.out.println("test/login =============");
		PrincipalDetails principalDetails = (PrincipalDetails) auth.getPrincipal();
		System.out.println("authentication:"+ principalDetails.getUser());
		
		System.out.println("userDetails : " + userDetails.getUser());
		return "세션확인";
	}
	
	@ResponseBody
	@GetMapping("/test/oauth")
	public String loadingOathTest(Authentication auth,
			@AuthenticationPrincipal OAuth2User oauth
			) {
		System.out.println("test/login =============");
		OAuth2User userInfo = (OAuth2User) auth.getPrincipal();
		System.out.println("oAuth attributes:"+ userInfo.getAttributes());
		System.out.println("test2 : " + oauth.getAttributes());
		
		return "세션확인";
	}
	
	
	@GetMapping({ "/", "" })
	public String index() {
		return "index";
	}
	
	@GetMapping("/user")
	@ResponseBody
	// @AuthenticationPrincipal PrincipalDetails userDetails은 사용자 PrincipalDetails 조회
	public String user(@AuthenticationPrincipal PrincipalDetails userDetails) {
		System.out.println("principal Details : " + userDetails.getUser());
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
