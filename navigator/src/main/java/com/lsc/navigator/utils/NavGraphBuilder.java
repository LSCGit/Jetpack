package com.lsc.navigator.utils;

import android.content.ComponentName;

import androidx.fragment.app.FragmentActivity;
import androidx.navigation.ActivityNavigator;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.NavGraphNavigator;
import androidx.navigation.NavigatorProvider;
import androidx.navigation.fragment.FragmentNavigator;

import com.lsc.navigator.FixFragmentNavigator;
import com.lsc.navigator.model.Nav;

import java.util.HashMap;

/**
 * Created by lsc on 2020-03-17 10:34.
 * E-Mail:2965219926@qq.com
 */
public class NavGraphBuilder {

    public static void build(NavController controller, FragmentActivity activity,int containerId){
        NavigatorProvider navigatorProvider = controller.getNavigatorProvider();

        FixFragmentNavigator fragmentNavigator = new FixFragmentNavigator(activity,activity.getSupportFragmentManager(),containerId);
        navigatorProvider.addNavigator(fragmentNavigator);

        ActivityNavigator activityNavigator = navigatorProvider.getNavigator(ActivityNavigator.class);

        NavGraph navGraph = new NavGraph(new NavGraphNavigator(navigatorProvider));

        HashMap<String, Nav> navConfigs = AppConfig.getNavConfigs();
        for (Nav value : navConfigs.values()){
            if (value.isActivity()){
                ActivityNavigator.Destination destination = activityNavigator.createDestination();
                destination.setId(value.getId());
                destination.setComponentName(new ComponentName(
                        AppGlobals.getApplication().getPackageName(),value.getClazzName()));
                destination.addDeepLink(value.getPageUrl());

                navGraph.addDestination(destination);
            }else {
                FragmentNavigator.Destination destination = fragmentNavigator.createDestination();
                destination.setClassName(value.getClazzName());
                destination.setId(value.getId());
                destination.addDeepLink(value.getPageUrl());

                navGraph.addDestination(destination);
            }

            if (value.asStarter()){
                navGraph.setStartDestination(value.getId());
            }
        }

        controller.setGraph(navGraph);
    }
}
