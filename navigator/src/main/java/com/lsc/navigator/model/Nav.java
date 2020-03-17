package com.lsc.navigator.model;

/**
 * Created by lsc on 2020-03-17 10:10.
 * E-Mail:2965219926@qq.com
 *
 * Json
 * "com.lsc.navigator.ui.home.HomeFragemnt": {
 *     "asStarter": true,
 *     "needLogin": false,
 *     "isActivity": false,
 *     "pageUrl": "com.lsc.navigator.ui.home.HomeFragemnt",
 *     "id": 451246214,
 *     "clazzName": "com.lsc.navigator.ui.home.HomeFragment"
 *   }
 */
public class Nav {

    /**
     * asStarter : true
     * needLogin : false
     * isActivity : false
     * pageUrl : com.lsc.navigator.ui.home.HomeFragemnt
     * id : 451246214
     * clazzName : com.lsc.navigator.ui.home.HomeFragment
     */

    private boolean asStarter;
    private boolean needLogin;
    private boolean isActivity;
    private String pageUrl;
    private int id;
    private String clazzName;

    public boolean asStarter() {
        return asStarter;
    }

    public void setAsStarter(boolean asStarter) {
        this.asStarter = asStarter;
    }

    public boolean isNeedLogin() {
        return needLogin;
    }

    public void setNeedLogin(boolean needLogin) {
        this.needLogin = needLogin;
    }

    public boolean isActivity() {
        return isActivity;
    }

    public void setIsActivity(boolean isActivity) {
        this.isActivity = isActivity;
    }

    public String getPageUrl() {
        return pageUrl;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getClazzName() {
        return clazzName;
    }

    public void setClazzName(String clazzName) {
        this.clazzName = clazzName;
    }
}
