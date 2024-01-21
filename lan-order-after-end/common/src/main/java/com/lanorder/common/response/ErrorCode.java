package com.lanorder.common.response;

public interface ErrorCode {
    /**
     * 获取结果码
     *
     * @return error code
     */
    String getCode();

    /**
     * 获取结果码描述信息
     *
     * @return message
     */
    String getMessage();

}
