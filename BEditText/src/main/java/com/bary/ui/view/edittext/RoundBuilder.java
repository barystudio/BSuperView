package com.bary.ui.view.edittext;

import android.content.res.TypedArray;
import android.graphics.Paint;
import android.view.View;


/**
 * author Bary
 * date on 2020/1/21.
 */
public class RoundBuilder extends BaseBuilder implements IRoundInterface {

    private Paint mPaint;
    private float mRoundradius,mTopLeftRadius,mTopRightRadius,mBottomLeftRadius,mBottomRightRadius;

    public RoundBuilder(View view, ISuperInterface parentInterface) {
        super(view,parentInterface);
        //矩形画笔
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
    }

    public void initBasicAttributes(TypedArray attr) {
        if (attr == null) {
            return;
        }
        mRoundradius = (int) attr.getDimension(getStyleableId("roundRadius"), 0);
        mTopLeftRadius = (int) attr.getDimension(getStyleableId("topLeftRadius"), 0);
        mTopRightRadius = (int) attr.getDimension(getStyleableId("topRightRadius"), 0);
        mBottomLeftRadius = (int) attr.getDimension(getStyleableId("bottomLeftRadius"), 0);
        mBottomRightRadius = (int) attr.getDimension(getStyleableId("bottomRightRadius"), 0);
        if(mRoundradius>0){
            mTopLeftRadius = mTopRightRadius = mBottomLeftRadius = mBottomRightRadius = mRoundradius;
        }
        mView.setClickable(true);
    }

    @Override
    public float getRoundRadius() {
        return mRoundradius;
    }

    @Override
    public void setRoundRadius(float radius) {
        mRoundradius = radius;
        mTopLeftRadius = mTopRightRadius = mBottomLeftRadius = mBottomRightRadius =  mRoundradius;
        mSuper.getShadowStyle().updateUI();
    }

    @Override
    public float getTopLeftRoundRadius() {
        return mTopLeftRadius;
    }

    @Override
    public void setTopLeftRoundRadius(float radius) {
        mTopLeftRadius = radius;
        mSuper.getShadowStyle().updateUI();
    }

    @Override
    public float getTopRightRoundRadius() {
        return mTopRightRadius;
    }

    @Override
    public void setTopRightRoundRadius(float radius) {
        mTopRightRadius = radius;
        mSuper.getShadowStyle().updateUI();
    }

    @Override
    public float getBottomLeftRoundRadius() {
        return mBottomLeftRadius;
    }

    @Override
    public void setBottomLeftRoundRadius(float radius) {
        mBottomLeftRadius = radius;
        mSuper.getShadowStyle().updateUI();
    }

    @Override
    public float getBottomRightRoundRadius() {
        return mBottomRightRadius;
    }

    @Override
    public void setBottomRightRoundRadius(float radius) {
        mBottomRightRadius = radius;
        mSuper.getShadowStyle().updateUI();
    }


}
