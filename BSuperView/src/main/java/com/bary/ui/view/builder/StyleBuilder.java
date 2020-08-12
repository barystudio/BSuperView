package com.bary.ui.view.builder;

import android.content.Context;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.DrawableRes;

import com.bary.ui.R;
import com.bary.ui.view.bean.LeftIconBeans;
import com.bary.ui.view.bean.RightIconBeans;
import com.bary.ui.view.eum.EditMode;
import com.bary.ui.view.interf.IStyleInterface;
import com.bary.ui.view.interf.ISuperInterface;
import com.bary.ui.view.utils.UnitUtils;

import java.lang.reflect.Method;


/**
 * 主题样式
 * author Bary
 * date on 2020/1/21.
 */
public class StyleBuilder extends BaseBuilder implements IStyleInterface, View.OnTouchListener, TextWatcher {
    private EditText mEditText;
    private RightIconBeans rightIconBeans;
    private LeftIconBeans leftIconBeans;
    private TextWatcher mDefTextWatcher;
    private View.OnTouchListener mDefTouchListener;
    private View.OnFocusChangeListener mDefFocusChangeListener;
    /**
     * 最大按钮宽度
     */
    private int MAX_ICON_WIDTH = 23;
    private float mIconWidth;
    private int DEF_DRAWABLE_PADDING = 5;
    private final int ICON_CLEAR = 0;
    private final int ICON_SECRET = 1;
    private boolean mClearIconEnable = true;
    private boolean mSecretIconEnable = false;
    private int mClearIconRes;
    private int mSecretVisibleRes;
    private int mSecretInvisibleRes;
    private int mLeftIcon, mRightIcon;
    private float mLeftIconWidth, mLeftIconHeight, mLeftIconPadding, mRightIconWidth, mRightIconHeight, mRightIconPadding;
    private EditMode mEditMode;
    public StyleBuilder(View view, ISuperInterface parentInterface) {
        super(view, parentInterface);
        mEditText = (EditText) mView;
        mIconWidth = UnitUtils.dp2px(mEditText.getContext(), MAX_ICON_WIDTH);
    }

