package com.bary.ui.view.edittext;


/**
 * author Bary
 * date on 2020/1/21.
 */
public interface ISuperInterface {
    RoundBuilder getBasicStyle();
    ShadowBuilder getShadowStyle();
    BorderBuilder getBorderStyle();
    void updatePadding(int left, int top, int right, int bottom);
    Padding getDefPadding();

    class Padding{
        public Padding(int LEFT, int TOP, int RIGHT, int BOTTOM) {
            this.LEFT = LEFT;
            this.TOP = TOP;
            this.RIGHT = RIGHT;
            this.BOTTOM = BOTTOM;
        }

        int LEFT;
        int TOP;
        int RIGHT;
        int BOTTOM;
    }
}
