package com.cony.roomy.core.user.service;

import com.cony.roomy.core.common.exception.ErrorType;
import com.cony.roomy.core.common.exception.RoomyException;
import com.cony.roomy.core.common.security.SecurityContextHolder;
import com.cony.roomy.core.user.domain.*;
import com.cony.roomy.core.user.dto.request.AddUserAgreementRequest;
import com.cony.roomy.core.user.dto.request.AddUserRequest;
import com.cony.roomy.core.user.dto.response.UserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final SmsRepository smsRepository;
    private final UserRepository userRepository;
    private final TermRepository termRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public void register(AddUserRequest addUserRequest) {
        // 1. 이메일 중복 and 휴대폰 중복 체크
        if(userRepository.existsByEmail(addUserRequest.getEmail())) {
            throw new RoomyException(ErrorType.USER_ALREADY_EXIST, Map.of("email", addUserRequest.getEmail()), log::error);
        } else if(userRepository.existsByPhoneNumber(addUserRequest.getPhoneNumber())) {
            throw new RoomyException(ErrorType.USER_ALREADY_EXIST, Map.of("phoneNumber", addUserRequest.getPhoneNumber()), log::error);
        }
        // 2. 휴대폰 인증 성공 체크
        if(!smsRepository.hasSuccessLog(addUserRequest.getPhoneNumber())) {
            throw new RoomyException(ErrorType.USER_NOT_CERTIFICATE, Map.of("phoneNumber", addUserRequest.getPhoneNumber(), "user", addUserRequest.getEmail()), log::error);
        }
        // 3. 비밀번호 변경
        String encryptedPw = bCryptPasswordEncoder.encode(addUserRequest.getPassword());
        User user = addUserRequest.toEntity(encryptedPw);
        // 4. 약관에 동의한 데이터 저장
        List<UserAgreement> userAgreements = new ArrayList<>();
        List<AddUserAgreementRequest> agreements = addUserRequest.getUserAgreements();
        for(AddUserAgreementRequest agreement : agreements) {
            // 약관 키로 변환해서 약관을 찾는다.
            TermId termId = agreement.toTermId();
            Term term = termRepository.findById(termId)
                    .orElseThrow(() -> new RoomyException(ErrorType.TERM_NOT_FOUND, Map.of("term subject", agreement.getSubject()), log::warn));
            // 유저 동의한 약관 엔티티를 만들고 리스트에 추가
            UserAgreement userAgreement = UserAgreement.builder().userAgreementId(agreement.toUserAgreementId(user, term)).build();
            userAgreements.add(userAgreement);
        }
        // 약관에 동의한 리스트 저장
        user.createUserAgreements(userAgreements);

        // 5. 회원가입 진행
        userRepository.save(user);
        // 6. 인증성공 키 제거
        smsRepository.deleteSuccessLog(addUserRequest.getPhoneNumber());
    }

    public UserResponse getUser() {
        User user = SecurityContextHolder.getContext();
        return UserResponse.from(user);
    }
}
