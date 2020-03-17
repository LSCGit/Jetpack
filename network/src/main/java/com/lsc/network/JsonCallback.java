package com.lsc.network;

/**
 * Created by lsc on 2020-03-17 14:53.
 * E-Mail:2965219926@qq.com
 */
public abstract class JsonCallback<T> {

    public void onSuccess(ApiResponse<T> response){

    }

    public void onError(ApiResponse<T> response){

    }

    public void onCacheSuccess(ApiResponse<T> response){

    }
}
