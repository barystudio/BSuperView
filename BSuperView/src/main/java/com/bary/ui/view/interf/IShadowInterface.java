package com.bary.ui.view.interf;

import androidx.annotation.ColorInt;

/**
 * 阴影方法定义
 * author Bary
 * create at 2020/8/4 10:06
 */
public interface IShadowInterface {
    void setBackgroundColor(@ColorInt int color);
    int getBackgroundColor();
    void setShadowDx(float dx);
    float getShadowDx();
    void setShadowDy(float dy);
    float getShadowDy();
    float getShadowSize();
    void setShadowSize(float size);
    void setShadowXSize(float size);
    float getShadowXSize();
    void setShadowYSize(float size);
    float getShadowYSize();
    void setShadowColor(int color);
    int getShadowColor();
    void setShadowAlpha(float alpha);
    float getShadowAlpha();
    void hideShadowEdges(int... edges);
    void showShadowEdges(int... edges);
    boolean isHiddenShadowEdges(int edges);
    void setShadowShow(boolean show);
    boolean isShadowShow();

}
