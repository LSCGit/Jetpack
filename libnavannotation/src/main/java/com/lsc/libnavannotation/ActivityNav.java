package com.lsc.libnavannotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Created by lsc on 2020-03-17 08:32.
 * E-Mail:2965219926@qq.com
 *
 * Activity的Nav注解
 */

@Target(ElementType.TYPE)  //只能标记在类的头部
public @interface ActivityNav {

    String pageUrl();//页面路径
    boolean needLogin() default false;//是否需要登录状态
    boolean asStarter() default false;//因为每一个NavGraph都有一个startDestination设置启动页面。
}
