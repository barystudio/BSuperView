package com.bary.ui.view.builder;

import android.content.res.TypedArray;
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
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.DrawableRes;

import com.bary.ui.R;
import com.bary.ui.view.interf.IStyleInterface;
import com.bary.ui.view.interf.ISuperInterface;


/**
 * 主题样式
 * author Bary
 * date on 2020/1/21.
 */
public class ThemeBuilder extends BaseBuilder implements IStyleInterface, View.OnTouchListener {

    /**
     * 清楚按钮的图标/密码可见/不可见的图标
     */
    private Drawable drawableClear, drawablePasswordVisible, drawablePasswordInvisible;
    /**
     * 文本变化前长度
     */
    private int mBeforeTextLength = 0;
    /**
     * 变化中文本长度
     */
    private int mChangingTextLength = 0;
    /**
     * 文本是否有变化
     */
    private boolean isChanged = false;
    /**
     * 光标位置
     */
    private int mSelectionLoc = 0;

    /**
     * 是否是银行卡类型
     **/
    private boolean isBankNoType;
    /**
     * 银行卡类型空格数量
     */
    private int mBankSpaceNumberB = 0;

    /**
     * 最大长度
     **/
    private int maxLength;

    /**
     * 右侧图标类型
     **/
    private Enum mDrawableType = DrawableEnum.CLEAR;



    private enum DrawableEnum {
        VISIBE_PWD,//显示密码图标
        INVISIBE_PWD,//隐藏密码图标
        CLEAR//清空文本图标
    }
    private StringBuffer mTextBuffer = new StringBuffer();
    private TextView mTextView;

