package com.canerture.quizapp.presentation.base.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.AlphaAnimation
import com.canerture.quizapp.R
import com.canerture.quizapp.common.extension.toPixel

class QuizAppButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : View(context, attrs, defStyleAttr) {

    private var radius = 0f
    private var buttonBackgroundColor = context.getColor(R.color.blue_darkest)

    private var text = ""
    private var textWidth = 0f
    private var textSmallGlyphHeight = 0f
    private var fontSize = 0f
    private var textColor = context.getColor(R.color.white)
    private var textStyle = TextStyle.Normal

    private lateinit var shadow: RectF
    private lateinit var background: RectF
    private lateinit var backgroundPaint: Paint
    private lateinit var textPaint: Paint

    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.QuizAppButton, 0, 0).apply {
            try {
                setText(getString(R.styleable.QuizAppButton_qab_text) ?: "")

                setFontSize(
                    resources.toPixel(
                        getDimensionPixelSize(
                            R.styleable.QuizAppButton_qab_fontSize,
                            30
                        )
                    )
                )

                setRadius(
                    resources.toPixel(
                        getDimensionPixelSize(
                            R.styleable.QuizAppButton_qab_radius,
                            10
                        )
                    )
                )

                setTextColor(
                    getColor(
                        R.styleable.QuizAppButton_qab_textColor,
                        context.getColor(R.color.white)
                    )
                )

                setButtonBackgroundColor(
                    getColor(
                        R.styleable.QuizAppButton_qab_backgroundColor,
                        context.getColor(R.color.blue_darkest)
                    )
                )

                val index = getInt(R.styleable.QuizAppButton_qab_textStyle, 0)
                setTextStyle(TextStyle.values()[index])
            } finally {
                recycle()

                setOnTouchListener { view, event ->
                    when (event.action) {
                        MotionEvent.ACTION_DOWN -> {
                            startAnimation(AlphaAnimation(1F, 0.5F))
                            view.invalidate()
                        }
                        MotionEvent.ACTION_UP -> {
                            performClick()
                        }
                        MotionEvent.ACTION_CANCEL -> {
                            clearAnimation()
                            view.invalidate()
                        }
                    }
                    false
                }
            }
        }
    }

    fun setText(text: String) {
        this.text = text
        invalidate()
        requestLayout()
    }

    fun setFontSize(size: Float) {
        this.fontSize = size
        invalidate()
        requestLayout()
    }

    fun setTextColor(color: Int) {
        this.textColor = color
        invalidate()
        requestLayout()
    }

    fun setButtonBackgroundColor(color: Int) {
        this.buttonBackgroundColor = color
        invalidate()
        requestLayout()
    }

    fun setRadius(radius: Float) {
        this.radius = radius
        invalidate()
        requestLayout()
    }

    fun setTextStyle(textStyle: TextStyle) {
        this.textStyle = textStyle
        invalidate()
        requestLayout()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        shadow = RectF(0f, 0f, w.toFloat(), h.toFloat())
        background = RectF(
            0f,
            0f,
            w.toFloat(),
            h.toFloat()
        )
        backgroundPaint = Paint().apply { color = buttonBackgroundColor }
        textPaint = Paint().apply {
            color = textColor
            textSize = fontSize
            typeface = when (textStyle) {
                TextStyle.Normal -> Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
                TextStyle.Bold -> Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            }
            textWidth = measureText(text)
            textSmallGlyphHeight = fontMetrics.run { ascent + descent }
        }
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.drawRoundRect(background, radius, radius, backgroundPaint)
        val textHorizontalPadding = (width - textWidth) / 2f
        val textVerticalPadding = (height - textSmallGlyphHeight) / 2f
        canvas?.drawText(text, textHorizontalPadding, textVerticalPadding, textPaint)
    }

    enum class TextStyle {
        Normal, Bold
    }
}