package com.cos.security1.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.cos.security1.config.oauth.PrincipalOauth2UserService;

@Configuration
@EnableWebSecurity // 스프링시큐리티 필터가 스프링 필터체인에 등록
//@EnableGlobalMethodSecurity(securedEnabled = true) //deprecated
@EnableMethodSecurity
public class SecurityConfig {

	@Autowired
	private PrincipalOauth2UserService principalOauth2UserService;

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
 					)
 			.oauth2Login((oAuthLogin) ->
 					oAuthLogin.loginPage("/loginForm")
 					.userInfoEndpoint().userService(principalOauth2UserService)
 					)
 			;
 		return http.build();
 	}
	
//	
//	 	@Bean
//	 	public ClientRegistrationRepository clientRegistrationRepository() {
//	 		return new InMemoryClientRegistrationRepository(this.googleClientRegistration());
//	 	}
//	 
//	  	private ClientRegistration googleClientRegistration() {
//	  		return ClientRegistration.withRegistrationId("google")
//	  			.clientId("google-client-id")
//	  			.clientSecret("google-client-secret")
//	  			.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
//	  			.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
//	  			.redirectUri("{baseUrl}/login/oauth2/code/{registrationId}")
//	  			.scope("openid", "profile", "email", "address", "phone")
//	  			.authorizationUri("https://accounts.google.com/o/oauth2/v2/auth")
//	  			.tokenUri("https://www.googleapis.com/oauth2/v4/token")
//	  			.userInfoUri("https://www.googleapis.com/oauth2/v3/userinfo")
//	  			.userNameAttributeName(IdTokenClaimNames.SUB)
//	  			.jwkSetUri("https://www.googleapis.com/oauth2/v3/certs")
//	 			.clientName("Google")
//	  			.build();
//	 	}
}
