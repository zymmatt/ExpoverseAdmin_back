package com.example.admin.entity.Response.Exveption;

public class ServiceException extends RuntimeException {
    private Integer code;
    private final String message;

    public ServiceException(int code, String state) {
        this.code = code;
        this.message = state;
    }

    public ServiceException(String state) {
        this.code = 400;
        this.message = state;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}

