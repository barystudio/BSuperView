package com.bary.ui.view.edittext;

/**
 * author Bary
 * date on 2020/1/21.
 */
public interface IBorderInterface {
    float getBorderSize();
    void setBorderSize(float limit);
    void setBorderColor(int color);
    int getBorderColor();
    void setBorderShow(boolean show);
    boolean isBorderShow();
}
