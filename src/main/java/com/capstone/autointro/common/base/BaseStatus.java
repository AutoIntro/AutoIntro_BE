package com.capstone.autointro.common.base;

import org.springframework.http.HttpStatus;

public interface BaseStatus {
    HttpStatus getHttpStatus();
    String getCode();zz
    String getMessage();
}
