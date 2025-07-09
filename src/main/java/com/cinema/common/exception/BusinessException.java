package com.cinema.common.exception;

import com.cinema.common.result.ResultCode;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 业务异常
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * 错误码
     */
    private Integer code;

    /**
     * 错误消息
     */
    private String message;

    /**
     * 构造函数
     *
     * @param message 错误消息
     */
    public BusinessException(String message) {
        this.code = ResultCode.BUSINESS_ERROR;
        this.message = message;
    }

    /**
     * 构造函数
     *
     * @param code 错误码
     * @param message 错误消息
     */
    public BusinessException(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
