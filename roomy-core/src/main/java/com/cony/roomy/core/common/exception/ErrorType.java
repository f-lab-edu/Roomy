package com.cony.roomy.core.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorType {

    // Unknown
    UNKNOWN_ERROR("ROOMY-0000", HttpStatus.INTERNAL_SERVER_ERROR.value(), "알 수 없는 에러 발생", "알 수 없는 에러, 잠시 후 다시 시도해주세요."),

    // Global
    INVALID("ROOMY-0001", HttpStatus.BAD_REQUEST.value(), "올바른 값을 입력하지 않았습니다.", "올바른 값을 입력하지 않았습니다."),
    BATCH_FAIL("ROOMY-0002", HttpStatus.INTERNAL_SERVER_ERROR.value(), "배치 동작 중 문제가 발생했습니다.", "배치 에러"),


    // Term & User
    TERM_NOT_FOUND("ROOMY-1000", HttpStatus.NOT_FOUND.value(), "약관 데이터가 누락되었습니다.", "약관 데이터 누락"),
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
    AUTH_AT_TOKEN_NOT_IN_WHITELIST("ROOMY-1102", HttpStatus.UNAUTHORIZED.value(), "인증 억세스 토큰을 화이트 리스트에서 가져오지 못했습니다.", "인증 토큰 오류"),
    AUTH_RT_TOKEN_INVALID("ROOMY-1103", HttpStatus.FORBIDDEN.value(), "인증 리프레시 토큰이 유효하지 않습니다.", "인증 토큰 오류"),
    AUTH_RT_TOKEN_EXPIRED("ROOMY-1104", HttpStatus.FORBIDDEN.value(), "인증 리프레시 토큰이 만료되었습니다.", "인증 토큰 오류"),

    // OAuth User
    OAUTH_UNAUTHORIZED("ROOMY-1201", HttpStatus.UNAUTHORIZED.value(), "OAuth 권한이 없습니다.", "OAuth 요청 실패"),
    OAUTH_BAD_REQUEST("ROOMY-1202", HttpStatus.BAD_REQUEST.value(), "OAuth 요청이 잘못 되었습니다.", "OAuth 요청 실패"),
    OAUTH_NOT_FOUND("ROOMY-1203", HttpStatus.NOT_FOUND.value(), "OAuth 요청 경로가 잘못 되었습니다.", "OAuth 요청 실패"),
    OAUTH_INTERNAL_SERVER_ERROR("ROOMY-1204", HttpStatus.INTERNAL_SERVER_ERROR.value(), "OAuth 요청에 실패했습니다.", "OAuth 요청 실패"),

    // Accommodation
    ACCOMMODATION_NOT_FOUND("ROOMY-3001", HttpStatus.NOT_FOUND.value(), "숙소 정보가 존재하지 않습니다.", "숙소 정보 요청 실패"),

    // Room
    ROOM_NOT_FOUND("ROOMY-4001", HttpStatus.NOT_FOUND.value(), "객실 정보가 존재하지 않습니다.", "객실 정보 요청 실패")

    ;

    private final String code;
    private final int httpCode;
    private final String innerMessage;
    private final String outerMessage;

}
