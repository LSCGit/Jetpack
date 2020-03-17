package com.lsc.network;

/**
 * Created by lsc on 2020-03-17 14:54.
 * E-Mail:2965219926@qq.com
 *
 * 对返回结果的包装
 */
public class ApiResponse<T> {

    public boolean success;
    public int status;
    public String message;
    public T body;
}
