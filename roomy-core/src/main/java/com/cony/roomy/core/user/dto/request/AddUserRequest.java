package com.cony.roomy.core.user.dto.request;

import com.cony.roomy.core.user.domain.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AddUserRequest {

    @NotBlank(message = "이메일 주소를 입력해주세요.")
    @Email(message = "올바른 이메일 주소를 입력해주세요.")
    private String email;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Size(min = 8, max = 16, message = "비밀번호는 8자 이상 16자 이하로 입력해주세요.")
    private String password;

    @NotBlank(message = "휴대폰 번호를 입력해주세요.")
    @Pattern(regexp = "(01[016789])(\\d{3,4})(\\d{4})", message = "올바른 휴대폰 번호를 입력해주세요.")
    private String phoneNumber;

    @NotNull(message = "생년월일을 입력해주세요.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd", timezone = "Asia/Seoul")
    private LocalDate birth;

    @NotBlank(message = "남자(M), 여자(W) 중 하나를 선택해주세요.")
    private String gender;

    @NotBlank(message = "닉네임은 필수에요.")
    private String nickname;

    private List<AddUserAgreementRequest> userAgreements;

    public User toEntity(String encryptedPw) {
        return User.builder()
                .email(email)
                .encryptPassword(encryptedPw)
                .phoneNumber(phoneNumber)
                .birth(birth)
                .gender(gender)
                .nickname(nickname)
                .build();
    }
}
