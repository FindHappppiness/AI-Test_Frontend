package happiness.diary.user;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String oauthId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column
    private String picture;

    @Column(nullable = false)
    private String provider;

    @Enumerated(EnumType.STRING)
    private Role role;

    public User update(String name, String email, String picture) {
        this.name = name;
        this.email = email;
        this.picture = picture;
        return this;
    }
}
