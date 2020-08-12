package com.bary.ui.view.bean;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.view.View;
import android.widget.TextView;

public class IconBean {
    public TextView view;
    public Bitmap bitmap;
    public float width;
    public float height;
    public boolean visible;
    public Rect region;

    public IconBean(TextView view) {
        this.view = view;
    }

    public Rect getRegion() {
        return region;
    }

    public void setRegion(Rect region) {
        this.region = region;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public void setPaint(float x,float y) {

       if(region.left<=x&&region.right>=x&&region.top<=y&&region.bottom>=y){
           if(mOnClickListener!=null){
               mOnClickListener.onClick(view);
           }
       }
    }

    private View.OnClickListener mOnClickListener;

    public void setOnClickListener(View.OnClickListener clickListener){
        mOnClickListener = clickListener;
    }
}