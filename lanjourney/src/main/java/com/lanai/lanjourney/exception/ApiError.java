package com.lanai.lanjourney.exception;

import java.time.OffsetDateTime;

public class ApiError {

    public OffsetDateTime timestamp = OffsetDateTime.now();
    public int status;
    public String message;

    public ApiError(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
