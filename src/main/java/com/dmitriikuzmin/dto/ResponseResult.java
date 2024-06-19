package com.dmitriikuzmin.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseResult<T> {
    private String message;
    private T data;

    public ResponseResult() {
        this.message = "";
    }

    public ResponseResult(String message, T data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResponseResult{" +
                "message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
