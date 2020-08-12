package com.bary.ui.view.bean;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.bary.ui.view.utils.UnitUtils;

import java.util.List;

public class BaseIconBean {

    public TextView mTextView;
    public int mDrawablePadding;

    public BaseIconBean(TextView view) {
        mTextView = view;

    }

    public void setDrawablePadding(int padding) {
        mDrawablePadding = padding;
    }

    public Drawable mergeBitmap(List<IconBean> icons, String type) {

        Bitmap bmp = null;
        if (icons.size() > 0) {
            float canvasHeight = 0;
            float canvasWidth = 0;
            for (int i = 0; i < icons.size(); i++) {
                if (icons.get(i).getHeight() > canvasHeight) {
                    canvasHeight = icons.get(i).getHeight();
                }
                canvasWidth += icons.get(i).getWidth() + mDrawablePadding;
            }
            bmp = Bitmap.createBitmap((int) canvasWidth, (int) canvasHeight, Bitmap.Config.ARGB_4444);
            Canvas canvas = new Canvas(bmp);
            int margeLeft = "LEFT".equals(type) ? 0 : mDrawablePadding;
            int margeTop = 0;
            for (int i = 0; i < icons.size(); i++) {
                margeTop = (int) ((canvasHeight - icons.get(i).getHeight()) / 2f);
                Bitmap bitmap = icons.get(i).getBitmap();
                Matrix matrix = new Matrix();
                matrix.postScale(icons.get(i).getWidth() / bitmap.getWidth(), icons.get(i).getHeight() / bitmap.getHeight());
                matrix.postTranslate(margeLeft,margeTop);
                canvas.drawBitmap(bitmap, matrix, null);
                icons.get(i).setRegion(new Rect(margeLeft, margeTop, margeLeft + (int) icons.get(i).getWidth(), margeTop + (int) icons.get(i).getHeight()));
                margeLeft += icons.get(i).getWidth() + mDrawablePadding;
            }
        }
        if (bmp == null) {
            return null;
        } else {
            Drawable drawable = new BitmapDrawable(bmp);
            drawable.setBounds(0, 0, bmp.getWidth(), bmp.getHeight());
            return drawable;
        }

    }

    public Bitmap resizeImage(Bitmap bitmap, float width, float height) {
        int bmpWidth = bitmap.getWidth();
        int bmpHeight = bitmap.getHeight();
        float scaleWidth = width / bmpWidth;
        float scaleHeight = height / bmpHeight;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(bitmap, 0, 0, bmpWidth, bmpHeight, matrix, true);
    }


}