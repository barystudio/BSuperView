package com.bary.sample

import android.os.Bundle
import android.view.View
import android.widget.RadioGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bary.sample.view.MyColorBar
import com.bary.ui.view.builder.ShadowBuilder
import com.bary.ui.view.eum.GradientOrientation
import com.bary.ui.view.eum.GradientType
import kotlinx.android.synthetic.main.activity_bedittext.*

class BEditTextActivity : AppCompatActivity(), View.OnClickListener, OnSeekBarChangeListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bedittext)
        AppUtils.setStatusBarColor(
            window,
            ContextCompat.getColor(this, R.color.colorPrimary),
            false
        )
        initView();
    }

    fun initView() {
        back.setColorFilter(ContextCompat.getColor(this, R.color.white))

        /**--圆角--**/
        //初始化圆角尺寸
        skbar_round.max = AppUtils.dip2px(this,70f)
        skbar_round_tl.max = skbar_round.max
        skbar_round_tr.max = skbar_round.max
        skbar_round_bl.max = skbar_round.max
        skbar_round_br.max = skbar_round.max
        skbar_round.progress = mBEditText.roundRadius.toInt()
        skbar_round_tl.progress = mBEditText.topLeftRoundRadius.toInt()
        skbar_round_tr.progress = mBEditText.topRightRoundRadius.toInt()
        skbar_round_bl.progress = mBEditText.bottomLeftRoundRadius.toInt()
        skbar_round_br.progress = mBEditText.bottomRightRoundRadius.toInt()
        roundTitleLayout.setOnClickListener(this)
        skbar_round.setOnSeekBarChangeListener(this)
        skbar_round_tl.setOnSeekBarChangeListener(this)
        skbar_round_tr.setOnSeekBarChangeListener(this)
        skbar_round_bl.setOnSeekBarChangeListener(this)
        skbar_round_br.setOnSeekBarChangeListener(this)
        /**--描边--**/
        //初始化描边数据
        borderTitleLayout.setOnClickListener(this)
        skbar_border.max =AppUtils.dip2px(this,20f)
        skbar_border.setOnSeekBarChangeListener(this)
        borderhide_top.setOnClickListener(this)
        borderhide_left.setOnClickListener(this)
        borderhide_right.setOnClickListener(this)
        borderhide_bottom.setOnClickListener(this)
        borderhide_top.isSelected = mBEditText.isHiddenBorderEdges(ShadowBuilder.TOP)
        borderhide_left.isSelected = mBEditText.isHiddenBorderEdges(ShadowBuilder.LEFT)
        borderhide_right.isSelected = mBEditText.isHiddenBorderEdges(ShadowBuilder.RIGHT)
        borderhide_bottom.isSelected = mBEditText.isHiddenBorderEdges(ShadowBuilder.BOTTOM)
        /**--阴影--**/
        //初始化阴影尺寸
        showShadow.setOnClickListener(this)
        shadowhide_top.setOnClickListener(this)
        shadowhide_left.setOnClickListener(this)
        shadowhide_right.setOnClickListener(this)
        shadowhide_bottom.setOnClickListener(this)
        showShadow.isSelected = mBEditText.isShadowShow

        shadowhide_top.isSelected = mBEditText.isHiddenShadowEdges(ShadowBuilder.TOP)
        shadowhide_left.isSelected = mBEditText.isHiddenShadowEdges(ShadowBuilder.LEFT)
        shadowhide_right.isSelected = mBEditText.isHiddenShadowEdges(ShadowBuilder.RIGHT)
        shadowhide_bottom.isSelected = mBEditText.isHiddenShadowEdges(ShadowBuilder.BOTTOM)
        skbar_size.max = (mBEditText.shadowSize * 2).toInt()
        skbar_size.progress = mBEditText.shadowSize.toInt()
        //初始化透明度
        skbar_alpha.max = 255
        skbar_alpha.progress = (mBEditText.shadowAlpha * 255).toInt()
        //初始化阴影颜色
        skbar_color.setMarkColor(mBEditText.shadowColor)
        skbar_size.setOnSeekBarChangeListener(this)
        skbar_x.setOnSeekBarChangeListener(this)
        skbar_y.setOnSeekBarChangeListener(this)
        skbar_alpha.setOnSeekBarChangeListener(this)
        skbar_color.setOnColorChangeListener(object : MyColorBar.OnStateChangeListener {
            override fun onProcessChanged(progress: Int, color: Int) {
                mBEditText.shadowColor = color
            }

        })

        /**--渐变色--**/
        //初始渐变色
        showGradientBg.setOnClickListener(this)
        gradient_bg_orientation.setOnCheckedChangeListener(object :RadioGroup.OnCheckedChangeListener{
            override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
                if(checkedId == R.id.gradient_bg_orientation_h){
                    mBEditText.backgroundGradientOrientation = GradientOrientation.HORIZONTAL
                }else if(checkedId == R.id.gradient_bg_orientation_v){
                    mBEditText.backgroundGradientOrientation = GradientOrientation.VERTICAL
                }else if(checkedId == R.id.gradient_bg_orientation_d){
                    mBEditText.backgroundGradientOrientation = GradientOrientation.DIAGONAL
                }
            }

        })
        gradient_bg_type.setOnCheckedChangeListener(object :RadioGroup.OnCheckedChangeListener{
            override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
                if(checkedId == R.id.gradient_bg_type_l){
                    mBEditText.backgroundGradientType = GradientType.LINEAR
                }else if(checkedId == R.id.gradient_bg_type_r){
                    mBEditText.backgroundGradientType = GradientType.RADIAL
                }else if(checkedId == R.id.gradient_bg_type_s){
                    mBEditText.backgroundGradientType = GradientType.SWEEP
                }
            }

        })

        showGradientBg.isSelected = false
        gradient_bg_orientation.check(R.id.gradient_bg_orientation_h)
        gradient_bg_type.check(R.id.gradient_bg_type_l)
    }

    override fun onClick(v: View?) {
        val i = v!!.id
        if (i == R.id.roundTitleLayout) {
            changeInfoVisible(roundTitleIv,roundIinfoLayout)
        } else if (i == R.id.borderTitleLayout) {
            changeInfoVisible(borderTitleIv,borderIinfoLayout)
        } else if (i == R.id.borderhide_left) {
            hideBorderEdges(borderhide_left,ShadowBuilder.LEFT);
        } else if (i == R.id.borderhide_top) {
            hideBorderEdges(borderhide_top,ShadowBuilder.TOP);
        } else if (i == R.id.borderhide_right) {
            hideBorderEdges(borderhide_right,ShadowBuilder.RIGHT);
        } else if (i == R.id.borderhide_bottom) {
            hideBorderEdges(borderhide_bottom,ShadowBuilder.BOTTOM);
        } else if (i == R.id.showShadow) {
            showShadow.isSelected = !showShadow.isSelected
            mBEditText.isShadowShow = showShadow.isSelected
            if (showShadow.isSelected){
                shadow_layout.visibility = View.VISIBLE;
                skbar_size.max = (mBEditText.shadowSize * 2).toInt()
                skbar_size.progress = mBEditText.shadowSize.toInt()
            }else{
                shadow_layout.visibility = View.GONE;
            }
        } else if (i == R.id.shadowhide_left) {
            hideShadowEdges(shadowhide_left,ShadowBuilder.LEFT);
        } else if (i == R.id.shadowhide_top) {
            hideShadowEdges(shadowhide_top,ShadowBuilder.TOP);
        } else if (i == R.id.shadowhide_right) {
            hideShadowEdges(shadowhide_right,ShadowBuilder.RIGHT);
        } else if (i == R.id.shadowhide_bottom) {
            hideShadowEdges(shadowhide_bottom,ShadowBuilder.BOTTOM);
        } else if (i == R.id.showGradientBg) {
            showGradientBg.isSelected = !showGradientBg.isSelected
            gradient_bg_layout.visibility = if(showGradientBg.isSelected) View.VISIBLE else View.GONE
            if(showGradientBg.isSelected){
                mBEditText.setBackgroundGradientColor("#FF8585","#03DAC5","#FF8B15")
            }else{
                mBEditText.clearBackgroundGradientColor()
            }
        }
    }

    fun changeInfoVisible(titleIv: View,info: View) {
        titleIv.isSelected = !titleIv.isSelected
        info.visibility = if(titleIv.isSelected) View.VISIBLE else View.GONE
    }
    fun hideShadowEdges(view: View, edge: Int) {
        view.isSelected = !view.isSelected
        if(view.isSelected)
            mBEditText.hideShadowEdges(edge)
        else
            mBEditText.showShadowEdges(edge)
    }
    fun hideBorderEdges(view: View, edge: Int) {
        view.isSelected = !view.isSelected
        if(view.isSelected)
            mBEditText.hideBorderEdges(edge)
        else
            mBEditText.showBorderEdges(edge)
    }
    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        when (seekBar!!.id) {
            R.id.skbar_round -> {
                mBEditText.roundRadius = progress.toFloat()
                skbar_round_tl.progress = progress;
                skbar_round_tr.progress = progress;
                skbar_round_bl.progress = progress;
                skbar_round_br.progress = progress;
            }
            R.id.skbar_round_tl -> {
                mBEditText.topLeftRoundRadius = progress.toFloat()
            }
            R.id.skbar_round_tr -> {
                mBEditText.topRightRoundRadius = progress.toFloat()
            }
            R.id.skbar_round_bl -> {
                mBEditText.bottomLeftRoundRadius = progress.toFloat()
            }
            R.id.skbar_round_br -> {
                mBEditText.bottomRightRoundRadius = progress.toFloat()
            }
            R.id.skbar_size -> {
                mBEditText.shadowSize = progress.toFloat()
            }
            R.id.skbar_border -> {
                mBEditText.borderSize = progress.toFloat()
            }
            R.id.skbar_x -> {
                mBEditText.shadowDx = (progress - 100).toFloat()
            }
            R.id.skbar_y -> {
                mBEditText.shadowDy = (progress - 100).toFloat()
            }
            R.id.skbar_alpha -> {
                mBEditText.shadowAlpha = progress / 255f
            }
        }

    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {}
    override fun onStopTrackingTouch(seekBar: SeekBar?) {}

    fun backup(v: View) {
        finish()
    }


}
