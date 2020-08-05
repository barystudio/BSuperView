package com.bary.sample;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.DisplayCutout;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.core.view.ViewCompat;

import com.bary.ui.BuildConfig;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class AppUtils {

    public static int[] getScreenSize(Context context, boolean useDeviceSize)
    {

        int[] size = new int[2];

        WindowManager w = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display d = w.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        d.getMetrics(metrics);
// since SDK_INT = 1;
        int widthPixels = metrics.widthPixels;
        int heightPixels = metrics.heightPixels;

        if (!useDeviceSize)
        {
            size[0] = widthPixels;
            size[1] = heightPixels - getStatusBarHeight(context);

            return size;
        }

// includes window decorations (statusbar bar/menu bar)
        if (Build.VERSION.SDK_INT >= 14 && Build.VERSION.SDK_INT < 17)
            try
            {
                widthPixels = (Integer) Display.class.getMethod("getRawWidth").invoke(d);
                heightPixels = (Integer) Display.class.getMethod("getRawHeight").invoke(d);
            } catch (Exception ignored)
            {
            }
// includes window decorations (statusbar bar/menu bar)
        if (Build.VERSION.SDK_INT >= 17)
            try
            {
                Point realSize = new Point();
                Display.class.getMethod("getRealSize", Point.class).invoke(d, realSize);
                widthPixels = realSize.x;
                heightPixels = realSize.y;
            } catch (Exception ignored)
            {
            }
        size[0] = widthPixels;
        size[1] = heightPixels;
        return size;
    }

    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Point size = new Point();
        wm.getDefaultDisplay().getSize(size);
        return size.x;
    }

    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Point size = new Point();
        wm.getDefaultDisplay().getSize(size);
        return size.y;
    }



    private static final String BRAND = Build.BRAND.toLowerCase();

    public static boolean isHuawei() {
        return BRAND.contains("huawei") || BRAND.contains("honor");
    }

    public static boolean isXiaomi() {
        return Build.MANUFACTURER.toLowerCase().equals("xiaomi");
    }

    public static boolean isVivo() {
        return BRAND.contains("vivo") || BRAND.contains("bbk");
    }

    public static boolean isOppo() {
        return BRAND.contains("oppo");
    }

    public static void setStatusBarTranslucent(Window window, boolean translucent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setRenderContentInShortEdgeCutoutAreas(window, translucent);

            View decorView = window.getDecorView();
            if (translucent) {
                decorView.setOnApplyWindowInsetsListener((v, insets) -> {
                    WindowInsets defaultInsets = v.onApplyWindowInsets(insets);
                    return defaultInsets.replaceSystemWindowInsets(
                            defaultInsets.getSystemWindowInsetLeft(),
                            0,
                            defaultInsets.getSystemWindowInsetRight(),
                            defaultInsets.getSystemWindowInsetBottom());
                });
            } else {
                decorView.setOnApplyWindowInsetsListener(null);
            }

            ViewCompat.requestApplyInsets(decorView);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (translucent) {
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            } else {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
            ViewCompat.requestApplyInsets(window.getDecorView());
        }
    }


    public static void setNavigationBarColor(final Window window, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setNavigationBarColor(color);
        }
    }

    public static void setNavigationBarStyle(Window window, boolean dark) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            View decorView = window.getDecorView();
            int systemUi = decorView.getSystemUiVisibility();
            if (dark) {
                systemUi |= View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
            } else {
                systemUi &= ~View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
            }
            decorView.setSystemUiVisibility(systemUi);
        }
    }

    public static void setNavigationBarHidden(Window window, boolean hidden) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decorView = window.getDecorView();
            int systemUi = decorView.getSystemUiVisibility();
            if (hidden) {
                systemUi |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
                systemUi |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            } else {
                systemUi &= ~View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
                systemUi &= ~View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            }
            window.getDecorView().setSystemUiVisibility(systemUi);
        }
    }


    public static void setStatusBarColor(final Window window, int color, boolean animated) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            if (animated) {
                int curColor = window.getStatusBarColor();
                ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), curColor, color);
                colorAnimation.addUpdateListener(
                        animator -> window.setStatusBarColor((Integer) animator.getAnimatedValue()));
                colorAnimation.setDuration(200).setStartDelay(0);
                colorAnimation.start();
            } else {
                window.setStatusBarColor(color);
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            ViewGroup decorViewGroup = (ViewGroup) window.getDecorView();
            View statusBarView = decorViewGroup.findViewWithTag("custom_status_bar_tag");
            if (statusBarView == null) {
                statusBarView = new View(window.getContext());
                statusBarView.setTag("custom_status_bar_tag");
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT, getStatusBarHeight(window.getContext()));
                params.gravity = Gravity.TOP;
                statusBarView.setLayoutParams(params);
                decorViewGroup.addView(statusBarView);
            }


            if (animated) {
                Drawable drawable = statusBarView.getBackground();
                int curColor = Integer.MAX_VALUE;
                if (drawable instanceof ColorDrawable) {
                    ColorDrawable colorDrawable = (ColorDrawable) drawable;
                    curColor = colorDrawable.getColor();
                }
                if (curColor != Integer.MAX_VALUE) {
                    final View barView = statusBarView;
                    ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), curColor, color);
                    colorAnimation.addUpdateListener(
                            animator -> barView.setBackground(new ColorDrawable((Integer) animator.getAnimatedValue())));
                    colorAnimation.setDuration(200).setStartDelay(0);
                    colorAnimation.start();
                } else {
                    statusBarView.setBackground(new ColorDrawable(color));
                }
            } else {
                statusBarView.setBackground(new ColorDrawable(color));
            }
        }
    }

    public static void setStatusBarStyle(Window window, boolean dark) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decorView = window.getDecorView();
            int systemUi = decorView.getSystemUiVisibility();
            if (dark) {
                systemUi |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            } else {
                systemUi &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            }
            decorView.setSystemUiVisibility(systemUi);
        }
    }

    public static boolean isDarkStatusBarStyle(Window window) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return (window.getDecorView().getSystemUiVisibility() & View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR) != 0;
        }
        return false;
    }

    public static void setStatusBarHidden(Window window, boolean hidden) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            View decorView = window.getDecorView();
            int systemUi = decorView.getSystemUiVisibility();
            if (hidden) {
                systemUi |= View.SYSTEM_UI_FLAG_FULLSCREEN;
                systemUi |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            } else {
                systemUi &= ~View.SYSTEM_UI_FLAG_FULLSCREEN;
                systemUi &= ~View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            }
            window.getDecorView().setSystemUiVisibility(systemUi);
        }

        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            ViewGroup decorViewGroup = (ViewGroup) window.getDecorView();
            final View statusBarView = decorViewGroup.findViewWithTag("custom_status_bar_tag");
            if (statusBarView != null) {
                boolean hiding = statusBarView.isClickable();
                if (hiding == hidden) {
                    return;
                }

                if (hidden) {
                    statusBarView.setClickable(true);
                    ObjectAnimator animator = ObjectAnimator.ofFloat(statusBarView, "y", -getStatusBarHeight(window.getContext()));
                    animator.setDuration(200);
                    animator.setStartDelay(200);
                    animator.start();
                } else {
                    statusBarView.setClickable(false);
                    ObjectAnimator animator = ObjectAnimator.ofFloat(statusBarView, "y", 0);
                    animator.setDuration(200);
                    animator.start();
                }
            }
        }
    }

    public static boolean isStatusBarHidden(Window window) {
        return (window.getDecorView().getSystemUiVisibility() & View.SYSTEM_UI_FLAG_FULLSCREEN) != 0;
    }

    public static void appendStatusBarPadding(View view, int viewHeight) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (view != null) {
                int statusBarHeight = getStatusBarHeight(view.getContext());
                view.setPadding(view.getPaddingLeft(), statusBarHeight, view.getPaddingRight(), view.getPaddingBottom());
                if (viewHeight > 0) {
                    view.getLayoutParams().height = statusBarHeight + viewHeight;
                } else {
                    view.getLayoutParams().height = viewHeight;
                }
            }
        }
    }

    public static void removeStatusBarPadding(View view, int viewHeight) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (view != null) {
                view.setPadding(view.getPaddingLeft(), 0, view.getPaddingRight(),
                        view.getPaddingBottom());
                view.getLayoutParams().height = viewHeight;
            }
        }
    }

    private static int statusBarHeight = -1;

    public static int getStatusBarHeight(Context context) {
        if (statusBarHeight != -1) {
            return statusBarHeight;
        }

        //获取status_bar_height资源的ID
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }

    private volatile static boolean sHasCheckCutout;
    private volatile static boolean sIsCutout;

    // 是否刘海屏
    public static boolean isCutout(Activity activity) {
        if (sHasCheckCutout) {
            return sIsCutout;
        }

        sHasCheckCutout = true;

        // 低于 API 27 的，都不会是刘海屏、凹凸屏
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O_MR1) {
            sIsCutout = false;
            return false;
        }

        sIsCutout = isHuaweiCutout(activity) || isOppoCutout(activity) || isVivoCutout(activity) || isXiaomiCutout(activity);

        if (!isGoogleCutoutSupport()) {
            return sIsCutout;
        }

        if (!sIsCutout) {
            Window window = activity.getWindow();
            if (window == null) {
                throw new IllegalStateException("activity has not attach to window");
            }
            View decorView = window.getDecorView();
            sIsCutout = attachHasOfficialNotch(decorView);
        }

        return sIsCutout;
    }


    @TargetApi(28)
    private static boolean attachHasOfficialNotch(View view) {
        WindowInsets windowInsets = view.getRootWindowInsets();
        if (windowInsets != null) {
            DisplayCutout displayCutout = windowInsets.getDisplayCutout();
            return displayCutout != null;
        } else {
            throw new IllegalStateException("activity has not yet attach to window, you must call `isCutout` after `Activity#onAttachedToWindow` is called.");
        }
    }

    public static boolean isHuaweiCutout(Context context) {
        if (!isHuawei()) {
            return false;
        }

        boolean ret = false;
        try {
            ClassLoader cl = context.getClassLoader();
            Class HwNotchSizeUtil = cl.loadClass("com.huawei.android.util.HwNotchSizeUtil");
            Method get = HwNotchSizeUtil.getMethod("hasNotchInScreen");
            ret = (boolean) get.invoke(HwNotchSizeUtil);
        } catch (Exception e) {
            // ignore
        }
        return ret;
    }

    public static boolean isOppoCutout(Context context) {
        if (!isOppo()) {
            return false;
        }
        return context.getPackageManager().hasSystemFeature("com.oppo.feature.screen.heteromorphism");
    }

    public static boolean isVivoCutout(Context context) {
        if (!isVivo()) {
            return false;
        }

        boolean ret = false;

        try {
            ClassLoader cl = context.getClassLoader();
            Class ftFeature = cl.loadClass("android.util.FtFeature");
            Method[] methods = ftFeature.getDeclaredMethods();
            if (methods != null) {
                for (int i = 0; i < methods.length; i++) {
                    Method method = methods[i];
                    if (method.getName().equalsIgnoreCase("isFeatureSupport")) {
                        ret = (boolean) method.invoke(ftFeature, 0x00000020);//是否有凹槽
                        break;
                    }
                }
            }
        } catch (Exception e) {
            // ignore
        }

        return ret;
    }

    @SuppressLint("PrivateApi")
    public static boolean isXiaomiCutout(Context context) {
        if (!isXiaomi()) {
            return false;
        }
        try {
            Class spClass = Class.forName("android.os.SystemProperties");
            Method getMethod = spClass.getDeclaredMethod("getInt", String.class, int.class);
            getMethod.setAccessible(true);
            int hasNotch = (int) getMethod.invoke(null, "ro.miui.notch", 0);
            return hasNotch == 1;
        } catch (Exception e) {
            // ignore
        }
        return false;
    }

    public static boolean isGoogleCutoutSupport() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.P;
    }

    public static void setRenderContentInShortEdgeCutoutAreas(Window window, boolean shortEdges) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            if (shortEdges) {
                layoutParams.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            } else {
                layoutParams.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_DEFAULT;
            }
            window.setAttributes(layoutParams);
        }
    }

    /**
     * 屏蔽Android P反射API 系统提示弹窗
     */
    public static void closeAndroidPDialog(){
        try {
            Class aClass = Class.forName("android.content.pm.PackageParser$Package");
            Constructor declaredConstructor = aClass.getDeclaredConstructor(String.class);
            declaredConstructor.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Class cls = Class.forName("android.app.ActivityThread");
            Method declaredMethod = cls.getDeclaredMethod("currentActivityThread");
            declaredMethod.setAccessible(true);
            Object activityThread = declaredMethod.invoke(null);
            Field mHiddenApiWarningShown = cls.getDeclaredField("mHiddenApiWarningShown");
            mHiddenApiWarningShown.setAccessible(true);
            mHiddenApiWarningShown.setBoolean(activityThread, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取版本名
     * @param context 上下文
     * @return 版本名
     */
    public static String getVersionName(Context context) {
        String mName = getPackageInfo(context).versionName.split("-")[0];
        return mName;
    }

    /**
     * 获取版本号
     * @return 版本号
     */
    public static String getVersionCode() {
        return BuildConfig.VERSION_CODE + "";
    }

    /**
     * 获取包名信息
     * @param context 上下文
     * @return PackageInfo
     */
    private static PackageInfo getPackageInfo(Context context) {
        PackageInfo pi = null;

        try {
            PackageManager pm = context.getPackageManager();
            pi = pm.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);

            return pi;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pi;
    }

    /**
     * 检查版本号变更
     * @param context 上下文
     * @param newViewsion 新版本号
     * @return 是否
     */
    public static boolean checkUpdate(Context context, String newViewsion) {
        String tagVersion = getVersionName(context);

        String[] newViewsions = newViewsion.split("\\.");
        String[] tagVersions = tagVersion.split("\\.");
        int size = tagVersions.length;
        if(newViewsions.length<tagVersions.length){
            size = newViewsions.length;
        }

        for (int i = 0; i <size ; i++) {
            if(Integer.parseInt(newViewsions[i])!= Integer.parseInt(tagVersions[i])){
                if(Integer.parseInt(newViewsions[i])> Integer.parseInt(tagVersions[i])){
                    return true;
                }else{
                    return false;
                }
            }
        }

        if(newViewsions.length>tagVersions.length){
            return true;
        }
        return false;
    }
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
