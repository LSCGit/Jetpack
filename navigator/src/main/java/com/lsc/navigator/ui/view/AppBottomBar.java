package com.lsc.navigator.ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MenuItem;


import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;
import com.lsc.navigator.R;
import com.lsc.navigator.model.BottomBar;
import com.lsc.navigator.model.Nav;
import com.lsc.navigator.utils.AppConfig;

import java.util.List;

/**
 * Created by lsc on 2020-03-17 11:08.
 * E-Mail:2965219926@qq.com
 * 自定义底部导航按钮
 */
public class AppBottomBar extends BottomNavigationView {

    private static int[] sIcons = new int[]{R.drawable.icon_tab_home,
            R.drawable.icon_tab_sofa,
            R.drawable.icon_tab_publish,
            R.drawable.icon_tab_find,
            R.drawable.icon_tab_mine};

    private BottomBar mBottomBar;

    private List<BottomBar.Tabs> mTabs;

    public AppBottomBar(Context context) {
        this(context, null);
    }

    public AppBottomBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @SuppressLint("RestrictedApi")
    public AppBottomBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mBottomBar = AppConfig.getBottomBar();
        mTabs = mBottomBar.tabs;

        //设置文本和图标的颜色
        int[][] state = new int[2][];
        state[0] = new int[]{android.R.attr.state_selected};
        state[1] = new int[]{};
            //选中时的颜色，常规时颜色
        int[] colors = new int[]{Color.parseColor(mBottomBar.activeColor), Color.parseColor(mBottomBar.inActiveColor)};
        ColorStateList stateList = new ColorStateList(state, colors);
        setItemTextColor(stateList);
        setItemIconTintList(stateList);
        //LABEL_VISIBILITY_LABELED:设置按钮的文本为一直显示模式
        //LABEL_VISIBILITY_AUTO:当按钮个数小于三个时一直显示，或者当按钮个数大于3个且小于5个时，被选中的那个按钮文本才会显示
        //LABEL_VISIBILITY_SELECTED：只有被选中的那个按钮的文本才会显示
        //LABEL_VISIBILITY_UNLABELED:所有的按钮文本都不显示
        setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
        setSelectedItemId(mBottomBar.selectTab);

        //设置按钮图片
        for (int i = 0; i < mTabs.size(); i++ ){
            BottomBar.Tabs tab = mTabs.get(i);
            if (!tab.enable){
                return;
            }
            int id = getId(tab.pageUrl);
            if (id < 0){
                return;
            }
            MenuItem menuItem = getMenu().add(0,id,tab.index,tab.title);
            menuItem.setIcon(sIcons[tab.index]);

        }

        //设置按钮大小,并着色中间按钮
        for (int i =0;i<mTabs.size();i++){

            BottomBar.Tabs tab = mTabs.get(i);
            int iconSize = dp2px(tab.size);
            //设置按钮大小
            //BottomNavigationMenuView 为底部按钮实例
            BottomNavigationMenuView menuView = (BottomNavigationMenuView) getChildAt(0);
            BottomNavigationItemView itemView = (BottomNavigationItemView) menuView.getChildAt(tab.index);

            itemView.setIconSize(iconSize);
            //为中间按钮找色
            if (TextUtils.isEmpty(tab.title)){
                itemView.setIconTintList(ColorStateList.valueOf(Color.parseColor(tab.tintColor)));
                itemView.setShifting(false);//禁止浮动效果
            }
        }
    }

    /**
     *
     * dp转px
     * @param size
     * @return
     */
    private int dp2px(int size){
        float value = getContext().getResources().getDisplayMetrics().density * size + 0.5f;
        return (int) value;
    }


    private int getId(String pageUrl) {
        Nav nav = AppConfig.getNavConfigs().get(pageUrl);
        if (nav == null){
            return -1;
        }
        return nav.getId();
    }


}