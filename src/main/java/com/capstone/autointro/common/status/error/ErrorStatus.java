package com.capstone.autointro.common.status.error;

import com.capstone.autointro.common.base.BaseStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseStatus {

    // Common
    BAD_REQUEST("COMM_400", HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    VALIDATION_ERROR("COMM_422", HttpStatus.UNPROCESSABLE_ENTITY, "요청 값이 올바르지 않습니다."),
    UNAUTHORIZED("COMM_401", HttpStatus.UNAUTHORIZED, "인증이 필요합니다."),
    FORBIDDEN("COMM_403", HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),
    NOT_FOUND("COMM_404", HttpStatus.NOT_FOUND, "요청한 자원을 찾을 수 없습니다."),
    METHOD_NOT_ALLOWED("COMM_405", HttpStatus.METHOD_NOT_ALLOWED, "허용되지 않은 메소드입니다."),
    INTERNAL_SERVER_ERROR("COMM_500", HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류입니다."),

    // Auth
    INVALID_TOKEN("AUTH_401", HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN("AUTH_401_1", HttpStatus.UNAUTHORIZED, "만료된 토큰입니다."),
    REFRESH_TOKEN_NOT_FOUND("AUTH_401_2", HttpStatus.UNAUTHORIZED, "리프레시 토큰을 찾을 수 없습니다."),
    REFRESH_TOKEN_EXPIRED("AUTH_401_3", HttpStatus.UNAUTHORIZED, "리프레시 토큰이 만료되었습니다."),
    REFRESH_TOKEN_MISSING("AUTH_400", HttpStatus.BAD_REQUEST, "리프레시 토큰이 필요합니다."),
    INVALID_SOCIAL_PROVIDER("AUTH_400_1", HttpStatus.BAD_REQUEST, "지원하지 않는 소셜 로그인 제공자입니다."),
    OAUTH2_LOGIN_FAILED("AUTH_401_4", HttpStatus.UNAUTHORIZED, "소셜 로그인에 실패했습니다."),

    // User
    USER_NOT_FOUND("USER_404", HttpStatus.NOT_FOUND, "회원을 찾을 수 없습니다."),
    USER_ALREADY_REGISTERED("USER_409", HttpStatus.CONFLICT, "이미 정보가 등록된 유저입니다."),
    USER_PROFILE_NOT_FOUND("USER_404_1", HttpStatus.NOT_FOUND, "프로필 정보를 먼저 등록해주세요."),

    // Resume
    RESUME_NOT_FOUND("RESUME_404", HttpStatus.NOT_FOUND, "이력서를 찾을 수 없습니다."),
    RESUME_NOT_AUTHORIZED("RESUME_403", HttpStatus.FORBIDDEN, "이력서에 대한 권한이 없습니다."),

    // Git
    PROJECT_NOT_FOUND("GIT_404", HttpStatus.NOT_FOUND, "깃 프로젝트를 찾을 수 없습니다."),
    PROJECT_NOT_AUTHORIZED("GIT_403", HttpStatus.FORBIDDEN, "깃 프로젝트에 대한 권한이 없습니다."),
    PROJECT_ANALYSIS_NOT_FOUND("GIT_404_1", HttpStatus.NOT_FOUND, "프로젝트 분석 결과를 찾을 수 없습니다."),

    // Introduction
    USER_REQUEST_NOT_FOUND("INTRO_404", HttpStatus.NOT_FOUND, "요청 정보를 찾을 수 없습니다."),
    AI_INTRODUCTION_NOT_FOUND("INTRO_404_1", HttpStatus.NOT_FOUND, "AI 자기소개서를 찾을 수 없습니다."),
    USER_INTRODUCTION_NOT_FOUND("INTRO_404_2", HttpStatus.NOT_FOUND, "자기소개서를 찾을 수 없습니다."),
    USER_INTRODUCTION_NOT_AUTHORIZED("INTRO_403", HttpStatus.FORBIDDEN, "자기소개서에 대한 권한이 없습니다."),
    AI_GENERATION_FAILED("INTRO_500", HttpStatus.INTERNAL_SERVER_ERROR, "AI 생성에 실패했습니다."),

    // Term
    TERM_NOT_FOUND("TERM_404", HttpStatus.NOT_FOUND, "약관을 찾을 수 없습니다."),
    TERM_ALREADY_AGREED("TERM_409", HttpStatus.CONFLICT, "이미 동의한 약관입니다.");

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
