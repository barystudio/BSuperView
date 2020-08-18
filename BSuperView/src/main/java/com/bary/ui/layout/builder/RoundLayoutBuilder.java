package com.bary.ui.layout.builder;

import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.view.View;

import com.bary.ui.layout.interf.ISuperLayoutInterface;
import com.bary.ui.common.interf.IRoundInterface;

/**
 * author Bary
 * date on 2020/1/21.
 */
public class RoundLayoutBuilder extends BaseLayoutBuilder implements IRoundInterface {

    private float mRoundradius, mTopLeftRadius, mTopRightRadius, mBottomLeftRadius, mBottomRightRadius;
    private Paint roundPaint;
    private Paint contextPaint;

    public RoundLayoutBuilder(View view, ISuperLayoutInterface parentInterface) {
        super(view, parentInterface);
    }

    @Override
    public void initAttributes(TypedArray attr) {
        if (attr == null) {
            return;
        }
        mRoundradius = (int) attr.getDimension(getStyleableId("bsv_roundRadius"), 0);
        mTopLeftRadius = (int) attr.getDimension(getStyleableId("bsv_topLeftRadius"), 0);
        mTopRightRadius = (int) attr.getDimension(getStyleableId("bsv_topRightRadius"), 0);
        mBottomLeftRadius = (int) attr.getDimension(getStyleableId("bsv_bottomLeftRadius"), 0);
        mBottomRightRadius = (int) attr.getDimension(getStyleableId("bsv_bottomRightRadius"), 0);
        if (mRoundradius > 0) {
            mTopLeftRadius = mTopRightRadius = mBottomLeftRadius = mBottomRightRadius = mRoundradius;
        }
        roundPaint = new Paint();
        roundPaint.setColor(Color.WHITE);
        roundPaint.setAntiAlias(true);
        roundPaint.setStyle(Paint.Style.FILL);

        roundPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));

        contextPaint = new Paint();
        contextPaint.setXfermode(null);
    }

    @Override
    public float getRoundRadius() {
        return mRoundradius;
    }

    @Override
    public void setRoundRadius(float radius) {
        mRoundradius = radius;
        mTopLeftRadius = mTopRightRadius = mBottomLeftRadius = mBottomRightRadius = mRoundradius;
        mView.invalidate();
        mSuper.getShadowStyle().updateUI();
    }

    @Override
    public float getTopLeftRoundRadius() {
        return mTopLeftRadius;
    }

    @Override
    public void setTopLeftRoundRadius(float radius) {
        mTopLeftRadius = radius;
        mView.invalidate();
        mSuper.getShadowStyle().updateUI();
    }

    @Override
    public float getTopRightRoundRadius() {
        return mTopRightRadius;
    }

    @Override
    public void setTopRightRoundRadius(float radius) {
        mTopRightRadius = radius;
        mView.invalidate();
        mSuper.getShadowStyle().updateUI();
    }

    @Override
    public float getBottomLeftRoundRadius() {
        return mBottomLeftRadius;
    }

    @Override
    public void setBottomLeftRoundRadius(float radius) {
        mBottomLeftRadius = radius;
        mView.invalidate();
        mSuper.getShadowStyle().updateUI();
    }

    @Override
    public float getBottomRightRoundRadius() {
        return mBottomRightRadius;
    }

    @Override
    public void setBottomRightRoundRadius(float radius) {
        mBottomRightRadius = radius;
        mView.invalidate();
        mSuper.getShadowStyle().updateUI();
    }

    public Paint getContextPaint() {
        return contextPaint;
    }

    public void drawRoundRadius(Canvas canvas) {

        int left = mView.getPaddingLeft();
        int top = mView.getPaddingTop();
        int right = mView.getPaddingRight();
        int bottom = mView.getPaddingBottom();

        float conHeight = mView.getHeight() - top - bottom;
        float conWidth = mView.getWidth() - right - left;
        float standard = conWidth;
        if (conWidth > conHeight) {
            standard = conHeight;
        }
        float mTopLeftRadius = this.mTopLeftRadius-mSuper.getBorderStyle().getBorderSize();
        float mBottomLeftRadius = this.mBottomLeftRadius-mSuper.getBorderStyle().getBorderSize();
        float mTopRightRadius = this.mTopRightRadius-mSuper.getBorderStyle().getBorderSize();
        float mBottomRightRadius = this.mBottomRightRadius -mSuper.getBorderStyle().getBorderSize();

        if (mTopLeftRadius + mBottomLeftRadius > standard) {
            float ratio = standard / (mTopLeftRadius + mBottomLeftRadius);
            mTopLeftRadius = mTopLeftRadius * ratio;
            mBottomLeftRadius = mBottomLeftRadius * ratio;
        }
        if (mTopRightRadius + mBottomRightRadius > standard) {
            float ratio = standard / (mTopRightRadius + mBottomRightRadius);
            mTopRightRadius = mTopRightRadius * ratio;
            mBottomRightRadius = mBottomRightRadius * ratio;
        }


        if (mTopLeftRadius > 0) {
            Path path = new Path();
            path.moveTo(left, mTopLeftRadius + top);
            path.lineTo(left, top);
            path.lineTo(mTopLeftRadius + left, top);
            path.arcTo(new RectF(left, top, mTopLeftRadius * 2 + left, mTopLeftRadius * 2 + top), -90, -90);
            path.close();
            canvas.drawPath(path, roundPaint);
        }
        if (mTopRightRadius > 0) {
            int width = mView.getWidth() - right;
            Path path = new Path();
            path.moveTo(width - mTopRightRadius, top);
            path.lineTo(width, top);
            path.lineTo(width, mTopRightRadius + top);
            path.arcTo(new RectF(width - 2 * mTopRightRadius, top, width, mTopRightRadius * 2 + top), 0, -90);
            path.close();
            canvas.drawPath(path, roundPaint);
        }
        if (mBottomLeftRadius > 0) {
            int height = mView.getHeight() - bottom;
            Path path = new Path();
            path.moveTo(left, height - mBottomLeftRadius);
            path.lineTo(left, height);
            path.lineTo(mBottomLeftRadius + left, height);
            path.arcTo(new RectF(left, height - 2 * mBottomLeftRadius, mBottomLeftRadius * 2 + left, height), 90, 90);
            path.close();
            canvas.drawPath(path, roundPaint);
        }
        if (mBottomRightRadius > 0) {
            int height = mView.getHeight() - bottom;
            int width = mView.getWidth() - right;
            Path path = new Path();
            path.moveTo(width - mBottomRightRadius, height);
            path.lineTo(width, height);
            path.lineTo(width, height - mBottomRightRadius);
            path.arcTo(new RectF(width - 2 * mBottomRightRadius, height - 2 * mBottomRightRadius, width, height), 0, 90);
            path.close();
            canvas.drawPath(path, roundPaint);
        }
    }
}
