package com.lsc.network;

import android.util.Log;

import androidx.annotation.IntDef;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by lsc on 2020-03-17 14:37.
 * E-Mail:2965219926@qq.com
 */
public abstract class Request<T,R extends Request> {
    protected String mUrl;
    protected HashMap<String,String> headers = new HashMap<>();
    protected HashMap<String,Object> params = new HashMap<>();

    protected String cacheKey;//缓存key
    protected Type mType;

    //仅仅只访问本地缓存，即便本地缓存不存在，也不会发起网络请求
    public static final int CACHE_ONLY = 1;
    //先访问缓存，同时发起网络的请求，成功后缓存到本地
    public static final int CACHE_FIRST = 2;
    //仅仅只访问服务器，不存任何存储
    public static final int NET_ONLY = 3;
    //先访问网络，成功后缓存到本地
    public static final int NET_CACHE = 4;
    private Class mClaz;

    @IntDef({CACHE_ONLY, CACHE_FIRST, NET_CACHE, NET_ONLY})
    @Retention(RetentionPolicy.SOURCE)
    public @interface CacheStrategy {

    }

    public Request(String url){
        mUrl = url;
    }

    public R addHeader(String key,String value){
        headers.put(key,value);
        return (R)this;
    }

    public R addParam(String key,Object value){

        if (value == null) {
            return (R) this;
        }

        try{
            if (value.getClass() == String.class) {// String类型
                params.put(key, value);
            } else {
                Field field = value.getClass().getField("TYPE");//基本类型的包装类都有这个属性
                Class claz = (Class) field.get(null);
                if (claz.isPrimitive()) {//如果是基本类型则添加进去
                    params.put(key, value);
                }
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return (R) this;
    }

    /**
     *
     * 缓存的key用来缓存和读取
     * @param key
     * @return
     */
    public R cacheKey(String key){
        this.cacheKey = key;
        return (R) this;
    }

    public void execute(final JsonCallback<T> callback){

        getCall().enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                ApiResponse<T> response = new ApiResponse<>();
                response.message = e.getMessage();
                callback.onError(response);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                ApiResponse<T> apiResponse = parseResponse(response,callback);
                if (apiResponse.success){
                    callback.onError(apiResponse);
                }else{
                    callback.onSuccess(apiResponse);
                }
            }
        });
    }

    /**
     * 同步请求
     */
    public ApiResponse execute(){
        Response response = null;
        try {
            response = getCall().execute();
            ApiResponse<T> result = parseResponse(response,null);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public R responseType(Type type){
        mType = type;
        return (R)this;
    }
    public R responseType(Class claz){
        mClaz = claz;
        return (R)this;
    }

    private ApiResponse<T> parseResponse(Response response,JsonCallback<T> callback) {

        String message = null;
        int status = response.code();
        boolean success = response.isSuccessful();
        ApiResponse<T> result = new ApiResponse<>();
        Convert convert = ApiService.mConvert;
        try{
            String content = response.body().string();
            if (success){
                if (callback != null){
                    ParameterizedType parameterizedType = (ParameterizedType) callback.getClass().getGenericSuperclass();
                    Type argument = parameterizedType.getActualTypeArguments()[0];
                    result.body = (T) convert.convert(content,argument);
                }else if (mType != null){
                    result.body = (T) convert.convert(content,mType);
                }else if (mClaz != null){
                    result.body = (T) convert.convert(content,mClaz);
                }else {
                    Log.e("networkRequest","parseResponse: 无法解析");
                }
            }else {
                message = content;
            }
        } catch (IOException e) {
            message = e.getMessage();
            success = false;
        }

        return result;
    }

    private okhttp3.Call getCall(){
        okhttp3.Request.Builder builder = new okhttp3.Request.Builder();
        addHeaders(builder);
        okhttp3.Request request = generateRequest(builder);
        Call call = ApiService.okHttpClient.newCall(request);
        return call;
    }

    protected abstract okhttp3.Request generateRequest(okhttp3.Request.Builder builder);

    private void addHeaders(okhttp3.Request.Builder builder){
        for (Map.Entry<String,String> entry : headers.entrySet()){
            builder.addHeader(entry.getKey(),entry.getValue());
        }
    }
}
