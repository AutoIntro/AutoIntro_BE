package com.capstone.autointro.common.status.success;

import com.capstone.autointro.common.base.BaseStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SuccessStatus implements BaseStatus {

    // common
    SUCCESS_200("COMM_200", HttpStatus.OK, "성공입니다."),
    SUCCESS_201("COMM_201", HttpStatus.CREATED, "성공입니다."),
    SUCCESS_204("COMM_204", HttpStatus.NO_CONTENT, "성공입니다.");

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
