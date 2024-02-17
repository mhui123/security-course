package com.cos.security1.config.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cos.security1.model.User;
import com.cos.security1.repository.UserRepository;

// 시큐리티 설정에서 loginProcessingUrl("/login");
// /login 요청이 오면 자동으로 UserDetailsService타입으로 IoC되어있는 loadUserByUsername 메서드 실행
@Service
public class PrincipalDetailsService implements UserDetailsService{
	@Autowired
	private UserRepository userRepo;
	
	//해당 이름의 유저가 존재하는지 확인하는 메서드
	// 시큐리티 session => Autentication => UserDetails
	// 함수종료시 @AuthenticationPrincipal 어노테이션 생성
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		System.out.println("123123  "+username);
		User entity = userRepo.findByUsername(username);
		if(entity != null) {
			return new PrincipalDetails(entity);
		}
		return null;
	}
	
}
