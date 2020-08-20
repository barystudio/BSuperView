package com.bary.ui.view.builder;

import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.bary.ui.R;
import com.bary.ui.common.interf.IShadowInterface;
import com.bary.ui.view.interf.ISuperViewInterface;

import java.util.HashMap;
import java.util.Map;

/**
 * author Bary
 * date on 2020/1/21.
 */
public class ShadowViewBuilder extends BaseViewBuilder implements IShadowInterface {
    public static final int LEFT = 1;
    public static final int RIGHT = 2;
    public static final int TOP = 4;
    public static final int BOTTOM = 8;
    private Map<Integer,Boolean> hiddenEdges = new HashMap<>();

    private int mShadowColor;
    private int mShadowHideEdges;
    private float mShadowSize,mShadowXSize,mShadowYSize,mShadowAlpha;
    private float mShadowDx, mShadowDy;
    private boolean isShowShadow = false;
    private Drawable mBackGroundDrawable;
    public ShadowViewBuilder(View view, ISuperViewInterface parentInterface) {
        super(view, parentInterface);
    }

    /**
     * 初始化阴影属性
     * @param attr 属性集合
     */
    @Override
    public void initAttributes(TypedArray attr) {
        if (attr == null) {
            return;
        }
        //隐藏某些边阴影
        mShadowHideEdges = attr.getInt(getStyleableId("bsv_shadowHideEdges"), -1);
        //默认扩散区域宽度
        mShadowSize = attr.getDimension(getStyleableId("bsv_shadowSize"), 0);
        //单独设置横向
        mShadowXSize = attr.getDimension(getStyleableId("bsv_shadowXSize"), 0);
        //单独设置纵向
        mShadowYSize = attr.getDimension(getStyleableId("bsv_shadowYSize"), 0);
        //x轴偏移量
        mShadowDx = attr.getDimension(getStyleableId("bsv_shadowDx"), 0);
        //y轴偏移量
        mShadowDy = attr.getDimension(getStyleableId("bsv_shadowDy"), 0);
        //阴影颜色
        mShadowColor = attr.getColor(getStyleableId("bsv_shadowColor"), Color.BLACK);
        //阴影透明度
        mShadowAlpha = attr.getFloat(getStyleableId("bsv_shadowAlpha"), 1);
        //背景图
        mBackGroundDrawable = attr.getDrawable(getStyleableId("android:background"));

        if(mShadowSize>0){
            mShadowXSize = mShadowSize;
            mShadowYSize = mShadowSize;
        }
        initShadowHideEdges();
        setShadowColor(mShadowColor);
        if(mShadowXSize==0&&mShadowYSize==0){
            isShowShadow = false;
        }else{
            isShowShadow = true;
        }
    }

