package com.mwaisaka.Library.Management.System.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ErrorResponse {
    private String errorCode;
    private String message;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    // Default constructor
    public ErrorResponse() {
        this.timestamp = LocalDateTime.now();
    }

    // Constructor with parameters
    public ErrorResponse(String errorCode, String message) {
        this();
        this.errorCode = errorCode;
        this.message = message;
    }

}
