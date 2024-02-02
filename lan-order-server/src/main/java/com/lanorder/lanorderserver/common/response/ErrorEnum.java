package com.lanorder.lanorderserver.common.response;

public enum ErrorEnum implements ErrorCode {
    /**
     * 成功
     */
    SUCCESS("SUCCESS", "成功"),

    //*********************系统异常*********************//
    /**
     * 请求失败(外域请求等)
     */
    REQUEST_FAIL("REQUEST_FAIL", "请求失败"),
    /**
     * 系统异常
     */
    SYSTEM_ERROR("SYSTEM_ERROR", "系统异常"),
    /**
     * 操作超时
     */
    OP_TIMEOUT("OP_TIMEOUT", "操作超时,请重试"),
    /**
     * 操作冲突(乐观锁、并发)
     */
    OP_CONFLICT("OP_CONFLICT", "操作冲突"),
    /**
     * 数据库执行错误
     */
    DB_ERROR("DB_ERROR", "数据库执行错误"),

    //*********************业务类异常*********************//
    /**
     * 参数错误
     */
    PARAMETER_ERROR("PARAMETER_ERROR", "参数错误"),
    /**
     * 没有权限
     */
    NO_PRIVILEGE("NO_PRIVILEGE", "没有权限"),
    /**
     * 数据异常(数据校验不通过等)
     */
    DATA_ERROR("DATA_ERROR", "数据异常"),
    /**
     * 数据不存在(数据校验等)
     */
    DATA_NOT_FOUND("DATA_NOT_FOUND", "数据不存在"),
    /**
     * 数据已存在(数据校验等)
     */
    DATA_EXIST("DATA_EXIST", "数据已存在");


    /**
     * 结果码
     */
    private final String code;

    /**
     * 结果信息
     */
    private final String message;

    ErrorEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}

