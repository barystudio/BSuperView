package com.bary.ui.view.builder;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.view.View;

import com.bary.ui.R;
import com.bary.ui.view.interf.IShadowInterface;
import com.bary.ui.view.interf.ISuperInterface;

import java.util.HashMap;
import java.util.Map;

/**
 * author Bary
 * date on 2020/1/21.
 */
public class ShadowBuilder extends BaseBuilder implements IShadowInterface {
    public static final int LEFT = 1;
    public static final int RIGHT = 2;
    public static final int TOP = 4;
    public static final int BOTTOM = 8;
    private Map<Integer,Boolean> hiddenEdges = new HashMap<>();

    private int mShadowColor;
    private int mBackGroundColor;
    private int mShadowHideEdges;
    private float mShadowSize,mShadowXSize,mShadowYSize,mShadowAlpha;
    private float mShadowDx, mShadowDy;
    private boolean isShowShadow = false;

    public ShadowBuilder(View view, ISuperInterface parentInterface) {
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
        //是否显示阴影
        isShowShadow = attr.getBoolean(getStyleableId("shadowShow"), false);
        //隐藏某些边阴影
        mShadowHideEdges = attr.getInt(getStyleableId("shadowHideEdges"), -1);
        //默认扩散区域宽度
        mShadowSize = attr.getDimension(getStyleableId("shadowSize"), 0);
        //单独设置横向
        mShadowXSize = attr.getDimension(getStyleableId("shadowXSize"), 0);
        //单独设置纵向
        mShadowYSize = attr.getDimension(getStyleableId("shadowYSize"), 0);
        //x轴偏移量
        mShadowDx = attr.getDimension(getStyleableId("shadowDx"), 0);
        //y轴偏移量
        mShadowDy = attr.getDimension(getStyleableId("shadowDy"), 0);
        //阴影颜色
        mShadowColor = attr.getColor(getStyleableId("shadowColor"), mView.getResources().getColor(R.color.black));
        //阴影透明度
        mShadowAlpha = attr.getFloat(getStyleableId("shadowAlpha"), 1);
        //背景填充色
        mBackGroundColor = attr.getColor(getStyleableId("backgroundColor"), Color.TRANSPARENT);

        if(mShadowSize>0){
            mShadowXSize = mShadowSize;
            mShadowYSize = mShadowSize;
        }
        initShadowHideEdges();
        setShadowColor(mShadowColor);
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
        return mShadowAlpha;
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
    public void setBackgroundColor(int color) {
        mBackGroundColor = color;
    }

    @Override
    public int getBackgroundColor() {
        return mBackGroundColor;
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

}
