package com.bary.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.StyleSpan;
import android.text.style.SubscriptSpan;
import android.text.style.SuperscriptSpan;
import android.text.style.UnderlineSpan;
import android.util.AttributeSet;

import androidx.annotation.ColorRes;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import com.bary.ui.R;
import com.bary.ui.common.bean.Padding;
import com.bary.ui.common.interf.IBorderInterface;
import com.bary.ui.common.interf.IRoundInterface;
import com.bary.ui.common.interf.IShadowInterface;
import com.bary.ui.view.builder.BorderViewBuilder;
import com.bary.ui.view.builder.GradientViewBuilder;
import com.bary.ui.view.builder.RoundViewBuilder;
import com.bary.ui.view.builder.ShadowViewBuilder;
import com.bary.ui.view.builder.StyleViewBuilder;
import com.bary.ui.view.eum.EditMode;
import com.bary.ui.view.eum.GradientOrientation;
import com.bary.ui.view.eum.GradientType;
import com.bary.ui.view.interf.IGradientInterface;
import com.bary.ui.view.interf.IStyleInterface;
import com.bary.ui.view.interf.ISuperViewInterface;

import java.util.List;


/**
 * 自定义TextView
 * author Bary
 * create at 2020/1/21 11:27
 */
public class BTextView extends AppCompatTextView implements IShadowInterface, IBorderInterface, IRoundInterface, IGradientInterface, ISuperViewInterface, IStyleInterface {
    private RoundViewBuilder mRoundBuilder;
    private ShadowViewBuilder mShadowBuilder;
    private BorderViewBuilder mBorderBuilder;
    private GradientViewBuilder mGradientBuilder;
    private StyleViewBuilder mStyleBuilder;
    private SpannableStringBuilder builder;
    private ForegroundColorSpan colorSpan;
    private int mPaddingLeft, mPaddingTop, mPaddingRight, mPaddingBottom;

    public BTextView(Context context) {
        this(context, null);
    }

    public BTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaddingLeft = getPaddingLeft();
        mPaddingRight = getPaddingRight();
        mPaddingTop = getPaddingTop();
        mPaddingBottom = getPaddingBottom();
        TypedArray attr = context.obtainStyledAttributes(attrs, R.styleable.BTextView);
        try {
            // 初始化阴影样式
            mShadowBuilder = new ShadowViewBuilder(this, this);
            // 初始化基础样式
            mBorderBuilder = new BorderViewBuilder(this, this);
            // 初始化描边样式
            mRoundBuilder = new RoundViewBuilder(this, this);
            // 初始化渐变样式
            mGradientBuilder = new GradientViewBuilder(this, this);
            // 初始化主题样式
            mStyleBuilder = new StyleViewBuilder(this, this);

            mShadowBuilder.initAttributes(attr);
            mBorderBuilder.initAttributes(attr);
            mRoundBuilder.initAttributes(attr);
            mGradientBuilder.initAttributes(attr);
            mStyleBuilder.initAttributes(attr);

        } finally {
            attr.recycle();
        }

