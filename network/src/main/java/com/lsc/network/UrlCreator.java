package com.lsc.network;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by lsc on 2020-03-17 15:06.
 * E-Mail:2965219926@qq.com
 * 拼接get 请求的 url地址
 */
public class UrlCreator {

    public static String createUrlFromParams(String url, Map<String,Object> params) {
        StringBuilder builder = new StringBuilder();
        builder.append(url);
        if (url.indexOf("?") > 0 || url.indexOf("&") > 0) {
            builder.append("&");
        } else {
            builder.append("?");
        }
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            try {
                String value = URLEncoder.encode(String.valueOf(entry.getValue()), "UTF-8");
                builder.append(entry.getKey()).append("=").append(value).append("&");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }
        builder.deleteCharAt(builder.length() - 1);
        return builder.toString();
    }
}
