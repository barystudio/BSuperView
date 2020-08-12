package com.bary.ui.view.bean;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class LeftIconBeans extends BaseIconBean {
    private Map<Integer, IconBean> mIconBeans;
    private List<IconBean> mVisibleIcons;

    public LeftIconBeans(TextView view) {
        super(view);
        mIconBeans = new TreeMap<>();
        mVisibleIcons = new ArrayList<>();
    }

    public void addIcon(int id, Drawable drawable, float width, float height, View.OnClickListener listener) {
        if(drawable==null)return;
        BitmapDrawable bd = (BitmapDrawable) drawable;
        addIcon(id, bd.getBitmap(), width, height, listener);
    }

    public void addIcon(int id, Bitmap bitmap, float width, float height, View.OnClickListener listener) {
        if(bitmap==null)return;
        IconBean bean = new IconBean(mTextView);
        bean.setBitmap(bitmap);
        bean.setWidth(width);
        bean.setHeight(height);
        bean.setVisible(true);
        bean.setOnClickListener(listener);
        mIconBeans.put(id, bean);
    }

    public void addIcon(int id, int res, float width, float height, View.OnClickListener listener) {
        if(res==0)return;
        addIcon(id, BitmapFactory.decodeResource(mTextView.getResources(), res), width, height, listener);
    }

    public void updateRes(int id, int res) {
        mIconBeans.get(id).setBitmap(BitmapFactory.decodeResource(mTextView.getResources(), res));
    }

    public void hideIcon(int id) {
        mIconBeans.get(id).setVisible(false);
    }

    public void showIcon(int id) {
        mIconBeans.get(id).setVisible(true);
    }
    public void setOnLeftIconClickListener(View.OnClickListener listener) {
        if(mIconBeans.containsKey(0)){
            mIconBeans.get(0).setOnClickListener(listener);
        }
    }

    public Drawable getDrawable() {
        mVisibleIcons.clear();
        Set<Integer> keySet = mIconBeans.keySet();
        Iterator<Integer> iter = keySet.iterator();
        while (iter.hasNext()) {
            IconBean bean = mIconBeans.get(iter.next());
            if (bean.isVisible()) {
                mVisibleIcons.add(bean);
            }
        }
        return mergeBitmap(mVisibleIcons,"LEFT");
    }

    public void onClick(float x, float y) {
        if(mTextView.getCompoundDrawables()[0]==null)return;
        float right = mTextView.getCompoundPaddingLeft();
        float left = right - mTextView.getCompoundDrawables()[0].getBounds().width();
        float top = (mTextView.getHeight() - mTextView.getCompoundDrawables()[0].getBounds().height()) / 2f;
        float bottom = top + mTextView.getCompoundDrawables()[0].getBounds().height();
        if (x < left || x > right) return;
        if (y < top || y > bottom) return;

        List<IconBean> list = new ArrayList<>(mVisibleIcons);
        for (IconBean bean : list) {
            bean.setPaint(x - left, y - top);
        }

    }

    public int aLLIconSize() {
        return mIconBeans.size();
    }

    public int visibleIconSize() {
        return mVisibleIcons.size();
    }


}