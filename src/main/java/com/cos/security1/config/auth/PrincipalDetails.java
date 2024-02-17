package com.cos.security1.config.auth;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.cos.security1.model.User;

//시큐리티가 /login요청을 받으면 낚아채서 대신 로그인을 진행한다.
// 이때 로그인 진행완료되면 시큐리티 session을 만들어 주는데,(Security ContextHolder 키값)
// 세션에 들어갈 오브젝트 => Authentication 타입 객체
// Authentication = {User 정보}
// 이 User오브젝트타입 == UserDetails타입 객체

// Security Session => Authentication => UserDetails

public class PrincipalDetails implements UserDetails{
	
	private User user;
	
	public PrincipalDetails(User user) {
		this.user = user;
	}
	
	//해당유저 권한 리턴
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> collect = new ArrayList<>();
		collect.add(new GrantedAuthority() {
			@Override
			public String getAuthority() {
				return user.getRole();
			}
		});
		return collect;
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		//휴면계정일 경우 return false 해주는 로직필요
		return true;
	}
	
}
