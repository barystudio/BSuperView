package com.bary.ui.layout.interf;


import com.bary.ui.common.bean.Padding;
import com.bary.ui.layout.builder.BorderLayoutBuilder;
import com.bary.ui.layout.builder.RoundLayoutBuilder;
import com.bary.ui.layout.builder.ShadowLayoutBuilder;

/**
 * author Bary
 * date on 2020/1/21.
 */
public interface ISuperLayoutInterface {
    RoundLayoutBuilder getRoundStyle();
    ShadowLayoutBuilder getShadowStyle();
    BorderLayoutBuilder getBorderStyle();
    void updatePadding(int left, int top, int right, int bottom);
    Padding getDefPadding();

}
