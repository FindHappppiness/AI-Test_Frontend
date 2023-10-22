package happiness.diary.oauth2;

import happiness.diary.user.Role;
import happiness.diary.user.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@ToString
@Builder(access = AccessLevel.PRIVATE)
@Getter
public class OAuth2Attributes {
    private Map<String, Object> attributes;
    private String attributeKey;
    private String oauthId;
    private String provider;
    private String email;
    private String name;
    private String picture;

    static OAuth2Attributes of(String provider, String attributeKey,
                               Map<String, Object> attributes) {
        switch (provider) {
            case "google":
                return ofGoogle(provider, attributeKey, attributes);
            case "kakao":
                return ofKakao(provider, "email", attributes);
            case "naver":
                return ofNaver(provider, "id", attributes);
            default:
                throw new RuntimeException();
        }
    }

    private static OAuth2Attributes ofGoogle(String provider, String attributeKey,
                                             Map<String, Object> attributes) {
        return OAuth2Attributes.builder()
                .oauthId((String) attributes.get("sub"))
                .provider(provider)
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .picture((String)attributes.get("picture"))
                .attributes(attributes)
                .attributeKey(attributeKey)
                .build();
    }

    private static OAuth2Attributes ofKakao(String provider, String attributeKey,
                                            Map<String, Object> attributes) {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> kakaoProfile = (Map<String, Object>) kakaoAccount.get("profile");

        return OAuth2Attributes.builder()
                .oauthId(String.valueOf(attributes.get("id")))
                .provider(provider)
                .name((String) kakaoProfile.get("nickname"))
                .email((String) kakaoAccount.get("email"))
                .picture((String) kakaoProfile.get("profile_image_url"))
                .attributes(kakaoAccount)
                .attributeKey(attributeKey)
                .build();
    }

    private static OAuth2Attributes ofNaver(String provider, String attributeKey,
                                            Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        return OAuth2Attributes.builder()
                .oauthId((String) response.get("id"))
                .provider(provider)
                .name((String) response.get("name"))
                .email((String) response.get("email"))
                .picture((String) response.get("profile_image"))
                .attributes(response)
                .attributeKey(attributeKey)
                .build();
    }

    public User toEntity() {
        return User.builder()
                .oauthId(oauthId)
                .provider(provider)
                .name(name)
                .email(email)
                .picture(picture)
                .role(Role.USER)
                .build();
    }

    Map<String, Object> convertToMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("email", email);
        map.put("picture", picture);
        return map;
    }
}
