package com.bary.ui.view.interf;

import com.bary.ui.view.eum.GradientOrientation;
import com.bary.ui.view.eum.GradientType;

import java.util.List;

/**
 * 渐变方法定义
 * author Bary
 * create at 2020/8/4 10:05
 */
public interface IGradientInterface {

    List<String> getTextGradientColor();
    void setTextGradientColor(String... colors);
    void clearTextGradientColor();
    GradientOrientation getTextGradientOrientation();
    void setTextGradientOrientation(GradientOrientation orientation);
    GradientType getTextGradientType();
    void setTextGradientType(GradientType type);
    List<String> getBackgroundGradientColor();
    void setBackgroundGradientColor(String... colors);
    void clearBackgroundGradientColor();
    GradientOrientation getBackgroundGradientOrientation();
    void setBackgroundGradientOrientation(GradientOrientation orientation);
    GradientType getBackgroundGradientType();
    void setBackgroundGradientType(GradientType type);

}
