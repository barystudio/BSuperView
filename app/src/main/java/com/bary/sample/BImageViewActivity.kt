package com.bary.sample

import android.os.Bundle
import android.view.View
import android.widget.RadioGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bary.sample.view.MyColorBar
import com.bary.ui.view.builder.ShadowViewBuilder
import com.bary.ui.view.eum.EditMode
import com.bary.ui.view.eum.GradientOrientation
import com.bary.ui.view.eum.GradientType
import kotlinx.android.synthetic.main.activity_bedittext.*
import kotlinx.android.synthetic.main.activity_bedittext.auxiliaryInfoLayout
import kotlinx.android.synthetic.main.activity_bedittext.auxiliaryTitleIv
import kotlinx.android.synthetic.main.activity_bedittext.auxiliaryTitleLayout
import kotlinx.android.synthetic.main.activity_bedittext.auxiliary_clear
import kotlinx.android.synthetic.main.activity_bedittext.auxiliary_editmode
import kotlinx.android.synthetic.main.activity_bedittext.auxiliary_icon_left
import kotlinx.android.synthetic.main.activity_bedittext.auxiliary_icon_right
import kotlinx.android.synthetic.main.activity_bedittext.auxiliary_secret
import kotlinx.android.synthetic.main.activity_bedittext.back
import kotlinx.android.synthetic.main.activity_bedittext.borderInfoLayout
import kotlinx.android.synthetic.main.activity_bedittext.borderTitleIv
import kotlinx.android.synthetic.main.activity_bedittext.borderTitleLayout
import kotlinx.android.synthetic.main.activity_bedittext.borderhide_bottom
import kotlinx.android.synthetic.main.activity_bedittext.borderhide_left
import kotlinx.android.synthetic.main.activity_bedittext.borderhide_right
import kotlinx.android.synthetic.main.activity_bedittext.borderhide_top
import kotlinx.android.synthetic.main.activity_bedittext.gradientInfoLayout
import kotlinx.android.synthetic.main.activity_bedittext.gradientTitleIv
import kotlinx.android.synthetic.main.activity_bedittext.gradientTitleLayout
import kotlinx.android.synthetic.main.activity_bedittext.gradient_kind
import kotlinx.android.synthetic.main.activity_bedittext.gradient_orientation
import kotlinx.android.synthetic.main.activity_bedittext.gradient_type
import kotlinx.android.synthetic.main.activity_bedittext.roundInfoLayout
import kotlinx.android.synthetic.main.activity_bedittext.roundTitleIv
import kotlinx.android.synthetic.main.activity_bedittext.roundTitleLayout
import kotlinx.android.synthetic.main.activity_bedittext.shadowInfoLayout
import kotlinx.android.synthetic.main.activity_bedittext.shadowTitleIv
import kotlinx.android.synthetic.main.activity_bedittext.shadowTitleLayout
import kotlinx.android.synthetic.main.activity_bedittext.shadowhide_bottom
import kotlinx.android.synthetic.main.activity_bedittext.shadowhide_left
import kotlinx.android.synthetic.main.activity_bedittext.shadowhide_right
import kotlinx.android.synthetic.main.activity_bedittext.shadowhide_top
import kotlinx.android.synthetic.main.activity_bedittext.showGradient
import kotlinx.android.synthetic.main.activity_bedittext.skbar_alpha
import kotlinx.android.synthetic.main.activity_bedittext.skbar_border_color
import kotlinx.android.synthetic.main.activity_bedittext.skbar_border_size
import kotlinx.android.synthetic.main.activity_bedittext.skbar_color
import kotlinx.android.synthetic.main.activity_bedittext.skbar_round
import kotlinx.android.synthetic.main.activity_bedittext.skbar_round_bl
import kotlinx.android.synthetic.main.activity_bedittext.skbar_round_br
import kotlinx.android.synthetic.main.activity_bedittext.skbar_round_tl
import kotlinx.android.synthetic.main.activity_bedittext.skbar_round_tr
import kotlinx.android.synthetic.main.activity_bedittext.skbar_shadow_size
import kotlinx.android.synthetic.main.activity_bedittext.skbar_x
import kotlinx.android.synthetic.main.activity_bedittext.skbar_y
import kotlinx.android.synthetic.main.activity_bimageview.*

