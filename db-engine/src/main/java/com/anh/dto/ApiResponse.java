package com.anh.dto;

public class ApiResponse<T> {

    public int status;
    public String message;
    public T data;

    public ApiResponse() {
    }

    public ApiResponse(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }
}
