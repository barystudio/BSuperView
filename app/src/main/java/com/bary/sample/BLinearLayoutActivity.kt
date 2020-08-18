package com.bary.sample

import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bary.sample.view.MyColorBar
import com.bary.ui.layout.builder.ShadowLayoutBuilder
import kotlinx.android.synthetic.main.activity_blinearlayout.*

class BLinearLayoutActivity : AppCompatActivity(), View.OnClickListener,
    SeekBar.OnSeekBarChangeListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blinearlayout)
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
        skbar_round.progress = mBLinearLayout.roundRadius.toInt()
        skbar_round_tl.progress = mBLinearLayout.topLeftRoundRadius.toInt()
        skbar_round_tr.progress = mBLinearLayout.topRightRoundRadius.toInt()
        skbar_round_bl.progress = mBLinearLayout.bottomLeftRoundRadius.toInt()
        skbar_round_br.progress = mBLinearLayout.bottomRightRoundRadius.toInt()
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
        borderhide_top.isSelected = mBLinearLayout.isHiddenBorderEdges(ShadowLayoutBuilder.TOP)
        borderhide_left.isSelected = mBLinearLayout.isHiddenBorderEdges(ShadowLayoutBuilder.LEFT)
        borderhide_right.isSelected = mBLinearLayout.isHiddenBorderEdges(ShadowLayoutBuilder.RIGHT)
        borderhide_bottom.isSelected = mBLinearLayout.isHiddenBorderEdges(ShadowLayoutBuilder.BOTTOM)
        skbar_border_color.setOnColorChangeListener(object : MyColorBar.OnStateChangeListener {
            override fun onProcessChanged(progress: Int, color: Int) {
                mBLinearLayout.borderColor = color
            }

        })
        /**--阴影--**/
        //初始化阴影尺寸
        shadowTitleLayout.setOnClickListener(this)
        shadowhide_top.setOnClickListener(this)
        shadowhide_left.setOnClickListener(this)
        shadowhide_right.setOnClickListener(this)
        shadowhide_bottom.setOnClickListener(this)

        shadowhide_top.isSelected = mBLinearLayout.isHiddenShadowEdges(ShadowLayoutBuilder.TOP)
        shadowhide_left.isSelected = mBLinearLayout.isHiddenShadowEdges(ShadowLayoutBuilder.LEFT)
        shadowhide_right.isSelected = mBLinearLayout.isHiddenShadowEdges(ShadowLayoutBuilder.RIGHT)
        shadowhide_bottom.isSelected = mBLinearLayout.isHiddenShadowEdges(ShadowLayoutBuilder.BOTTOM)
        skbar_shadow_size.max = AppUtils.dip2px(this, 20f)
        //初始化透明度
        skbar_alpha.max = 255
        skbar_alpha.progress = (mBLinearLayout.shadowAlpha * 255).toInt()
        //初始化阴影颜色
        skbar_color.setMarkColor(mBLinearLayout.shadowColor)
        skbar_shadow_size.setOnSeekBarChangeListener(this)
        skbar_x.setOnSeekBarChangeListener(this)
        skbar_y.setOnSeekBarChangeListener(this)
        skbar_alpha.setOnSeekBarChangeListener(this)
        skbar_color.setOnColorChangeListener(object : MyColorBar.OnStateChangeListener {
            override fun onProcessChanged(progress: Int, color: Int) {
                mBLinearLayout.shadowColor = color
            }

        })


    }

    override fun onClick(v: View?) {
        val i = v!!.id
        if (i == R.id.roundTitleLayout) {
            changeInfoVisible(roundTitleIv, roundInfoLayout)
        } else if (i == R.id.borderTitleLayout) {
            changeInfoVisible(borderTitleIv, borderInfoLayout)
        } else if (i == R.id.borderhide_left) {
            hideBorderEdges(borderhide_left, ShadowLayoutBuilder.LEFT);
        } else if (i == R.id.borderhide_top) {
            hideBorderEdges(borderhide_top, ShadowLayoutBuilder.TOP);
        } else if (i == R.id.borderhide_right) {
            hideBorderEdges(borderhide_right, ShadowLayoutBuilder.RIGHT);
        } else if (i == R.id.borderhide_bottom) {
            hideBorderEdges(borderhide_bottom, ShadowLayoutBuilder.BOTTOM);
        } else if (i == R.id.shadowTitleLayout) {
            changeInfoVisible(shadowTitleIv, shadowInfoLayout)
        } else if (i == R.id.shadowhide_left) {
            hideShadowEdges(shadowhide_left, ShadowLayoutBuilder.LEFT);
        } else if (i == R.id.shadowhide_top) {
            hideShadowEdges(shadowhide_top, ShadowLayoutBuilder.TOP);
        } else if (i == R.id.shadowhide_right) {
            hideShadowEdges(shadowhide_right, ShadowLayoutBuilder.RIGHT);
        } else if (i == R.id.shadowhide_bottom) {
            hideShadowEdges(shadowhide_bottom, ShadowLayoutBuilder.BOTTOM);
        }
    }

    fun changeInfoVisible(titleIv: View, info: View) {
        titleIv.isSelected = !titleIv.isSelected
        info.visibility = if (titleIv.isSelected) View.VISIBLE else View.GONE
    }

    fun hideShadowEdges(view: View, edge: Int) {
        view.isSelected = !view.isSelected
        if (view.isSelected)
            mBLinearLayout.hideShadowEdges(edge)
        else
            mBLinearLayout.showShadowEdges(edge)
    }

    fun hideBorderEdges(view: View, edge: Int) {
        view.isSelected = !view.isSelected
        if (view.isSelected)
            mBLinearLayout.hideBorderEdges(edge)
        else
            mBLinearLayout.showBorderEdges(edge)
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        when (seekBar!!.id) {
            R.id.skbar_round -> {
                mBLinearLayout.roundRadius = progress.toFloat()
                skbar_round_tl.progress = progress;
                skbar_round_tr.progress = progress;
                skbar_round_bl.progress = progress;
                skbar_round_br.progress = progress;
            }
            R.id.skbar_round_tl -> {
                mBLinearLayout.topLeftRoundRadius = progress.toFloat()
            }
            R.id.skbar_round_tr -> {
                mBLinearLayout.topRightRoundRadius = progress.toFloat()
            }
            R.id.skbar_round_bl -> {
                mBLinearLayout.bottomLeftRoundRadius = progress.toFloat()
            }
            R.id.skbar_round_br -> {
                mBLinearLayout.bottomRightRoundRadius = progress.toFloat()
            }
            R.id.skbar_shadow_size -> {
                mBLinearLayout.shadowSize = progress.toFloat()
            }
            R.id.skbar_border_size -> {
                mBLinearLayout.borderSize = progress.toFloat()
            }
            R.id.skbar_x -> {
                mBLinearLayout.shadowDx = (progress - 100).toFloat()
            }
            R.id.skbar_y -> {
                mBLinearLayout.shadowDy = (progress - 100).toFloat()
            }
            R.id.skbar_alpha -> {
                mBLinearLayout.shadowAlpha = progress / 255f
            }
        }

    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {}
    override fun onStopTrackingTouch(seekBar: SeekBar?) {}

    fun backup(v: View) {
        finish()
    }

}