    @Override
    public void setShadowDx(float dx) {
        if (Math.abs(dx) > mShadowXSize) {
            if (dx > 0) {
                this.mShadowDx = mShadowXSize;
            } else {
                this.mShadowDx = -mShadowXSize;
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
        if (Math.abs(dy) > mShadowYSize) {
            if (dy > 0) {
                this.mShadowDy = mShadowYSize;
            } else {
                this.mShadowDy = -mShadowYSize;
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
    public float getShadowXSize() {
        if (!isShadowShow()) {
            return 0;
        }
        return mShadowXSize;
    }
    @Override
    public void setShadowXSize(float size) {
        this.mShadowXSize = size;
        if(mShadowXSize==0&&mShadowYSize==0){
            isShowShadow = false;
        }else{
            isShowShadow = true;
        }
        updateUI();
    }

    @Override
    public float getShadowYSize() {
        if (!isShadowShow()) {
            return 0;
        }
        return mShadowYSize;
    }
    @Override
    public void setShadowYSize(float size) {
        this.mShadowYSize = size;
        if(mShadowXSize==0&&mShadowYSize==0){
            isShowShadow = false;
        }else{
            isShowShadow = true;
        }
        updateUI();
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
        mShadowXSize = mShadowSize;
        mShadowYSize = mShadowSize;

        if(mShadowXSize==0&&mShadowYSize==0){
            isShowShadow = false;
        }else{
            isShowShadow = true;
        }
        updateUI();
    }

    @Override
    public void setShadowColor(int color) {
        this.mShadowColor = color;
        updateUI();
    }

    @Override
    public int getShadowColor() {
        return mShadowColor;
    }

    @Override
    public void setShadowAlpha(float alpha) {
        this.mShadowAlpha = alpha;
        updateUI();
    }

    @Override
    public float getShadowAlpha() {
        return mShadowAlpha>=1?0.99f:mShadowAlpha;
    }

    @Override
    public void showShadowEdges(int... edges) {
        for (int i = 0; i <edges.length ; i++) {
            hiddenEdges.put(edges[i],false);
        }
        updateUI();
    }
    @Override
    public void hideShadowEdges(int... edges) {
        for (int i = 0; i <edges.length ; i++) {
            hiddenEdges.put(edges[i],true);
        }
        updateUI();
    }
    @Override
    public boolean isHiddenShadowEdges(int edge) {
        return hiddenEdges.get(edge);
    }

    @Override
    public boolean isShadowShow() {
        return isShowShadow;
    }

    public Map<Integer, Boolean> getHiddenEdges() {
        return hiddenEdges;
    }


    private void initShadowHideEdges() {
        hiddenEdges = new HashMap<>();
        hiddenEdges.put(LEFT,false);
        hiddenEdges.put(RIGHT,false);
        hiddenEdges.put(TOP,false);
        hiddenEdges.put(BOTTOM,false);
       switch (mShadowHideEdges){
           case 1:
               hiddenEdges.put(LEFT,true);
               break;
           case 2:
               hiddenEdges.put(RIGHT,true);
               break;
           case 3:
               hiddenEdges.put(LEFT,true);
               hiddenEdges.put(RIGHT,true);
               break;
           case 4:
               hiddenEdges.put(TOP,true);
               break;
           case 5:
               hiddenEdges.put(LEFT,true);
               hiddenEdges.put(TOP,true);
               break;
           case 6:
               hiddenEdges.put(RIGHT,true);
               hiddenEdges.put(TOP,true);
               break;
           case 7:
               hiddenEdges.put(LEFT,true);
               hiddenEdges.put(RIGHT,true);
               hiddenEdges.put(TOP,true);
               break;
           case 8:
               hiddenEdges.put(BOTTOM,true);
               break;
           case 9:
               hiddenEdges.put(LEFT,true);
               hiddenEdges.put(BOTTOM,true);
               break;
           case 10:
               hiddenEdges.put(RIGHT,true);
               hiddenEdges.put(BOTTOM,true);
               break;
           case 11:
               hiddenEdges.put(LEFT,true);
               hiddenEdges.put(RIGHT,true);
               hiddenEdges.put(BOTTOM,true);
               break;
           case 12:
               hiddenEdges.put(TOP,true);
               hiddenEdges.put(BOTTOM,true);
               break;
           case 13:
               hiddenEdges.put(LEFT,true);
               hiddenEdges.put(TOP,true);
               hiddenEdges.put(BOTTOM,true);
               break;
           case 14:
               hiddenEdges.put(TOP,true);
               hiddenEdges.put(RIGHT,true);
               hiddenEdges.put(BOTTOM,true);
               break;
           case 15:
               hiddenEdges.put(LEFT,true);
               hiddenEdges.put(TOP,true);
               hiddenEdges.put(RIGHT,true);
               hiddenEdges.put(BOTTOM,true);
               break;
       }
    }
    public void setBackground(Drawable drawable) {
        mBackGroundDrawable = drawable;
    }

    public Drawable getBackground() {
        return mBackGroundDrawable;
    }
}