    public ThemeBuilder(View view, ISuperInterface parentInterface) {
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
        //默认扩散区域宽度
//        mBorderSize = attr.getDimension(getStyleableId("borderSize"), 0);

        // 获取自定义属性
        drawableClear = mView.getResources().getDrawable(R.drawable.fty_edit_icon_del);
        drawablePasswordVisible = mView.getResources().getDrawable(R.drawable.fty_edit_icon_visible);
        drawablePasswordInvisible = mView.getResources().getDrawable(R.drawable.fty_edit_icon_invisible);
        mView.setTag(R.id.fty_id_edit_show_password, false);
        updateIcon();
        mTextView.setOnTouchListener(this);
        // 设置TextWatcher用于更新清除按钮显示状态
        mTextView.addTextChangedListener(mTextWatcher);
        mTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (mOnFocusChangeListener != null) {
                    mOnFocusChangeListener.onFocusChange(v, hasFocus);
                }
                updateIcon();
            }
        });
    }

    private View.OnFocusChangeListener mOnFocusChangeListener;

    public void setOnFocusChangeListener(View.OnFocusChangeListener listener) {
        mOnFocusChangeListener = listener;
    }

    /***
     * 设置提示文字大小
     * @param size 大小
     * @param isDip true是dp，false是sp
     */
    public void setHintSizeSp(int size,boolean isDip) {
        SpannableString ss = new SpannableString(mTextView.getHint());
        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(size, isDip);
        ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mTextView.setHint(new SpannedString(ss));
    }

    /**
     * 本文最大长度
     * @param maxLength 长度
     */
    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
        mTextView.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
        mTextView.invalidate();
    }

    /**
     * 显示密码可见图标
     */
    public void showPwdVisibeIcon() {
        mDrawableType = DrawableEnum.INVISIBE_PWD;
        mView.post(new Runnable() {
            @Override
            public void run() {
                updateIcon();
            }
        });
    }

    /**
     * 设置密码可见图标
     */
    public void setPwdVisibeIcon(@DrawableRes int visibeIcon, @DrawableRes int invisibeIcon) {
        drawablePasswordVisible = mTextView.getResources().getDrawable(visibeIcon);
        drawablePasswordInvisible = mTextView.getResources().getDrawable(invisibeIcon);
        mView.post(new Runnable() {
            @Override
            public void run() {
                updateIcon();
            }
        });
    }

    /**
     * 银行卡类型
     *
     * @param bankNoType 是否是银行卡类型
     */
    public void setBankNoType(boolean bankNoType) {
        isBankNoType = bankNoType;
        mView.invalidate();
    }

    /**
     * 字符变化监听
     */
    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            if (isBankNoType) {
                mBeforeTextLength = s.length();
                if (mTextBuffer.length() > 0) {
                    mTextBuffer.delete(0, mTextBuffer.length());
                }
                mBankSpaceNumberB = 0;
                for (int i = 0; i < s.length(); i++) {
                    if (s.charAt(i) == ' ') {
                        mBankSpaceNumberB++;
                    }
                }
            }
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (isBankNoType) {

                mChangingTextLength = s.length();
                mTextBuffer.append(s.toString());
                if (mChangingTextLength == mBeforeTextLength || mChangingTextLength <= 3 || isChanged) {
                    isChanged = false;
                    return;
                }
                isChanged = true;
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            updateIcon();
            if (isChanged && isBankNoType) {
                mSelectionLoc = mTextView.getSelectionEnd();
                int index = 0;
                while (index < mTextBuffer.length()) {
                    if (mTextBuffer.charAt(index) == ' ') {
                        mTextBuffer.deleteCharAt(index);
                    } else {
                        index++;
                    }
                }

                index = 0;
                int konggeNumberC = 0;
                while (index < mTextBuffer.length()) {
                    if ((index == 4 || index == 9 || index == 14 || index == 19)) {
                        mTextBuffer.insert(index, ' ');
                        konggeNumberC++;
                    }
                    index++;
                }

                if (konggeNumberC > mBankSpaceNumberB) {
                    mSelectionLoc += (konggeNumberC - mBankSpaceNumberB);
                }

                char[] tempChar = new char[mTextBuffer.length()];
                mTextBuffer.getChars(0, mTextBuffer.length(), tempChar, 0);
                String str = mTextBuffer.toString();
                if (mSelectionLoc > str.length()) {
                    mSelectionLoc = str.length();
                } else if (mSelectionLoc < 0) {
                    mSelectionLoc = 0;
                }

                mTextView.setText(str);
                Editable etable = (Editable) mTextView.getText();
                Selection.setSelection(etable, mSelectionLoc);
                isChanged = false;
            }
        }
    };


    /**
     * 更新清除按钮图标显示
     */
    private void updateIcon() {
        Drawable mDrawable = null;
        // 获取设置好的drawableLeft、drawableTop、drawableRight、drawableBottom
        Drawable[] drawables = mTextView.getCompoundDrawables();
        if (mDrawableType == DrawableEnum.INVISIBE_PWD) {
            mDrawable = drawablePasswordInvisible;
        } else if (mDrawableType == DrawableEnum.VISIBE_PWD) {
            mDrawable = drawablePasswordVisible;
        } else if (mDrawableType == DrawableEnum.CLEAR) {
            if (mTextView.length() > 0 && mTextView.isFocused()) {
                mDrawable = drawableClear;
            }
        }
        if (mDrawable != null) {
            int size = mTextView.getHeight() / 2;
//            if(size>PxTransUtils.auto2px(60)){
//                size = (int) PxTransUtils.auto2px(60);
//            }
            mDrawable.setBounds(0, 0, size, size);
        }
        mTextView.setCompoundDrawables(drawables[0], drawables[1], mDrawable,
                drawables[3]);
    }


    /**
     * 清空文本的方法
     */
    public void clearText() {
        mTextView.setText("");
    }


    public void setLeftIcon(Drawable drawable, int width, int height, int padding) {
        setDrawable("left", drawable, width, height, padding);
    }

    public void setTopIcon(Drawable drawable, int width, int height, int padding) {
        setDrawable("top", drawable, width, height, padding);
    }

    public void setRightIcon(Drawable drawable, int width, int height, int padding) {
        setDrawable("right", drawable, width, height, padding);
    }

    public void setBottomIcon(Drawable drawable, int width, int height, int padding) {
        setDrawable("bottom", drawable, width, height, padding);
    }

    private void setDrawable(String type, Drawable drawable, int width, int height, int padding) {
        drawable.setBounds(0, 0, width <= 0 ? drawable.getMinimumWidth() : width, height <= 0 ? drawable.getMinimumHeight() : height);
        Drawable[] drawables = mTextView.getCompoundDrawables();
        switch (type) {
            case "left":
                drawables[0] = drawable;
                break;
            case "top":
                drawables[1] = drawable;
                break;
            case "right":
                drawables[2] = drawable;
                break;
            case "bottom":
                drawables[3] = drawable;
                break;
        }
        mTextView.setCompoundDrawables(drawables[0], drawables[1], drawables[2], drawables[3]);
        mTextView.setCompoundDrawablePadding(padding);//设置图片和text之间的间距
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int xDown = (int) event.getX();
        if (event.getAction() == MotionEvent.ACTION_DOWN && xDown >= (mTextView.getWidth() - mTextView.getCompoundPaddingRight() * 2) && xDown < mTextView.getWidth() && mTextView.isEnabled()) {
            // 清除按钮的点击范围 按钮自身大小 +-padding
            if (mDrawableType == DrawableEnum.CLEAR) {
                clearText();
            } else if (mDrawableType == DrawableEnum.INVISIBE_PWD) {
                // 显示密码
                mTextView.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                mTextView.setTag(R.id.fty_id_edit_show_password, true);
                // 使光标始终在最后位置
                Editable etable = (Editable) mTextView.getText();
                Selection.setSelection(etable, etable.length());
                mDrawableType = DrawableEnum.VISIBE_PWD;
                updateIcon();
            } else if (mDrawableType == DrawableEnum.VISIBE_PWD) {
                // 隐藏密码
                mTextView.setTransformationMethod(PasswordTransformationMethod.getInstance());
                mTextView.setTag(R.id.fty_id_edit_show_password, false);
                // 使光标始终在最后位置
                Editable etable = (Editable) mTextView.getText();
                Selection.setSelection(etable, etable.length());
                mDrawableType = DrawableEnum.INVISIBE_PWD;
                updateIcon();
            }
            return false;
        }
        return false;
    }
}
