package com.bary.ui.view.builder;

import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.RectF;
import android.view.View;

import com.bary.ui.view.interf.IBorderInterface;
import com.bary.ui.view.interf.ISuperInterface;

import java.util.HashMap;
import java.util.Map;


/**
 * author Bary
 * date on 2020/1/21.
 */
public class BorderBuilder extends BaseBuilder implements IBorderInterface {
    public static final int LEFT = 1;
    public static final int RIGHT = 2;
    public static final int TOP = 4;
    public static final int BOTTOM = 8;
    private Map<Integer,Boolean> hiddenEdges = new HashMap<>();

    private int mBorderColor;
    private float mBorderSize;
    private boolean isShowBorder = false;
    private int mBorderHideEdges;

    public BorderBuilder(View view, ISuperInterface parentInterface) {
        super(view, parentInterface);
    }

    /**
     * 初始化描边属性
     *
     * @param attr 属性集合
     */
    @Override
    public void initAttributes(TypedArray attr) {
        if (attr == null) {
            return;
        }
        //默认扩散区域宽度
        mBorderSize = attr.getDimension(getStyleableId("borderSize"), 0);
        mBorderColor = attr.getColor(getStyleableId("borderColor"), Color.BLACK);
        //隐藏某些边描边
        mBorderHideEdges = attr.getInt(getStyleableId("borderHideEdges"), -1);

        initBorderHideEdges();
    }


    @Override
    public float getBorderSize() {
        return mBorderSize;
    }

    @Override
    public void setBorderSize(float size) {
        mBorderSize = size;
        if(mBorderSize<=0){
            isShowBorder = false;
        }else{
            isShowBorder = true;
        }
        updateUI();
    }

    @Override
    public void setBorderColor(int color) {
        mBorderColor = color;
        updateUI();
    }

    @Override
    public int getBorderColor() {
        if (!isBorderShow()) {
            return Color.TRANSPARENT;
        }
        return mBorderColor;
    }

    @Override
    public boolean isBorderShow() {
        return isShowBorder;
    }

    @Override
    public void hideBorderEdges(int... edges) {
        for (int i = 0; i <edges.length ; i++) {
            hiddenEdges.put(edges[i],true);
        }
        updateUI();
    }

    @Override
    public void showBorderEdges(int... edges) {
        for (int i = 0; i <edges.length ; i++) {
            hiddenEdges.put(edges[i],false);
        }
        updateUI();
    }

    @Override
    public boolean isHiddenBorderEdges(int edge) {
        return hiddenEdges.get(edge);
    }

    public Map<Integer, Boolean> getHiddenEdges() {
        return hiddenEdges;
    }
    private void initBorderHideEdges() {
        hiddenEdges = new HashMap<>();
        hiddenEdges.put(LEFT,false);
        hiddenEdges.put(RIGHT,false);
        hiddenEdges.put(TOP,false);
        hiddenEdges.put(BOTTOM,false);
        switch (mBorderHideEdges){
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
