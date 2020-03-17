package com.lsc.navigator.utils;

import android.app.Application;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static java.lang.Class.forName;

/**
 * Created by lsc on 2020-03-17 10:15.
 * E-Mail:2965219926@qq.com
 *
 * 用于获取Application对象
 */
public class AppGlobals {

    private static Application mApplication;
    public static Application getApplication(){

        if (mApplication == null){
            try {
                Class activityThread = Class.forName("android.app.ActivityThread");
                Method method = activityThread.getDeclaredMethod("currentApplication");
                mApplication = (Application) method.invoke(null,null);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        return mApplication;
    }
}
