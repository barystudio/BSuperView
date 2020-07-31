package com.bary.ui.view.edittext;

import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.RectF;
import android.view.View;


/**
 * author Bary
 * date on 2020/1/21.
 */
public class BorderBuilder extends BaseBuilder implements IBorderInterface {

    private int mBorderColor;
    private float mBorderSize;
    private boolean isShowBorder = false;
    //阴影布局子空间区域
    private RectF rectf = new RectF();

    public BorderBuilder(View view, ISuperInterface parentInterface) {
        super(view, parentInterface);
    }

    /**
     * 初始化描边属性
     *
     * @param attr 属性集合
     */
    public void initBorderAttributes(TypedArray attr) {
        if (attr == null) {
            return;
        }
        try {
            //默认是显示
            isShowBorder = attr.getBoolean(getStyleableId("showBorder"), false);
            //默认扩散区域宽度
            mBorderSize = attr.getDimension(getStyleableId("borderSize"), 0);
            mBorderColor = attr.getColor(getStyleableId("borderColor"), Color.TRANSPARENT);
        } finally {
            attr.recycle();
        }
    }


    @Override
    public float getBorderSize() {
        if (!isBorderShow()) {
            return 0;
        }
        return mBorderSize;
    }

    @Override
    public void setBorderSize(float size) {
        mBorderSize = size;
        updateUI();
    }

    @Override
    public void setBorderColor(int color) {
        mBorderColor = color;
        updateUI();
    }

    @Override
    public int getBorderColor() {
        if (!isBorderShow()) {
            return Color.TRANSPARENT;
        }
        return mBorderColor;
    }

    @Override
    public void setBorderShow(boolean show) {
        isShowBorder = show;
        updateUI();
    }

    @Override
    public boolean isBorderShow() {
        return isShowBorder;
    }

}
