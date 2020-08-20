package com.bary.ui.layout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.core.content.ContextCompat;

import com.bary.ui.R;
import com.bary.ui.common.bean.Padding;
import com.bary.ui.common.interf.IBorderInterface;
import com.bary.ui.common.interf.IRoundInterface;
import com.bary.ui.common.interf.IShadowInterface;
import com.bary.ui.layout.builder.BorderLayoutBuilder;
import com.bary.ui.layout.builder.RoundLayoutBuilder;
import com.bary.ui.layout.builder.ShadowLayoutBuilder;
import com.bary.ui.layout.interf.ISuperLayoutInterface;

public class BFrameLayout extends FrameLayout implements IRoundInterface, IShadowInterface, IBorderInterface,ISuperLayoutInterface {

    private RoundLayoutBuilder mRoundBuilder;
    private ShadowLayoutBuilder mShadowBuilder;
    private BorderLayoutBuilder mBorderBuilder;
    private int mPaddingLeft, mPaddingTop, mPaddingRight, mPaddingBottom;

    public BFrameLayout(Context context) {
        this(context, null);
    }

    public BFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mPaddingLeft = getPaddingLeft();
        mPaddingRight = getPaddingRight();
        mPaddingTop = getPaddingTop();
        mPaddingBottom = getPaddingBottom();
        TypedArray attr = context.obtainStyledAttributes(attrs, R.styleable.BLinearLayout);
        try {
            // 初始化圆角样式
            mRoundBuilder = new RoundLayoutBuilder(this, this);
            // 初始化阴影样式
            mShadowBuilder = new ShadowLayoutBuilder(this, this);
            // 初始描边样式
            mBorderBuilder = new BorderLayoutBuilder(this, this);

            mShadowBuilder.initAttributes(attr);
            mRoundBuilder.initAttributes(attr);
            mBorderBuilder.initAttributes(attr);

        } finally {
            attr.recycle();
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        canvas.saveLayer(new RectF(0, 0, canvas.getWidth(), canvas.getHeight()), mRoundBuilder.getContextPaint(), Canvas.ALL_SAVE_FLAG);
        super.dispatchDraw(canvas);
        mRoundBuilder.drawRoundRadius(canvas);
        canvas.restore();

    }


    @Override
    public float getRoundRadius() {
        return mRoundBuilder.getRoundRadius();
    }

    @Override
    public void setRoundRadius(float radius) {
        mRoundBuilder.setRoundRadius(radius);
    }

    @Override
    public float getTopLeftRoundRadius() {
        return mRoundBuilder.getTopLeftRoundRadius();
    }

    @Override
    public void setTopLeftRoundRadius(float radius) {
        mRoundBuilder.setTopLeftRoundRadius(radius);
    }

    @Override
    public float getTopRightRoundRadius() {
        return mRoundBuilder.getTopRightRoundRadius();
    }

    @Override
    public void setTopRightRoundRadius(float radius) {
        mRoundBuilder.setTopRightRoundRadius(radius);
    }

    @Override
    public float getBottomLeftRoundRadius() {
        return mRoundBuilder.getBottomLeftRoundRadius();
    }

    @Override
    public void setBottomLeftRoundRadius(float radius) {
        mRoundBuilder.setBottomLeftRoundRadius(radius);
    }

    @Override
    public float getBottomRightRoundRadius() {
        return mRoundBuilder.getBottomRightRoundRadius();
    }

    @Override
    public void setBottomRightRoundRadius(float radius) {
        mRoundBuilder.setBottomRightRoundRadius(radius);
    }

    @Override
    public void setShadowDx(float dx) {
        mShadowBuilder.setShadowDx(dx);
    }

    @Override
    public float getShadowDx() {
        return mShadowBuilder.getShadowDx();
    }

    @Override
    public void setShadowDy(float dy) {
        mShadowBuilder.setShadowDy(dy);
    }

