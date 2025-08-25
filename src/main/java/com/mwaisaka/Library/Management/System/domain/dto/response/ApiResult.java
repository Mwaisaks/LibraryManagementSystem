package com.mwaisaka.Library.Management.System.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResult<T> {
    private boolean success;
    private String message;
    private T data;

    public static <T> ApiResult<T> success(String message, T data) {
        return new ApiResult<>(true, message, data);
    }

    public static <T> ApiResult<T> success(String message) {
        return new ApiResult<>(true, message, null);
    }

    public static <T> ApiResult<T> error(String message) {
        return new ApiResult<>(false, message, null);
    }
}