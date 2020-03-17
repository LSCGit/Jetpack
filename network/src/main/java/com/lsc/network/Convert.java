package com.lsc.network;

import java.lang.reflect.Type;

/**
 * Created by lsc on 2020-03-17 15:32.
 * E-Mail:2965219926@qq.com
 *
 * 解析JSON数据
 */
public interface Convert<T> {

    T convert(String response, Type type);

    T convert(String response,Class claz);
}