    @Override
    public float getShadowDy() {
        return mShadowBuilder.getShadowDy();
    }

    @Override
    public float getShadowXSize() {
        return mShadowBuilder.getShadowXSize();
    }

    @Override
    public void setShadowYSize(float size) {
        mShadowBuilder.setShadowYSize(size);
    }

    @Override
    public float getShadowYSize() {
        return mShadowBuilder.getShadowYSize();
    }

    @Override
    public float getShadowSize() {
        return mShadowBuilder.getShadowSize();
    }

    @Override
    public void setShadowSize(float size) {
        mShadowBuilder.setShadowSize(size);
    }

    @Override
    public void setShadowXSize(float size) {
        mShadowBuilder.setShadowXSize(size);
    }

    @Override
    public void setShadowColor(int color) {
        mShadowBuilder.setShadowColor(color);
    }

    @Override
    public int getShadowColor() {
        return mShadowBuilder.getShadowColor();
    }

    @Override
    public void setShadowAlpha(float alpha) {
        mShadowBuilder.setShadowAlpha(alpha);
    }

    @Override
    public float getShadowAlpha() {
        return mShadowBuilder.getShadowAlpha();
    }

    @Override
    public void showShadowEdges(int... edges) {
        mShadowBuilder.showShadowEdges(edges);
    }

    @Override
    public void hideShadowEdges(int... edges) {
        mShadowBuilder.hideShadowEdges(edges);
    }

    @Override
    public boolean isHiddenShadowEdges(int edges) {
        return mShadowBuilder.isHiddenShadowEdges(edges);
    }


    @Override
    public boolean isShadowShow() {
        return mShadowBuilder.isShadowShow();
    }

    @Override
    public float getBorderSize() {
        return mBorderBuilder.getBorderSize();
    }

    @Override
    public void setBorderSize(float size) {
        mBorderBuilder.setBorderSize(size);
    }

    @Override
    public void setBorderColor(int color) {
        mBorderBuilder.setBorderColor(color);
    }

    @Override
    public int getBorderColor() {
        return mBorderBuilder.getBorderColor();
    }

    @Override
    public boolean isBorderShow() {
        return mBorderBuilder.isBorderShow();
    }

    @Override
    public void hideBorderEdges(int... edges) {
        mBorderBuilder.hideBorderEdges(edges);
    }

    @Override
    public void showBorderEdges(int... edges) {
        mBorderBuilder.showBorderEdges(edges);
    }

    @Override
    public boolean isHiddenBorderEdges(int edges) {
        return mBorderBuilder.isHiddenBorderEdges(edges);
    }

    @Override
    public void setBackgroundResource(int resid) {
        mShadowBuilder.setBackground(ContextCompat.getDrawable(getContext(),resid));
    }

    @Override
    public void setBackground(Drawable background) {
        if(mShadowBuilder==null)return;
        mShadowBuilder.setBackground(background);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mShadowBuilder.updateUI();
    }

    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        mPaddingLeft = left;
        mPaddingRight = right;
        mPaddingTop = top;
        mPaddingBottom = bottom;
        mShadowBuilder.updateUI();
    }

    @Override
    public RoundLayoutBuilder getRoundStyle() {
        return mRoundBuilder;
    }

    @Override
    public ShadowLayoutBuilder getShadowStyle() {
        return mShadowBuilder;
    }

    @Override
    public BorderLayoutBuilder getBorderStyle() {
        return mBorderBuilder;
    }

    @Override
    public void updatePadding(int left, int top, int right, int bottom) {
        super.setPadding(left, top, right, bottom);
    }
    @Override
    public void updateBackground(Drawable drawable) {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN) {
            super.setBackgroundDrawable(drawable);
        } else {
            super.setBackground(drawable);
        }
    }
    @Override
    public Padding getDefPadding() {
        return new Padding(mPaddingLeft, mPaddingTop, mPaddingRight, mPaddingBottom);
    }

}