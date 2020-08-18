package com.bary.ui.common.interf;

/**
 * 描边方法定义
 * author Bary
 * create at 2020/8/4 10:06
 */
public interface IBorderInterface {
    float getBorderSize();
    void setBorderSize(float limit);
    void setBorderColor(int color);
    int getBorderColor();
    boolean isBorderShow();
    void hideBorderEdges(int... edges);
    void showBorderEdges(int... edges);
    boolean isHiddenBorderEdges(int edges);
}
