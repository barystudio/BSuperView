package com.bary.ui.view.utils;

import android.content.Context;

/**
 * 单位换算
 * author Bary
 * date on 2020/8/10.
 */
public class UnitUtils {
    public static float dp2px(Context context,float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return dipValue * scale + 0.5f;
    }
}
