package com.bary.ui.view.interf;


import com.bary.ui.view.builder.BorderBuilder;
import com.bary.ui.view.builder.GradientBuilder;
import com.bary.ui.view.builder.RoundBuilder;
import com.bary.ui.view.builder.ShadowBuilder;

/**
 * author Bary
 * date on 2020/1/21.
 */
public interface ISuperInterface {
    RoundBuilder getBasicStyle();
    ShadowBuilder getShadowStyle();
    BorderBuilder getBorderStyle();
    GradientBuilder getGradientStyle();
    void updatePadding(int left, int top, int right, int bottom);
    Padding getDefPadding();

    class Padding{
        public Padding(int LEFT, int TOP, int RIGHT, int BOTTOM) {
            this.LEFT = LEFT;
            this.TOP = TOP;
            this.RIGHT = RIGHT;
            this.BOTTOM = BOTTOM;
        }

        public int LEFT;
        public int TOP;
        public int RIGHT;
        public int BOTTOM;
    }
}
