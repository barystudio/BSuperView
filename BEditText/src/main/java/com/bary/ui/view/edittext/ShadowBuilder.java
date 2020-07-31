package com.bary.ui.view.edittext;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.view.View;

import com.bary.ui.R;

import java.lang.reflect.Field;

/**
 * author Bary
 * date on 2020/1/21.
 */
public class ShadowBuilder extends BaseBuilder implements IShadowInterface {


    private int mShadowColor;
    private int mBackGroundColor;
    private float mShadowSize;
    private float mShadowDx, mShadowDy;
    private boolean isShowShadow = false;
    private Context mContext;

    public ShadowBuilder(View view, ISuperInterface parentInterface) {
        super(view, parentInterface);
        mContext = view.getContext();

    }

    /**
     * 初始化阴影属性
     *
     * @param attr 属性集合
     */
    public void initShadowAttributes(TypedArray attr) {
        if (attr == null) {
            return;
        }
        //默认是显示
        isShowShadow = attr.getBoolean(getStyleableId("showShadow"), false);
        //默认扩散区域宽度
        mShadowSize = attr.getDimension(getStyleableId("shadowSize"), 15);
        //x轴偏移量
        mShadowDx = attr.getDimension(getStyleableId("shadowDx"), 0);
        //y轴偏移量
        mShadowDy = attr.getDimension(getStyleableId("shadowDy"), 0);
        //阴影颜色
        mShadowColor = attr.getColor(getStyleableId("shadowColor"), mContext.getResources().getColor(R.color.black));
        //背景填充色
        mBackGroundColor = attr.getColor(getStyleableId("backgroundColor"), Color.TRANSPARENT);
        setShadowColor(mShadowColor);
    }

    @Override
    public void setShadowDx(float dx) {
        if (Math.abs(dx) > mShadowSize) {
            if (dx > 0) {
                this.mShadowDx = mShadowSize;
            } else {
                this.mShadowDx = -mShadowSize;
            }
        } else {
            this.mShadowDx = dx;
        }
        updateUI();
    }

    @Override
    public float getShadowDx() {
        if (!isShadowShow()) {
            return 0;
        }
        return mShadowDx;
    }

    @Override
    public void setShadowDy(float dy) {
        if (Math.abs(dy) > mShadowSize) {
            if (dy > 0) {
                this.mShadowDy = mShadowSize;
            } else {
                this.mShadowDy = -mShadowSize;
            }
        } else {
            this.mShadowDy = dy;
        }
        updateUI();
    }

    @Override
    public float getShadowDy() {
        if (!isShadowShow()) {
            return 0;
        }
        return mShadowDy;
    }


    @Override
    public float getShadowSize() {
        if (!isShadowShow()) {
            return 0;
        }
        return mShadowSize;
    }

    @Override
    public void setShadowSize(float size) {
        this.mShadowSize = size;
        updateUI();
    }

    @Override
    public void setShadowColor(int color) {
        this.mShadowColor = color;
        isAddAlpha(color);
        updateUI();
    }

    @Override
    public int getShadowColor() {
        return mShadowColor;
    }

    @Override
    public void setBackgroundColor(int color) {
        mBackGroundColor = color;
    }

    @Override
    public int getBackgroundColor() {
        return mBackGroundColor;
    }

    @Override
    public void setShadowShow(boolean show) {
        isShowShadow = show;
        setShadowColor(getShadowColor());
    }

    @Override
    public boolean isShadowShow() {
        return isShowShadow;
    }

    /***
     * 添加默认透明度  默认#bb
     * @param color 阴影样色
     */
    private void isAddAlpha(int color) {
        //获取单签颜色值的透明度，如果没有设置透明度，默认加上#bb
        if (Color.alpha(color) == 255) {
            String red = Integer.toHexString(Color.red(color));
            String green = Integer.toHexString(Color.green(color));
            String blue = Integer.toHexString(Color.blue(color));
            if (red.length() == 1) {
                red = "0" + red;
            }
            if (green.length() == 1) {
                green = "0" + green;
            }
            if (blue.length() == 1) {
                blue = "0" + blue;
            }
            String endColor = "#bb" + red + green + blue;
            if (!endColor.startsWith("#")) {
                endColor = "#" + endColor;
            }
            mShadowColor = Color.parseColor(endColor);
        }
    }
}
