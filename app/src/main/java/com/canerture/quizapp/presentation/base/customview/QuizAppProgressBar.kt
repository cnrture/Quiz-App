package com.canerture.quizapp.presentation.base.customview

import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.canerture.quizapp.R
import com.canerture.quizapp.common.extension.toPixel

class QuizAppProgressBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet,
    defStyleAttr: Int = 0,
) : View(context, attrs, defStyleAttr) {

    private var maxProgress = 60f
    private var progress = 30f

    private var thickness = 0f

    private var parentColor = 0
    private var fillColor = 0

    private var indicatorType = IndicatorType.Circular

    private lateinit var ovalSpace: RectF

    private lateinit var paint: Paint

    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.QuizAppProgressBar, 0, 0).apply {
            try {
                setThickness(
                    resources.toPixel(
                        getDimensionPixelSize(
                            R.styleable.QuizAppProgressBar_qapb_thickness,
                            24
                        )
                    )
                )

                setMaxProgress(getFloat(R.styleable.QuizAppProgressBar_qapb_maxProgress, 60f))

                setProgress(getFloat(R.styleable.QuizAppProgressBar_qapb_progress, 10f))

                setParentColor(
                    getColor(
                        R.styleable.QuizAppProgressBar_qapb_parentColor,
                        context.getColor(R.color.blue_dark)
                    )
                )

                setFillColor(
                    getColor(
                        R.styleable.QuizAppProgressBar_qapb_fillColor,
                        context.getColor(R.color.white)
                    )
                )

                val index: Int = getInt(R.styleable.QuizAppProgressBar_qapb_indicatorType, 0)
                setIndicatorType(IndicatorType.values()[index])
            } finally {
                recycle()
            }
        }
    }

    private fun setSpace() {
        val horizontalCenter = (width.div(2)).toFloat()
        val verticalCenter = (height.div(2)).toFloat()
        val ovalSize = 240
        ovalSpace = RectF().apply {
            set(
                horizontalCenter - ovalSize,
                verticalCenter - ovalSize,
                horizontalCenter + ovalSize,
                verticalCenter + ovalSize
            )
        }
    }

    private fun drawCircularProgress(canvas: Canvas) {
        val indicatorPosition = (360 * (progress / maxProgress))
        paint = Paint().apply {
            style = Paint.Style.STROKE
            strokeCap = Paint.Cap.ROUND
            isAntiAlias = true
            color = parentColor
            strokeWidth = thickness
            canvas.drawArc(ovalSpace, 0f, 360f, false, this)
            color = fillColor
            canvas.drawArc(ovalSpace, 270f, indicatorPosition, false, this)
        }
    }

    private fun drawLinearProgress(canvas: Canvas) {
        val halfHeight = (height / 2).toFloat()
        val indicatorPosition = (width * progress / 100f)
        paint = Paint().apply {
            style = Paint.Style.FILL
            strokeCap = Paint.Cap.ROUND
            strokeWidth = thickness
            color = parentColor
            canvas.drawLine(0f, halfHeight, width.toFloat(), halfHeight, this)
            color = fillColor
            canvas.drawLine(0f, halfHeight, indicatorPosition, halfHeight, this)
        }
    }

    override fun onDraw(canvas: Canvas?) {
        setSpace()
        canvas?.let {
            when (indicatorType) {
                IndicatorType.Linear -> drawLinearProgress(it)
                IndicatorType.Circular -> drawCircularProgress(it)
            }
        }
    }

    fun setProgress(progress: Float) {
        this.progress = progress
        invalidate()
        requestLayout()
    }

    fun setMaxProgress(progress: Float) {
        this.maxProgress = progress
        invalidate()
        requestLayout()
    }

    fun setThickness(thickness: Float) {
        this.thickness = thickness
        invalidate()
        requestLayout()
    }

    fun setParentColor(color: Int) {
        this.parentColor = color
        invalidate()
        requestLayout()
    }

    fun setFillColor(color: Int) {
        this.fillColor = color
        invalidate()
        requestLayout()
    }

    fun setIndicatorType(indicatorType: IndicatorType) {
        this.indicatorType = indicatorType
        invalidate()
        requestLayout()
    }

    fun animateProgress() {
        val valuesHolder = PropertyValuesHolder.ofFloat("percentage", 0f, 100f)
        val animator = ValueAnimator().apply {
            setValues(valuesHolder)
            duration = 1000
            addUpdateListener {
                val percentage = it.getAnimatedValue("percentage") as Float
                maxProgress = percentage
                invalidate()
            }
        }
        animator.start()
    }

    enum class IndicatorType {
        Circular, Linear
    }
}