package happiness.diary.member;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MemberProfile {
    private final String name;
    private final String email;
    private final String imageUrl;
    private final String provider;
    private final String oauthId;
}
