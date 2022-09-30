package com.epam.esm.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BaseResponseDto {
    @JsonProperty("http_status")
    private int httpStatus;
    @JsonProperty("error_code")
    private int errorCode;
    @JsonProperty("message")
    private String message;
    @JsonProperty("data")
    private Object data;

    public BaseResponseDto(int httpStatus, int errorCode, String message) {
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
        this.message = message;
    }

    public BaseResponseDto(int httpStatus, String message, Object data) {
        this.httpStatus = httpStatus;
        this.message = message;
        this.data = data;
    }

    public static BaseResponseDto success(final int httpStatus, final String message, final Object data){
        return new BaseResponseDto(httpStatus, message, data);
    }

    public static BaseResponseDto error(final int httpStatus, final int errorCode, final String message){
        return new BaseResponseDto(httpStatus, errorCode, message);
    }

}
