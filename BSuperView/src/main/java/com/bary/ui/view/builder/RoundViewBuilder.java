package com.bary.ui.view.builder;

import android.content.res.TypedArray;
import android.view.View;

import com.bary.ui.common.interf.IRoundInterface;
import com.bary.ui.view.interf.ISuperViewInterface;


/**
 * author Bary
 * date on 2020/1/21.
 */
public class RoundViewBuilder extends BaseViewBuilder implements IRoundInterface {

    private float mRoundradius,mTopLeftRadius,mTopRightRadius,mBottomLeftRadius,mBottomRightRadius;

    public RoundViewBuilder(View view, ISuperViewInterface parentInterface) {
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
        if(mRoundradius>0){
            mTopLeftRadius = mTopRightRadius = mBottomLeftRadius = mBottomRightRadius = mRoundradius;
        }
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
