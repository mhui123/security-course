package com.cos.security1.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cos.security1.model.User;

//JpaRepository를 상속했으므로 @Repository 어노테이션 없어도 됨
public interface UserRepository extends JpaRepository<User, Long>{
	public User findByUsername(String username);
}