        setFocusableInTouchMode(true);
    }

    public TextStyleBuild appendText(CharSequence text) {
        if(builder==null){
            builder = new SpannableStringBuilder();
        }
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(getCurrentTextColor());
        builder.append(text,colorSpan,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        setText(builder);
        TextStyleBuild styleBuild = new TextStyleBuild(text);
        styleBuild.withSize(getTextSize());
        return styleBuild;
    }

    public class TextStyleBuild{
        private CharSequence mText;
        public TextStyleBuild(CharSequence text) {
            mText = text;
        }

        //颜色
        public TextStyleBuild withColor(@ColorRes int color){
            return setSpan(new ForegroundColorSpan(ContextCompat.getColor(getContext(),color)));
        }
        //背景颜色
        public TextStyleBuild withBackgroundColor(@ColorRes int color){
            return setSpan(new BackgroundColorSpan(ContextCompat.getColor(getContext(),color)));
        }
        //字号
        public TextStyleBuild withSize(float size){
            return setSpan(new AbsoluteSizeSpan((int) size));
        }
        //加粗
        public TextStyleBuild withBold(){
            return setSpan(new StyleSpan(Typeface.BOLD));
        }
        //倾斜
        public TextStyleBuild withItalic(){
            return setSpan(new StyleSpan(Typeface.ITALIC));
        }
        //下划线
        public TextStyleBuild withUnderline(){
            return setSpan(new UnderlineSpan());
        }
        //上标
        public TextStyleBuild withSuperscript(){
            return setSpan(new SuperscriptSpan());
        }
        //下标
        public TextStyleBuild withSubscript(){
            return setSpan(new SubscriptSpan());
        }
        //图片
        public TextStyleBuild withImage(Drawable drawable, int width, int height){
            drawable.setBounds(0, 0, width, height);
            return setSpan( new ImageSpan(drawable));
        }

        private TextStyleBuild setSpan(Object what){
            builder.setSpan(what,getText().length()-mText.length(),getText().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            setText(builder);
            return this;
        }
    }


    @Override
    public float getRoundRadius() {
        return mRoundBuilder.getRoundRadius();
    }

    @Override
    public void setRoundRadius(float radius) {
        mRoundBuilder.setRoundRadius(radius);
    }

    @Override
    public float getTopLeftRoundRadius() {
        return mRoundBuilder.getTopLeftRoundRadius();
    }

    @Override
    public void setTopLeftRoundRadius(float radius) {
        mRoundBuilder.setTopLeftRoundRadius(radius);
    }

    @Override
    public float getTopRightRoundRadius() {
        return mRoundBuilder.getTopRightRoundRadius();
    }

    @Override
    public void setTopRightRoundRadius(float radius) {
        mRoundBuilder.setTopRightRoundRadius(radius);
    }

    @Override
    public float getBottomLeftRoundRadius() {
        return mRoundBuilder.getBottomLeftRoundRadius();
    }

    @Override
    public void setBottomLeftRoundRadius(float radius) {
        mRoundBuilder.setBottomLeftRoundRadius(radius);
    }

    @Override
    public float getBottomRightRoundRadius() {
        return mRoundBuilder.getBottomRightRoundRadius();
    }

    @Override
    public void setBottomRightRoundRadius(float radius) {
        mRoundBuilder.setBottomRightRoundRadius(radius);
    }

    @Override
    public void setShadowDx(float dx) {
        mShadowBuilder.setShadowDx(dx);
    }

    @Override
    public float getShadowDx() {
        return mShadowBuilder.getShadowDx();
    }

    @Override
    public void setShadowDy(float dy) {
        mShadowBuilder.setShadowDy(dy);
    }

    @Override
    public float getShadowDy() {
        return mShadowBuilder.getShadowDy();
    }

    @Override
    public float getShadowXSize() {
        return mShadowBuilder.getShadowXSize();
    }

    @Override
    public void setShadowYSize(float size) {
        mShadowBuilder.setShadowYSize(size);
    }

    @Override
    public float getShadowYSize() {
        return mShadowBuilder.getShadowYSize();
    }

    @Override
    public float getShadowSize() {
        return mShadowBuilder.getShadowSize();
    }

    @Override
    public void setShadowSize(float size) {
        mShadowBuilder.setShadowSize(size);
    }

    @Override
    public void setShadowXSize(float size) {
        mShadowBuilder.setShadowXSize(size);
    }

    @Override
    public void setShadowColor(int color) {
        mShadowBuilder.setShadowColor(color);
    }

    @Override
    public int getShadowColor() {
        return mShadowBuilder.getShadowColor();
    }

    @Override
    public void setShadowAlpha(float alpha) {
        mShadowBuilder.setShadowAlpha(alpha);
    }

    @Override
    public float getShadowAlpha() {
        return mShadowBuilder.getShadowAlpha();
    }

    @Override
    public void showShadowEdges(int... edges) {
        mShadowBuilder.showShadowEdges(edges);
    }

    @Override
    public void hideShadowEdges(int... edges) {
        mShadowBuilder.hideShadowEdges(edges);
    }

    @Override
    public boolean isHiddenShadowEdges(int edges) {
        return mShadowBuilder.isHiddenShadowEdges(edges);
    }


    @Override
    public boolean isShadowShow() {
        return mShadowBuilder.isShadowShow();
    }

    @Override
    public float getBorderSize() {
        return mBorderBuilder.getBorderSize();
    }

    @Override
    public void setBorderSize(float size) {
        mBorderBuilder.setBorderSize(size);
    }

    @Override
    public void setBorderColor(int color) {
        mBorderBuilder.setBorderColor(color);
    }

    @Override
    public int getBorderColor() {
        return mBorderBuilder.getBorderColor();
    }

    @Override
    public boolean isBorderShow() {
        return mBorderBuilder.isBorderShow();
    }

    @Override
    public void hideBorderEdges(int... edges) {
        mBorderBuilder.hideBorderEdges(edges);
    }

    @Override
    public void showBorderEdges(int... edges) {
        mBorderBuilder.showBorderEdges(edges);
    }

    @Override
    public boolean isHiddenBorderEdges(int edges) {
        return mBorderBuilder.isHiddenBorderEdges(edges);
    }

    @Override
    public List<String> getTextGradientColor() {
        return mGradientBuilder.getTextGradientColor();
    }

    @Override
    public void setTextGradientColor(String... colors) {
        mGradientBuilder.setTextGradientColor(colors);
    }

    @Override
    public void clearTextGradientColor() {
        mGradientBuilder.clearTextGradientColor();
    }

    @Override
    public GradientOrientation getTextGradientOrientation() {
        return mGradientBuilder.getTextGradientOrientation();
    }

    @Override
    public void setTextGradientOrientation(GradientOrientation orientation) {
        mGradientBuilder.setTextGradientOrientation(orientation);
    }

    @Override
    public GradientType getTextGradientType() {
        return mGradientBuilder.getTextGradientType();
    }

    @Override
    public void setTextGradientType(GradientType type) {
        mGradientBuilder.setTextGradientType(type);
    }

    @Override
    public List<String> getBackgroundGradientColor() {
        return mGradientBuilder.getBackgroundGradientColor();
    }

    @Override
    public void setBackgroundGradientColor(String... colors) {
        mGradientBuilder.setBackgroundGradientColor(colors);
    }

    @Override
    public void clearBackgroundGradientColor() {
        mGradientBuilder.clearBackgroundGradientColor();
    }

    @Override
    public GradientOrientation getBackgroundGradientOrientation() {
        return mGradientBuilder.getBackgroundGradientOrientation();
    }

    @Override
    public void setBackgroundGradientOrientation(GradientOrientation orientation) {
        mGradientBuilder.setBackgroundGradientOrientation(orientation);
    }

    @Override
    public GradientType getBackgroundGradientType() {
        return mGradientBuilder.getBackgroundGradientType();
    }

    @Override
    public void setBackgroundGradientType(GradientType type) {
        mGradientBuilder.setBackgroundGradientType(type);
    }


    @Override
    public void setEditMode(EditMode mode) {
        mStyleBuilder.setEditMode(mode);
    }

    @Override
    public void showClearIcon(boolean show) {
        mStyleBuilder.showClearIcon(show);
    }

    @Override
    public void showSecretIcon(boolean show) {
        mStyleBuilder.showSecretIcon(show);
    }

    @Override
    public void setSecretIcon(int visibeIcon, int invisibeIcon) {
        mStyleBuilder.setSecretIcon(visibeIcon,invisibeIcon);
    }

    @Override
    public void setClearIcon(int res) {
        mStyleBuilder.setClearIcon(res);
    }

    @Override
    public void addLeftIcon(Drawable drawable, int width, int height, int padding, OnClickListener listener) {
        mStyleBuilder.addLeftIcon(drawable, width, height, padding, listener);
    }

    @Override
    public void addRightIcon(Drawable drawable, int width, int height, int padding, OnClickListener listener) {
        mStyleBuilder.addRightIcon(drawable, width, height, padding, listener);
    }

    @Override
    public void setOnLeftIconClickListener(OnClickListener listener) {
        mStyleBuilder.setOnLeftIconClickListener(listener);
    }

    @Override
    public void setOnRightIconClickListener(OnClickListener listener) {
        mStyleBuilder.setOnRightIconClickListener(listener);
    }

    @Override
    public void addNewTextChangedListener(TextWatcher watcher) {
        mStyleBuilder.addNewTextChangedListener(watcher);
    }

    @Override
    public void setNewOnTouchListener(OnTouchListener listener) {
        mStyleBuilder.setNewOnTouchListener(listener);
    }

    @Override
    public void setNewOnFocusChangeListener(OnFocusChangeListener listener) {
        mStyleBuilder.setNewOnFocusChangeListener(listener);
    }

    @Override
    public void setBackgroundResource(int resid) {
        mShadowBuilder.setBackground(ContextCompat.getDrawable(getContext(),resid));
    }

    @Override
    public void setBackground(Drawable background) {
        if(mShadowBuilder==null)return;
        mShadowBuilder.setBackground(background);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mShadowBuilder.updateUI();
    }

    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        mPaddingLeft = left;
        mPaddingRight = right;
        mPaddingTop = top;
        mPaddingBottom = bottom;
        mShadowBuilder.updateUI();
    }

    @Override
    public RoundViewBuilder getRoundStyle() {
        return mRoundBuilder;
    }

    @Override
    public ShadowViewBuilder getShadowStyle() {
        return mShadowBuilder;
    }

    @Override
    public BorderViewBuilder getBorderStyle() {
        return mBorderBuilder;
    }

    @Override
    public GradientViewBuilder getGradientStyle() {
        return mGradientBuilder;
    }

    @Override
    public void updatePadding(int left, int top, int right, int bottom) {
        super.setPadding(left, top, right, bottom);
    }

    @Override
    public void updateBackground(Drawable drawable) {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN) {
            super.setBackgroundDrawable(drawable);
        } else {
            super.setBackground(drawable);
        }
    }

    @Override
    public Padding getDefPadding() {
        return new Padding(mPaddingLeft, mPaddingTop, mPaddingRight, mPaddingBottom);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        mGradientBuilder.updateTextGradient();
        super.onDraw(canvas);
    }


}

