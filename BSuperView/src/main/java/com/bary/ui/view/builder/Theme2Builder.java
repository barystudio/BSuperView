package com.bary.ui.view.builder;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Selection;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.AbsoluteSizeSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.bary.ui.R;
import com.bary.ui.view.interf.IStyleInterface;
import com.bary.ui.view.interf.ISuperInterface;


/**
 * 主题样式
 * author Bary
 * date on 2020/1/21.
 */
public class Theme2Builder extends BaseBuilder implements IStyleInterface, View.OnTouchListener,TextWatcher {
    //按钮资源
    private final int CLEAR = R.drawable.fty_edit_icon_del;
    //动画时长
    private final int ANIMATOR_TIME = 200;
    //按钮左右间隔,单位DP
    private final int INTERVAL = 5;
    //清除按钮宽度,单位DP
    private final int WIDTH_OF_CLEAR = 23;


    //间隔记录
    private int Interval;
    //清除按钮宽度记录
    private int mWidth_clear;
    //右内边距
    private int mPaddingRight;
    //清除按钮的bitmap
    private Bitmap mBitmap_clear;
    //清除按钮出现动画
    private ValueAnimator mAnimator_visible;
    //消失动画
    private ValueAnimator mAnimator_gone;
    //是否显示的记录
    private boolean isVisible = false;
    //右边添加其他按钮时使用
    private int mRight = 0;
    private TextView mTextView;

    public Theme2Builder(View view, ISuperInterface parentInterface) {
        super(view, parentInterface);
        mTextView = (TextView) mView;
    }

    /**
     * 初始化样式属性
     *
     * @param attr 属性集合
     */
    @Override
    public void initAttributes(TypedArray attr) {
        if (attr == null) {
            return;
        }

        mTextView.setOnTouchListener(this);
        // 设置TextWatcher用于更新清除按钮显示状态
        mTextView.addTextChangedListener(this);


        mBitmap_clear = createBitmap(CLEAR,mView.getContext());

        Interval = dp2px(INTERVAL);
        mWidth_clear = dp2px(WIDTH_OF_CLEAR);
        mPaddingRight = Interval + mWidth_clear + Interval ;
        mAnimator_gone = ValueAnimator.ofFloat(1f, 0f).setDuration(ANIMATOR_TIME);
        mAnimator_visible = ValueAnimator.ofInt(mWidth_clear + Interval,0).setDuration(ANIMATOR_TIME);

    }

    private View.OnFocusChangeListener mOnFocusChangeListener;

    public void setOnFocusChangeListener(View.OnFocusChangeListener listener) {
        mOnFocusChangeListener = listener;
    }

    /**
     * 绘制清除按钮出现的图案
     * @param translationX 水平移动距离
     * @param canvas
     */
    protected void drawClear(int translationX, Canvas canvas){
        int right = mTextView.getWidth()+mTextView.getScrollX() - Interval - mRight +translationX;
        int left = right-mWidth_clear;
        int top = (mTextView.getHeight()-mWidth_clear)/2;
        int bottom = top + mWidth_clear;
        Rect rect = new Rect(left,top,right,bottom);
        canvas.drawBitmap(mBitmap_clear, null, rect, null);

    }

    /**
     * 绘制清除按钮消失的图案
     * @param scale 缩放比例
     * @param canvas
     */
    protected void drawClearGone( float scale,Canvas canvas){
        int right = (int) (mTextView.getWidth()+mTextView.getScrollX()- Interval - mRight -mWidth_clear*(1f-scale)/2f);
        int left = (int) (mTextView.getWidth()+mTextView.getScrollX()- Interval - mRight -mWidth_clear*(scale+(1f-scale)/2f));
        int top = (int) ((mTextView.getHeight()-mWidth_clear*scale)/2);
        int bottom = (int) (top + mWidth_clear*scale);
        Rect rect = new Rect(left,top,right,bottom);
        canvas.drawBitmap(mBitmap_clear, null, rect, null);
    }

    /**
     * 开始清除按钮的显示动画
     */
    private void startVisibleAnimator() {
        endAnaimator();
        mAnimator_visible.start();
        mTextView.invalidate();
    }

    /**
     * 开始清除按钮的消失动画
     */
    private void startGoneAnimator() {
        endAnaimator();
        mAnimator_gone.start();
        mTextView.invalidate();
    }

    /**
     * 结束所有动画
     */
    private void endAnaimator(){
        mAnimator_gone.end();
        mAnimator_visible.end();
    }



    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int xDown = (int) event.getX();
        if (event.getAction() == MotionEvent.ACTION_UP) {

            boolean touchable = ( mTextView.getWidth() - Interval - mRight - mWidth_clear < event.getX() ) && (event.getX() < mTextView.getWidth() - Interval - mRight);
            if (touchable) {
                mTextView.setError(null);
                mTextView.setText("");
            }
        }
        return false;
    }

    /**
     * 开始晃动动画
     */
    public void startShakeAnimation(){
        if(mTextView.getAnimation() == null){
            mTextView.setAnimation(shakeAnimation(4));
        }
        mTextView.startAnimation(mTextView.getAnimation());
    }

    /**
     * 晃动动画
     * @param counts 0.5秒钟晃动多少下
     * @return
     */
    private Animation shakeAnimation(int counts){
        Animation translateAnimation = new TranslateAnimation(0, 10, 0, 0);
        translateAnimation.setInterpolator(new CycleInterpolator(counts));
        translateAnimation.setDuration(500);
        return translateAnimation;
    }

    /**
     * 给图标染上当前提示文本的颜色并且转出Bitmap
     * @param resources
     * @param context
     * @return
     */
    public Bitmap createBitmap(int resources, Context context) {
        final Drawable drawable = ContextCompat.getDrawable(context, resources);
        final Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(wrappedDrawable, mTextView.getCurrentHintTextColor());
        return drawableToBitamp(wrappedDrawable);
    }

    /**
     * drawable转换成bitmap
     * @param drawable
     * @return
     */
    private Bitmap drawableToBitamp(Drawable drawable)
    {
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);
        return bitmap;
    }

    public int dp2px(float dipValue) {
        final float scale = mTextView.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    //----------------以下方法为方便子类继承，只使用ClearEditText就没有用处---------------------------------------------------------------------

    public int getInterval() {
        return  Interval;
    }

    public int getmWidth_clear() {
        return mWidth_clear;
    }

    public Bitmap getmBitmap_clear() {
        return mBitmap_clear;
    }

    public void addRight(int right){
        mRight += right;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if(s.length()>0) {
            if (!isVisible) {
                isVisible = true;
                startVisibleAnimator();
            }
        }else{
            if (isVisible) {
                isVisible = false;
                startGoneAnimator();
            }
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    public void onDraw(Canvas canvas) {
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));//抗锯齿
        if (mAnimator_visible.isRunning()) {
            int x = (int) mAnimator_visible.getAnimatedValue();
            drawClear(x,canvas);
            mTextView.invalidate();
        }else if (isVisible){
            drawClear(0,canvas);
        }

        if(mAnimator_gone.isRunning()){
            float scale = (float) mAnimator_gone.getAnimatedValue();
            drawClearGone(scale, canvas);
            mTextView.invalidate();
        }
    }
}
