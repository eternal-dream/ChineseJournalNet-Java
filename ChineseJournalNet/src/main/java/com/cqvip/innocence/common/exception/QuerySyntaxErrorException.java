package com.cqvip.innocence.common.exception;

/**
 * @Author eternal
 * @Date 2022/1/6
 * @Version 1.0
 */
public class QuerySyntaxErrorException extends RuntimeException {
    public QuerySyntaxErrorException(String msg){
        super(msg);
    }
}