class BImageViewActivity : AppCompatActivity(), View.OnClickListener, OnSeekBarChangeListener {
    var mGradientKind = R.id.gradient_kind_bg
    var mGradientOrientation = R.id.gradient_orientation_h
    var mGradientType = R.id.gradient_type_l
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bimageview)
        AppUtils.setStatusBarColor(
            window,
            ContextCompat.getColor(this, R.color.colorPrimary),
            false
        )
        initView();
    }

    fun initView() {
        back.setColorFilter(ContextCompat.getColor(this, R.color.bsv_white))

        /**--圆角--**/
        //初始化圆角尺寸
        skbar_round.max = AppUtils.dip2px(this, 70f)
        skbar_round_tl.max = skbar_round.max
        skbar_round_tr.max = skbar_round.max
        skbar_round_bl.max = skbar_round.max
        skbar_round_br.max = skbar_round.max
        skbar_round.progress = mBImageView.roundRadius.toInt()
        skbar_round_tl.progress = mBImageView.topLeftRoundRadius.toInt()
        skbar_round_tr.progress = mBImageView.topRightRoundRadius.toInt()
        skbar_round_bl.progress = mBImageView.bottomLeftRoundRadius.toInt()
        skbar_round_br.progress = mBImageView.bottomRightRoundRadius.toInt()
        roundTitleLayout.setOnClickListener(this)
        skbar_round.setOnSeekBarChangeListener(this)
        skbar_round_tl.setOnSeekBarChangeListener(this)
        skbar_round_tr.setOnSeekBarChangeListener(this)
        skbar_round_bl.setOnSeekBarChangeListener(this)
        skbar_round_br.setOnSeekBarChangeListener(this)
        /**--描边--**/
        //初始化描边数据
        borderTitleLayout.setOnClickListener(this)
        skbar_border_size.max = AppUtils.dip2px(this, 20f)
        skbar_border_size.setOnSeekBarChangeListener(this)
        borderhide_top.setOnClickListener(this)
        borderhide_left.setOnClickListener(this)
        borderhide_right.setOnClickListener(this)
        borderhide_bottom.setOnClickListener(this)
        borderhide_top.isSelected = mBImageView.isHiddenBorderEdges(ShadowViewBuilder.TOP)
        borderhide_left.isSelected = mBImageView.isHiddenBorderEdges(ShadowViewBuilder.LEFT)
        borderhide_right.isSelected = mBImageView.isHiddenBorderEdges(ShadowViewBuilder.RIGHT)
        borderhide_bottom.isSelected = mBImageView.isHiddenBorderEdges(ShadowViewBuilder.BOTTOM)
        skbar_border_color.setOnColorChangeListener(object : MyColorBar.OnStateChangeListener {
            override fun onProcessChanged(progress: Int, color: Int) {
                mBImageView.borderColor = color
            }

        })
        /**--阴影--**/
        //初始化阴影尺寸
        shadowTitleLayout.setOnClickListener(this)
        shadowhide_top.setOnClickListener(this)
        shadowhide_left.setOnClickListener(this)
        shadowhide_right.setOnClickListener(this)
        shadowhide_bottom.setOnClickListener(this)

        shadowhide_top.isSelected = mBImageView.isHiddenShadowEdges(ShadowViewBuilder.TOP)
        shadowhide_left.isSelected = mBImageView.isHiddenShadowEdges(ShadowViewBuilder.LEFT)
        shadowhide_right.isSelected = mBImageView.isHiddenShadowEdges(ShadowViewBuilder.RIGHT)
        shadowhide_bottom.isSelected = mBImageView.isHiddenShadowEdges(ShadowViewBuilder.BOTTOM)
        skbar_shadow_size.max = AppUtils.dip2px(this, 20f)
        //初始化透明度
        skbar_alpha.max = 255
        skbar_alpha.progress = (mBImageView.shadowAlpha * 255).toInt()
        //初始化阴影颜色
        skbar_color.setMarkColor(mBImageView.shadowColor)
        skbar_shadow_size.setOnSeekBarChangeListener(this)
        skbar_x.setOnSeekBarChangeListener(this)
        skbar_y.setOnSeekBarChangeListener(this)
        skbar_alpha.setOnSeekBarChangeListener(this)
        skbar_color.setOnColorChangeListener(object : MyColorBar.OnStateChangeListener {
            override fun onProcessChanged(progress: Int, color: Int) {
                mBImageView.shadowColor = color
            }

        })



        /**--渐变色--**/
        //初始渐变色
        gradientTitleLayout.setOnClickListener(this)
        showGradient.setOnClickListener(this)
        gradient_kind.setOnCheckedChangeListener(object : RadioGroup.OnCheckedChangeListener {
            override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
                mGradientKind = checkedId
                changeGradientInfo()
            }

        })
        gradient_orientation.setOnCheckedChangeListener(object :
            RadioGroup.OnCheckedChangeListener {
            override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
                mGradientOrientation = checkedId;
                changeGradientInfo()
            }

        })
        gradient_type.setOnCheckedChangeListener(object : RadioGroup.OnCheckedChangeListener {
            override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
                mGradientType = checkedId;
                changeGradientInfo()
            }

        })

        showGradient.isSelected = false
        gradient_orientation.check(R.id.gradient_orientation_h)
        gradient_type.check(R.id.gradient_type_l)


    }

    override fun onClick(v: View?) {
        val i = v!!.id
        if (i == R.id.roundTitleLayout) {
            changeInfoVisible(roundTitleIv, roundInfoLayout)
        } else if (i == R.id.borderTitleLayout) {
            changeInfoVisible(borderTitleIv, borderInfoLayout)
        } else if (i == R.id.borderhide_left) {
            hideBorderEdges(borderhide_left, ShadowViewBuilder.LEFT);
        } else if (i == R.id.borderhide_top) {
            hideBorderEdges(borderhide_top, ShadowViewBuilder.TOP);
        } else if (i == R.id.borderhide_right) {
            hideBorderEdges(borderhide_right, ShadowViewBuilder.RIGHT);
        } else if (i == R.id.borderhide_bottom) {
            hideBorderEdges(borderhide_bottom, ShadowViewBuilder.BOTTOM);
        } else if (i == R.id.shadowTitleLayout) {
            changeInfoVisible(shadowTitleIv, shadowInfoLayout)
        } else if (i == R.id.shadowhide_left) {
            hideShadowEdges(shadowhide_left, ShadowViewBuilder.LEFT);
        } else if (i == R.id.shadowhide_top) {
            hideShadowEdges(shadowhide_top, ShadowViewBuilder.TOP);
        } else if (i == R.id.shadowhide_right) {
            hideShadowEdges(shadowhide_right, ShadowViewBuilder.RIGHT);
        } else if (i == R.id.shadowhide_bottom) {
            hideShadowEdges(shadowhide_bottom, ShadowViewBuilder.BOTTOM);
        } else if (i == R.id.gradientTitleLayout) {
            changeInfoVisible(gradientTitleIv, gradientInfoLayout)
        } else if (i == R.id.showGradient) {
            showGradient.isSelected = !showGradient.isSelected
            changeGradientInfo();
        } else if (i == R.id.auxiliaryTitleLayout) {
            changeInfoVisible(auxiliaryTitleIv, auxiliaryInfoLayout)
        }
    }

    fun changeInfoVisible(titleIv: View, info: View) {
        titleIv.isSelected = !titleIv.isSelected
        info.visibility = if (titleIv.isSelected) View.VISIBLE else View.GONE
    }

    fun hideShadowEdges(view: View, edge: Int) {
        view.isSelected = !view.isSelected
        if (view.isSelected)
            mBImageView.hideShadowEdges(edge)
        else
            mBImageView.showShadowEdges(edge)
    }

    fun hideBorderEdges(view: View, edge: Int) {
        view.isSelected = !view.isSelected
        if (view.isSelected)
            mBImageView.hideBorderEdges(edge)
        else
            mBImageView.showBorderEdges(edge)
    }

    fun changeGradientInfo() {
        mBImageView.clearTextGradientColor();
        mBImageView.clearBackgroundGradientColor()
        if (!showGradient.isSelected) return

         var mGt:GradientType = when (mGradientType) {
            R.id.gradient_type_l -> {
                GradientType.LINEAR
            }
            R.id.gradient_type_r -> {
                GradientType.RADIAL
            }
            R.id.gradient_type_s -> {
                GradientType.SWEEP
            }
            else -> GradientType.LINEAR
        }
        var  mGo:GradientOrientation = when (mGradientOrientation) {
            R.id.gradient_orientation_h -> {
                GradientOrientation.HORIZONTAL
            }
            R.id.gradient_orientation_v -> {
                GradientOrientation.VERTICAL
            }
            R.id.gradient_orientation_d -> {
                GradientOrientation.DIAGONAL
            }
            else -> GradientOrientation.HORIZONTAL
        }

        if (mGradientKind == R.id.gradient_kind_bg) {
            mBImageView.setBackgroundGradientColor("#FF8585", "#03DAC5", "#FF8B15")
            mBImageView.backgroundGradientType = mGt
            mBImageView.backgroundGradientOrientation = mGo

        } else {
            mBImageView.setTextGradientColor("#FF8585", "#03DAC5", "#FF8B15")
            mBImageView.textGradientType = mGt
            mBImageView.textGradientOrientation = mGo
        }


    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        when (seekBar!!.id) {
            R.id.skbar_round -> {
                mBImageView.roundRadius = progress.toFloat()
                skbar_round_tl.progress = progress;
                skbar_round_tr.progress = progress;
                skbar_round_bl.progress = progress;
                skbar_round_br.progress = progress;
            }
            R.id.skbar_round_tl -> {
                mBImageView.topLeftRoundRadius = progress.toFloat()
            }
            R.id.skbar_round_tr -> {
                mBImageView.topRightRoundRadius = progress.toFloat()
            }
            R.id.skbar_round_bl -> {
                mBImageView.bottomLeftRoundRadius = progress.toFloat()
            }
            R.id.skbar_round_br -> {
                mBImageView.bottomRightRoundRadius = progress.toFloat()
            }
            R.id.skbar_shadow_size -> {
                mBImageView.shadowSize = progress.toFloat()
            }
            R.id.skbar_border_size -> {
                mBImageView.borderSize = progress.toFloat()
            }
            R.id.skbar_x -> {
                mBImageView.shadowDx = (progress - 100).toFloat()
            }
            R.id.skbar_y -> {
                mBImageView.shadowDy = (progress - 100).toFloat()
            }
            R.id.skbar_alpha -> {
                mBImageView.shadowAlpha = progress / 255f
            }
        }

    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {}
    override fun onStopTrackingTouch(seekBar: SeekBar?) {}

    fun backup(v: View) {
        finish()
    }


}
