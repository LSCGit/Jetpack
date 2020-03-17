package com.lsc.navigator;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavDestination;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigator;
import androidx.navigation.fragment.FragmentNavigator;

import java.lang.reflect.Field;
import java.util.ArrayDeque;
import java.util.Map;

/**
 * Created by lsc on 2020-03-17 13:35.
 * E-Mail:2965219926@qq.com
 *
 * 因为默认的Navigator是通过replace来设置fragment的，这种方式会造成fragment的重启
 * 数据和布局的重新加载，所以我们使用hide和show。
 */

@Navigator.Name("fixfragment")  //每个Navigator都需要标记这个注解
public class FixFragmentNavigator extends FragmentNavigator {

    private static final String TAG = "FixFragmentNavigator";

    private Context mContext;
    private FragmentManager mManager;
    private int mContainerId;
    public FixFragmentNavigator(@NonNull Context context, @NonNull FragmentManager manager, int containerId) {
        super(context, manager, containerId);

        mContext = context;
        mManager = manager;
        mContainerId = containerId;
    }

    @Nullable
    @Override
    public NavDestination navigate(@NonNull Destination destination, @Nullable Bundle args, @Nullable NavOptions navOptions, @Nullable Navigator.Extras navigatorExtras) {

        if (mManager.isStateSaved()) {
            Log.i(TAG, "Ignoring navigate() call: FragmentManager has already"
                    + " saved its state");
            return null;
        }
        String className = destination.getClassName();
        if (className.charAt(0) == '.') {
            className = mContext.getPackageName() + className;
        }

        //final Fragment frag = instantiateFragment(mContext, mManager,
        //        className, args);
        //frag.setArguments(args);
        final FragmentTransaction ft = mManager.beginTransaction();

        int enterAnim = navOptions != null ? navOptions.getEnterAnim() : -1;
        int exitAnim = navOptions != null ? navOptions.getExitAnim() : -1;
        int popEnterAnim = navOptions != null ? navOptions.getPopEnterAnim() : -1;
        int popExitAnim = navOptions != null ? navOptions.getPopExitAnim() : -1;
        if (enterAnim != -1 || exitAnim != -1 || popEnterAnim != -1 || popExitAnim != -1) {
            enterAnim = enterAnim != -1 ? enterAnim : 0;
            exitAnim = exitAnim != -1 ? exitAnim : 0;
            popEnterAnim = popEnterAnim != -1 ? popEnterAnim : 0;
            popExitAnim = popExitAnim != -1 ? popExitAnim : 0;
            ft.setCustomAnimations(enterAnim, exitAnim, popEnterAnim, popExitAnim);
        }
//-------------  优化  将replace 改为 hide和show-----------------------
        Fragment fragment = mManager.getPrimaryNavigationFragment();//获取到当前显示的Fragment
        if (fragment != null){
            ft.hide(fragment);
        }

        Fragment frag = null;
        String tag = String.valueOf(destination.getId());

        frag = mManager.findFragmentByTag(tag);
        if (frag != null){
            ft.show(frag);
        }else {
            frag = instantiateFragment(mContext,mManager,className,args);
            frag.setArguments(args);
            ft.add(mContainerId,frag,tag);
        }
        //ft.replace(mContainerId, frag);
// ---------------------------------------------
        ft.setPrimaryNavigationFragment(frag);

        final @IdRes int destId = destination.getId();
//-----通过反射得到 私有变量-----------------------
        ArrayDeque<Integer> mBackStack = null;
        boolean mIsPendingBackStackOperation = false;
        try {
            Field field = FragmentNavigator.class.getDeclaredField("mBackStack");
            field.setAccessible(true);
            mBackStack = (ArrayDeque<Integer>) field.get(this);

            Field field1 = FragmentNavigator.class.getDeclaredField("mIsPendingBackStackOperation");
            field1.setAccessible(true);
            mIsPendingBackStackOperation = (boolean) field1.get(this);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
//--------------------------

        final boolean initialNavigation = mBackStack.isEmpty();
        // TODO Build first class singleTop behavior for fragments
        final boolean isSingleTopReplacement = navOptions != null && !initialNavigation
                && navOptions.shouldLaunchSingleTop()
                && mBackStack.peekLast() == destId;

        boolean isAdded;
        if (initialNavigation) {
            isAdded = true;
        } else if (isSingleTopReplacement) {
            // Single Top means we only want one instance on the back stack
            if (mBackStack.size() > 1) {
                // If the Fragment to be replaced is on the FragmentManager's
                // back stack, a simple replace() isn't enough so we
                // remove it from the back stack and put our replacement
                // on the back stack in its place
                mManager.popBackStack(
                        generateBackStackName(mBackStack.size(), mBackStack.peekLast()),
                        FragmentManager.POP_BACK_STACK_INCLUSIVE);
                ft.addToBackStack(generateBackStackName(mBackStack.size(), destId));
                mIsPendingBackStackOperation = true;
            }
            isAdded = false;
        } else {
            ft.addToBackStack(generateBackStackName(mBackStack.size() + 1, destId));
            mIsPendingBackStackOperation = true;
            isAdded = true;
        }
        if (navigatorExtras instanceof Extras) {
            Extras extras = (Extras) navigatorExtras;
            for (Map.Entry<View, String> sharedElement : extras.getSharedElements().entrySet()) {
                ft.addSharedElement(sharedElement.getKey(), sharedElement.getValue());
            }
        }
        ft.setReorderingAllowed(true);
        ft.commit();
        // The commit succeeded, update our view of the world
        if (isAdded) {
            mBackStack.add(destId);
            return destination;
        } else {
            return null;
        }
    }

    private String generateBackStackName(int backStackindex,int destid){

        return backStackindex+"-"+destid;
    }
}
