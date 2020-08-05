package com.bary.sample.view

import android.animation.ArgbEvaluator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View

class MyColorBar @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    val colors = intArrayOf(
        0xffffffff.toInt(),
        0xffff5c5c.toInt(),
        0xfffff600.toInt(),
        0xff00ff31.toInt(),
        0xff01ccff.toInt(),
        0xff3d38ff.toInt(),
        0xffee3dff.toInt(),
        0xffff2c2c.toInt(),
        0xff000000.toInt()
    )

    val backPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }
    val thumbPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        style = Paint.Style.FILL
        isAntiAlias = true
        setShadowLayer(10f, 0f, 5f, Color.parseColor("#7d000000"))//滑块阴影
        setLayerType(View.LAYER_TYPE_SOFTWARE, this)
    }

    var mRadius = 0f
    var mPointY = 0F
    var mPointX = 0F
    var mLinearGradient: LinearGradient? = null
    var mBackRectF: RectF? = null
    var mListener: OnStateChangeListener? = null
    var mBackgroundHeight: Float = 0F
    var mMarkColor: Int? = Color.WHITE
    val segments: Double = 100 / (colors.size - 1).toDouble()
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        //背景栏高度
        mBackgroundHeight = dp2px(2F)
        //半径
        mRadius = dp2px(6F)
        //渐变
        mLinearGradient = LinearGradient(
            0f,
            0F,
            width.toFloat(),
            height.toFloat(),
            colors,
            null,
            Shader.TileMode.MIRROR
        )
        backPaint.shader = mLinearGradient
        backPaint.strokeCap = Paint.Cap.ROUND
        //垂直高度
        val centerY = h / 2
        //背景的框
        mBackRectF = RectF(
            mRadius,
            centerY - mBackgroundHeight,
            w.toFloat() - mRadius,
            centerY + mBackgroundHeight
        )
        //初始位置
        mPointX = width / 2F
        mPointY = height / 2F
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //back
        canvas.drawRect(mBackRectF!!, backPaint)
        //circle
        canvas.drawCircle(mPointX, mPointY, mRadius, thumbPaint)
    }

    private fun dp2px(dp: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics
        )
    }

    private var mProgress: Float = 0.toFloat()

    override fun onTouchEvent(event: MotionEvent): Boolean {
        var x = event.x
        x = if (x < mRadius) mRadius else x//判断thumb边界
        x = if (x > width - mRadius) width - (mRadius) else x
        mProgress = 100 - ((width - mRadius) - x) / (width - 2 * mRadius) * 100
        mPointX = x
        when (event.action) {
            MotionEvent.ACTION_MOVE -> {
                parent.requestDisallowInterceptTouchEvent(true)
                updateProcess(mProgress.toInt(), Position_2_Color(mProgress.toInt()) - 3)
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                updateProcess(mProgress.toInt(), Position_2_Color(mProgress.toInt()) - 3)
                invalidate()
            }
        }
        return true
    }

    private fun updateProcess(position: Int, color: Int) {
        mListener?.onProcessChanged(position, color)
    }

    fun setProcess(process: Float) {
        this.mProgress = process
        mPointX = (mProgress / 100) * (width - mRadius * 2)
        updateProcess(mProgress.toInt(), Position_2_Color(mProgress.toInt()))
        invalidate()
    }

    public fun Color_2_Position(color: Int): Int {
        var position: Int = 0
        var offset: Double = 0.0
        for (i in 0..colors.size - 2) {
            if (colors[i] <= color && color <= colors[i + 1]) {
                offset = 1.0 - (color.toDouble() / (colors[i] + colors[i + 1]))
                position = (offset * segments).toInt() + (segments * i).toInt()
            }
        }
        if (color == -6626990) {
            //-6626990为绿色，在蓝红区域也有改值。暂时先收到处理
            position = 30
        }
        //处理设置位置越界
        if (position < 3) {
            position = 3
        } else if (position > 100) {
            position = 50
        }
        return position
    }

    private fun Position_2_Color(position: Int): Int {
        var offset = 0f //偏移量
        var startColor = 0 //当前区段开始颜色
        var endColor = 0 //前区段结束颜色
        val segments: Double = 100 / (colors.size - 1).toDouble()

        for (i in 0..colors.size - 2) {
            if (segments * (i) <= position && position <= segments * (i + 1)) {
                offset = ((position - segments * i) / segments).toFloat()
                startColor = colors[i]
                endColor = colors[i + 1]
            }
        }
        return if (offset <= 1) {
            //偏移量为0和1.0需要处理argbEvaluator内部计算导致不精确。
            if (offset == 0F) {
                offset = 0.08f
            } else if (offset == 1.0F) {
                offset = 0.76f
            }
            val argbEvaluator = ArgbEvaluator()
            argbEvaluator.evaluate(offset, startColor, endColor) as Int
        } else {
            endColor
        }
    }

    //当前默认颜色
    public fun setMarkColor(colorInt: Int) {
        this.mMarkColor = colorInt or (0xff000000.toInt())
        setProcess(Color_2_Position(mMarkColor!!).toFloat())
    }

    //回复到选择前的默认颜色
    public fun reset() {
        setProcess(Color_2_Position(mMarkColor!!).toFloat())
    }

    interface OnStateChangeListener {
        fun onProcessChanged(progress: Int, color: Int)
    }

    public fun setOnColorChangeListener(onStateChangeListener: OnStateChangeListener) {
        this.mListener = onStateChangeListener
    }
}