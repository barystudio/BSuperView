package com.bary.ui.layout.builder;

import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;

import com.bary.ui.layout.interf.ISuperLayoutInterface;
import com.bary.ui.view.builder.BorderViewBuilder;
import com.bary.ui.view.builder.ShadowViewBuilder;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * author Bary
 * date on 2020/1/21.
 */
public abstract class BaseLayoutBuilder {
    public View mView;
    public String mLastBackGroundFlag = "";
    public ISuperLayoutInterface mSuper;
    private Paint mShadowPaint;
    private Paint mBorderPaint;
    private Paint mBackGroundPaint;

    public BaseLayoutBuilder(View view, ISuperLayoutInterface iSuper) {
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
        //背景画笔
        mBackGroundPaint = new Paint();
        mBackGroundPaint.setAntiAlias(true);
        mBackGroundPaint.setStyle(Paint.Style.FILL);
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
        if (edges.get(ShadowViewBuilder.LEFT) && edges.get(ShadowViewBuilder.RIGHT)) {
            mShadowXSize = 0;
            mShadowDx = 0;
        } else if (edges.get(ShadowViewBuilder.LEFT) && !edges.get(ShadowViewBuilder.RIGHT)) {
            mShadowXSize = mShadowXSize / 2;
            mShadowDx = mShadowXSize;
        } else if (!edges.get(ShadowViewBuilder.LEFT) && edges.get(ShadowViewBuilder.RIGHT)) {
            mShadowXSize = mShadowXSize / 2;
            mShadowDx = -mShadowXSize;
        }
        if (edges.get(ShadowViewBuilder.TOP) && edges.get(ShadowViewBuilder.BOTTOM)) {
            mShadowYSize = 0;
            mShadowDy = 0;
        } else if (edges.get(ShadowViewBuilder.TOP) && !edges.get(ShadowViewBuilder.BOTTOM)) {
            mShadowYSize = mShadowYSize / 2;
            mShadowDy = mShadowYSize;
        } else if (!edges.get(ShadowViewBuilder.TOP) && edges.get(ShadowViewBuilder.BOTTOM)) {
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
        Bitmap bitmap = getShadowBitmap(mView.getWidth(), mView.getHeight(),
                mSuper.getRoundStyle().getTopLeftRoundRadius(),
                mSuper.getRoundStyle().getTopRightRoundRadius(),
                mSuper.getRoundStyle().getBottomLeftRoundRadius(),
                mSuper.getRoundStyle().getBottomRightRoundRadius(),
                mShadowXSize,
                mShadowYSize,
                mShadowDx,
                mShadowDy,
                mSuper.getShadowStyle().getShadowColor(),
                mSuper.getShadowStyle().getShadowAlpha(),
                mSuper.getBorderStyle().getBorderSize(),
                mSuper.getBorderStyle().getBorderColor()
        );
        if(bitmap==null)return;
        BitmapDrawable drawable = new BitmapDrawable(bitmap);
        mSuper.updateBackground(drawable);
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
     * @param borderSize        描边尺寸
     * @param borderColor       描边颜色
     * @return 返回位图
     */
    private Bitmap getShadowBitmap(int shadowWidth, int shadowHeight, float topLeftRadius, float topRightRadius, float bottomLeftRadius, float bottomRightRadius, float shadowXSize, float shadowYSize,
                                   float dx, float dy, int shadowColor, float shadowAlpha, float borderSize, int borderColor) {
        StringBuilder buffer = new StringBuilder();
        buffer.append(shadowWidth).append("-")
                .append(shadowHeight).append("-")
                .append(topLeftRadius).append("-")
                .append(topRightRadius).append("-")
                .append(bottomLeftRadius).append("-")
                .append(bottomRightRadius).append("-")
                .append(shadowXSize).append("-")
                .append(shadowYSize).append("-")
                .append(dx).append("-")
                .append(dy).append("-")
                .append(shadowColor).append("-")
                .append(shadowAlpha).append("-")
                .append(borderSize).append("-")
                .append(mSuper.getBorderStyle().getHiddenEdges()).append("-")
                .append(borderColor);
        if(mLastBackGroundFlag.equals(buffer.toString())) return null;

        mLastBackGroundFlag = buffer.toString();
        //优化阴影bitmap大小,将尺寸缩小至原来的1/4。
//        dx = dx / 4;
//        dy = dy / 4;
//        shadowWidth = (int) (shadowWidth / 4f);
//        shadowHeight = (int) (shadowHeight / 4f);
//        topLeftRadius = topLeftRadius / 4;
//        topRightRadius = topRightRadius / 4;
//        bottomLeftRadius = bottomLeftRadius / 4;
//        bottomRightRadius = bottomRightRadius / 4;
//        shadowXSize = shadowXSize / 4;
//        shadowYSize = shadowYSize / 4;
//        borderSize = borderSize / 4;

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
        if (shadowAlpha > 0) {
            int alpha = (int) (Color.alpha(shadowColor) * shadowAlpha);
            shadowColor = Color.argb(alpha, Color.red(shadowColor), Color.green(shadowColor), Color.blue(shadowColor));
        }
        mShadowPaint.setShadowLayer(Math.max(shadowXSize, shadowYSize), dx, dy, shadowColor);
        Canvas canvas = new Canvas(output);
        drawCancas(canvas, rectf, topLeftRadius, topRightRadius, bottomLeftRadius, bottomRightRadius, mShadowPaint);
        drawBackGround(canvas, rectf, topLeftRadius, topRightRadius, bottomLeftRadius , bottomRightRadius, mSuper.getShadowStyle().getBackground(), mBackGroundPaint);
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
        if (edges.get(BorderViewBuilder.LEFT)) {
            tl = l = bl = Color.TRANSPARENT;
        }
        if (edges.get(BorderViewBuilder.RIGHT)) {
            tr = r = br = Color.TRANSPARENT;
        }

        if (edges.get(BorderViewBuilder.TOP)) {
            tl = t = tr = Color.TRANSPARENT;
        }
        if (edges.get(BorderViewBuilder.BOTTOM)) {
            bl = b = br = Color.TRANSPARENT;
        }

        float fault = 0.5f;

        float lineTopLeft = rectf.left + topLeftRadius-fault;
        float lineLeftTop = rectf.top + topLeftRadius-fault;
        if(topLeftRadius <=0){
            lineTopLeft = rectf.left-borderSize/2;
            lineLeftTop = rectf.top-borderSize/2;
        }
        float lineTopRight = rectf.right - topRightRadius+fault;
        float lineRightTop = rectf.top+topRightRadius-1;
        if(topRightRadius <=0){
            lineTopRight = rectf.right+borderSize/2;
            lineRightTop = rectf.top-borderSize/2;
        }
        float lineBottomRight = rectf.right-bottomRightRadius+fault;
        float lineRightBottom = rectf.bottom - bottomRightRadius+fault;
        if(bottomRightRadius <=0){
            lineBottomRight = rectf.right+borderSize/2;
            lineRightBottom = rectf.bottom+borderSize/2;
        }

        float lineBottomLeft = rectf.left + bottomLeftRadius-fault;
        float lineLeftBottom = rectf.bottom-bottomLeftRadius+fault;
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

    private void drawBackGround(Canvas parentCanvas, RectF rectf, float topLeftRadius, float topRightRadius, float bottomLeftRadius, float bottomRightRadius, Drawable background, Paint paint) {

        float height =rectf.bottom - rectf.top;
        float width = rectf.right - rectf.left;
        float standard = width;
        if (width > height) {
            standard = height;
        }
        float mTopLeftRadius = topLeftRadius;
        float mBottomLeftRadius = bottomLeftRadius;
        float mTopRightRadius = topRightRadius ;
        float mBottomRightRadius = bottomRightRadius;

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
        Bitmap bitmap = DrawableToBitmap(background);

        int bmpWidth = bitmap.getWidth();
        int bmpHeight = bitmap.getHeight();

        Bitmap output = Bitmap.createBitmap((int)width, (int)height, Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(output);
        canvas.drawBitmap(bitmap,new Rect(0,0,bmpWidth,bmpHeight),new Rect(0,0,(int)width, (int)height),paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        if (mTopLeftRadius > 0) {
            Path path = new Path();
            path.moveTo(0, mTopLeftRadius);
            path.lineTo(0, 0);
            path.lineTo(mTopLeftRadius, 0);
            path.arcTo(new RectF(0, 0, mTopLeftRadius * 2, mTopLeftRadius * 2), -90, -90);
            path.close();
            canvas.drawPath(path, paint);
        }
        if (mTopRightRadius > 0) {
            Path path = new Path();
            path.moveTo(width - mTopRightRadius, 0);
            path.lineTo(width, 0);
            path.lineTo(width, mTopRightRadius);
            path.arcTo(new RectF(width - 2 * mTopRightRadius, 0, width, mTopRightRadius * 2), 0, -90);
            path.close();
            canvas.drawPath(path, paint);
        }
        if (mBottomLeftRadius > 0) {
            Path path = new Path();
            path.moveTo(0, height - mBottomLeftRadius);
            path.lineTo(0, height);
            path.lineTo(mBottomLeftRadius, height);
            path.arcTo(new RectF(0, height - 2 * mBottomLeftRadius, mBottomLeftRadius * 2, height), 90, 90);
            path.close();
            canvas.drawPath(path, paint);
        }
        if (mBottomRightRadius > 0) {
            Path path = new Path();
            path.moveTo(width - mBottomRightRadius, height);
            path.lineTo(width, height);
            path.lineTo(width, height - mBottomRightRadius);
            path.arcTo(new RectF(width - 2 * mBottomRightRadius, height - 2 * mBottomRightRadius, width, height), 0, 90);
            path.close();
            canvas.drawPath(path, paint);
        }

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
        parentCanvas.drawBitmap(output, rectf.left, rectf.top, paint);
    }
    public static Bitmap DrawableToBitmap(Drawable drawable) {

        // 获取 drawable 长宽
        int width = drawable.getIntrinsicWidth()<=0?100:drawable.getIntrinsicWidth();
        int heigh = drawable.getIntrinsicHeight()<=0?100:drawable.getIntrinsicHeight();

        drawable.setBounds(0, 0, width, heigh);

        // 获取drawable的颜色格式
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565;
        // 创建bitmap
        Bitmap bitmap = Bitmap.createBitmap(width, heigh, config);
        // 创建bitmap画布
        Canvas canvas = new Canvas(bitmap);
        // 将drawable 内容画到画布中
        drawable.draw(canvas);
        return bitmap;
    }
}
