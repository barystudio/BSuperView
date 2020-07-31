package com.bary.sample

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_bedittext.*

class BEditTextActivity : AppCompatActivity(), View.OnClickListener,OnSeekBarChangeListener {
    private var alpha = 255
    private var red = 0
    private var green = 0
    private var blue = 0
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
    fun initView(){
        back.setColorFilter(ContextCompat.getColor(this, R.color.white))
        showShadow.setOnClickListener(this)
        showBorder.setOnClickListener(this)

        showShadow.isSelected = mBEditText.isShadowShow
        showBorder.isSelected = mBEditText.isBorderShow
        //初始化圆角尺寸
        skbar_round.max = mBEditText.roundRadius.toInt()*3
        skbar_round_tl.max = mBEditText.roundRadius.toInt()*3
        skbar_round_tr.max = mBEditText.roundRadius.toInt()*3
        skbar_round_bl.max = mBEditText.roundRadius.toInt()*3
        skbar_round_br.max = mBEditText.roundRadius.toInt()*3
        skbar_round.progress = mBEditText.roundRadius.toInt()
        skbar_round_tl.progress = mBEditText.topLeftRoundRadius.toInt()
        skbar_round_tr.progress = mBEditText.topRightRoundRadius.toInt()
        skbar_round_bl.progress = mBEditText.bottomLeftRoundRadius.toInt()
        skbar_round_br.progress = mBEditText.bottomRightRoundRadius.toInt()
        //初始化阴影尺寸
        skbar_size.max = (mBEditText.shadowSize * 2).toInt()
        skbar_size.progress = mBEditText.shadowSize.toInt()
        //初始化描边数据
        skbar_border.max = (mBEditText.borderSize * 3).toInt()
        skbar_border.progress = mBEditText.borderSize.toInt()
        initRGBA(mBEditText.shadowColor)
        //初始化透明度
        skbar_alpha.progress = if (alpha > 254) 254 else alpha;
        //初始化红元色
        skbar_red.progress = red
        //初始化绿元色
        skbar_green.progress = green
        //初始化蓝元色
        skbar_blue.progress = blue

        skbar_round.setOnSeekBarChangeListener(this)
        skbar_round_tl.setOnSeekBarChangeListener(this)
        skbar_round_tr.setOnSeekBarChangeListener(this)
        skbar_round_bl.setOnSeekBarChangeListener(this)
        skbar_round_br.setOnSeekBarChangeListener(this)
        skbar_size.setOnSeekBarChangeListener(this)
        skbar_border.setOnSeekBarChangeListener(this)
        skbar_x.setOnSeekBarChangeListener(this)
        skbar_y.setOnSeekBarChangeListener(this)
        skbar_alpha.setOnSeekBarChangeListener(this)
        skbar_red.setOnSeekBarChangeListener(this)
        skbar_green.setOnSeekBarChangeListener(this)
        skbar_blue.setOnSeekBarChangeListener(this)

    }

    override fun onClick(v: View?) {
        val i = v!!.id
        if (i == R.id.showShadow) {
            showShadow.isSelected = !showShadow.isSelected
            mBEditText.isShadowShow = showShadow.isSelected
            shadow_layout.visibility = if (showShadow.isSelected) View.VISIBLE else View.GONE
        } else if (i == R.id.showBorder) {
            showBorder.isSelected = !showBorder.isSelected
            mBEditText.isBorderShow = showBorder.isSelected
            skbar_border.visibility = if (showBorder.isSelected) View.VISIBLE else View.GONE

        }
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        when(seekBar!!.id){
            R.id.skbar_round->{
                mBEditText.roundRadius = progress.toFloat()
               skbar_round_tl.progress = progress;
               skbar_round_tr.progress = progress;
               skbar_round_bl.progress = progress;
               skbar_round_br.progress = progress;
            }
            R.id.skbar_round_tl->{
                mBEditText.topLeftRoundRadius = progress.toFloat()
            }
            R.id.skbar_round_tr->{
                mBEditText.topRightRoundRadius = progress.toFloat()
            }
            R.id.skbar_round_bl->{
                mBEditText.bottomLeftRoundRadius = progress.toFloat()
            }
            R.id.skbar_round_br->{
                mBEditText.bottomRightRoundRadius = progress.toFloat()
            }
            R.id.skbar_size->{
                mBEditText.shadowSize = progress.toFloat()
            }
            R.id.skbar_border->{
                mBEditText.borderSize = progress.toFloat()
            }
            R.id.skbar_x->{
                mBEditText.shadowDx = (progress - 100).toFloat()
            }
            R.id.skbar_y->{
                mBEditText.shadowDy = (progress - 100).toFloat()
            }
            R.id.skbar_alpha->{
                alpha = progress
                mBEditText.shadowColor = Color.argb(alpha, red, green, blue)
            }
            R.id.skbar_red->{
                red = progress
                mBEditText.shadowColor = Color.argb(alpha, red, green, blue)
            }
            R.id.skbar_green->{
                green = progress
                mBEditText.shadowColor = Color.argb(alpha, red, green, blue)
            }
            R.id.skbar_blue ->{
                blue = progress
                mBEditText.shadowColor = Color.argb(alpha, red, green, blue)
            }
        }

    }
    override fun onStartTrackingTouch(seekBar: SeekBar?) {}
    override fun onStopTrackingTouch(seekBar: SeekBar?) {}

    fun initRGBA(color: Int) {
        alpha = Color.alpha(color)
        red = Color.red(color)
        green = Color.green(color)
        blue = Color.blue(color)
    }


    fun backup(v: View) {
        finish()
    }


}
