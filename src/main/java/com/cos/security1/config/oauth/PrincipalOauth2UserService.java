package com.cos.security1.config.oauth;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.cos.security1.config.auth.PrincipalDetails;
import com.cos.security1.model.User;
import com.cos.security1.repository.UserRepository;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService{
//	@Autowired
//	private PasswordEncoder encoder;
	@Autowired
	private UserRepository repo;
	// oAuth 후처리함수. 회원프로필 받는 함수
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		//userRequest는
		System.out.println("user getClientRegistration :" + userRequest.getClientRegistration()); 
		//ClientRegistration{registrationId='google', clientId='1071348618858-au3dbp9lbrgjcvtoqqoalceo1f58mc1n.apps.googleusercontent.com', clientSecret='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', clientAuthenticationMethod=client_secret_basic, authorizationGrantType=org.springframework.security.oauth2.core.AuthorizationGrantType@5da5e9f3, redirectUri='{baseUrl}/{action}/oauth2/code/{registrationId}', scopes=[email, profile], providerDetails=org.springframework.security.oauth2.client.registration.ClientRegistration$ProviderDetails@705d9517, clientName='Google'}
		System.out.println("user getAccessToken :" + userRequest.getAccessToken());
		System.out.println("getAttributes :" + super.loadUser(userRequest).getAttributes());
		//{sub=101296074394196240412, name=김명회, given_name=명회, family_name=김, picture=https://lh3.googleusercontent.com/a/ACg8ocKkeFQXluvrgIHpYfmwJTOFsFrsddUHhbogDsxl-Fg=s96-c, email=mhui7864@gmail.com, email_verified=true, locale=ko}
		//구글로 부터 받은 user request data의 후처리를 해야한다.
		
		Map<String, Object> userInfo = super.loadUser(userRequest).getAttributes();
		String provider = userRequest.getClientRegistration().getRegistrationId();
		String providerId = String.valueOf(userInfo.get("sub"));
		String username = provider+"_"+providerId;
		String password = "getInthere";
		String email = String.valueOf(userInfo.get("email"));
		String role = "ROLE_USER";
		
		User userEntity = repo.findByUsername(username);
		if(userEntity == null) {
			userEntity = User.builder()
					.username(username).password(password).email(email)
					.role(role).provider(provider).providerId(providerId)
					.build();
			//회원가입
			repo.save(userEntity);
			return super.loadUser(userRequest);
		}
		return new PrincipalDetails(userEntity, userInfo);
	}
}
