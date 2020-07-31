package com.bary.ui.view.edittext;

import androidx.annotation.ColorInt;

/**
 * author Bary
 * date on 2020/1/21.
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
    void setShadowColor(int color);
    int getShadowColor();
    void setShadowShow(boolean show);
    boolean isShadowShow();

}
