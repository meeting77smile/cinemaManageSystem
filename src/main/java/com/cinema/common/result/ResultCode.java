package com.cinema.common.result;

/**
 * 响应状态码
 */
public class ResultCode {

    /**
     * 成功
     */
    public static final Integer SUCCESS = 200;

    /**
     * 失败
     */
    public static final Integer ERROR = 500;

    /**
     * 未认证
     */
    public static final Integer UNAUTHORIZED = 401;

    /**
     * 未授权
     */
    public static final Integer FORBIDDEN = 403;

    /**
     * 参数错误
     */
    public static final Integer PARAM_ERROR = 400;

    /**
     * 不允许的请求方法
     */
    public static final Integer METHOD_NOT_ALLOWED = 405;

    /**
     * 不支持的媒体类型
     */
    public static final Integer UNSUPPORTED_MEDIA_TYPE = 415;

    /**
     * 资源不存在
     */
    public static final Integer NOT_FOUND = 404;

    /**
     * 业务异常
     */
    public static final Integer BUSINESS_ERROR = 501;
}
