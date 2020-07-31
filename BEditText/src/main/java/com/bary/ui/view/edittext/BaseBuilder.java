package com.bary.ui.view.edittext;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.view.View;

import java.lang.reflect.Field;

/**
 * author Bary
 * date on 2020/1/21.
 */
public class BaseBuilder {
    public View mView;
    public ISuperInterface mSuper;
    private Paint mShadowPaint;
    private Paint mBorderPaint;
    private Paint imagePaint;


    public BaseBuilder(View view, ISuperInterface iSuper) {
        mView = view;
        mSuper = iSuper;
        //阴影画笔
        mShadowPaint = new Paint();
        mShadowPaint.setAntiAlias(true);
        mShadowPaint.setStyle(Paint.Style.FILL);
        //描边画笔
        mBorderPaint = new Paint();
        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setStyle(Paint.Style.STROKE);

    }

    /**
     * 反射获取属性id值
     *
     * @param styleableFieldName 属性名
     * @return 返回id值
     */
    public int getStyleableId(String styleableFieldName) {
        String className = mView.getContext().getPackageName() + ".R";
        String type = "styleable";
        String name = mView.getClass().getSimpleName() + "_" + styleableFieldName;
        try {
            Class<?> cla = Class.forName(className);
            for (Class<?> childClass : cla.getClasses()) {
                String simpleName = childClass.getSimpleName();
                if (simpleName.equals(type)) {
                    for (Field field : childClass.getFields()) {
                        String fieldName = field.getName();
                        if (fieldName.equals(name)) {
                            return (int) field.get(null);
                        }
                    }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return 0;
    }


    /**
     * 计算并设置偏移数据
     */
    private void setPading() {
        float dx = mSuper.getShadowStyle().getShadowDx();
        float dy = mSuper.getShadowStyle().getShadowDy();
        int paddingLeft = (int) (mSuper.getShadowStyle().getShadowSize() + mSuper.getBorderStyle().getBorderSize() + mSuper.getDefPadding().LEFT - dx);
        int paddingRight = (int) (mSuper.getShadowStyle().getShadowSize() + mSuper.getBorderStyle().getBorderSize() + mSuper.getDefPadding().RIGHT + dx);
        int paddingTop = (int) (mSuper.getShadowStyle().getShadowSize() + mSuper.getBorderStyle().getBorderSize() + mSuper.getDefPadding().TOP - dy);
        int paddingBottom = (int) (mSuper.getShadowStyle().getShadowSize() + mSuper.getBorderStyle().getBorderSize() + mSuper.getDefPadding().BOTTOM + dy);
        mSuper.updatePadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
    }


    /**
     * 更新UI样式
     */
    public void updateUI() {
        if (mView.getWidth() == 0 || mView.getHeight() == 0) return;
        setPading();
        Bitmap bitmap = getShadowBitmap(mView.getWidth(), mView.getHeight(),
                mSuper.getBasicStyle().getTopLeftRoundRadius(),
                mSuper.getBasicStyle().getTopRightRoundRadius(),
                mSuper.getBasicStyle().getBottomLeftRoundRadius(),
                mSuper.getBasicStyle().getBottomRightRoundRadius(),
                mSuper.getShadowStyle().getShadowSize(),
                mSuper.getShadowStyle().getShadowDx(),
                mSuper.getShadowStyle().getShadowDy(),
                mSuper.getShadowStyle().getShadowColor(),
                mSuper.getShadowStyle().getBackgroundColor(),
                mSuper.getBorderStyle().getBorderSize(),
                mSuper.getBorderStyle().getBorderColor()
        );
        BitmapDrawable drawable = new BitmapDrawable(bitmap);
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN) {
            mView.setBackgroundDrawable(drawable);
        } else {
            mView.setBackground(drawable);
        }
    }

    /**
     * 生成阴影图像
     *
     * @param shadowWidth       阴影视图宽度
     * @param shadowHeight      阴影视图高度
     * @param topLeftRadius     左上角圆角半径
     * @param topRightRadius    右上角圆角半径
     * @param bottomLeftRadius  左下角圆角半径
     * @param bottomRightRadius 右下角圆角半径
     * @param shadowSize        阴影大小
     * @param dx                横坐标偏移量
     * @param dy                纵坐标偏移量
     * @param shadowColor       阴影颜色
     * @param fillColor         背景填充色
     * @param borderSize        描边尺寸
     * @param borderColor       描边颜色
     * @return 返回位图
     */
    private Bitmap getShadowBitmap(int shadowWidth, int shadowHeight, float topLeftRadius, float topRightRadius, float bottomLeftRadius, float bottomRightRadius, float shadowSize,
                                   float dx, float dy, int shadowColor, int fillColor, float borderSize, int borderColor) {
        //优化阴影bitmap大小,将尺寸缩小至原来的1/4。
        dx = dx / 4;
        dy = dy / 4;
        shadowWidth = (int) (shadowWidth / 4f);
        shadowHeight = (int) (shadowHeight / 4f);
        topLeftRadius = topLeftRadius / 4;
        topRightRadius = topRightRadius / 4;
        bottomLeftRadius = bottomLeftRadius / 4;
        bottomRightRadius = bottomRightRadius / 4;
        shadowSize = shadowSize / 4;
        borderSize = borderSize / 4;

        if (Math.abs(dy) > shadowSize) {
            dy = dy < 0 ? -shadowSize : shadowSize;
        }
        if (Math.abs(dx) > shadowSize) {
            dx = dx < 0 ? -shadowSize : shadowSize;
        }
        Bitmap output = Bitmap.createBitmap(shadowWidth, shadowHeight, Bitmap.Config.ARGB_4444);
        RectF rectf = new RectF(
                shadowSize,
                shadowSize,
                shadowWidth - shadowSize,
                shadowHeight - shadowSize);

        rectf.top -= dy;
        rectf.bottom -= dy;
        rectf.left -= dx;
        rectf.right -= dx;

        mShadowPaint.setColor(fillColor);
        if (!mView.isInEditMode()) {
            mShadowPaint.setShadowLayer(shadowSize, dx, dy, shadowColor);
        }
        Canvas canvas = new Canvas(output);

        drawTopLeft(canvas, rectf, topLeftRadius, topRightRadius, bottomLeftRadius, bottomRightRadius, mShadowPaint);

        final float sHeight = rectf.bottom - rectf.top;

        rectf.left += borderSize / 2f;
        rectf.top += borderSize / 2f;
        rectf.right -= borderSize / 2f;
        rectf.bottom -= borderSize / 2f;
        mBorderPaint.setStrokeWidth(borderSize);
        mBorderPaint.setColor(borderColor);

        final float bHeight = rectf.bottom - rectf.top - borderSize;

        drawTopLeft(canvas, rectf, topLeftRadius * (bHeight / sHeight), topRightRadius * (bHeight / sHeight), bottomLeftRadius * (bHeight / sHeight), bottomRightRadius * (bHeight / sHeight), mBorderPaint);
        return output;
    }


    private void drawTopLeft(Canvas canvas, RectF rectf, float topLeftRadius, float topRightRadius, float bottomLeftRadius, float bottomRightRadius, Paint roundPaint) {

        float height = rectf.bottom - rectf.top ;
        float width = rectf.right - rectf.left ;
        float standard = width;
        if(width>height){
            standard=height;
        }
        if(topLeftRadius+bottomLeftRadius>standard){
            float ratio = standard/(topLeftRadius+bottomLeftRadius);
            topLeftRadius = topLeftRadius*ratio;
            bottomLeftRadius = bottomLeftRadius*ratio;
        }
        if(topRightRadius+bottomRightRadius>standard){
            float ratio = standard/(topRightRadius+bottomRightRadius);
            topRightRadius = topRightRadius*ratio;
            bottomRightRadius = bottomRightRadius*ratio;
        }
        Path path = new Path();
        path.moveTo(rectf.left + topLeftRadius, rectf.top);
        path.lineTo(rectf.right - topRightRadius, rectf.top);
        path.arcTo(new RectF(rectf.right - topRightRadius * 2, rectf.top, rectf.right, rectf.top + topRightRadius * 2), 270, 90);
        path.lineTo(rectf.right, rectf.bottom - bottomRightRadius);
        path.arcTo(new RectF(rectf.right - bottomRightRadius * 2, rectf.bottom - bottomRightRadius * 2, rectf.right, rectf.bottom), 0, 90);
        path.lineTo(rectf.left + bottomLeftRadius, rectf.bottom);
        path.arcTo(new RectF(rectf.left, rectf.bottom - bottomLeftRadius * 2, rectf.left + bottomLeftRadius * 2, rectf.bottom), 90, 90);
        path.lineTo(rectf.left, rectf.top + topLeftRadius);
        path.arcTo(new RectF(rectf.left, rectf.top, rectf.left + topLeftRadius * 2, rectf.top + topLeftRadius * 2), 180, 90);
        path.close();
        canvas.drawPath(path, roundPaint);
    }


}
