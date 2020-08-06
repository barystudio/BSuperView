package com.bary.ui.view.builder;

import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bary.ui.view.eum.GradientOrientation;
import com.bary.ui.view.eum.GradientType;
import com.bary.ui.view.interf.IGradientInterface;
import com.bary.ui.view.interf.ISuperInterface;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * 渐变色管理器
 * author Bary
 * date on 2020/1/21.
 */
public class GradientBuilder extends BaseBuilder implements IGradientInterface {

    public String mLastGradientFlag = "";
    private List<String> mTextGradientColor = new ArrayList<>();
    private List<String> mBackgroundGradientColor = new ArrayList<>();
    private GradientOrientation mBackgroundGradientOrientation;
    private GradientType mBackgroundGradientType;
    private GradientOrientation mTextGradientOrientation;
    private GradientType mTextGradientType;


    public GradientBuilder(View view, ISuperInterface parentInterface) {
        super(view, parentInterface);
    }

    @Override
    public void initAttributes(TypedArray attr) {
        if (attr == null) {
            return;
        }
        String textGradientColor = attr.getString(getStyleableId("textGradientColor"));
        mTextGradientOrientation = GradientOrientation.fromId(attr.getInt(getStyleableId("textGradientOrientation"), 1));
        mTextGradientType = GradientType.fromId(attr.getInt(getStyleableId("textGradientType"), 1));
        String backgroundGradientColor = attr.getString(getStyleableId("backgroundGradientColor"));
        mBackgroundGradientOrientation = GradientOrientation.fromId(attr.getInt(getStyleableId("backgroundGradientOrientation"), 1));
        mBackgroundGradientType = GradientType.fromId(attr.getInt(getStyleableId("backgroundGradientType"), 1));
        if (!TextUtils.isEmpty(textGradientColor)) {
            textGradientColor = textGradientColor.replaceAll("\\s*", "").replaceAll("#", "");
            String[] colors = textGradientColor.split("\\|");
            for (String color : colors) {
                if (isHexColor(color)) {
                    mTextGradientColor.add("#" + color);
                }
            }
        }
        if (!TextUtils.isEmpty(backgroundGradientColor)) {
            backgroundGradientColor = backgroundGradientColor.replaceAll("\\s*", "").replaceAll("#", "");
            String[] colors = backgroundGradientColor.split("\\|");
            for (String color : colors) {
                if (isHexColor(color)) {
                    mBackgroundGradientColor.add("#" + color);
                }
            }
        }
    }


    @Override
    public List<String> getTextGradientColor() {
        return mTextGradientColor;
    }

    @Override
    public void setTextGradientColor(String... colors) {
        mTextGradientColor = new ArrayList<>(Arrays.asList(colors));
    }

    @Override
    public void clearTextGradientColor() {
        mTextGradientColor = new ArrayList();
    }

    @Override
    public GradientOrientation getTextGradientOrientation() {
        return mTextGradientOrientation;
    }

    @Override
    public void setTextGradientOrientation(GradientOrientation orientation) {
        mTextGradientOrientation = orientation;
        mSuper.getShadowStyle().updateUI();
    }

    @Override
    public GradientType getTextGradientType() {
        return mTextGradientType;
    }

    @Override
    public void setTextGradientType(GradientType type) {
        mTextGradientType = type;
        mSuper.getShadowStyle().updateUI();
    }

    @Override
    public List<String> getBackgroundGradientColor() {
        return mBackgroundGradientColor;
    }

    @Override
    public void setBackgroundGradientColor(String... colors) {
        mBackgroundGradientColor = new ArrayList<>(Arrays.asList(colors));
        mSuper.getShadowStyle().updateUI();
    }

    @Override
    public void clearBackgroundGradientColor() {
        mBackgroundGradientColor = new ArrayList<>();
        mSuper.getShadowStyle().updateUI();
    }

    @Override
    public GradientOrientation getBackgroundGradientOrientation() {
        return mBackgroundGradientOrientation;
    }

    @Override
    public void setBackgroundGradientOrientation(GradientOrientation orientation) {
        mBackgroundGradientOrientation = orientation;
        mSuper.getShadowStyle().updateUI();
    }

    @Override
    public GradientType getBackgroundGradientType() {
        return mBackgroundGradientType;
    }

    @Override
    public void setBackgroundGradientType(GradientType type) {
        mBackgroundGradientType = type;
        mSuper.getShadowStyle().updateUI();
    }

