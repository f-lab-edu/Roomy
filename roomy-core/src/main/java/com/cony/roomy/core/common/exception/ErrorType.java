package com.cony.roomy.core.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorType {

    // Global or Policy
    INVALID("ROOMY-0001", HttpStatus.BAD_REQUEST.value(), "올바른 값을 입력하지 않았습니다.", "올바른 값을 입력하지 않았습니다."),
    TERM_NOT_FOUND("ROOMY-0002", HttpStatus.NOT_FOUND.value(), "약관 데이터가 누락되었습니다.", "약관 데이터 누락"),

    // User
    USER_NOT_FOUND("ROOMY-1001", HttpStatus.NOT_FOUND.value(), "유저를 찾지 못했습니다.", "유저 조회 실패"),
    USER_ALREADY_EXIST("ROOMY-1002", HttpStatus.BAD_REQUEST.value(), "동일한 정보를 가진 유저가 이미 존재합니다.", "회원가입 실패"),
    USER_EXCEED_RATE_LIMIT("ROOMY-1003", HttpStatus.FORBIDDEN.value(), "10초 후 다시 시도해주세요.", "인증번호 전송 실패"),
    USER_EXCEED_FAIL_COUNT("ROOMY-1004", HttpStatus.FORBIDDEN.value(), "인증번호 5회 이상 실패하여 30분 동안 제한됩니다.", "인증번호 연속 5회 실패"),
    USER_CODE_NOT_FOUND("ROOMY-1005", HttpStatus.FORBIDDEN.value(), "인증번호 유효시간이 만료되었거나, 존재하지 않습니다.", "인증번호 검증 실패"),
    USER_CODE_INVALID("ROOMY-1006", HttpStatus.FORBIDDEN.value(), "인증번호가 일치하지 않습니다. 확인 후 다시 시도해 주세요.", "인증번호 검증 실패"),
    USER_NOT_CERTIFICATE("ROOMY-1007", HttpStatus.FORBIDDEN.value(), "유저의 인증완료 코드가 존재하지 않습니다.", "인증번호 검증 실패"),
    USER_INVALID_PASSWORD("ROOMY-1008", HttpStatus.FORBIDDEN.value(), "비밀번호가 틀립니다.", "유저 비밀번호 실패"),

    // User auth
    AUTH_TOKEN_NOT_BEARER("ROOMY-1101", HttpStatus.UNAUTHORIZED.value(), "인증 토큰을 읽기 위한 헤더가 잘못 되었습니다.", "인증 헤더 오류"),
    AUTH_AT_TOKEN_INVALID("ROOMY-1102", HttpStatus.UNAUTHORIZED.value(), "인증 억세스 토큰이 유효하지 않습니다.", "인증 토큰 오류"),
    AUTH_RT_TOKEN_INVALID("ROOMY-1103", HttpStatus.FORBIDDEN.value(), "인증 리프레시 토큰이 유효하지 않습니다.", "인증 토큰 오류"),
    AUTH_RT_TOKEN_EXPIRED("ROOMY-1104", HttpStatus.FORBIDDEN.value(), "인증 리프레시 토큰이 만료되었습니다.", "인증 토큰 오류"),

    // OAuth User
    OAUTH_USER_PARSING_INVALID("ROOMY-1201", HttpStatus.BAD_REQUEST.value(), "외부 유저 정보를 가져오는데에 실패했습니다.", "외부 유저 오류"),

    ;

    private final String code;
    private final int httpCode;
    private final String innerMessage;
    private final String outerMessage;

}