    /**
     * 初始化样式属性
     * @param attr 属性集合
     */
    @Override
    public void initAttributes(TypedArray attr) {
        if (attr == null) {
            return;
        }
        mEditMode = EditMode.fromId(attr.getInt(getStyleableId("editMode"), 1));

        mClearIconEnable = attr.getBoolean(getStyleableId("showClearIcon"), true);
        mSecretIconEnable = attr.getBoolean(getStyleableId("showSecretIcon"), false);
        mClearIconRes = attr.getResourceId(getStyleableId("clearIcon"), R.drawable.fty_edit_icon_del);
        mSecretVisibleRes = attr.getResourceId(getStyleableId("secretIconVisible"), R.drawable.fty_edit_icon_visible);
        mSecretInvisibleRes = attr.getResourceId(getStyleableId("secretIcoInvisible"), R.drawable.fty_edit_icon_invisible);

        mLeftIcon = attr.getResourceId(getStyleableId("leftIcon"), 0);
        mLeftIconWidth = attr.getDimension(getStyleableId("leftIconWidth"), mIconWidth);
        mLeftIconHeight = attr.getDimension(getStyleableId("leftIconHeight"), mIconWidth);
        mLeftIconPadding = attr.getDimension(getStyleableId("leftIconPadding"), (int) UnitUtils.dp2px(mView.getContext(), DEF_DRAWABLE_PADDING));

        mRightIcon = attr.getResourceId(getStyleableId("rightIcon"), 0);
        mRightIconWidth = attr.getDimension(getStyleableId("rightIconWidth"), mIconWidth);
        mRightIconHeight = attr.getDimension(getStyleableId("rightIconHeight"), mIconWidth);
        mRightIconPadding = attr.getDimension(getStyleableId("rightIconPadding"), (int) UnitUtils.dp2px(mView.getContext(), DEF_DRAWABLE_PADDING));

        // 获取自定义属性
        mView.setTag(R.id.fty_id_edit_show_password, false);
        leftIconBeans = new LeftIconBeans(mEditText);
        leftIconBeans.setDrawablePadding((int) mLeftIconPadding);

        rightIconBeans = new RightIconBeans(mEditText);
        rightIconBeans.setDrawablePadding((int) mRightIconPadding);
        rightIconBeans.addIcon(ICON_CLEAR, mClearIconRes, mIconWidth, mIconWidth, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearText();
            }
        });
        rightIconBeans.addIcon(ICON_SECRET, getSecretIconRes(), mIconWidth, mIconWidth, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((boolean) mEditText.getTag(R.id.fty_id_edit_show_password)) {
                    // 隐藏密码
                    mEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    mEditText.setTag(R.id.fty_id_edit_show_password, false);
                } else {
                    // 显示密码
                    mEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    mEditText.setTag(R.id.fty_id_edit_show_password, true);
                }
                rightIconBeans.updateRes(ICON_SECRET, getSecretIconRes());
                // 使光标始终在最后位置
                Editable etable = (Editable) mEditText.getText();
                Selection.setSelection(etable, etable.length());
                updateIcon();
            }
        });
        leftIconBeans.addIcon(leftIconBeans.aLLIconSize(), mLeftIcon, mLeftIconWidth, mLeftIconHeight, null);
        rightIconBeans.addIcon(rightIconBeans.aLLIconSize(), mRightIcon, mRightIconWidth, mRightIconHeight, null);

        mEditText.setOnTouchListener(this);
        // 设置TextWatcher用于更新清除按钮显示状态
        mEditText.addTextChangedListener(this);
        mEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (mDefFocusChangeListener != null) {
                    mDefFocusChangeListener.onFocusChange(v, hasFocus);
                }
                if (mEditText.getText().length() > 0 && mClearIconEnable) {
                    rightIconBeans.showIcon(ICON_CLEAR);
                } else {
                    rightIconBeans.hideIcon(ICON_CLEAR);
                }
                updateIcon();
            }
        });
        if (mSecretIconEnable) {
            rightIconBeans.showIcon(ICON_SECRET);
        } else {
            rightIconBeans.hideIcon(ICON_SECRET);
        }

        changeEditMode();
        updateIcon();
    }


    @Override
    public void setNewOnFocusChangeListener(View.OnFocusChangeListener listener) {
        mDefFocusChangeListener = listener;
    }

    @Override
    public void setEditMode(EditMode mode) {
        mEditMode = mode;
        changeEditMode();
    }

    @Override
    public void showClearIcon(boolean show) {
        mClearIconEnable = show;
        updateIcon();
    }

    /**
     * 显示密码可见图标
     */
    @Override
    public void showSecretIcon(boolean show) {
        mSecretIconEnable = show;
        updateIcon();
    }

    /**
     * 设置密文内容可见图标
     */
    @Override
    public void setSecretIcon(@DrawableRes int visibeIcon, @DrawableRes int invisibeIcon) {
        mSecretVisibleRes = visibeIcon;
        mSecretInvisibleRes = invisibeIcon;
        rightIconBeans.updateRes(ICON_SECRET, getSecretIconRes());
        updateIcon();
    }

    @Override
    public void setClearIcon(int res) {
        mClearIconRes = res;
        updateIcon();
    }

    @Override
    public void addLeftIcon(Drawable drawable, int width, int height, int padding, View.OnClickListener listener) {
        leftIconBeans.addIcon(leftIconBeans.aLLIconSize(), drawable, width, height, listener);
        leftIconBeans.setDrawablePadding(padding);
        updateIcon();
    }

    @Override
    public void addRightIcon(Drawable drawable, int width, int height, int padding, View.OnClickListener listener) {
        int id = rightIconBeans.aLLIconSize();
        rightIconBeans.addIcon(id, drawable, width, height, listener);
        rightIconBeans.showIcon(id);
        rightIconBeans.setDrawablePadding(padding);
        updateIcon();
    }

    @Override
    public void setOnLeftIconClickListener(View.OnClickListener listener) {
        leftIconBeans.setOnLeftIconClickListener(listener);
    }

    @Override
    public void setOnRightIconClickListener(View.OnClickListener listener) {
        rightIconBeans.setOnRightIconClickListener(listener);
    }

    @Override
    public void addNewTextChangedListener(TextWatcher watcher) {
        mDefTextWatcher = watcher;
    }

    @Override
    public void setNewOnTouchListener(View.OnTouchListener listener) {
        mDefTouchListener = listener;
    }



    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN && mEditText.isEnabled()) {
            rightIconBeans.onClick(event.getX(), event.getY());
            leftIconBeans.onClick(event.getX(), event.getY());
        }
        if (mDefTouchListener != null) return mDefTouchListener.onTouch(v, event);
        return false;
    }

    /**
     * 更新清除按钮图标显示
     */
    private void updateIcon() {
        if (mSecretIconEnable) {
            rightIconBeans.showIcon(ICON_SECRET);
        } else {
            rightIconBeans.hideIcon(ICON_SECRET);
        }
        mEditText.post(new Runnable() {
            @Override
            public void run() {
                Drawable[] drawables = mEditText.getCompoundDrawables();
                Drawable right = rightIconBeans.getDrawable();
                Drawable left = leftIconBeans.getDrawable();
                mEditText.setCompoundDrawables(left, drawables[1], right, drawables[3]);
            }
        });
    }


    /**
     * 清空文本的方法
     */
    public void clearText() {
        mEditText.setText("");
    }


    private int getSecretIconRes() {
        if ((boolean) mView.getTag(R.id.fty_id_edit_show_password)) {
            return mSecretVisibleRes;
        } else {
            return mSecretInvisibleRes;
        }
    }
    private void changeEditMode() {
        switch (mEditMode){
            case NORMAL:
                mEditText.setEnabled(true);
                try {
                    Method method = EditText.class.getMethod("setShowSoftInputOnFocus", boolean.class);
                    method.setAccessible(true);
                    method.invoke(mEditText, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ((InputMethodManager) mEditText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(mEditText, 0);
                break;
            case UNEDITABLE:
                mEditText.setEnabled(false);
                break;
            case NOKEYBOARD:
                mEditText.setEnabled(true);
                try {
                    Method method = EditText.class.getMethod("setShowSoftInputOnFocus", boolean.class);
                    method.setAccessible(true);
                    method.invoke(mEditText, false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ((InputMethodManager) mEditText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        if (mDefTextWatcher != null) mDefTextWatcher.beforeTextChanged(s, start, count, after);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (mDefTextWatcher != null) mDefTextWatcher.onTextChanged(s, start, before, count);
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (mEditText.getText().length() > 0 && mClearIconEnable) {
            rightIconBeans.showIcon(ICON_CLEAR);
        } else {
            rightIconBeans.hideIcon(ICON_CLEAR);
        }
        updateIcon();
        if (mDefTextWatcher != null) mDefTextWatcher.afterTextChanged(s);
    }

}
