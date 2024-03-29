package com.cos.security1.config;

import static org.springframework.security.config.Customizer.withDefaults;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity // 스프링시큐리티 필터가 스프링 필터체인에 등록. 최신 Security버전에서는 사용 안하는듯
public class SecurityConfig {

//	@Autowired
//	private PrincipalOauth2UserService principalOauth2UserService;

	@Bean
	public BCryptPasswordEncoder encodePwd() {
		return new BCryptPasswordEncoder();
	}
	
	
	@Bean
 	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
 		http
 			.authorizeHttpRequests((authorizeHttpRequests) ->
 				authorizeHttpRequests
 					.requestMatchers("/user/**").authenticated()
 					.requestMatchers("/admin/**").hasRole("ADMIN")
 					.requestMatchers("/manager/**").hasAnyRole("ADMIN", "MANAGER")
 					.anyRequest().permitAll()
 			)
 			.formLogin((formLogin) ->
				formLogin
//					.usernameParameter("username")
//					.passwordParameter("password")
					.loginPage("/login")
//					.failureUrl("/authentication/login?failed")
					.loginProcessingUrl("/loginProc")
			);
 		return http.build();
 	}

 	@Bean
 	public UserDetailsService userDetailsService() {
 		PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
 		String pw = encoder.encode("password");
 		UserDetails user = User.withUsername("user")
 			     .password("1234")
 			     .roles("USER")
 			     .build();
 		UserDetails admin = User.withUsername("admin")
			     .password("password")
			     .roles("ADMIN", "USER")
			     .build();
 		
 		return new InMemoryUserDetailsManager(user, admin);
 	}
}
