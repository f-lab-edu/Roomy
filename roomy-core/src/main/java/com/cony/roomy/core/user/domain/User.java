package com.cony.roomy.core.user.domain;

import com.cony.roomy.core.common.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "user")
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", updatable = false)
    private Long userId;

    @Column(name = "email", updatable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String encryptPassword;

    @Column(name = "phone_number", unique = true)
    private String phoneNumber;

    @Column(name = "birth")
    private LocalDate birth;

    @Column(name = "gender")
    private String gender;

    @Column(name = "nickname")
    private String nickname;

    @OneToMany(mappedBy = "userAgreementId.user", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<UserAgreement> userAgreements = new ArrayList<>();

    @Builder
    public User(String email, String encryptPassword, String phoneNumber, LocalDate birth, String gender, String nickname) {
        this.email = email;
        this.encryptPassword = encryptPassword;
        this.phoneNumber = phoneNumber;
        this.birth = birth;
        this.gender = gender;
        this.nickname = nickname;
    }

    public void createUserAgreements(List<UserAgreement> userAgreements) {
        this.userAgreements = userAgreements;
    }

    public void updateUser(String nickname) {
        this.nickname = nickname;
    }

}
