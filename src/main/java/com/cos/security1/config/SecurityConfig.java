package com.cos.security1.config;

import static org.springframework.security.config.Customizer.withDefaults;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity // 스프링시큐리티 필터가 스프링 필터체인에 등록
//@EnableGlobalMethodSecurity(securedEnabled = true) //deprecated
@EnableMethodSecurity
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
 			.csrf((csrf)->csrf.disable())
 			.authorizeHttpRequests((authorizeHttpRequests) ->
 				authorizeHttpRequests
 					.requestMatchers("/user/**").authenticated()
 					.requestMatchers("/admin/**").hasAnyRole("ADMIN")
 					.requestMatchers("/manager/**").hasAnyRole("ADMIN", "MANAGER")
 					.anyRequest().permitAll()
 			)
 			.formLogin((formLogin) ->
				formLogin
//					.usernameParameter("username") // 파라미터로 보낼 이름값 설정하는 부분. username이라고 안쓰고 다른이름 쓰고 싶은 경우 for loadUserByUsername
//					.passwordParameter("password")
					.loginPage("/login")
//					.failureUrl("/authentication/login?failed")
					.loginProcessingUrl("/loginProc") // login주소가 호출되면 시큐리티가 낚아채서 대신 로그인 진행
					.defaultSuccessUrl("/")
			);
 		return http.build();
 	}
// auth에서 따로 관리하므로 아래의 Bean은 불필요하다.
// 	@Bean
// 	public UserDetailsService userDetailsService() {
// 		PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
// 		String pw = encoder.encode("password");
// 		UserDetails user = User.withUsername("user")
// 			     .password("1234")
// 			     .roles("USER")
// 			     .build();
// 		UserDetails admin = User.withUsername("admin")
//			     .password("password")
//			     .roles("ADMIN", "USER")
//			     .build();
// 		
// 		return new InMemoryUserDetailsManager(user, admin);
// 	}
}
