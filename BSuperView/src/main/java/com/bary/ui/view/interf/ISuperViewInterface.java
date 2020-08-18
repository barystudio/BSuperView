package com.bary.ui.view.interf;


import com.bary.ui.common.bean.Padding;
import com.bary.ui.view.builder.BorderViewBuilder;
import com.bary.ui.view.builder.GradientViewBuilder;
import com.bary.ui.view.builder.RoundViewBuilder;
import com.bary.ui.view.builder.ShadowViewBuilder;

/**
 * author Bary
 * date on 2020/1/21.
 */
public interface ISuperViewInterface {
    RoundViewBuilder getRoundStyle();
    ShadowViewBuilder getShadowStyle();
    BorderViewBuilder getBorderStyle();
    GradientViewBuilder getGradientStyle();
    void updatePadding(int left, int top, int right, int bottom);
    Padding getDefPadding();
}
