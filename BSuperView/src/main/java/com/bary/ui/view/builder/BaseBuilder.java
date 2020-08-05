package com.bary.ui.view.builder;

import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.view.View;

import com.bary.ui.view.interf.ISuperInterface;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * author Bary
 * date on 2020/1/21.
 */
public abstract class BaseBuilder {
    public View mView;
    public ISuperInterface mSuper;
    private Paint mShadowPaint;
    private Paint mGradientPaint;
    private Paint mBorderPaint;


    public BaseBuilder(View view, ISuperInterface iSuper) {
        mView = view;
        mSuper = iSuper;
        //阴影画笔
        mShadowPaint = new Paint();
        mShadowPaint.setAntiAlias(true);
        mShadowPaint.setStyle(Paint.Style.FILL);
        //渐变画笔
        mGradientPaint = new Paint();
        mGradientPaint.setAntiAlias(true);
        mGradientPaint.setStyle(Paint.Style.FILL);
        //描边画笔
        mBorderPaint = new Paint();
        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setStyle(Paint.Style.STROKE);
    }

    abstract void initAttributes(TypedArray attr);

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
        int paddingLeft = (int) (mShadowXSize + mSuper.getBorderStyle().getBorderSize() + mSuper.getDefPadding().LEFT - mShadowDx);
        int paddingRight = (int) (mShadowXSize + mSuper.getBorderStyle().getBorderSize() + mSuper.getDefPadding().RIGHT + mShadowDx);
        int paddingTop = (int) (mShadowYSize + mSuper.getBorderStyle().getBorderSize() + mSuper.getDefPadding().TOP - mShadowDy);
        int paddingBottom = (int) (mShadowYSize + mSuper.getBorderStyle().getBorderSize() + mSuper.getDefPadding().BOTTOM + mShadowDy);
        mSuper.updatePadding(paddingLeft, paddingTop, paddingRight, paddingBottom);

    }

    /**
     * 隐藏边换算
     */
    private void setHiddenEdges() {
        Map<Integer, Boolean> edges = mSuper.getShadowStyle().getHiddenEdges();
        if (edges.get(ShadowBuilder.LEFT) && edges.get(ShadowBuilder.RIGHT)) {
            mShadowXSize = 0;
            mShadowDx = 0;
        } else if (edges.get(ShadowBuilder.LEFT) && !edges.get(ShadowBuilder.RIGHT)) {
            mShadowXSize = mShadowXSize / 2;
            mShadowDx = mShadowXSize;
        } else if (!edges.get(ShadowBuilder.LEFT) && edges.get(ShadowBuilder.RIGHT)) {
            mShadowXSize = mShadowXSize / 2;
            mShadowDx = -mShadowXSize;
        }
        if (edges.get(ShadowBuilder.TOP) && edges.get(ShadowBuilder.BOTTOM)) {
            mShadowYSize = 0;
            mShadowDy = 0;
        } else if (edges.get(ShadowBuilder.TOP) && !edges.get(ShadowBuilder.BOTTOM)) {
            mShadowYSize = mShadowYSize / 2;
            mShadowDy = mShadowYSize;
        } else if (!edges.get(ShadowBuilder.TOP) && edges.get(ShadowBuilder.BOTTOM)) {
            mShadowYSize = mShadowYSize / 2;
            mShadowDy = -mShadowYSize;
        }
    }

    float mShadowXSize, mShadowYSize, mShadowDx, mShadowDy;

    /**
     * 更新UI样式
     */
    public void updateUI() {
        if (mView.getWidth() == 0 || mView.getHeight() == 0) return;

        mShadowXSize = mSuper.getShadowStyle().getShadowXSize();
        mShadowYSize = mSuper.getShadowStyle().getShadowYSize();
        mShadowDx = mSuper.getShadowStyle().getShadowDx();
        mShadowDy = mSuper.getShadowStyle().getShadowDy();
        setHiddenEdges();
        setPading();

        int[] mBgColors = new int[Math.max(mSuper.getGradientStyle().getBackgroundGradientColor().size(),2)];
        if(mSuper.getGradientStyle().getBackgroundGradientColor().size()>0){
            if(mSuper.getGradientStyle().getBackgroundGradientColor().size()>1){
                for (int i = 0; i <mSuper.getGradientStyle().getBackgroundGradientColor().size() ; i++) {
                    mBgColors[i] = Color.parseColor(mSuper.getGradientStyle().getBackgroundGradientColor().get(i));
                }
            }else{
                mBgColors[0] = Color.parseColor(mSuper.getGradientStyle().getBackgroundGradientColor().get(0));
                mBgColors[1] = Color.parseColor(mSuper.getGradientStyle().getBackgroundGradientColor().get(0));
            }

        }else{
            mBgColors[0] = mSuper.getShadowStyle().getBackgroundColor();
            mBgColors[1] = mSuper.getShadowStyle().getBackgroundColor();
        }

        Bitmap bitmap = getShadowBitmap(mView.getWidth(), mView.getHeight(),
                mSuper.getBasicStyle().getTopLeftRoundRadius(),
                mSuper.getBasicStyle().getTopRightRoundRadius(),
                mSuper.getBasicStyle().getBottomLeftRoundRadius(),
                mSuper.getBasicStyle().getBottomRightRoundRadius(),
                mShadowXSize,
                mShadowYSize,
                mShadowDx,
                mShadowDy,
                mSuper.getShadowStyle().getShadowColor(),
                mSuper.getShadowStyle().getShadowAlpha(),
                mBgColors,
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
     * @param shadowXSize       阴影横向大小
     * @param shadowYSize       阴影竖向大小
     * @param dx                横坐标偏移量
     * @param dy                纵坐标偏移量
     * @param shadowColor       阴影颜色
     * @param shadowAlpha       阴影透明度
     * @param fillColor         背景填充色
     * @param borderSize        描边尺寸
     * @param borderColor       描边颜色
     * @return 返回位图
     */
    private Bitmap getShadowBitmap(int shadowWidth, int shadowHeight, float topLeftRadius, float topRightRadius, float bottomLeftRadius, float bottomRightRadius, float shadowXSize, float shadowYSize,
                                   float dx, float dy, int shadowColor, float shadowAlpha, int[] fillColor, float borderSize, int borderColor) {
        //优化阴影bitmap大小,将尺寸缩小至原来的1/4。
        dx = dx / 4;
        dy = dy / 4;
        shadowWidth = (int) (shadowWidth / 4f);
        shadowHeight = (int) (shadowHeight / 4f);
        topLeftRadius = topLeftRadius / 4;
        topRightRadius = topRightRadius / 4;
        bottomLeftRadius = bottomLeftRadius / 4;
        bottomRightRadius = bottomRightRadius / 4;
        shadowXSize = shadowXSize / 4;
        shadowYSize = shadowYSize / 4;
        borderSize = borderSize / 4;

        if (Math.abs(dy) > shadowYSize) {
            dy = dy < 0 ? -shadowYSize : shadowYSize;
        }
        if (Math.abs(dx) > shadowXSize) {
            dx = dx < 0 ? -shadowXSize : shadowXSize;
        }
        Bitmap output = Bitmap.createBitmap(shadowWidth, shadowHeight, Bitmap.Config.ARGB_4444);
        RectF rectf = new RectF(
                shadowXSize,
                shadowYSize,
                shadowWidth - shadowXSize,
                shadowHeight - shadowYSize);

        rectf.top -= dy;
        rectf.bottom -= dy;
        rectf.left -= dx;
        rectf.right -= dx;

        mShadowPaint.setColor(Color.TRANSPARENT);
        if (shadowAlpha < 1) {
            int alpha = (int) (Color.alpha(shadowColor) * shadowAlpha);
            shadowColor = Color.argb(alpha, Color.red(shadowColor), Color.green(shadowColor), Color.blue(shadowColor));
        }
        mShadowPaint.setShadowLayer(Math.max(shadowXSize, shadowYSize), dx, dy, shadowColor);
        Canvas canvas = new Canvas(output);
        drawCancas(canvas, rectf, topLeftRadius, topRightRadius, bottomLeftRadius, bottomRightRadius, mShadowPaint);
        Shader mBgShader = null;
        switch (mSuper.getGradientStyle().getBackgroundGradientType()){
            case LINEAR:
                float targetX = rectf.right;
                float targetY = rectf.bottom;
                switch (mSuper.getGradientStyle().getBackgroundGradientOrientation()){
                    case HORIZONTAL:
                        targetY = rectf.top;
                        break;
                    case VERTICAL:
                        targetX = rectf.left;
                        break;
                    case DIAGONAL:
                        //默认值
                        break;
                }
                mBgShader = new LinearGradient(rectf.left, rectf.top, targetX, targetY,fillColor, null, Shader.TileMode.REPEAT);
                break;
            case RADIAL:
                mBgShader = new RadialGradient(rectf.centerX(), rectf.centerY(),Math.max(rectf.width(), rectf.height())/2,fillColor, null,Shader.TileMode.REPEAT);
                break;
            case SWEEP:
                mBgShader = new SweepGradient(rectf.centerX(), rectf.centerY(),fillColor,null);
                break;
        }

        mGradientPaint.setShader(mBgShader);
        drawCancas(canvas, rectf, topLeftRadius, topRightRadius, bottomLeftRadius, bottomRightRadius, mGradientPaint);
        final float sHeight = rectf.bottom - rectf.top;
        rectf.left += borderSize / 2f;
        rectf.top += borderSize / 2f;
        rectf.right -= borderSize / 2f;
        rectf.bottom -= borderSize / 2f;
        mBorderPaint.setStrokeWidth(borderSize);
        mBorderPaint.setColor(borderColor);
        final float bHeight = rectf.bottom - rectf.top - borderSize;
        drawBorder(canvas, rectf, topLeftRadius * (bHeight / sHeight), topRightRadius * (bHeight / sHeight), bottomLeftRadius * (bHeight / sHeight), bottomRightRadius * (bHeight / sHeight),borderSize, mBorderPaint);
        return output;
    }


    private void drawCancas(Canvas canvas, RectF rectf, float topLeftRadius, float topRightRadius, float bottomLeftRadius, float bottomRightRadius, Paint roundPaint) {

        float height = rectf.bottom - rectf.top;
        float width = rectf.right - rectf.left;
        float standard = width;
        if (width > height) {
            standard = height;
        }
        if (topLeftRadius + bottomLeftRadius > standard) {
            float ratio = standard / (topLeftRadius + bottomLeftRadius);
            topLeftRadius = topLeftRadius * ratio;
            bottomLeftRadius = bottomLeftRadius * ratio;
        }
        if (topRightRadius + bottomRightRadius > standard) {
            float ratio = standard / (topRightRadius + bottomRightRadius);
            topRightRadius = topRightRadius * ratio;
            bottomRightRadius = bottomRightRadius * ratio;
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

    private void drawBorder(Canvas canvas, RectF rectf, float topLeftRadius, float topRightRadius, float bottomLeftRadius, float bottomRightRadius,float borderSize, Paint roundPaint) {

        float height = rectf.bottom - rectf.top;
        float width = rectf.right - rectf.left;
        float standard = width;
        if (width > height) {
            standard = height;
        }
        if (topLeftRadius + bottomLeftRadius > standard) {
            float ratio = standard / (topLeftRadius + bottomLeftRadius);
            topLeftRadius = topLeftRadius * ratio;
            bottomLeftRadius = bottomLeftRadius * ratio;
        }
        if (topRightRadius + bottomRightRadius > standard) {
            float ratio = standard / (topRightRadius + bottomRightRadius);
            topRightRadius = topRightRadius * ratio;
            bottomRightRadius = bottomRightRadius * ratio;
        }

        int defColor = roundPaint.getColor();
        int tl = defColor;
        int t = defColor;
        int tr = defColor;
        int r = defColor;
        int br = defColor;
        int b = defColor;
        int bl = defColor;
        int l = defColor;
        Map<Integer, Boolean> edges = mSuper.getBorderStyle().getHiddenEdges();
        if (edges.get(BorderBuilder.LEFT)) {
            tl = l = bl = Color.TRANSPARENT;
        }
        if (edges.get(BorderBuilder.RIGHT)) {
            tr = r = br = Color.TRANSPARENT;
        }

        if (edges.get(BorderBuilder.TOP)) {
            tl = t = tr = Color.TRANSPARENT;
        }
        if (edges.get(BorderBuilder.BOTTOM)) {
            bl = b = br = Color.TRANSPARENT;
        }

        float lineTopLeft = rectf.left + topLeftRadius-1;
        float lineLeftTop = rectf.top + topLeftRadius-1;
        if(topLeftRadius <=0){
            lineTopLeft = rectf.left-borderSize/2;
            lineLeftTop = rectf.top-borderSize/2;
        }
        float lineTopRight = rectf.right - topRightRadius+1;
        float lineRightTop = rectf.top+topRightRadius-1;
        if(topRightRadius <=0){
            lineTopRight = rectf.right+borderSize/2;
            lineRightTop = rectf.top-borderSize/2;
        }
        float lineBottomRight = rectf.right-bottomRightRadius+1;
        float lineRightBottom = rectf.bottom - bottomRightRadius+1;
        if(bottomRightRadius <=0){
            lineBottomRight = rectf.right+borderSize/2;
            lineRightBottom = rectf.bottom+borderSize/2;
        }

        float lineBottomLeft = rectf.left + bottomLeftRadius-1;
        float lineLeftBottom = rectf.bottom-bottomLeftRadius+1;
        if(bottomLeftRadius <=0){
            lineBottomLeft = rectf.left-borderSize/2;
            lineLeftBottom = rectf.bottom+borderSize/2;
        }

        //上
        roundPaint.setColor(t);
        canvas.drawLine(lineTopLeft, rectf.top,lineTopRight, rectf.top,roundPaint);

        //右上
        roundPaint.setColor(tr);
        canvas.drawArc(new RectF(rectf.right - topRightRadius * 2, rectf.top, rectf.right, rectf.top + topRightRadius * 2), 270, 90,false,roundPaint);

        //右
        roundPaint.setColor(r);
        canvas.drawLine(rectf.right, lineRightTop,rectf.right, lineRightBottom,roundPaint);

        //右下
        roundPaint.setColor(br);
        canvas.drawArc(new RectF(rectf.right - bottomRightRadius * 2, rectf.bottom - bottomRightRadius * 2, rectf.right, rectf.bottom), 0, 90,false,roundPaint);

        //下
        roundPaint.setColor(b);
        canvas.drawLine(lineBottomRight, rectf.bottom, lineBottomLeft, rectf.bottom,roundPaint);
        //左下
        roundPaint.setColor(bl);
        canvas.drawArc(new RectF(rectf.left, rectf.bottom - bottomLeftRadius * 2, rectf.left + bottomLeftRadius * 2, rectf.bottom), 90, 90,false,roundPaint);

        //左
        roundPaint.setColor(l);
        canvas.drawLine(rectf.left, lineLeftBottom, rectf.left, lineLeftTop,roundPaint);
        //左上
        roundPaint.setColor(tl);
        canvas.drawArc(new RectF(rectf.left, rectf.top, rectf.left + topLeftRadius * 2, rectf.top + topLeftRadius * 2), 180, 90,false,roundPaint);

    }


}
