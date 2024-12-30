package com.cony.roomy.core.user.dto.response;

import com.cony.roomy.core.user.domain.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class UserResponse {
    private String nickname;
    private String email;
    private String phoneNumber;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd", timezone = "Asia/Seoul")
    private LocalDate birth;
    private String gender;

    public static UserResponse from(User user) {
        return UserResponse.builder()
                .nickname(user.getNickname())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .birth(user.getBirth())
                .gender(user.getGender())
                .build();
    }
}
