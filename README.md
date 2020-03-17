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

FixFragmentNavigator 默认的Navigator是通过replace来设置fragment的，这种方式会造成fragment的重启数据和布局的重新加载，所以我们使用hide和show。             

## libnavannotation
navigator 注解  编译JDK为1.8


## libnavcompile
navigator 注解处理器 编译JDK为1.8

implementation project(':libnavannotation')
implementation 'com.alibaba:fastjson:1.2.59'
implementation 'com.google.auto.service:auto-service:1.0-rc6'
annotationProcessor 'com.google.auto.service:auto-service:1.0-rc6'

## network 
AndroidManifest 允许明文请求，允许对Http接口进行访问 android:usesCleartextTraffic="true"

网络库 OkHttp

## room
room 是Google为了简化旧式的SQLite，只是对SQLite的简单封装。拥有SQLite所有功能，可以和liveData，lifeCycle，paging融合。
