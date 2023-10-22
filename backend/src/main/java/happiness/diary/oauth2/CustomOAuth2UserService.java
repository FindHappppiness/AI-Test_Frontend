package happiness.diary.oauth2;

import happiness.diary.user.User;
import happiness.diary.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final UserRepository userRepository;
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);// OAuth 서비스(kakao, google, naver)에서 가져온 유저 정보를 담고있음

        String registrationId = userRequest.getClientRegistration().getRegistrationId();// OAuth 서비스 이름(ex. kakao, naver, google)
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();// OAuth 로그인 시 키(pk)가 되는 값

        log.info("userNameAttributeName= {}",userNameAttributeName);
        Map<String, Object> attributes = oAuth2User.getAttributes();
        for (String key : attributes.keySet()) {
            log.info("{}= {}", key, attributes.get(key));
        }
        OAuth2Attributes oAuth2Attributes =
                OAuth2Attributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());
        User user = saveOrUpdate(oAuth2Attributes);


        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getRole().getKey())),
                oAuth2Attributes.convertToMap(), "email");
//        return new DefaultOAuth2User(
//                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
//                oAuth2Attribute.getAttributes(), oAuth2Attribute.getAttributeKey());
//        return new DefaultOAuth2User(
//                Collections.singleton(new SimpleGrantedAuthority(user.getRole().getKey())),
//                attributes, userNameAttributeName);
    }

    private User saveOrUpdate(OAuth2Attributes oAuth2Attributes) {
        User user = userRepository.findByEmail(oAuth2Attributes.getEmail())
                .map(entity -> entity.update(oAuth2Attributes.getName(), oAuth2Attributes.getEmail(), oAuth2Attributes.getPicture()))
                .orElse(oAuth2Attributes.toEntity());

        return userRepository.save(user);
    }
}