    private Rect mTextBound = new Rect();
    private RectF rectf = new RectF();
    private Shader mTextShader;

    public void updateTextGradient() {
        if (!(mView instanceof TextView)) return;
        TextView textView = ((TextView) mView);


        int[] textColor = new int[mSuper.getGradientStyle().getTextGradientColor().size()];
        if (mSuper.getGradientStyle().getTextGradientColor().size() < 2) {
            textColor = new int[2];
            textColor[0] = textView.getTextColors().getDefaultColor();
            textColor[1] = textView.getTextColors().getDefaultColor();
        }else{
            for (int i = 0 ;i < mSuper.getGradientStyle().getTextGradientColor().size(); i++) {
                textColor[i] = Color.parseColor(mSuper.getGradientStyle().getTextGradientColor().get(i));
            }
        }

        String mText = textView.getText().toString();
        textView.getPaint().getTextBounds(mText, 0, mText.length(), mTextBound);

        rectf.left = textView.getPaddingLeft()+(textView.getCompoundDrawables()[0]==null?0:textView.getCompoundDrawables()[0].getBounds().right-textView.getCompoundDrawables()[0].getBounds().left+textView.getCompoundDrawablePadding());
        rectf.right = textView.getWidth()-textView.getPaddingRight()-(textView.getCompoundDrawables()[2]==null?0:textView.getCompoundDrawables()[2].getBounds().right-textView.getCompoundDrawables()[2].getBounds().left+textView.getCompoundDrawablePadding());
        rectf.top = textView.getPaddingTop()+(textView.getCompoundDrawables()[1]==null?0:textView.getCompoundDrawables()[1].getBounds().bottom-textView.getCompoundDrawables()[1].getBounds().top+textView.getCompoundDrawablePadding());
        rectf.bottom = textView.getHeight()-textView.getPaddingBottom()-(textView.getCompoundDrawables()[3]==null?0:textView.getCompoundDrawables()[3].getBounds().bottom-textView.getCompoundDrawables()[3].getBounds().top+textView.getCompoundDrawablePadding());

        StringBuffer buffer = new StringBuffer();
        buffer.append(textView.getText()).append("-").append(textView.getHint()).append("-").append(textView.getTextSize()).append("-").append(textView.getTextColors().getDefaultColor()).append("-")
                .append(mSuper.getGradientStyle().getTextGradientColor()).append("-").append(rectf.left).append("-").append(rectf.top).append("-").append(rectf.right).append("-").append(rectf.bottom).append("-")
                .append(mSuper.getGradientStyle().getTextGradientType()).append("-").append(mSuper.getGradientStyle().getTextGradientOrientation());
        if(mLastGradientFlag.equals(buffer.toString())) return;

        mLastGradientFlag = buffer.toString();

        switch (mSuper.getGradientStyle().getTextGradientType()) {
            case LINEAR:
                float targetX = rectf.right;
                float targetY = rectf.bottom;
                switch (mSuper.getGradientStyle().getTextGradientOrientation()) {
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
                mTextShader = new LinearGradient(rectf.left, rectf.top, targetX, targetY, textColor, null, Shader.TileMode.REPEAT);
                break;
            case RADIAL:
                if(Math.max(rectf.width(), rectf.height()) / 2>0){
                    mTextShader = new RadialGradient(rectf.centerX(), rectf.centerY(), Math.max(rectf.width(), rectf.height()) / 2, textColor, null, Shader.TileMode.REPEAT);
                }
                break;
            case SWEEP:
                mTextShader = new SweepGradient(rectf.centerX(), rectf.centerY(), textColor, null);
                break;
        }
        if(textView.getText().length()>0&&mTextShader!=null){
            textView.getPaint().setShader(mTextShader);
        }else{
            textColor = new int[]{textView.getHintTextColors().getDefaultColor(), textView.getHintTextColors().getDefaultColor()};
            textView.getPaint().setShader(new LinearGradient(0, 0,textView.getMeasuredWidth(),textView.getMeasuredHeight(), textColor, null, Shader.TileMode.REPEAT));
        }
        if(mView instanceof EditText){
            int index = ((EditText)mView).getSelectionStart();
            ((EditText)mView).setText(textView.getText());
            ((EditText)mView).setSelection(index);
        }

    }

    private boolean isHexColor(String color) {
        return color != null && (color.length() == 3 || color.length() == 6)
                && color.matches("[0-9A-Fa-f]+");
    }

}
