package com.capstone.autointro.common.status.success;

import com.capstone.autointro.common.base.BaseStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SuccessStatus implements BaseStatus {

    // Common
    SUCCESS("COMM_200", HttpStatus.OK, "성공입니다."),

    // Auth
    LOGIN_SUCCESS("AUTH_200", HttpStatus.OK, "로그인 성공입니다."),
    LOGOUT_SUCCESS("AUTH_200_1", HttpStatus.OK, "로그아웃 성공입니다."),
    TOKEN_REISSUE_SUCCESS("AUTH_200_2", HttpStatus.OK, "토큰 재발급 성공입니다."),

    // User
    USER_INFO_SUCCESS("USER_200", HttpStatus.OK, "회원 정보 조회 성공입니다."),
    USER_PROFILE_CREATED("USER_201", HttpStatus.CREATED, "회원 프로필 등록 성공입니다."),
    USER_UPDATED("USER_200_1", HttpStatus.OK, "회원 정보 수정 성공입니다."),
    USER_DELETED("USER_200_2", HttpStatus.OK, "회원 탈퇴 성공입니다."),

    // Resume
    RESUME_LIST_SUCCESS("RESUME_200", HttpStatus.OK, "이력서 목록 조회 성공입니다."),
    RESUME_DETAIL_SUCCESS("RESUME_200_1", HttpStatus.OK, "이력서 상세 조회 성공입니다."),

    // Git
    PROJECT_LIST_SUCCESS("GIT_200", HttpStatus.OK, "깃 프로젝트 목록 조회 성공입니다."),
    PROJECT_DETAIL_SUCCESS("GIT_200_1", HttpStatus.OK, "깃 프로젝트 상세 조회 성공입니다."),
    PROJECT_SAVED("GIT_201", HttpStatus.CREATED, "깃 프로젝트 저장 성공입니다."),
    PROJECT_UPDATED("GIT_200_2", HttpStatus.OK, "깃 프로젝트 수정 성공입니다."),
    PROJECT_REORGANIZE_SUCCESS("GIT_200_3", HttpStatus.OK, "깃 프로젝트 AI 재정리 성공입니다."),

    // Introduction
    INTRODUCTION_LIST_SUCCESS("INTRO_200", HttpStatus.OK, "자기소개서 목록 조회 성공입니다."),
    INTRODUCTION_DETAIL_SUCCESS("INTRO_200_1", HttpStatus.OK, "자기소개서 조회 성공입니다."),
    INTRODUCTION_CREATED("INTRO_201", HttpStatus.CREATED, "자기소개서 생성 성공입니다."),
    INTRODUCTION_UPDATED("INTRO_200_2", HttpStatus.OK, "자기소개서 수정 성공입니다."),
    INTRODUCTION_DELETED("INTRO_200_3", HttpStatus.OK, "자기소개서 삭제 성공입니다."),
    INTRODUCTION_REORGANIZE_SUCCESS("INTRO_200_4", HttpStatus.OK, "AI 자기소개서 재작성 요청 성공입니다."),
    AI_INTRODUCTION_SUCCESS("INTRO_200_5", HttpStatus.OK, "AI 자기소개서 조회 성공입니다."),

    // Home
    HOME_SUCCESS("HOME_200", HttpStatus.OK, "홈 화면 조회 성공입니다.");

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
