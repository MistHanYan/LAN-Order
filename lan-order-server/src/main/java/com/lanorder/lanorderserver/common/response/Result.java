package com.lanorder.lanorderserver.common.response;

import lombok.Data;

@Data
public class Result<T> {
    private String code;
    private String msg;
    private T data;

    public boolean getSuccess() {
        return this.code.equals(ErrorEnum.SUCCESS.getCode());
    }

    private Result(String code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    private Result(ErrorEnum errorEnum, T data) {
        this.code = errorEnum.getCode();
        this.msg = errorEnum.getMessage();
        this.data = data;
    }

    public static <T> Result<T> result(ErrorEnum errorEnum, T data) {
        return new Result<>(errorEnum, data);
    }

    public static <T> Result<T> result(String code, String msg, T data) {
        return new Result<>(code, msg, data);
    }
}
