package com.bary.ui.view.interf;


import android.graphics.drawable.Drawable;
import android.text.TextWatcher;
import android.view.View;

import androidx.annotation.DrawableRes;

import com.bary.ui.view.eum.EditMode;

/**
 * 样式方法定义
 * author Bary
 * create at 2020/8/4 10:05
 */
public interface IStyleInterface {
    void setEditMode(EditMode mode);
    void showClearIcon(boolean show);
    void showSecretIcon(boolean show);
    void setSecretIcon(@DrawableRes int visibeIcon, @DrawableRes int invisibeIcon);
    void setClearIcon(@DrawableRes int res);
    void addLeftIcon(Drawable drawable, int width, int height, int padding, View.OnClickListener listener);
    void addRightIcon(Drawable drawable, int width, int height, int padding, View.OnClickListener listener);
    void setOnLeftIconClickListener(View.OnClickListener listener);
    void setOnRightIconClickListener(View.OnClickListener listener);
    void addNewTextChangedListener(TextWatcher watcher);
    void setNewOnTouchListener(View.OnTouchListener listener);
    void setNewOnFocusChangeListener(View.OnFocusChangeListener listener);
}
