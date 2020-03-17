# Jetpack
学习jetpack组件

## Jetpack和AndroidX的关系
AndroidX包含Jetpack。

## Navigator
底部按钮
xml BottomNavigationView app:menu="@menu/bottom_nav_menu"
    
xml fragment  android:name="androidx.navigation.fragment.NavHostFragment"
              app:defaultNavHost="true"
              app:navGraph="@navigation/mobile_navigation"

## libnavannotation
navigator 注解  编译JDK为1.8


## libnavcompile
navigator 注解处理器 编译JDK为1.8

implementation project(':libnavannotation')
implementation 'com.alibaba:fastjson:1.2.59'
implementation 'com.google.auto.service:auto-service:1.0-rc6'
annotationProcessor 'com.google.auto.service:auto-service:1.0-rc6'